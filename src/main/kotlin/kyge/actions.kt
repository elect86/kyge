package kyge

import kyge.actions.checkout
import kyge.actions.downloadArtifact
import kyge.actions.uploadArtifact

interface Action<T> : WithInterface {
    val actionName: String
    val actionVersion: String
    val actionNameVersioned: String
        get() = "$actionName@$actionVersion"
    val t: T

    //    interface Builder
//    operator fun invoke(block: T.() -> Unit) {
//        builder += "with:"
//        indent {
//            t.block()
//        }
//    }

    operator fun invoke(block: With.() -> Unit) {
        builder += "with:"
        indent {
            With.block()
        }
    }
}

interface Actions {

    val checkout: checkout
        get() = kyge.actions.checkout
    val uploadArtifact: uploadArtifact
        get() = kyge.actions.uploadArtifact
    val downloadArtifact: downloadArtifact
        get() = kyge.actions.downloadArtifact
}