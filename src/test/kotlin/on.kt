import kyge.*
import kyge.triggerEvents.WebhookEvent.Release
import kyge.triggerEvents.WebhookEvent.pullRequest
import kyge.triggerEvents.WebhookEvent.push
import kyge.triggerEvents.WorkflowCall
import kotlin.test.BeforeTest
import kotlin.test.Test

class On {

    @BeforeTest
    fun clearBuilder() {
        builder.clear()
    }

    @Test
    fun push() {

        val wf = workflow {
            on(push)
        }
        assert(wf == "on: push")
    }

    @Test
    fun `push and pull request`() {

        val wf = workflow {
            on(push, pullRequest)
        }
        assert(wf == "on: [push, pull_request]")
    }

    @Test
    fun `multiple events with activity type`() {
        val wf = workflow {
            on {
                push { branches("main") }
                pullRequest { branches("master") }
                pageBuild
                release {
                    created
                }
                release(Release.Type.published, Release.Type.created, Release.Type.edited)
            }
        }

        assert(wf ==
                       """|on:
                          |  push:
                          |    branches:
                          |      - main
                          |  pull_request:
                          |    branches:
                          |      - master
                          |  page_build:
                          |  release:
                          |    types:
                          |      - created
                          |  release:
                          |    types: [published, created, edited]""".trimMargin())
    }

    @Test
    fun `including branches and tags`() {
        val wf = workflow {
            on {
                push {
                    branches {
                        +"main"
                        +"mona/octocat"
                        +"releases/**"
                    }
                    tags("v1", "v1.*")
                }
            }
        }
        assert(wf ==
                       """|on:
                          |  push:
                          |    branches:
                          |      - main
                          |      - 'mona/octocat'
                          |      - 'releases/**'
                          |    tags:
                          |      - v1
                          |      - v1.*""".trimMargin())
    }

    @Test
    fun `ignoring branches and tags`() {
        val wf = workflow {
            on {
                push {
                    ignoreBranches("mona/octocat", "releases/**-alpha")
                    ignoreTags("v1.*")
                }
            }
        }
        assert(wf ==
                       """|on:
                          |  push:
                          |    branches-ignore:
                          |      - 'mona/octocat'
                          |      - 'releases/**-alpha'
                          |    tags-ignore:
                          |      - v1.*""".trimMargin())
    }

    @Test
    fun `excluding branches with negative patter`() {
        val wf = workflow {
            on {
                push {
                    branches {
                        +"mona/octocat"
                        -"releases/**-alpha"
                    }
                }
            }
        }
        assert(wf ==
                       """|on:
                          |  push:
                          |    branches:
                          |      - 'mona/octocat'
                          |      - '!releases/**-alpha'""".trimMargin())
    }

    @Test
    fun paths() {
        val wf = workflow {
            on {
                push {
                    ignorePaths("docs/**")
                    paths("**.js")
                }
            }
        }
        assert(wf ==
                       """|on:
                          |  push:
                          |    paths-ignore:
                          |      - 'docs/**'
                          |    paths:
                          |      - '**.js'""".trimMargin())
    }

    @Test
    fun `workflow call`() {

        val wf = workflow {
            on {
                workflowCallInputs {
                    "username" {
                        description = "A username passed from the caller workflow"
                        default = "john-doe"
                        required = false
                        type = WorkflowCall.Input.Type.string
                    }
                }
            }
        }
        assert(wf ==
                       """|on:
                          |  workflow_call:
                          |    inputs:
                          |      username:
                          |        description: 'A username passed from the caller workflow'
                          |        default: 'john-doe'
                          |        required: false
                          |        type: string""".trimMargin())
    }

    @Test
    fun `workflow call with variable`() {

        val username by WorkflowCall.Input {
            description = "A username passed from the caller workflow"
            default = "john-doe"
            required = false
            type = WorkflowCall.Input.Type.string
        }

        val wf = workflow {
            on {
                workflowCallInputs(username)
            }
            jobs {
                "print-username" {
                    runsOn = RunsOn.ubuntuLatest

                    "Print the input name to STDOUT" {
                        run = "echo The username is $username"
                    }
                }
            }
        }
        assert(wf ==
                       """|on:
                          |  workflow_call:
                          |    inputs:
                          |      username:
                          |        description: 'A username passed from the caller workflow'
                          |        default: 'john-doe'
                          |        required: false
                          |        type: string
                          |jobs:
                          |  print-username:
                          |    runs-on: ubuntu-latest
                          |    steps:
                          |      - name: Print the input name to STDOUT
                          |        run: echo The username is ${"inputs.username".ref}""".trimMargin())
    }

