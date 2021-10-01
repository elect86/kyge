package kyge

interface Jobs {

    fun jobs(block: Builder.() -> Unit) {
        builder += "jobs:"
        indent {
            Builder.block()
        }
    }

    object Builder {

        operator fun String.invoke(block: Job.() -> Unit) {
            builder += "$this:"
            indent {
                Job().block()
            }
        }
    }
}

class Job : Context<Job>,
            RunIfInterface,
            Env,
            StepsInterface {

    override var name = "job"

    var enabled: Expression
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "if: ${value.value}"
        }

    var runsOn: RunsOn
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "runs-on: ${value.label}"
            val a = value == RunsOn.windowsLatest
        }

    sealed class RunsOn(val label: String) {
        /** Windows Server 2022[beta] */
        object windows2022 : RunsOn("windows-2022")

        /** Windows Server 2019 */
        object windows2019 : RunsOn("windows-2019")

        /** Windows Server 2016 */
        object windows2016 : RunsOn("windows-2016")

        /** Ubuntu 20.04 */
        object ubuntu20_04 : RunsOn("ubuntu-20.04")

        /** Ubuntu 18.04 */
        object ubuntu18_04 : RunsOn("ubuntu-18.04")

        /** macOS Big Sur 11 */
        object macos11 : RunsOn("macos-11")

        /** macOS Catalina 10.15 */
        object macos10_15 : RunsOn("macos-10.15")

        object windowsLatest : RunsOn("windows-latest")

        object ubuntuLatest : RunsOn("ubuntu-latest")

        object macosLatest : RunsOn("macos-latest")

        final override fun equals(other: Any?): Boolean = when {
            (this is windows2019 && other is windowsLatest) || (this is windowsLatest && other is windows2019) -> true
            (this is ubuntu20_04 && other is ubuntuLatest) || (this is ubuntuLatest && other is ubuntu20_04) -> true
            (this is macos10_15 && other is macosLatest) || (this is macosLatest && other is macos10_15) -> true
            else -> super.equals(other)
        }
    }

    class Builder {

        var condition = ""

        operator fun String.invoke(block: Job.() -> Unit) {
            builder += "$this:"
            indent {
                if (condition.isNotEmpty()) builder += condition
                Job().block()
            }
        }
    }
}

interface RunIfInterface {

    var runIf: RunIf
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            if (value != RunIf.allSucceded)
                builder += "if: \${{ ${value.func}() }}"
        }

    enum class RunIf {
        allSucceded, always, canceledWorkflow, anyFailed;

        val func: String
            get() = when (this) {
                allSucceded -> "success"
                canceledWorkflow -> "cancelled"
                anyFailed -> "failure"
                else -> name
            }
    }
}