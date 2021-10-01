package kyge

import kyge.actions.checkout

interface StepsInterface {

    fun steps(block: Steps.() -> Unit) {
        builder += "steps:"
        indent {
            Steps().block()
        }
    }
}

class Steps {

    fun step(block: Step.() -> Unit) {
        builder.append(" ".repeat(indentation) + "- ")
        skipIndentation = true
        indent {
            Step().block()
        }
    }

    operator fun String.invoke(block: Step.() -> Unit) =
        step {
            id = this@invoke
            block()
        }

    val checkout = kyge.actions.checkout
    val uploadArtifact = kyge.actions.uploadArtifact
}

open class Step {

    var id: String = ""
        set(value) {
            builder += "id: $value"
            field = value
        }
    var name: String = ""
        set(value) {
            builder += "name: $value"
            field = value
        }

    var uses: String = ""
        set(value) {
            builder += "uses: $value"
            field = value
        }
}