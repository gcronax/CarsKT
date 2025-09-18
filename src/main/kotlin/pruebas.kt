import java.nio.file.Files
import java.nio.file.Path
import java.io.File
// Clases de la librería oficial de Kotlin para la serialización/deserialización.
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class Planta(val id_planta: Int, val nombre_comun: String, val
nombre_cientifico: String, val frecuencia_riego: Int, val altura_maxima: Double)
fun main() {
    val entradaJSON = Path.of("datos_ini/mis_plantas.json")
    val salidaJSON = Path.of("datos_ini/mis_plantas2.json")
    val datos: List<Planta>
    datos = leerDatosInicialesJSON(entradaJSON)
    for (dato in datos) {
        println(" - ID: ${dato.id_planta}, Nombre común: ${dato.nombre_comun}, " +
                "Nombre científico: ${dato.nombre_cientifico}, Frecuencia de riego:" +
                "${dato.frecuencia_riego} días, Altura: ${dato.altura_maxima} metros")
    }
    escribirDatosJSON(salidaJSON, datos)
}
fun leerDatosInicialesJSON(ruta: Path): List<Planta> {
    var plantas: List<Planta> =emptyList()
    val jsonString = Files.readString(ruta)
    /* A `Json.decodeFromString` le pasamos el String con el JSON.
    Con `<List<Planta>>`, le indicamos que debe interpretarlo como
    una lista de objetos de tipo planta".
    La librería usará la anotación @Serializable de la clase Planta para saber
    cómo mapear los campos del JSON ("id_planta", "nombre_comun", etc.)
    a las propiedades del objeto. */
    plantas = Json.decodeFromString<List<Planta>>(jsonString)
    return plantas
}
fun escribirDatosJSON(ruta: Path,plantas: List<Planta>) {
    try {
        /* La librería `kotlinx.serialization`
        toma la lista de objetos `Planta` (`List<Planta>`) y la convierte en una
        única cadena de texto con formato JSON.
        `prettyPrint` formatea el JSON para que sea legible. */
        val json = Json { prettyPrint = true }.encodeToString(plantas)
// Con `Files.writeString` escribimos el String JSON en el fichero de salida
        Files.writeString(ruta, json)
        println("\nInformación guardada en: $ruta")
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}