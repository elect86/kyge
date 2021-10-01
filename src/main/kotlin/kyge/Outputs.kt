package kyge

interface Outputs {

    fun outputs(block: OutputId.() -> Unit) {
        builder += "outputs:"
        indent {
            OutputId.block()
        }
    }

    object OutputId {
        operator fun String.invoke(block: OutputBuilder.() -> Unit) {
            builder += "$this:"
            indent {
                OutputBuilder().apply {
                    block()
                    github.event.outputs += output
                }
            }
        }

        class OutputBuilder {
            val output = github.event.Output()
            var description: String
                @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
                set(value) {
                    builder += "description: '$value'"
                    output.description = value
                }
        }
    }
}