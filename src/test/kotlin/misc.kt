import kyge.builder
import kotlin.test.BeforeTest

@BeforeTest
fun clearBuilder() {
    println("clearBuilder")
    builder.clear()
}