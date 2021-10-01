package kyge.triggerEvents

import kyge.*
import kyge.builder


interface WorkflowDispatch {

    fun workflowDispatch() {
        indent {
            builder += "workflow_dispatch:"
        }
    }

    fun workflowDispatch(block: InputOutput.() -> Unit) {
        indent {
            builder += "workflow_dispatch:"
            indent {
                InputOutput().block()
            }
        }
    }

    class InputOutput : Inputs, Outputs
}

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

