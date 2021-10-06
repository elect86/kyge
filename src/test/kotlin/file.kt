import kyge.yml
import java.io.File
import kotlin.test.Test

class File {

    @Test
    fun file() {

        val filename = "build"
        filename.yml { }

        val file = File("$filename.yml")
        assert(file.exists())
        file.delete()
    }
}