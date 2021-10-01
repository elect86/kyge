package kyge.actions

import kyge.*
import kyge.builder

object uploadArtifact {

    val v2 = V2

    object V2 : Step(), Action<V2.Builder> {

        override val actionName = "upload-artifact"
        override val actionVersion = "v2"
        override val t = Builder()

        class Builder {

            infix fun String.to(path: String) {
                builder += "name: $this"
                builder += "path: $path"
            }

            fun String.to(vararg paths: String) {
                builder += "name: $this"
                builder.appendMultiLine("path:", *paths)
            }

            var onFileNotFound: Policy
                @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
                set(value) {
                    builder += "if-no-files-found: $value"
                }


            enum class Policy { warn, ignore }
        }
    }
}