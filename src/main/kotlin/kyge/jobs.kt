package kyge

import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty

interface JobsInterface {

    fun jobs(block: Jobs.() -> Unit) {
        builder += "jobs:"
        indent {
            Jobs.block()
        }
    }

    var jobNameOrdinal: Int
    fun job(id: String = "job$jobNameOrdinal", block: Job.() -> Unit) {
        jobs {
            id {
                block()
            }
        }
    }
}

@KygeMarker
object Jobs {

    val newJob
        get() = PropertyDelegateProvider { _: Any?, prop ->
            val job = Job(prop.name)
            prop.name { }
            ReadOnlyProperty<Any?, Job> { _, _ -> job }
        }

    operator fun String.invoke(block: Job.() -> Unit) {
        builder += "$this:"
        indent {
            Job(this).block()
        }
    }

    fun job(id: String = "job", block: Job.() -> Unit) = id { block() }

    fun Job(block: Job.() -> Unit) = PropertyDelegateProvider { _: Any?, prop ->
        val job = Job(prop.name)
        prop.name.invoke(block)
        ReadOnlyProperty<Any?, Job> { _, _ -> job }
    }

}

@KygeMarker
class Job(override val id: String) : Context<Job>,
                                     RunIfInterface,
                                     EnvInterface,
                                     StepsInterface,
                                     NeedsInterface,
                                     PermissionsInterface,
                                     EnvironmentInterface,
                                     ConcurrencyInterface,
                                     OutputsInterface,
                                     StrategyInterface,
                                     ContinueOnError,
                                     ContainerInterface,
                                     SecretsInterface,
                                     ServiceInterface,
                                     WithInterface {

    override var name: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "name: $value"
        }

    var enabled: Expression
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "if: $value"
        }

    var runsOn: RunsOn
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "runs-on: ${value.label}"
        }

    fun runsOn(value: Matrix<*>) {
        builder += "runs-on: $value"
    }

    fun step(block: Step.() -> Unit) {
        steps {
            bulletPoint {
                Step().block()
            }
        }
    }

    fun `if`(predicate: () -> Expression, block: Step.Builder.() -> Unit) {
        steps {
            Step.Builder().apply {
                condition = "if: ${predicate().toString().ref}"
                block()
            }
        }
    }

    override fun toString() = id

    class Builder {

        var condition = ""

        operator fun String.invoke(block: Job.() -> Unit) {
            builder += "$this:"
            indent {
                if (condition.isNotEmpty()) builder += condition
                Job(this).block()
            }
        }
    }

    private var inSteps = false
    override var _inStrategy = false

    // TODO switch to id as Job for coherence?
    operator fun String.invoke(block: Step.() -> Unit) {
        val lambda = {
            bulletPoint {
                Step().apply {
                    name = this@invoke
                    block()
                }
            }
        }
        if (inSteps)
            indent {
                lambda()
            }
        else {
            steps {
                lambda()
            }
            inSteps = true
        }
    }

    var uses: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "uses: $value"
        }
}

fun Job(block: Job.() -> Unit) = PropertyDelegateProvider { _: Any?, prop ->
    val job = Job(prop.name).apply(block)
    ReadOnlyProperty<Any?, Job> { _, _ -> job }
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

    override fun toString() = label
}

interface RunIfInterface {

    var runIf: RunIf
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            if (value != RunIf.allSucceded)
                builder += "if: ${value.func}()"
        }

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
