package kyge

interface ContainerInterface {

    var container: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "container: $value"
        }

    fun container(block: Container.() -> Unit) {
        builder += "container:"
        indent {
            Container.block()
        }
    }
}

@KygeMarker
object Container : EnvInterface,
                   OptionsInterface,
                   VolumesInterface {

    var image: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "image: $value"
        }

    // env -> interface

    var port: Int
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = ports(value)

    fun ports(vararg ports: Int) {
        builder += "ports:"
        indent {
            for (port in ports)
                builder += "- $port"
        }
    }
}

@JvmInline
value class MicroSeconds(val value: Int)

@JvmInline
value class Seconds(val value: Int)