package kyge

interface StepsInterface {

    fun steps(block: Steps.() -> Unit) {
        builder += "steps:"
        indent {
            Steps().block()
        }
    }
}

@KygeMarker
class Steps : Actions {

    fun step(block: Step.() -> Unit) {
        bulletPoint {
            Step().block()
        }
    }

    fun name(name: String, block: Step.() -> Unit) {
        step {
            this.name = name
            block()
        }
    }

    operator fun String.invoke(block: Step.() -> Unit) =
        step {
            id = this@invoke
            block()
        }

    var action: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            step {
                builder += "uses: actions/$value"
            }
        }

    //    fun action(action: String, with: With.() -> Unit) {
    //        this.action = action
    //        val dummyAction = object : Action<Unit> {
    //            override val actionName: String
    //                get() = TODO("Not yet implemented")
    //            override val actionVersion: String
    //                get() = TODO("Not yet implemented")
    //            override val t: Unit
    //                get() = TODO("Not yet implemented")
    //        }
    //        dummyAction {
    //            with()
    //        }
    //    }

    var localAction: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            bulletPoint {
                builder += "uses: ./.github/actions/$value"
            }
        }

    var dockerAction: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            bulletPoint {
                builder += "uses: docker://$value"
            }
        }

    var ghcrDockerAction: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            bulletPoint {
                builder += "uses: docker://ghcr.io/$value"
            }
        }

    var uses: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            bulletPoint {
                builder += "uses: $value"
            }
        }

    var run: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "- run: $value"
        }
}

open class Step : Actions,
                  ContinueOnError,
                  Timeout,
                  WithInterface {

    var id: String = ""
        set(value) {
            builder += "id: $value"
            field = value
        }
    var name: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "name: $value"
        }

    var uses: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "uses: $value"
        }

    var run: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "run: $value"
        }

    var workingDirectory: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "working-directory: $value"
        }

    fun run(vararg cmds: String) = "run:".appendMultiLine(*cmds)
    fun run(cmds: List<String>) = run(*cmds.toTypedArray())

    fun echo(string: String) {
        builder += "run: echo $string"
    }

    var Output.value: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            stepId = id
            builder += "run: echo \"::set-output name=$name::$value\""
        }

    fun action(action: String, with: With.() -> Unit) {
        builder += "uses: actions/$action"
        object : Action<Unit> {
            override val actionName: String
                get() = TODO("Not yet implemented")
            override val actionVersion: String
                get() = TODO("Not yet implemented")
            override val t: Unit
                get() = TODO("Not yet implemented")
        }.invoke(with)
    }

    var action: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "uses: actions/$value"
        }

    //    fun action(action: String, with: With.() -> Unit) {
    //        this.action = action
    //        val dummyAction = object : Action<Unit> {
    //            override val actionName: String
    //                get() = TODO("Not yet implemented")
    //            override val actionVersion: String
    //                get() = TODO("Not yet implemented")
    //            override val t: Unit
    //                get() = TODO("Not yet implemented")
    //        }
    //        dummyAction {
    //            with()
    //        }
    //    }

    var localAction: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "uses: ./.github/actions/$value"
        }

    var dockerAction: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "uses: docker://$value"
        }

    var ghcrDockerAction: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "uses: docker://ghcr.io/$value"
        }


    var shell: Shell
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "shell: $value"
        }

    class Builder {

        var condition = ""

        operator fun String.invoke(block: Step.() -> Unit) {
            bulletPoint {
                Step().apply {
                    name = this@invoke
                    if (condition.isNotEmpty()) builder += condition
                    block()
                }
            }
        }
    }
}

enum class Shell { bash, cmd, pwsh, powershell, python, perl, kotlin }

interface ContinueOnError {

    var continueOnError: Boolean
        get() {
            builder += "continue-on-error: true"
            return true
        }
        set(value) {
            builder += "continue-on-error: $value"
        }

    fun continueOnError(matrix: Matrix<*>) {
        builder += "continue-on-error: $matrix"
    }
}

interface Timeout {

    var timeout: Minute
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "timeout-minutes: $value"
        }
}

@JvmInline
value class Minute(val value: Int) {
    override fun toString() = value.toString()
}