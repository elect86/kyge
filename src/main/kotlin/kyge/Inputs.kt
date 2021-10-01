package kyge

interface Inputs {

    fun inputs(block: InputId.() -> Unit) {
        builder += "inputs:"
        indent {
            InputId.block()
        }
    }

    object InputId {
        operator fun String.invoke(block: InputBuilder.() -> Unit) {
            builder += "$this:"
            indent {
                InputBuilder().apply {
                    block()
                    github.event.inputs += input
                }
            }
        }

        class InputBuilder {
            val input = github.event.Input()
            var description: String
                @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
                set(value) {
                    builder += "description: '$value'"
                    input.description = value
                }

            var required: Boolean
                get() {
                    builder += "required: true"
                    input.required = true
                    return true
                }
                set(value) {
                    builder += "required: $value"
                    input.required = value
                }

            var default: String
                @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
                set(value) {
                    input.default = value
                    builder += "default: '$value'"
                }
        }
    }
}

