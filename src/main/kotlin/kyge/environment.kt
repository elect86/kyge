package kyge

interface EnvironmentInterface {

    var environment: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "environment: $value"
        }

    fun environment(block: Environment.() -> Unit) {
        builder += "environment:"
        indent {
            Environment.block()
        }
    }
}

object Environment {

    var name: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "name: $value"
        }

    var url: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "url: $value"
        }
}