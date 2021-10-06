package kyge

import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty

interface OutputsInterface {

    val id: String

//    fun outputs(block: Outputs.() -> Unit) {
//        builder += "outputs:"
//        indent {
//            OutputId.block()
//        }
//    }

    fun outputs(vararg outputs: Output) {
        builder += "outputs:"
        indent {
            for (output in outputs) {
                builder += "${output.name}: ${"steps.${output.stepId}.outputs.${output.name}".ref}"
                output.jobId = id
            }
        }
    }
}

class Output(val name: String) {

    lateinit var jobId: String
    lateinit var stepId: String

    override fun toString() = "needs.$jobId.outputs.$name".ref
}

val newOutput
    get() = PropertyDelegateProvider { _: Any?, prop ->
        val output = Output(prop.name)
        ReadOnlyProperty<Any?, Output> { _, _ -> output }
    }