import kyge.*
import kyge.Permissions.Permission.read
import kyge.Permissions.Permission.write
import kyge.RunIfInterface.*
import kyge.actions.uploadArtifact.V2.Builder.*
//import kyge.actions.checkout
import kyge.triggerEvents.RepositoryDispatch
import kyge.triggerEvents.Schedule.Cron.Builder.Day
import kyge.triggerEvents.Schedule.Cron.Builder.Month
import kyge.triggerEvents.WebhookEvent.*
import kyge.triggerEvents.WebhookEvent.release.Type.*

fun main() {
    "build".yml {

        name = "build name"

        on(push)
        on {
            push()
            release(created, published)
            push {
                branches("main", "mona/octocat")
            }
            pullRequest {
                branches {
                    +"releases/**"
                    -"releases/**-alpha"
                }
                tags("v1", "v2/*")
            }
            workflowDispatch()
            workflowDispatch {
                inputs {
                    "name"{
                        description = "Person to greet"
                        required
                        default = "test-file.js"
                    }
                    "home" {
                        description = "location"
                        required = false
                        default = "The Octoverse"
                    }
                }
                outputs {
                    "results-file" {
                        description = "Path to results file"
                    }
                }
            }
            schedule {
                cron = "30 5,17 * * *"
                cron {
                    minute = 0
                    hour = 4..16 step 2
                    month = Month.JAN
                    dayOfWeek = Day.MON
                }
            }
            repositoryDispatch(RepositoryDispatch.Type.created)
        }

        permissions(readAll = true, writeAll = true)
        permissions {
            contents = read
            pullRequests = write
        }
        env("SERVER" to "production")

        // TODO
        // - defaults https://docs.github.com/en/actions/learn-github-actions/workflow-syntax-for-github-actions#defaults
        // - job.needs https://docs.github.com/en/actions/learn-github-actions/workflow-syntax-for-github-actions#jobsjob_idneeds
        // - job.environment https://docs.github.com/en/actions/learn-github-actions/workflow-syntax-for-github-actions#jobsjob_idenvironment
        // - job.concurrency https://docs.github.com/en/actions/learn-github-actions/workflow-syntax-for-github-actions#jobsjob_idconcurrency
        // - job.defaults https://docs.github.com/en/actions/learn-github-actions/workflow-syntax-for-github-actions#jobsjob_iddefaults

        jobs {
            `if`({ github.ref `==` "refs/heads/main" }) {
                "prod-check" {
                    builder += "custom"
                    env("FIRST_NAME" to "Mona")
                }
            }
            "production" {
                enabled = github.branch `==` "master"
            }
            "job1" {
                runIf = RunIf.always
                runsOn = Job.RunsOn.ubuntuLatest
                steps {
                    step {
                        id = "id1"
                        name = "name1"
                    }
                    step {
                        id = "id0"
                        name = "name0"
                    }
                    builder += "test"
                    "id2" {
                        name = "name2"
                        uses = "actions/checkout@a81bbbf8298c0fa03ea29cdc473d45769f953675"
                    }
                    builder += "test"
                    checkout.v2 {
                        fetchAll
                    }
                    uploadArtifact.v2 {
                        "my-artifact" to "path/to/artifact/world.txt"
                        "my-artifact".to("path/output/bin/", "path/output/test-results", "!path/**/*.tmp")
                        onFileNotFound = Policy.warn
                    }
                }
            }
        }
    }
}