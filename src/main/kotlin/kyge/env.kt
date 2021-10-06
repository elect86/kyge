package kyge

interface EnvInterface {

    var env: Pair<String, String>
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = env(value)

    fun env(vararg envs: Pair<String, String>) {
        builder += "env:"
        indent {
            for (env in envs)
                builder += "${env.first}: ${env.second}"
        }
    }

    fun env(block: Env.() -> Unit) {
        builder += "env:"
        indent {
            Env.block()
        }
    }
}

object Env {
    infix fun String.to(string: String) {
        builder += "$this: $string"
    }

    infix fun String.to(matrix: Matrix<*>) {
        builder += "$this: $matrix"
    }
}