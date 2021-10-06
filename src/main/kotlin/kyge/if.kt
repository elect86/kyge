package kyge

import kotlin.String

@JvmInline
value class Expression(val value: String) {
    infix fun `&&`(exp: Expression) = Expression("$value && $exp")

    override fun toString() = value
}

interface ExpA
interface ExpOp
interface ExpB