    @Test
    fun `workflow call secrets`() {

        val `access-token` by Secret {
            description = "A token passed from the caller workflow"
            required = false
        }

        val wf = workflow {
            on {
                workflowCallSecrets(`access-token`)
            }
            jobs {
                "pass-secret-to-action" {
                    runsOn = RunsOn.ubuntuLatest

                    "Pass the received secret to an action" {
                        builder += "uses: ./.github/actions/my-action@v1"
                        builder += "with:"
                        indent {
                            builder += "token: $`access-token`"
                        }
                    }
                }
            }
        }
        assert(wf ==
                       """|on:
                          |  workflow_call:
                          |    secrets:
                          |      access-token:
                          |        description: 'A token passed from the caller workflow'
                          |        required: false
                          |jobs:
                          |  pass-secret-to-action:
                          |    runs-on: ubuntu-latest
                          |    steps:
                          |      - name: Pass the received secret to an action
                          |        uses: ./.github/actions/my-action@v1
                          |        with:
                          |          token: ${"secrets.access-token".ref}""".trimMargin())
    }

    @Test
    fun `workflow dispatch inputs`() {

        val wf = workflow {
            on {
                workflowDispatch {
                    inputs {
                        "logLevel" {
                            description = "Log level"
                            required
                            default = "warning"
                        }
                        "tags" {
                            description = "Test scenario tags"
                            required = false
                        }
                    }
                }
            }
        }
        assert(wf ==
                       """|on:
                          |  workflow_dispatch:
                          |    inputs:
                          |      logLevel:
                          |        description: 'Log level'
                          |        required: true
                          |        default: 'warning'
                          |      tags:
                          |        description: 'Test scenario tags'
                          |        required: false""".trimMargin())
    }

    @Test
    fun schedule() {
        val wf = workflow {
            on {
                schedule = "30 5,17 * * *"
            }
        }
        assert(wf ==
                       """|on:
                          |  schedule:
                          |    - cron:  '30 5,17 * * *'""".trimMargin())
    }

    @Test
    fun permissions() {
        val wf = workflow {
            permissions = Permission.readAndWrite
            permissions {
                actions = Permission.read
                checks = Permission.write
                contents = Permission.readAndWrite
                deployments = Permission.none
                issues = Permission.read
                discussions = Permission.write
                packages = Permission.readAndWrite
                pullRequests = Permission.none
                repositoryProjects = Permission.read
                securityEvents = Permission.write
                statuses = Permission.readAndWrite
            }
        }
        assert(wf ==
                       """|permissions: read-all|write-all
                          |permissions:
                          |  actions: read
                          |  checks: write
                          |  contents: read|write
                          |  deployments: none
                          |  issues: read
                          |  discussions: write
                          |  packages: read|write
                          |  pull-requests: none
                          |  repository-projects: read
                          |  security-events: write
                          |  statuses: read|write""".trimMargin())
    }

    @Test
    fun permissions2() {
        val wf = workflow {
            name = "My workflow"
            on(push)
            permissions = Permission.read
        }
        assert(wf ==
                       """|name: My workflow
                          |on: push
                          |permissions: read-all""".trimMargin())
    }

    @Test
    fun env() {
        val wf = workflow {
            env("SERVER" to "production")
        }
        assert(wf ==
                       """|env:
                          |  SERVER: production""".trimMargin())
    }

    // TODO defaults https://docs.github.com/en/actions/learn-github-actions/workflow-syntax-for-github-actions#defaults

    @Test
    fun concurrency() {
        val wf = workflow {
            concurrency = "staging_environment"
        }
        assert(wf == "concurrency: staging_environment")
    }

    @Test
    fun concurrency2() {
        val wf = workflow {
            concurrency = "ci-${github.ref}"
        }
        assert(wf == "concurrency: ci-${"github.ref".ref}")
    }

