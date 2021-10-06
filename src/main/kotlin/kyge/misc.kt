package kyge

import java.io.File
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.text.StringBuilder

internal lateinit var file: File
internal var indentation = 0
internal val builder = StringBuilder()
internal var skipIndentation = false
internal var bulletPoint: Boolean? = null

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

fun bulletPoint(block: () -> Unit) {
    bulletPoint = true
    //    indent {
    block()
    //    }
    bulletPoint = null
}

operator fun StringBuilder.plusAssign(string: String) {
    append(" ".repeat(indentation))
    bulletPoint?.let {
        append(when {
                   it -> {
                       bulletPoint = false
                       "- "
                   }
                   else -> "  "
               })
    }
    appendLine(string)
}

fun String.appendMultiLine(vararg strings: String) {
    indent {
        val indent = " ".repeat(indentation)
        builder.appendLine("$indent$this |")
        for (s in strings)
            builder += s
    }
}

@DslMarker
annotation class KygeMarker

val String.singleQuote: Boolean
    get() = first() == '*' || any { !it.isLetterOrDigit() && it != '.' && it != '*' }

val String.ref: String
    get() = "\${{ $this }}"