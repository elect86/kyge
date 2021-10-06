package kyge

import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty


interface SecretsInterface {

    fun secrets(block: Secrets.() -> Unit) {
        builder += "secrets:"
        indent {
            Secrets.block()
        }
    }

    fun secrets(vararg secrets: Secret) {
        for (secret in secrets) {
            builder += "${secret.id}:"
            secret.configure(secret)
        }
    }

    var secret: Pair<String, Any>
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "secrets:"
            indent {
                builder += "${value.first}: ${value.second}"
            }
        }
}

object Secrets {

    operator fun get(secret: String) = "secrets.$secret".ref
}

class Secret(val id: String) {

    internal var configure: Secret.() -> Unit = {}

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

    override fun toString() = "secrets.$id".ref
}

fun Secret(block: Secret.() -> Unit) = PropertyDelegateProvider { _: Any?, prop ->
    val secret = Secret(prop.name).apply {
        configure = block
    }
    ReadOnlyProperty<Any?, Secret> { _, _ -> secret }
}