import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.extension // Extensión de Kotlin para obtener la extensión
fun main() {
    val carpeta = Path.of("multimedia")
    println("--- Iniciando la organización de la carpeta: " + carpeta + "---")
    try {
        Files.list(carpeta).use { streamDePaths ->
            streamDePaths.forEach { pathFichero ->
                if (Files.isRegularFile(pathFichero)) {
                    val extension = pathFichero.extension.lowercase()
                    if (extension.isBlank()) {
                        println("-> Ignorando: " + pathFichero.fileName)
                        return@forEach // Salta a la siguiente iteración del bucle
                    }
                    val carpetaDestino = carpeta.resolve(extension)
                    if (Files.notExists(carpetaDestino)) {
                        println("-> Creando nueva carpeta " + extension)
                        Files.createDirectories(carpetaDestino)
                    }
                    val pathDestino = carpetaDestino.resolve(pathFichero.fileName)
                    Files.move(pathFichero, pathDestino, StandardCopyOption.REPLACE_EXISTING)
                    println("-> Moviendo " + pathFichero.fileName + " a " + extension)
                }
            }
        }
        println("\n--- ¡Organización completada con éxito! ---")
    } catch (e: Exception) {
        println("\n--- Ocurrió un error durante la organización ---")
        e.printStackTrace()
    }
}