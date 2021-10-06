package kyge

import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty

interface StrategyInterface {

    var _inStrategy: Boolean

    fun strategy(block: Strategy.() -> Unit) {
        if (_inStrategy)
            _inStrategy = false
        else {
            builder += "strategy:"
            _inStrategy = true
        }
        indent {
            Strategy.block()
        }
    }

    fun <T> Matrix(vararg element: T) = PropertyDelegateProvider { _: Any?, prop ->
        val matrix = Matrix<T>(prop.name, element.toList())
        if (element.isNotEmpty()) {
            val text = "${matrix.name}: [${matrix.joinToString()}]"
            if (_inStrategy) {
                builder += "    $text"
            } else {
                _inStrategy = true
                builder += "strategy:"
                indent {
                    builder += "matrix:"
                    indent {
                        builder += text
                    }
                }
            }
        }
        ReadOnlyProperty<Any?, Matrix<T>> { _, _ -> matrix }
    }

    fun includeMatrix(block: IncludeMatrix.() -> Unit) {
        indent {
            indent {
                builder += "include:"
                indent {
                    bulletPoint {
                        IncludeMatrix.block()
                    }
                }
            }
        }
    }

    fun excludeMatrix(block: ExcludeMatrix.() -> Unit) {
        indent {
            indent {
                builder += "exclude:"
                indent {
                    bulletPoint {
                        ExcludeMatrix.block()
                    }
                }
            }
        }
    }
}

object IncludeMatrix {

    operator fun <T> Matrix<T>.plusAssign(t: T) {
        builder += when (t) {
            is String -> "$name: \"$t\""
            else -> "$name: $t"
        }
    }

    infix fun String.to(value: Any) {
        builder += "$this: $value"
    }

    operator fun String.invoke(block: Step.() -> Unit) {
        bulletPoint {
            builder += this
            Step().also(block)
        }
    }
}

object ExcludeMatrix {

    operator fun <T> Matrix<T>.minusAssign(t: T) {
        builder += "$name: $t"
    }

    infix fun String.to(value: Any) {
        builder += "$this: $value"
    }
}

object Strategy {

    fun includeMatrix(block: IncludeMatrix.() -> Unit) {
        builder += "matrix:"
        indent {
            builder += "include:"
            indent {
                bulletPoint {
                    IncludeMatrix.block()
                }
            }
        }
    }

    var failFast: Boolean
        get() {
            builder += "fail-fast: true"
            return true
        }
        set(value) {
            builder += "fail-fast: $value"
        }

    var maxParallel: Int
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "max-parallel: $value"
        }
}

class Matrix<T>(val name: String, list: List<T>) : List<T> by list {

    override fun toString() = "matrix.$name".ref
}

