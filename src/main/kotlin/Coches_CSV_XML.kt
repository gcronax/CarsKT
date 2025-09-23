//csv a xml
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.nio.file.Files
import java.nio.file.Path
import java.io.File
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader

data class Coche_csv_xml(val id_coche: Int, val nombre_modelo: String, val
nombre_marca: String, val consumo: Double, val HP: Int)

data class Coches_list_csv_xml(@JacksonXmlElementWrapper(useWrapping = false)
                         @JacksonXmlProperty(localName = "auto")
                         val listaAutos: List<Coche_csv_xml> = emptyList()
)




fun main() {
    val entradaCSV = Path.of("datos_ini/coches.csv")
    val salidaCSV = Path.of("datos_ini/cochesCSV->XML.xml")
    val datos: List<Coche_csv_xml>
    datos = leerDatosInicialesCSV_XML(entradaCSV)
    for (dato in datos) {println(" - ID: ${dato.id_coche}," +
            " Nombre modelo: ${dato.nombre_modelo}, Nombre marca: ${dato.nombre_marca}," +
            " Consumo: ${dato.consumo}, Cavallos: ${dato.HP} metros")
    }
    escribirDatosCSV_XML(salidaCSV, datos)
}
fun leerDatosInicialesCSV_XML(ruta: Path): List<Coche_csv_xml>
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
fun escribirDatosCSV_XML(ruta: Path, coches: List<Coche_csv_xml>){
    try {
        val fichero: File = ruta.toFile()
        val contenedorXml = Coches_list_csv_xml(coches)
        val xmlMapper = XmlMapper().registerKotlinModule()

        val xmlString =
            xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(contenedorXml)
        fichero.writeText(xmlString)
        println("\nInformación guardada en: $fichero")
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}