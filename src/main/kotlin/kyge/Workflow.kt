package kyge

import kyge.triggerEvents.*

@KygeMarker
class Workflow : PermissionsInterface,
                 EnvInterface,
                 JobsInterface,
                 ConcurrencyInterface {

    var name: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "name: $value"
        }

    operator fun String.unaryPlus() {}

    fun on(block: On.() -> Unit) {
        builder += "on:"
        indent {
            On().block()
        }
    }

    fun on(event: WebhookEventInterface<*>) {
        builder += "on: ${event.name}"
    }

    fun on(vararg events: WebhookEventInterface<*>) {
        builder += "on: [${events.joinToString { it.name }}]"
    }

    @KygeMarker
    inner class On : WebhookEvents,
                     WorkflowCallInterface,
                     WorkflowDispatchInterface,
                     Schedule,
                     RepositoryDispatch

    override var jobNameOrdinal = 1

    operator fun String.invoke(block: Job.() -> Unit) {
        jobs {
            invoke(block)
        }
    }
}

fun workflow(block: Workflow.() -> Unit): String {
    Workflow().block()
    return builder.toString().trim()
}