    @Test
    fun concurrency3() {
        val wf = workflow {
            concurrency {
                group = github.headRef.toString()
                cancelInProgress
            }
        }
        assert(wf ==
                       """|concurrency:
                          |  group: ${"github.head_ref".ref}
                          |  cancel-in-progress: true""".trimMargin())
    }

    @Test
    fun jobs() {
        val wf = workflow {
            jobs {
                "my_first_job" {
                    name = "My first job"
                }
                "my_second_job" {
                    name = "My second job"
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  my_first_job:
                          |    name: My first job
                          |  my_second_job:
                          |    name: My second job""".trimMargin())
    }

    @Test
    fun `requiring dependent jobs to be successful`() {
        val wf = workflow {
            jobs {
                val job1 by newJob
                val job2 by Job { needs = job1 }
                "job3" { needs(job1, job2) }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |  job2:
                          |    needs: job1
                          |  job3:
                          |    needs: [job1, job2]""".trimMargin())
    }

    @Test
    fun `not requiring dependent jobs to be successful`() {
        val wf = workflow {
            jobs {
                val job1 by newJob
                val job2 by Job { needs = job1 }
                "job3" {
                    runIf = RunIf.always
                    needs(job1, job2)
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |  job2:
                          |    needs: job1
                          |  job3:
                          |    if: always()
                          |    needs: [job1, job2]""".trimMargin())
    }

    @Test
    fun `runs-on`() {
        val wf = workflow {
            job {
                runsOn = RunsOn.ubuntuLatest
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    runs-on: ubuntu-latest""".trimMargin())
    }

    @Test
    fun `job permissions`() {
        val wf = workflow {
            job {
                permissions = Permission.readAndWrite
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    permissions: read-all|write-all""".trimMargin())
    }

    @Test
    fun `job permissions 1`() {
        val wf = workflow {
            jobs {
                "stale" {
                    runsOn = RunsOn.ubuntuLatest
                    permissions {
                        issues = Permission.write
                        pullRequests = Permission.write
                    }
                    steps {
                        action = "stale@v3"
                    }
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  stale:
                          |    runs-on: ubuntu-latest
                          |    permissions:
                          |      issues: write
                          |      pull-requests: write
                          |    steps:
                          |      - uses: actions/stale@v3""".trimMargin())
    }

    @Test
    fun `job environment`() {
        val wf = workflow {
            job {
                environment = "staging_environment"
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    environment: staging_environment""".trimMargin())
    }

    @Test
    fun `job environment 2`() {
        val wf = workflow {
            job {
                environment {
                    name = "production_environment"
                    url = "https://github.com"
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    environment:
                          |      name: production_environment
                          |      url: https://github.com""".trimMargin())
    }

    @Test
    fun `job concurrency`() {
        val wf = workflow {
            job {
                concurrency = "staging_environment"
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    concurrency: staging_environment""".trimMargin())
    }

    @Test
    fun `job concurrency 2`() {
        val wf = workflow {
            job {
                concurrency = "ci-${github.ref}"
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    concurrency: ci-${"github.ref".ref}""".trimMargin())
    }

    @Test
    fun `job concurrency 3`() {
        val wf = workflow {
            job {
                concurrency {
                    group = github.headRef.toString()
                    cancelInProgress
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    concurrency:
                          |      group: ${"github.head_ref".ref}
                          |      cancel-in-progress: true""".trimMargin())
    }

    @Test
    fun `job output`() {
        val wf = workflow {
            val first by newOutput
            val second by newOutput
            jobs {
                val job1 by Job {
                    runsOn = RunsOn.ubuntuLatest
                    steps {
                        "step1" {
                            first.value = "hello"
                        }
                        "step2" {
                            second.value = "world"
                        }
                    }
                    outputs(first, second)
                }
                job("job2") {
                    runsOn = RunsOn.ubuntuLatest
                    needs = job1
                    steps {
                        run = "echo $first $second"
                    }
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    runs-on: ubuntu-latest
                          |    steps:
                          |      - id: step1
                          |        run: echo "::set-output name=first::hello"
                          |      - id: step2
                          |        run: echo "::set-output name=second::world"
                          |    outputs:
                          |      first: ${"steps.step1.outputs.first".ref}
                          |      second: ${"steps.step2.outputs.second".ref}
                          |  job2:
                          |    runs-on: ubuntu-latest
                          |    needs: job1
                          |    steps:
                          |      - run: echo ${"needs.job1.outputs.first".ref} ${"needs.job1.outputs.second".ref}""".trimMargin())
    }

    @Test
    fun `job env`() {
        val wf = workflow {
            job {
                env("FIRST_NAME" to "Mona")
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    env:
                          |      FIRST_NAME: Mona""".trimMargin())
    }

    // TODO job default https://docs.github.com/en/actions/learn-github-actions/workflow-syntax-for-github-actions#jobsjob_iddefaults

    @Test
    fun `job steps`() {
        val wf = workflow {
            name = "Greeting from Mona"
            on(push)
            jobs {
                "my-job" {
                    name = "My Job"
                    runsOn = RunsOn.ubuntuLatest
                    step {
                        name = "Print a greeting"
                        env {
                            "MY_VAR" to "Hi there! My name is"
                            "FIRST_NAME" to "Mona"
                            "MIDDLE_NAME" to "The"
                            "LAST_NAME" to "Octocat"
                        }
                        "run:".appendMultiLine("echo \$MY_VAR \$FIRST_NAME \$MIDDLE_NAME \$LAST_NAME.")
                    }
                }
            }
        }
        assert(wf ==
                       """|name: Greeting from Mona
                          |on: push
                          |jobs:
                          |  my-job:
                          |    name: My Job
                          |    runs-on: ubuntu-latest
                          |    steps:
                          |      - name: Print a greeting
                          |        env:
                          |          MY_VAR: Hi there! My name is
                          |          FIRST_NAME: Mona
                          |          MIDDLE_NAME: The
                          |          LAST_NAME: Octocat
                          |        run: |
                          |          echo ${"\$MY_VAR \$FIRST_NAME \$MIDDLE_NAME \$LAST_NAME"}.""".trimMargin())
    }

    @Test
    fun `step if`() {
        val wf = workflow {
            job {
                `if`({ github.eventName `==` "pull_request" `&&` Expression("github.event.action == 'unassigned'") }) {
                    "My first step" {
                        //                        if: ${ { github.event_name == 'pull_request' && github.event.action == 'unassigned' } }
                        echo("This event is a pull request that had an assignee removed.")
                    }
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    steps:
                          |      - name: My first step
                          |        if: ${"github.event_name == 'pull_request' && github.event.action == 'unassigned'".ref}
                          |        run: echo This event is a pull request that had an assignee removed.""".trimMargin())
    }

    @Test
    fun `step uses`() {
        val wf = workflow {
            job {
                steps {
                    // Reference a specific commit
                    action = "checkout@a81bbbf8298c0fa03ea29cdc473d45769f953675"
                    // Reference the major version of a release
                    action = "checkout@v2"
                    // Reference a specific version
                    action = "checkout@v2.2.0"
                    // Reference a branch
                    action = "checkout@main"
                    // Using Action Type directly
                    checkout.v2
                    // Using an action in the same repository as the workflow
                    localAction = "my-action"
                    // Using a Docker Hub action
                    dockerAction = "alpine:3.8"
                    // Using the GitHub Packages Container registry
                    ghcrDockerAction = "OWNER/IMAGE_NAME"
                    // Using a Docker public registry action
                    dockerAction = "gcr.io/cloud-builders/gradle"
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    steps:
                          |      - uses: actions/checkout@a81bbbf8298c0fa03ea29cdc473d45769f953675
                          |      - uses: actions/checkout@v2
                          |      - uses: actions/checkout@v2.2.0
                          |      - uses: actions/checkout@main
                          |      - uses: actions/checkout@v2
                          |      - uses: ./.github/actions/my-action
                          |      - uses: docker://alpine:3.8
                          |      - uses: docker://ghcr.io/OWNER/IMAGE_NAME
                          |      - uses: docker://gcr.io/cloud-builders/gradle""".trimMargin())
    }

    @Test
    fun `step action inside a different private repository`() {
        val wf = workflow {
            "my_first_job" {
                steps {
                    name("Check out repository") {
                        action("checkout@v2") {
                            repository = "octocat/my-private-repo"
                            ref = "v1.0"
                            token = Secrets["PERSONAL_ACCESS_TOKEN"]
                            path = "./.github/actions/my-private-repo"
                        }
                    }
                    name("Run my action") {
                        localAction = "my-private-repo/my-action"
                    }
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  my_first_job:
                          |    steps:
                          |      - name: Check out repository
                          |        uses: actions/checkout@v2
                          |        with:
                          |          repository: octocat/my-private-repo
                          |          ref: v1.0
                          |          token: ${"secrets.PERSONAL_ACCESS_TOKEN".ref}
                          |          path: ./.github/actions/my-private-repo
                          |      - name: Run my action
                          |        uses: ./.github/actions/my-private-repo/my-action""".trimMargin())
    }

    @Test
    fun `step run single command`() {
        val wf = workflow {
            job {
                "Install Dependencies" {
                    run = "npm install"
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    steps:
                          |      - name: Install Dependencies
                          |        run: npm install""".trimMargin())
    }

    @Test
    fun `step run multi-line command`() {
        val wf = workflow {
            job {
                "Clean install dependencies and build" {
                    run("npm ci", "npm run build")
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    steps:
                          |      - name: Clean install dependencies and build
                          |        run: |
                          |          npm ci
                          |          npm run build""".trimMargin())
    }

    @Test
    fun `step run with working directory`() {
        val wf = workflow {
            job {
                "Clean temp directory" {
                    run = "rm -rf *"
                    workingDirectory = "./temp"
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    steps:
                          |      - name: Clean temp directory
                          |        run: rm -rf *
                          |        working-directory: ./temp""".trimMargin())
    }

    @Test
    fun `step run using bash`() {
        val wf = workflow {
            job {
                "Display the path" {
                    echo("\$PATH")
                    shell = Shell.bash
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    steps:
                          |      - name: Display the path
                          |        run: echo ${"\$PATH"}
                          |        shell: bash""".trimMargin())
    }

    @Test
    fun `step run using Windows cmd`() {
        val wf = workflow {
            job {
                "Display the path" {
                    echo("%PATH%")
                    shell = Shell.cmd
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    steps:
                          |      - name: Display the path
                          |        run: echo %PATH%
                          |        shell: cmd""".trimMargin())
    }

    @Test
    fun `step run using Powershell Core`() {
        val wf = workflow {
            job {
                "Display the path" {
                    echo("\${env:PATH}")
                    shell = Shell.pwsh
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    steps:
                          |      - name: Display the path
                          |        run: echo ${"\${env:PATH}"}
                          |        shell: pwsh""".trimMargin())
    }

    @Test
    fun `step run using Powershell Desktop to run a script`() {
        val wf = workflow {
            job {
                "Display the path" {
                    echo("\${env:PATH}")
                    shell = Shell.powershell
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    steps:
                          |      - name: Display the path
                          |        run: echo ${"\${env:PATH}"}
                          |        shell: powershell""".trimMargin())
    }

    @Test
    fun `running a python script`() {
        val wf = workflow {
            job {
                "Display the path" {
                    run("""|import os
                           |print(os.environ['PATH'])""".trimMargin().lines())
                    shell = Shell.python
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    steps:
                          |      - name: Display the path
                          |        run: |
                          |          import os
                          |          print(os.environ['PATH'])
                          |        shell: python""".trimMargin())
    }

    @Test
    fun `running a kotlin script`() {
        val wf = workflow {
            job {
                "Display the path" {
                    run = """println(System.getenv("PATH"))"""
                    shell = Shell.kotlin
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    steps:
                          |      - name: Display the path
                          |        run: println(System.getenv("PATH"))
                          |        shell: kotlin""".trimMargin())
    }

    @Test
    fun `step with`() {
        val wf = workflow {
            "my_first_job" {
                "My first step" {
                    action = "hello_world@main"
                    with {
                        "first_name" to "Mona"
                        "middle_name" to "The"
                        "last_name" to "Octocat"
                    }
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  my_first_job:
                          |    steps:
                          |      - name: My first step
                          |        uses: actions/hello_world@main
                          |        with:
                          |          first_name: Mona
                          |          middle_name: The
                          |          last_name: Octocat""".trimMargin())
    }

    @Test
    fun `step with args`() {
        val wf = workflow {
            job {
                "Explain why this job ran" {
                    uses = "octo-org/action-name@main"
                    with {
                        entrypoint = "/bin/echo"
                        args = "The ${"github.event_name".ref} event triggered this step."
                    }
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    steps:
                          |      - name: Explain why this job ran
                          |        uses: octo-org/action-name@main
                          |        with:
                          |          entrypoint: /bin/echo
                          |          args: The ${"github.event_name".ref} event triggered this step.""".trimMargin())
    }

    @Test
    fun `step with entrypoint`() {
        val wf = workflow {
            job {
                "Run a custom command" {
                    uses = "octo-org/action-name@main"
                    with {
                        entrypoint = "/a/different/executable"
                    }
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    steps:
                          |      - name: Run a custom command
                          |        uses: octo-org/action-name@main
                          |        with:
                          |          entrypoint: /a/different/executable""".trimMargin())
    }

    @Test
    fun `step env`() {
        val wf = workflow {
            job {
                "My first action" {
                    env {
                        "GITHUB_TOKEN" to Secrets["GITHUB_TOKEN"]
                        "FIRST_NAME" to "Mona"
                        "LAST_NAME" to "Octocat"
                    }
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    steps:
                          |      - name: My first action
                          |        env:
                          |          GITHUB_TOKEN: ${"secrets.GITHUB_TOKEN".ref}
                          |          FIRST_NAME: Mona
                          |          LAST_NAME: Octocat""".trimMargin())
    }

    @Test
    fun `continue on error`() {
        val wf = workflow {
            job {
                "my step" {
                    continueOnError
                }
                "my step 2" {
                    continueOnError = false
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    steps:
                          |      - name: my step
                          |        continue-on-error: true
                          |      - name: my step 2
                          |        continue-on-error: false""".trimMargin())
    }

    @Test
    fun timeout() {
        val wf = workflow {
            job {
                "my step" {
                    timeout = Minute(5)
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    steps:
                          |      - name: my step
                          |        timeout-minutes: 5""".trimMargin())
    }

    @Test
    fun matrix() {
        val wf = workflow {
            job {
                val node by Matrix(10, 12, 14)
                step {
                    action("setup-node@v2") {
                        "node-version" to node
                    }
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    strategy:
                          |      matrix:
                          |        node: [10, 12, 14]
                          |    steps:
                          |      - uses: actions/setup-node@v2
                          |        with:
                          |          node-version: ${"matrix.node".ref}""".trimMargin())
    }

    @Test
    fun `matrix combination`() {
        val wf = workflow {
            job {
                val os by Matrix(RunsOn.ubuntu18_04, RunsOn.ubuntu20_04)
                val node by Matrix(10, 12, 14)
                runsOn(os)
                step {
                    action("setup-node@v2") {
                        "node-version" to node
                    }
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    strategy:
                          |      matrix:
                          |        os: [ubuntu-18.04, ubuntu-20.04]
                          |        node: [10, 12, 14]
                          |    runs-on: ${"matrix.os".ref}
                          |    steps:
                          |      - uses: actions/setup-node@v2
                          |        with:
                          |          node-version: ${"matrix.node".ref}""".trimMargin())
    }

    @Test
    fun `matrix combination include`() {
        val wf = workflow {
            job {
                val os by Matrix(RunsOn.macosLatest, RunsOn.windowsLatest, RunsOn.ubuntu18_04)
                val node by Matrix(8, 10, 12, 14)
                includeMatrix {
                    os += RunsOn.windowsLatest
                    node += 8
                    "npm" to 6
                }
                runsOn(os)
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    strategy:
                          |      matrix:
                          |        os: [macos-latest, windows-latest, ubuntu-18.04]
                          |        node: [8, 10, 12, 14]
                          |        include:
                          |          - os: windows-latest
                          |            node: 8
                          |            npm: 6
                          |    runs-on: ${"matrix.os".ref}""".trimMargin())
    }

    @Test
    fun `matrix combination include new`() {
        val wf = workflow {
            job {
                val node by Matrix(14)
                val os by Matrix(RunsOn.macosLatest, RunsOn.windowsLatest, RunsOn.ubuntu18_04)
                includeMatrix {
                    node += 15
                    os += RunsOn.ubuntu18_04
                    "experimental" to true
                }
                runsOn(os)
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    strategy:
                          |      matrix:
                          |        node: [14]
                          |        os: [macos-latest, windows-latest, ubuntu-18.04]
                          |        include:
                          |          - node: 15
                          |            os: ubuntu-18.04
                          |            experimental: true
                          |    runs-on: ${"matrix.os".ref}""".trimMargin())
    }

    @Test
    fun `matrix combination exclude`() {
        val wf = workflow {
            job {
                val os by Matrix(RunsOn.macosLatest, RunsOn.windowsLatest, RunsOn.ubuntu18_04)
                val node by Matrix(8, 10, 12, 14)
                excludeMatrix {
                    os -= RunsOn.macosLatest
                    node -= 8
                }
                runsOn(os)
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    strategy:
                          |      matrix:
                          |        os: [macos-latest, windows-latest, ubuntu-18.04]
                          |        node: [8, 10, 12, 14]
                          |        exclude:
                          |          - os: macos-latest
                          |            node: 8
                          |    runs-on: ${"matrix.os".ref}""".trimMargin())
    }

    @Test
    fun `matrix using environment variables`() {
        val wf = workflow {
            name = "Node.js CI"
            on(push)
            "build" {
                runsOn = RunsOn.ubuntuLatest
                val site by Matrix<String>()
                val datacenter by Matrix<String>()
                strategy {
                    includeMatrix {
                        "node-version: 10.x" {
                            site += "prod"
                            datacenter += "site-a"
                        }
                        "node-version: 12.x" {
                            site += "dev"
                            datacenter += "site-b"
                        }
                    }
                }
                "Echo site details" {
                    env {
                        "SITE" to site
                        "DATACENTER" to datacenter
                    }
                    echo("\$SITE \$DATACENTER")
                }
            }
        }
        assert(wf ==
                       """|name: Node.js CI
                          |on: push
                          |jobs:
                          |  build:
                          |    runs-on: ubuntu-latest
                          |    strategy:
                          |      matrix:
                          |        include:
                          |          - node-version: 10.x
                          |            site: "prod"
                          |            datacenter: "site-a"
                          |          - node-version: 12.x
                          |            site: "dev"
                          |            datacenter: "site-b"
                          |    steps:
                          |      - name: Echo site details
                          |        env:
                          |          SITE: ${"matrix.site".ref}
                          |          DATACENTER: ${"matrix.datacenter".ref}
                          |        run: echo ${"\$SITE \$DATACENTER"}""".trimMargin())
    }

    @Test
    fun `strategy options`() {
        val wf = workflow {
            job {
                strategy {
                    failFast
                    maxParallel = 2
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    strategy:
                          |      fail-fast: true
                          |      max-parallel: 2""".trimMargin())
    }

    @Test
    fun `preventing a specific failing matrix job from failing a workflow run`() {
        val wf = workflow {
            job {
                val node by Matrix(13, 14)
                val os by Matrix(RunsOn.macosLatest, RunsOn.ubuntu18_04)
                val experimental by Matrix(false)
                includeMatrix {
                    node += 15
                    os += RunsOn.ubuntu18_04
                    experimental += true
                }
                strategy {
                    failFast = false
                }
                runsOn(os)
                continueOnError(experimental)
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    strategy:
                          |      matrix:
                          |        node: [13, 14]
                          |        os: [macos-latest, ubuntu-18.04]
                          |        experimental: [false]
                          |        include:
                          |          - node: 15
                          |            os: ubuntu-18.04
                          |            experimental: true
                          |      fail-fast: false
                          |    runs-on: ${"matrix.os".ref}
                          |    continue-on-error: ${"matrix.experimental".ref}""".trimMargin())
    }

    @Test
    fun container() {
        val wf = workflow {
            "my_job" {
                container {
                    image = "node:14.16"
                    env = "NODE_ENV" to "development"
                    port = 80
                    volume = "my_docker_volume:/volume_mount"
                    options = "--cpus 1"
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  my_job:
                          |    container:
                          |      image: node:14.16
                          |      env:
                          |        NODE_ENV: development
                          |      ports:
                          |        - 80
                          |      volumes:
                          |        - my_docker_volume:/volume_mount
                          |      options: --cpus 1""".trimMargin())
    }

    @Test
    fun `container 2`() {
        val wf = workflow {
            "my_job" {
                container = "node:14.16"
            }
        }
        assert(wf ==
                       """|jobs:
                          |  my_job:
                          |    container: node:14.16""".trimMargin())
    }

    // TODO jobs.<job_id>.container.image https://docs.github.com/en/actions/learn-github-actions/workflow-syntax-for-github-actions#jobsjob_idcontainerimage

    @Test
    fun `container 3`() {
        val wf = workflow {
            job {
                container {
                    volumes(
                        "my_docker_volume:/volume_mount",
                        "/data/my_data",
                        "/source/directory:/destination/directory")
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    container:
                          |      volumes:
                          |        - my_docker_volume:/volume_mount
                          |        - /data/my_data
                          |        - /source/directory:/destination/directory""".trimMargin())
    }

    @Test
    fun `container options`() {
        val wf = workflow {
            job {
                container {
                    options {
                        cpus = 1
                    }
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    container:
                          |      options: --cpus 1""".trimMargin())
    }

    @Test
    fun services() {
        val wf = workflow {
            job {
                services {
                    "nginx" {
                        image = "nginx"
                        port = "8080:80"
                    }
                    "redis" {
                        image = "redis"
                        port = "6379/tcp"
                    }
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    services:
                          |      nginx:
                          |        image: nginx
                          |        ports:
                          |          - 8080:80
                          |      redis:
                          |        image: redis
                          |        ports:
                          |          - 6379/tcp""".trimMargin())
    }

    @Test
    fun `services credential`() {
        val wf = workflow {
            job {
                services {
                    "myservice1" {
                        image = "ghcr.io/owner/myservice1"
                        credentials("github.actor".ref, "secrets.ghcr_token".ref)
                    }
                    "myservice2" {
                        image = "dockerhub_org/myservice2"
                        credentials("secrets.DOCKER_USER".ref, "secrets.DOCKER_PASSWORD".ref)
                    }
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    services:
                          |      myservice1:
                          |        image: ghcr.io/owner/myservice1
                          |        credentials:
                          |          username: ${"github.actor".ref}
                          |          password: ${"secrets.ghcr_token".ref}
                          |      myservice2:
                          |        image: dockerhub_org/myservice2
                          |        credentials:
                          |          username: ${"secrets.DOCKER_USER".ref}
                          |          password: ${"secrets.DOCKER_PASSWORD".ref}""".trimMargin())
    }

    @Test
    fun `services volumes`() {
        val wf = workflow {
            job {
                services {
                    "myservice1" {
                        volumes(
                            "my_docker_volume:/volume_mount",
                            "/data/my_data",
                            "/source/directory:/destination/directory")
                    }
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  job1:
                          |    services:
                          |      myservice1:
                          |        volumes:
                          |          - my_docker_volume:/volume_mount
                          |          - /data/my_data
                          |          - /source/directory:/destination/directory""".trimMargin())
    }

    @Test
    fun `job uses`() {
        val wf = workflow {
            jobs {
                "call-workflow-1" {
                    uses = "octo-org/this-repo/.github/workflows/workflow-1.yml@172239021f7ba04fe7327647b213799853a9eb89"
                }
                "call-workflow-2" {
                    uses = "octo-org/another-repo/.github/workflows/workflow-2.yml@v1"
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  call-workflow-1:
                          |    uses: octo-org/this-repo/.github/workflows/workflow-1.yml@172239021f7ba04fe7327647b213799853a9eb89
                          |  call-workflow-2:
                          |    uses: octo-org/another-repo/.github/workflows/workflow-2.yml@v1""".trimMargin())
    }

    @Test
    fun `job with`() {
        val wf = workflow {
            jobs {
                "call-workflow" {
                    uses = "octo-org/example-repo/.github/workflows/called-workflow.yml@main"
                    with {
                        "username" to "mona"
                    }
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  call-workflow:
                          |    uses: octo-org/example-repo/.github/workflows/called-workflow.yml@main
                          |    with:
                          |      username: mona""".trimMargin())
    }

    @Test
    fun `job secrets`() {
        val wf = workflow {
            jobs {
                "call-workflow" {
                    uses = "octo-org/example-repo/.github/workflows/called-workflow.yml@main"
                    secret = "access-token" to Secrets["PERSONAL_ACCESS_TOKEN"]
                }
            }
        }
        assert(wf ==
                       """|jobs:
                          |  call-workflow:
                          |    uses: octo-org/example-repo/.github/workflows/called-workflow.yml@main
                          |    secrets:
                          |      access-token: ${"secrets.PERSONAL_ACCESS_TOKEN".ref}""".trimMargin())
    }
}