package kyge

interface ServiceInterface {

    fun services(block: Services.() -> Unit) {
        builder += "services:"
        indent {
            Services.block()
        }
    }
}

@KygeMarker
object Services {

    operator fun String.invoke(block: Service.() -> Unit) {
        builder += "$this:"
        indent {
            Service.block()
        }
    }
}

@KygeMarker
object Service : EnvInterface,
                 OptionsInterface,
                 VolumesInterface {

    var image: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "image: $value"
        }

    fun credentials(username: String, password: String) {
        builder += "credentials:"
        indent {
            builder += "username: $username"
            builder += "password: $password"
        }
    }

    var port: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = ports(value)

    fun ports(vararg ports: String) {
        builder += "ports:"
        indent {
            for (port in ports)
                builder += "- $port"
        }
    }
}