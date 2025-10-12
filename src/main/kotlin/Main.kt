import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
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
    if (leer_ficheros("datos_ini","coches.csv")){
        cochesCSV()
        coches_csv_a_xml()
    }
    if (leer_ficheros("datos_ini","coches.xml")){
        cochesXML()
    }
    if (leer_ficheros("datos_ini","coches.json")){
        cochesJson()
    }
    val cochecsvPath: Path = Paths.get("coches.csv")

    val cochesBinPath: Path = Paths.get("coches.bin")







}


fun leer_ficheros(directorio: String, archivo: String): Boolean {
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
    return maybe

}
