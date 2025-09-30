import java.nio.file.Files
import java.nio.file.Path
import java.io.File
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
data class Cochecsv(val id_coche: Int, val nombre_modelo: String, val
nombre_marca: String, val consumo: Double, val HP: Int)

fun cochesCSV() {
    val entradaCSV = Path.of("datos_ini/coches.csv")
    val salidaCSV = Path.of("datos_ini/coches2.csv")
    val datos: List<Coche_csv_xml>
    datos = leerDatosInicialesCSV(entradaCSV)
    for (dato in datos) {println(" - ID: ${dato.id_coche}," +
            " Nombre modelo: ${dato.nombre_modelo}, Nombre marca: ${dato.nombre_marca}," +
            " Consumo: ${dato.consumo}, Cavallos: ${dato.HP} metros")
    }
    escribirDatosCSV(salidaCSV, datos)
}
fun leerDatosInicialesCSV(ruta: Path): List<Coche_csv_xml>
{
    var coches: List<Coche_csv_xml> =emptyList()
    if (!Files.isReadable(ruta)) {
        println("Error: No se puede leer el fichero en la ruta: $ruta")
    } else{
        val reader = csvReader {
            delimiter = ';'
        }
        val filas: List<List<String>> = reader.readAll(ruta.toFile())
        coches = filas.mapNotNull { columnas ->
            if (columnas.size >= 5) {
                try {
                    val id_planta = columnas[0].toInt()
                    val nombre_modelo = columnas[1]
                    val nombre_marca = columnas[2]
                    val consumo = columnas[3].toDouble()
                    val hp = columnas[4].toInt()
                    Coche_csv_xml(id_planta,nombre_modelo, nombre_marca, consumo, hp)
                } catch (e: Exception) {

                    println("Fila inválida ignorada: $columnas -> Error: ${e.message}")
                    null
                }
            } else {
                println("Fila con formato incorrecto ignorada: $columnas")
                null
            }
        }
    }
    return coches
}
fun escribirDatosCSV(ruta: Path, coches: List<Coche_csv_xml>){
    try {
        val fichero: File = ruta.toFile()
        csvWriter {
            delimiter = ';'
        }.writeAll(
            coches.map { coche ->
                listOf(coche.id_coche.toString(),
                    coche.nombre_modelo,
                    coche.nombre_marca,
                    coche.consumo.toString(),
                    coche.HP.toString())
            },
            fichero
        )
        println("\nInformación guardada en: $fichero")
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}