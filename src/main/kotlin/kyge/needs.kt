package kyge

interface NeedsInterface {

    var needs: Job
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "needs: $value"
        }

    fun needs(vararg jobs: String) {
        builder += when (jobs.size) {
            1 -> "needs: ${jobs[0]}"
            else -> "needs: [${jobs.joinToString()}]"
        }
    }

    fun needs(vararg jobs: Job) {
        builder += when (jobs.size) {
            1 -> "needs: ${jobs[0]}"
            else -> "needs: [${jobs.joinToString()}]"
        }
    }
}