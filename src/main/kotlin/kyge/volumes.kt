package kyge

interface VolumesInterface {

    var volume: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = volumes(value)

    fun volumes(vararg volumes: String) {
        builder += "volumes:"
        indent {
            for (volume in volumes)
                builder += "- $volume"
        }
    }
}