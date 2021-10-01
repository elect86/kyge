package kyge

import kotlin.String

@JvmInline
value class Expression(val value: String)

fun `if`(predicate: () -> Expression, block: Job.Builder.() -> Unit) {
    val jobBuilder = Job.Builder()
    jobBuilder.condition = "if: ${predicate().value}"
    jobBuilder.block()
}

interface ExpA
interface ExpOp
interface ExpB