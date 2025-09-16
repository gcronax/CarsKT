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
}

fun leer_ficheros(directorio: String) {
    val carpetaPrincipal = Path.of(directorio)
    println("--- Mostrando la estructura final con Files.walk() ---")
    try {
        Files.walk(carpetaPrincipal).use { stream ->
            stream.sorted().forEach { path ->
                val profundidad = path.nameCount - carpetaPrincipal.nameCount
                val indentacion = "\t".repeat(profundidad)
                val prefijo = if (Files.isDirectory(path)) "[DIR]" else "[FILE]"
                if (profundidad > 0) {
                    println("$indentacion$prefijo ${path.fileName}")
                }
            }
        }
    } catch (e: Exception) {
        println("\n--- Ocurrió un error durante el recorrido ---")
        e.printStackTrace()
    }
}
