package kyge

interface ConcurrencyInterface {

    var concurrency: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "concurrency: $value"
        }

    fun concurrency(block: Concurrency.() -> Unit) {
        builder += "concurrency:"
        indent {
            Concurrency.block()
        }
    }
}

object Concurrency {

    var group: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "group: $value"
        }

    var cancelInProgress: Boolean
        get() {
            builder += "cancel-in-progress: true"
            return true
        }
        set(value) {
            builder += "cancel-in-progress: $value"
        }
}