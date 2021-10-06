package kyge

interface InputsInterface {

    fun inputs(block: Inputs.() -> Unit) {
        builder += "inputs:"
        indent {
            Inputs.block()
        }
    }

    fun input(id: String, block: Input.() -> Unit) {
        builder += "inputs:"
        indent {
            Input(id).block()
        }
    }
}

object Inputs {
    operator fun String.invoke(block: Input.() -> Unit) {
        builder += "$this:"
        indent {
            val input = Input(this).apply(block)
            github.event.inputs += input
        }
    }
}

open class Input(val id: String) {

    var description: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "description: '$value'"
        }

    var required: Boolean
        get() {
            builder += "required: true"
            return true
        }
        set(value) {
            builder += "required: $value"
        }

    var default: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "default: '$value'"
        }

    override fun toString() = "github.event.inputs.$id".ref
}
