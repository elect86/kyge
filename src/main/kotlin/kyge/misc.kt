package kyge

import kyge.actions.checkout
import java.io.File
import kotlin.text.StringBuilder

internal lateinit var file: File
internal var indentation = 0
internal val builder = StringBuilder()
internal var skipIndentation = false

fun String.yml(block: Workflow.() -> Unit) {
    file = File("$this.yml")
    if (file.exists()) file.delete()
    check(file.createNewFile())
    val wf = Workflow()
    wf.block()
    file.writeText(builder.toString())
}
//    set(value) {
//        field = value
//        File("$value.yml").createNewFile()
//    }

fun indent(block: () -> Unit) {
    indentation += 2
    block()
    indentation -= 2
}

operator fun StringBuilder.plusAssign(string: String) {
    if (!skipIndentation)
        append(" ".repeat(indentation))
    else
        skipIndentation = false
    appendLine(string)
}

fun StringBuilder.appendMultiLine(name: String, vararg strings: String) {
    val indent = " ".repeat(indentation)
    builder.appendLine("$indent$name |")
    indent {
        for (s in strings)
            builder += s
    }
}

@DslMarker
annotation class KygeMarker

interface Action<T> {
    val actionName: String
    val actionVersion: String
    val t: T

    //    interface Builder
    operator fun invoke(block: T.() -> Unit) {
        builder += "- uses: actions/$actionName@$actionVersion"
        indent {
            checkout.v2
            builder += "with:"
            indent {
                t.block()
            }
        }
    }
}