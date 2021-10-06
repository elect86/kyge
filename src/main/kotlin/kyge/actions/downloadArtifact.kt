package kyge.actions

import kyge.Action
import kyge.Step
import kyge.builder
import kyge.plusAssign

object downloadArtifact {

    val v2 = V2

    object V2 : Step(), Action<V2.Builder> {

        override val actionName = "download-artifact"
        override val actionVersion = "v2"
        override val t = Builder()

        class Builder {

            var name: String
                @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
                set(value) {
                    builder += "name: $value"
                }

            infix fun String.to(path: String) {
                builder += "name: $this"
                builder += "path: $path"
            }
            var downloadAllto: String
                @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
                set(value) {
                    builder += "path: $value"
                }
        }
    }
}