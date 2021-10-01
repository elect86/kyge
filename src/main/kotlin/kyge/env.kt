package kyge

interface Env {

    fun env(vararg envs: Pair<String, String>) {
        builder += "env:"
        indent {
            for (env in envs)
                builder += "${env.first}: ${env.second}"
        }
    }
}