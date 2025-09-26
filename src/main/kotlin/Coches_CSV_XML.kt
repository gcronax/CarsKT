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

data class AutoXML(
    @JacksonXmlProperty(localName = "id_auto")
    val id_auto: Int,
    @JacksonXmlProperty(localName = "modelo")
    val modelo: String,
    @JacksonXmlProperty(localName = "marca")
    val marca: String,
    @JacksonXmlProperty(localName = "consumo")
    val consumo: Double,
    @JacksonXmlProperty(localName = "potencia")
    val potencia: Int
)
data class AutoXML_raiz(@JacksonXmlElementWrapper(useWrapping = false)
                         @JacksonXmlProperty(localName = "auto")
                         val listaAutos: List<AutoXML> = emptyList()
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

    val datosXML =datos.map { Coche_csv_xml -> AutoXML(
        id_auto = Coche_csv_xml.id_coche,
        modelo = Coche_csv_xml.nombre_modelo,
        marca = Coche_csv_xml.nombre_marca,
        consumo = Coche_csv_xml.consumo,
        potencia = Coche_csv_xml.HP
    )
    }



    escribirDatosCSV_XML(salidaCSV, datosXML)
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
                    val id_coche = columnas[0].toInt()
                    val nombre_modelo = columnas[1]
                    val nombre_marca = columnas[2]
                    val consumo = columnas[3].toDouble()
                    val hp = columnas[4].toInt()
                    Coche_csv_xml(id_coche,nombre_modelo, nombre_marca, consumo, hp)
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


fun escribirDatosCSV_XML(ruta: Path, autosXML: List<AutoXML>) {
    try {
        val fichero: File = ruta.toFile()
        val contenedorXml = AutoXML_raiz(autosXML)
        val xmlMapper = XmlMapper().registerKotlinModule()

        val xmlString =
            xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(contenedorXml)
        fichero.writeText(xmlString)
        println("\nInformación guardada en: $fichero")
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}