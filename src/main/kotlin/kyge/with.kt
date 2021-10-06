package kyge

interface WithInterface {

    fun with(block: With.() -> Unit) {
        builder += "with:"
        indent {
            With.block()
        }
    }
}

object With {

    var repository: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "repository: $value"
        }

    var ref: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "ref: $value"
        }

    var token: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "token: $value"
        }

    var path: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "path: $value"
        }


    var entrypoint: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "entrypoint: $value"
        }

    var args: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "args: $value"
        }

    infix fun String.to(value: String) {
        builder += "$this: $value"
    }

    infix fun String.to(matrix: Matrix<*>) {
        builder += "$this: $matrix"
    }
}