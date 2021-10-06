package kyge.triggerEvents

import kyge.*
import kyge.builder
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty


interface WorkflowDispatchInterface {

    fun workflowDispatch() {
        indent {
            builder += "workflow_dispatch:"
        }
    }

    fun workflowDispatch(block: WorkflowDispatch.() -> Unit) {
        builder += "workflow_dispatch:"
        indent {
            WorkflowDispatch().block()
        }
    }
}

class WorkflowDispatch : InputsInterface

interface RepositoryDispatch {

    fun repositoryDispatch() {
        indent {
            builder += "repository_dispatch:"
        }
    }

    fun repositoryDispatch(vararg types: Type) {
        indent {
            builder += "repository_dispatch:"
            indent {
                builder += "types: [${types.joinToString()}]"
            }
        }
    }

    enum class Type { created, rerequested, completed }
}

interface WorkflowCallInterface {

    fun workflowCallInputs(vararg inputs: WorkflowCall.Input) =
        workflowCallInputs {
            for (i in inputs) {
                builder += "${i.id}:"
                indent {
                    i.configure(i)
                }
            }
        }

    fun workflowCallSecrets(vararg secrets: Secret) =
        workflowCallSecrets {
            for (s in secrets) {
                builder += "${s.id}:"
                indent {
                    s.configure(s)
                }
            }
        }

    fun workflowCallInputs(block: WorkflowCall.() -> Unit) {
        workflowCall {
            builder += "inputs:"
            indent {
                WorkflowCall().block()
            }
        }
    }

    fun workflowCallSecrets(block: WorkflowCall.() -> Unit) {
        workflowCall {
            builder += "secrets:"
            indent {
                WorkflowCall().block()
            }
        }
    }

    fun workflowCall(block: WorkflowCall.() -> Unit) {
        builder += "workflow_call:"
        indent {
            WorkflowCall().block()
        }
    }
}

class WorkflowCall : SecretsInterface {

    operator fun String.invoke(block: Input.() -> Unit) {
        builder += "$this:"
        indent {
            Input(this).block()
        }
    }

    companion object {
        fun Input(block: Input.() -> Unit) = PropertyDelegateProvider { _: Any?, prop ->
            val input = Input(prop.name).apply {
                configure = block
            }
            ReadOnlyProperty<Any?, Input> { _, _ -> input }
        }
    }

    /** Only for workflowCall */
    class Input(id: String) : kyge.Input(id) {

        var configure: Input.() -> Unit = {}

        enum class Type { boolean, number, string }

        var type: Type
            @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
            set(value) {
                builder += "type: $value"
            }

        override fun toString() = "inputs.$id".ref
    }
}