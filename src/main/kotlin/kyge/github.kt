package kyge

import java.util.ArrayList

/** object	The top-level context available during any job or step in a workflow. */
object github : Context<github> {

    override val name = "github"

    /** The name of the action currently running. GitHub removes special characters or uses the name __run when the
     *  current step runs a script. If you use the same action more than once in the same job, the name will include a
     *  suffix with the sequence number with underscore before it. For example, the first script you run will have the
     *  name __run, and the second script will be named __run_2. Similarly, the second invocation of actions/checkout
     *  will be actionscheckout2. */
    object action : Property<String>(github) {
        override val name = "action"
    }

    /** The path where your action is located. You can use this path to easily access files located in the same
     *  repository as your action. This attribute is only supported in composite actions. */
    object actionPath : Property<String>(github) {
        override val name = "action_path"
    }

    /** The login of the user that initiated the workflow run. */
    object actor : Property<String>(github) {
        override val name = "actor"
    }

    /** The base_ref or target branch of the pull request in a workflow run. This property is only available when the
     *  event that triggers a workflow run is either pull_request or pull_request_target. */
    object baseRef : Property<String>(github) {
        override val name = "base_ref"
    }

    /** The full event webhook payload. For more information, see "Events that trigger workflows." You can access
     *  individual properties of the event using this context. */
    object event : Property<Object>(github) {

        override val name = "event"

        class Input {
            lateinit var id: String
            lateinit var description: String
            var required = true
            lateinit var default: String

            override fun toString() = "\${{ github.event.inputs.$id }}"
        }

        class Output {
            lateinit var id: String
            lateinit var description: String

            override fun toString() = "\${{ github.event.output.$id }}"
        }

        val inputs = ArrayList<Input>()
        val outputs = ArrayList<Output>()
    }

    /** The name of the event that triggered the workflow run. */
    object eventName : Property<String>(github) {
        override val name = "event_name"
    }

    /** The path to the full event webhook payload on the runner. */
    object eventPath : Property<String>(github) {
        override val name = "event_path"
    }

    /** The head_ref or source branch of the pull request in a workflow run. This property is only available when the
     *  event that triggers a workflow run is either pull_request or pull_request_target. */
    object headRef : Property<String>(github) {
        override val name = "head_ref"
    }

    /** The job_id of the current job. */
    object job : Property<String>(github) {
        override val name = "job"
    }

    /** The branch or tag ref that triggered the workflow run. For branches this is the format refs/heads/<branch_name>,
     *  and for tags it is refs/tags/<tag_name>. */
    val ref = object : Property<String>(github) {
        override val name = "ref"
    }

    /** [Kyge] custom, `ref` overwrite
     *  @see [ref] */
    object branch : Property<String>(github) {
        override val name = "ref"

        init {
            prefix = "refs/heads/"
        }
    }

    /** [Kyge] custom, `ref` overwrite
     *  @see [ref] */
    object tag : Property<String>(github) {
        override val name = "ref"

        init {
            prefix = "refs/tags/"
        }
    }

    /** The owner and repository name. For example, Codertocat/Hello-World. */
    object repository : Property<String>(github) {
        override val name = "repository"
    }

    /** The repository owner's name. For example, Codertocat. */
    object repositoryOwner : Property<String>(github) {
        override val name = "repository_owner"
    }

    /** A unique number for each run within a repository. This number does not change if you re-run the workflow run. */
    object runID : Property<String>(github) {
        override val name = "run_id"
    }

    /** A unique number for each run of a particular workflow in a repository. This number begins at 1 for the
     *  workflow's first run, and increments with each new run. This number does not change if you re-run the workflow run. */
    object runNumber : Property<String>(github) {
        override val name = "run_number"
    }

    /** Returns the URL of the GitHub server. For example: https://github.com. */
    object serverUrl : Property<String>(github) {
        override val name = "server_url"
    }

    /** The commit SHA that triggered the workflow run. */
    object sha : Property<String>(github) {
        override val name = "sha"
    }

    /** A token to authenticate on behalf of the GitHub App installed on your repository. This is functionally
     *  equivalent to the GITHUB_TOKEN secret. For more information, see "Authenticating with the GITHUB_TOKEN." */
    object token : Property<String>(github) {
        override val name = "token"
    }

    /** The name of the workflow. If the workflow file doesn't specify a name, the value of this property is the full
     *  path of the workflow file in the repository. */
    object workflow : Property<String>(github) {
        override val name = "workflow"
    }

    /** The default working directory for steps and the default location of your repository when using the checkout
     *  action. */
    object workspace : Property<String>(github) {
        override val name = "workspace"
    }
}


interface Context<Type> {
    val name: String
}

abstract class Property<Type>(val ctx: Context<*>) {

    abstract val name: String

    var prefix: String? = null

    infix fun `==`(string: String): Expression {
        //        println(this as? Context<github>)
        //        println(this as? Context<Job>)
        //        val ctxName = when(this) {
        //            is Context<github> -> ""
        //            else -> ""
        //        }
        var b = string
        prefix?.let { b = "$it$string" }
        return Expression("\${{ ${ctx.name}.$name == '$b' }}")
    }
}
