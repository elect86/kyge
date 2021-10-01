package kyge

import kyge.triggerEvents.*

@KygeMarker
class Workflow : Permissions,
                 Env,
                 Jobs {

    var name: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "name: $value"
        }

    operator fun String.unaryPlus() {}

    fun on(block: On.() -> Unit) {
        builder += "on:"
        On().block()
    }

    fun on(vararg events: WebhookEventInterface<*>) {
        builder += "on: [${events.joinToString { it.name }}]"
    }

    @KygeMarker
    inner class On : WebhookEvent,
                     WorkflowDispatch,
                     Schedule,
                     RepositoryDispatch
}