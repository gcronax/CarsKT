import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.extension // Extensión de Kotlin para obtener la extensión
fun main() {
// 1. Ruta de la carpeta a organizar
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
}

fun leer_ficheros(directorio: String, archivo: String) {
    val carpetaPrincipal = Path.of(directorio)
    try {
        Files.walk(carpetaPrincipal).use { stream ->
            stream.sorted().forEach { path ->
                if (path.fileName.toString() == archivo){
                    println("se ha encontrado "+archivo)
                }

            }
        }
    } catch (e: Exception) {
        println("\n--- Ocurrió un error durante el recorrido ---")
        e.printStackTrace()
    }
}
