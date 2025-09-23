import java.nio.file.Files
import java.nio.file.Path
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class Cochejson(val id: Int, val modelo: String, val
marca: String, val consumo_litros_por_100km: Double, val potencia_hp: Int)
fun main() {
    val entradaJSON = Path.of("datos_ini/coches.json")
    val salidaJSON = Path.of("datos_ini/coches2.json")
    val datos: List<Cochejson>
    datos = leerDatosInicialesJSON(entradaJSON)
    for (dato in datos) {
        println(" - ID: ${dato.id}, Modelo: ${dato.modelo}, " +
                "Marca: ${dato.marca}, Consumo:" +
                "${dato.consumo_litros_por_100km} potencia: ${dato.potencia_hp} ")
    }
    escribirDatosJSON(salidaJSON, datos)
}
fun leerDatosInicialesJSON(ruta: Path): List<Cochejson> {
    var coches: List<Cochejson> =emptyList()
    val jsonString = Files.readString(ruta)
    coches = Json.decodeFromString<List<Cochejson>>(jsonString)
    return coches
}
fun escribirDatosJSON(ruta: Path, coches: List<Cochejson>) {
    try {
        val json = Json { prettyPrint = true }.encodeToString(coches)
        Files.writeString(ruta, json)
        println("\nInformación guardada en: $ruta")
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}