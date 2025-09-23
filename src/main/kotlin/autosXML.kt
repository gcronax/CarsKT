import java.nio.file.Path
import java.io.File
// Anotaciones y clases de la librería Jackson para el mapeo a XML.
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
/*Representa la estructura de una única planta. La propiedad 'id_planta' será la
etiqueta <id_planta>...</id_planta> (así todas) */
data class Auto(
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
//nombre del elemento raíz
@JacksonXmlRootElement(localName = "autos")
// Data class que representa el elemento raíz del XML.
data class Coches(@JacksonXmlElementWrapper(useWrapping = false) // No necesitamos la etiqueta <plantas> aquí
                  @JacksonXmlProperty(localName = "auto")
                  val listaAutos: List<Auto> = emptyList()
)fun main() {
    val entradaXML = Path.of("datos_ini/coches.xml")
    val salidaXML = Path.of("datos_ini/coches2.xml")
    val datos: List<Auto>
    datos = leerDatosInicialesXML(entradaXML)
    for (dato in datos) {
        println(" - ID: ${dato.id_auto}, Nombre modelo:" +
                " ${dato.modelo}, Nombre marca: " +
                "${dato.marca}, " +
                "consumo:" +
                "${dato.consumo}" +
                " potencia: ${dato.potencia} ")
    }
    escribirDatosXML(salidaXML, datos)
}
fun leerDatosInicialesXML(ruta: Path): List<Auto> {
    val fichero: File = ruta.toFile()
    val xmlMapper = XmlMapper().registerKotlinModule()
    val cochesWrapper: Coches = xmlMapper.readValue(fichero)
    return cochesWrapper.listaAutos
}
fun escribirDatosXML(ruta: Path, autos: List<Auto>) {
    try {
        val fichero: File = ruta.toFile()
        val contenedorXml = Coches(autos)
        val xmlMapper = XmlMapper().registerKotlinModule()

        val xmlString =
            xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(contenedorXml)
        fichero.writeText(xmlString)
        println("\nInformación guardada en: $fichero")
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}