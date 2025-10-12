import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption

data class CocheCSV(val id_coche: Int, val nombre_modelo: String, val nombre_marca: String, val consumo: Double, val HP: Int)

data class CocheBinario(val id_coche: Int, val nombre_modelo: String, val nombre_marca: String, val consumo: Double, val HP: Int)
// Tamaño fijo para cada registro en el fichero
const val TAMANO_ID = Int.SIZE_BYTES //4Bytes
const val TAMANO_MODELO = 40 //40bytes
const val TAMANO_MARCA = 40
const val TAMANO_CONSUMO = Double.SIZE_BYTES //8 bytes
const val TAMANO_HP = Int.SIZE_BYTES

const val TAMANO_REGISTRO = TAMANO_ID + TAMANO_MODELO + TAMANO_MARCA + TAMANO_CONSUMO + TAMANO_HP //96 bytes


fun vaciarCrearFichero(path: Path) {
    try {
        FileChannel.open(path, StandardOpenOption.WRITE,
            StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING).close()
        println("El fichero '${path.fileName}' existe y está vacío.")
    } catch (e: Exception) {
        println("Error al vaciar o crear el fichero: ${e.message}")
    }
}


fun anadir(path: Path, id_coche: Int, nombre_modelo: String, nombre_marca: String, consumo: Double, HP: Int) {
    val nuevaCocche = CocheBinario(id_coche,  nombre_modelo,  nombre_marca,  consumo,  HP)

    try {
        FileChannel.open(path, StandardOpenOption.WRITE,
            StandardOpenOption.CREATE, StandardOpenOption.APPEND).use { canal ->
            val buffer = ByteBuffer.allocate(TAMANO_REGISTRO)

            buffer.putInt(nuevaCocche.id_coche)

            val modeloBytes = nuevaCocche.nombre_modelo
                .padEnd(40, ' ')
                .toByteArray(Charset.defaultCharset())
            buffer.put(modeloBytes, 0, 40)
            val marcaBytes = nuevaCocche.nombre_marca
                .padEnd(40, ' ')
                .toByteArray(Charset.defaultCharset())
            buffer.put(marcaBytes, 0, 40)

            buffer.putDouble(nuevaCocche.consumo)
            buffer.putInt(nuevaCocche.HP)

            buffer.flip()
            while (buffer.hasRemaining()) {
                canal.write(buffer)
            }
            println("Coche ${nuevaCocche.nombre_marca} '${nuevaCocche.nombre_modelo}' añadido con éxito.")
        }
    } catch (e: Exception) {
        println("Error al añadir la coche: ${e.message}")
    }
}


fun importar(archivoInicial: Path, archivoFinal: Path){
    val coches = leerCSV(archivoInicial)
    coches.forEach { coche ->
        anadir(archivoFinal, coche.id_coche, coche.nombre_modelo, coche.nombre_marca,
            coche.consumo, coche.HP)
    }
}
fun leerCSV(ruta: Path): List<CocheBinario>
{
    var coches: List<CocheBinario> =emptyList()
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
                    CocheBinario(id_coche,nombre_modelo, nombre_marca, consumo, hp)
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

fun mostrar(path: Path): List<CocheBinario> {
    //como en la documentacion de referencia esta funcion devolvia una lista para luego ser tratada
    //lo he deado igual salvo que printeo tambien lo que se ha leido
    val coches = mutableListOf<CocheBinario>()
    FileChannel.open(path, StandardOpenOption.READ).use { canal ->
        val buffer = ByteBuffer.allocate(TAMANO_REGISTRO)
        while (canal.read(buffer) > 0) {
            buffer.flip()

            val id = buffer.getInt()

            val modeloBytes = ByteArray(TAMANO_MODELO)
            buffer.get(modeloBytes)
            val modelo = String(modeloBytes,
                Charset.defaultCharset()).trim()

            val marcaBytes = ByteArray(TAMANO_MARCA)
            buffer.get(marcaBytes)
            val marca = String(marcaBytes,
                Charset.defaultCharset()).trim()


            val consumo = buffer.getDouble()
            val HP = buffer.getInt()

            coches.add(CocheBinario(id, modelo, marca, consumo, HP))
            println("ID: ${id}, Modelo: ${modelo}, Marca: ${marca}," +
                    " Consumo: ${consumo}, Potencia: ${HP}")
            buffer.clear()
        }
    }
    return coches
}

fun modificar(path: Path, idCoche: Int, nuevoHP: Int)
{       //modificamos HP horsepower
    FileChannel.open(path, StandardOpenOption.READ,
        StandardOpenOption.WRITE).use { canal ->
        val buffer = ByteBuffer.allocate(TAMANO_REGISTRO)
        var encontrado = false
        while (canal.read(buffer) > 0 && !encontrado) {
            val posicionActual = canal.position()
            buffer.flip()
            val id = buffer.getInt()
            if (id == idCoche) {
                val posicionConsumo = posicionActual - TAMANO_REGISTRO + TAMANO_ID + TAMANO_MODELO + TAMANO_MARCA + TAMANO_CONSUMO

                val bufferHP = ByteBuffer.allocate(TAMANO_HP)
                bufferHP.putInt(nuevoHP)

                bufferHP.flip()
                canal.write(bufferHP, posicionConsumo)
                encontrado = true
            }
            buffer.clear()
        }
        if (encontrado) {
            println("Potencia del coche con ID $idCoche modificado a $nuevoHP")
        } else {
            println("No se encontró el coche con ID $idCoche")
        }
    }
}
fun eliminar(path: Path, idCoche: Int) {
    val pathTemporal = Paths.get(path.toString() + ".tmp")
    var cocheEncontrado = false
    FileChannel.open(path, StandardOpenOption.READ).use { canalLectura ->
        FileChannel.open(pathTemporal, StandardOpenOption.WRITE,
            StandardOpenOption.CREATE).use { canalEscritura ->
            val buffer = ByteBuffer.allocate(TAMANO_REGISTRO)
            while (canalLectura.read(buffer) > 0) {
                buffer.flip()
                val id = buffer.getInt()
                if (id == idCoche) {
                    cocheEncontrado = true
                } else {
                    buffer.rewind()
                    canalEscritura.write(buffer)
                }
                buffer.clear()
            }
        }
    }
    if (cocheEncontrado) {
        Files.move(pathTemporal, path, StandardCopyOption.REPLACE_EXISTING)
        println("Coche con ID $idCoche eliminado con éxito.")
    } else {
        Files.delete(pathTemporal)
        println("No se encontró ningun conche con ID $idCoche.")
    }
}
