import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.extension // Extensión de Kotlin para obtener la extensión
fun main() {

    val carpeta_ini = Path.of("datos_ini")
    val carpeta_fin = Path.of("datos_fin")
    try {
        if (Files.notExists(carpeta_ini)) {
                        println("-> Creando nueva carpeta " + carpeta_ini.fileName)
                        Files.createDirectories(carpeta_ini.fileName)
                    }
        if (Files.notExists(carpeta_fin)) {
            println("-> Creando nueva carpeta " + carpeta_fin.fileName)
            Files.createDirectories(carpeta_fin.fileName)
        }
    } catch (e: Exception) {
        println("\n--- Ocurrió un error durante la organización ---")
        e.printStackTrace()
    }
    leer_ficheros("datos_ini","text.txt")
    cochesCSV()
    cochesXML()
    cochesJson()
    coches_csv_a_xml()
    pruebas()
}

fun leer_ficheros(directorio: String, archivo: String) {
    val carpetaPrincipal = Path.of(directorio)
    var maybe = false
    try {
        Files.walk(carpetaPrincipal).use { stream ->
            stream.sorted().forEach { path ->
                if (path.fileName.toString() == archivo){
                    maybe=true
                }
            }
        }
        if (maybe){
            println("Se ha encontrado "+archivo)
        } else{
            println("No se ha encontrado "+archivo)

        }

    } catch (e: Exception) {
        println("\n--- Ocurrió un error durante el recorrido ---")
        e.printStackTrace()
    }

}
