//import java.nio.ByteBuffer
//import java.nio.channels.FileChannel
//import java.nio.charset.Charset
//import java.nio.file.Files
//import java.nio.file.Path
//import java.nio.file.Paths
//import java.nio.file.StandardCopyOption
//import java.nio.file.StandardOpenOption
//data class PlantaBinaria(val id_planta: Int, val nombre_comun: String, val altura_maxima: Double)
//// Tamaño fijo para cada registro en el fichero
//const val TAMANO_ID = Int.SIZE_BYTES // 4 bytes
//const val TAMANO_NOMBRE = 20 // String de tamaño fijo 20 bytes
//const val TAMANO_ALTURA = Double.SIZE_BYTES // 8 bytes
//const val TAMANO_REGISTRO = TAMANO_ID + TAMANO_NOMBRE + TAMANO_ALTURA
////Función que crea un fichero (si no existe) o lo vacía (si existe)
////Si el fichero existe: CREATE se ignora. TRUNCATE_EXISTING se activa y
////Si el fichero no existe: CREATE se activa y crea un fichero nuevo y
//fun vaciarCrearFichero(path: Path) {
//    try {
//// AÑADIMOS StandardOpenOption.CREATE
//// WRITE: Permite la escritura.
//// CREATE: Crea el fichero si no existe.
//// TRUNCATE_EXISTING: Si el fichero ya existía, lo vacía. Si fue
//        FileChannel.open(path, StandardOpenOption.WRITE,
//            StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING).close()
//        println("El fichero '${path.fileName}' existe y está vacío.")
//    } catch (e: Exception) {
//        println("Error al vaciar o crear el fichero: ${e.message}")
//    }
//}
//fun anadirPlanta(path: Path, idPlanta: Int, nombre: String, altura:
//Double) {
//    val nuevaPlanta = PlantaBinaria(idPlanta, nombre, altura)
//// Abrimos el canal en modo APPEND.
//// CREATE: crea el fichero si no existe.
//// WRITE: es necesario para poder escribir.
//// APPEND: asegura que cada escritura se haga al final del fichero.
//    try {
//        FileChannel.open(path, StandardOpenOption.WRITE,
//            StandardOpenOption.CREATE, StandardOpenOption.APPEND).use { canal ->
//// Creamos un buffer para la nueva planta
//            val buffer = ByteBuffer.allocate(TAMANO_REGISTRO)
//// Llenamos el buffer con los datos de la nueva planta
//// (misma lógica que en la función de escritura inicial)
//            buffer.putInt(nuevaPlanta.id_planta)
//            val nombreBytes = nuevaPlanta.nombre_comun
//                .padEnd(20, ' ')
//                .toByteArray(Charset.defaultCharset())
//            buffer.put(nombreBytes, 0, 20)
//            buffer.putDouble(nuevaPlanta.altura_maxima)
//// Preparamos el buffer para ser leído por el canal
//            buffer.flip()
//// Escribimos el buffer en el canal. Gracias a APPEND,
//// se escribirá al final del fichero.
//            while (buffer.hasRemaining()) {
//                canal.write(buffer)
//            }
//            println("Planta '${nuevaPlanta.nombre_comun}' añadida con éxito.")
//        }
//    } catch (e: Exception) {
//        println("Error al añadir la planta: ${e.message}")
//    }
//}
//fun leerPlantas(path: Path): List<PlantaBinaria> {
//    val plantas = mutableListOf<PlantaBinaria>()
//// Abrimos un canal de solo lectura al fichero
//    FileChannel.open(path, StandardOpenOption.READ).use { canal ->
//// Buffer para leer un registro a la vez
//        val buffer = ByteBuffer.allocate(TAMANO_REGISTRO)
//// Leer del canal al buffer hasta el final del fichero (-1)
//        while (canal.read(buffer) > 0) {
//// Preparamos el buffer para la lectura de datos
//            buffer.flip()
//// Leemos los datos en el mismo orden en que se escribieron
//            val id = buffer.getInt()
//            val nombreBytes = ByteArray(TAMANO_NOMBRE)
//            buffer.get(nombreBytes)
//            val nombre = String(nombreBytes,
//                Charset.defaultCharset()).trim()
//            val altura = buffer.getDouble()
//            plantas.add(PlantaBinaria(id, nombre, altura))
//// Compactamos o limpiamos el buffer para la siguiente lectura
//            buffer.clear()
//        }
//    }
//    return plantas
//}
//
//fun modificarAlturaPlanta(path: Path, idPlanta: Int, nuevaAltura: Double)
//{
//// Abrimos un canal con permisos de lectura y escritura
//    FileChannel.open(path, StandardOpenOption.READ,
//        StandardOpenOption.WRITE).use { canal ->
//        val buffer = ByteBuffer.allocate(TAMANO_REGISTRO)
//        var encontrado = false
//        while (canal.read(buffer) > 0 && !encontrado) {
//            /*canal.read(buffer) lee un bloque completo de 32 bytes
//            y los guarda en el buffer. Después de esta operación,
//            el "puntero" o "cursor" interno del canal (canal.position())
//            ha avanzado 32 bytes y ahora se encuentra al final
//            del registro que acabamos de leer.*/
//            val posicionActual = canal.position()
//            buffer.flip()
//            val id = buffer.getInt()
//            if (id == idPlanta) {
//
//
//                val posicionAltura = posicionActual - TAMANO_REGISTRO +
//                        TAMANO_ID + TAMANO_NOMBRE
//// Creamos un buffer solo para el double
//                val bufferAltura = ByteBuffer.allocate(TAMANO_ALTURA)
//                bufferAltura.putDouble(nuevaAltura)
//                bufferAltura.flip()
//// Escribimos el nuevo valor en la posición exacta del
//                canal.write(bufferAltura, posicionAltura)
//                encontrado = true
//            }
//            buffer.clear()
//        }
//        if (encontrado) {
//            println("Altura de la planta con ID $idPlanta modificada a$nuevaAltura")
//        } else {
//            println("No se encontró la planta con ID $idPlanta")
//        }
//    }
//}
//fun eliminarPlanta(path: Path, idPlanta: Int) {
//// Creamos un fichero temporal en el mismo directorio
//    val pathTemporal = Paths.get(path.toString() + ".tmp")
//    var plantaEncontrada = false
//// Abrimos el canal de lectura para el fichero original
//    FileChannel.open(path, StandardOpenOption.READ).use { canalLectura ->
//// Abrimos el canal de escritura para el fichero temporal
//        FileChannel.open(pathTemporal, StandardOpenOption.WRITE,
//            StandardOpenOption.CREATE).use { canalEscritura ->
//            val buffer = ByteBuffer.allocate(TAMANO_REGISTRO)
//// Leemos el fichero original registro a registro
//            while (canalLectura.read(buffer) > 0) {
//                buffer.flip()
//                val id = buffer.getInt() // Solo necesitamos el ID
//                if (id == idPlanta) {
//                    plantaEncontrada = true
//// Si es la planta a eliminar, no hacemos nada.
//// El buffer se limpiará para la siguiente lectura.
//                } else {
//// Si NO es la planta a eliminar, escribimos el registro
//// completo en el fichero temporal.
//                    buffer.rewind() // Rebobinamos para ir al principio
//                    canalEscritura.write(buffer)
//                }
//                buffer.clear() // Preparamos para siguiente iteración
//            }
//        }
//    }
//    if (plantaEncontrada) {
//        Files.move(pathTemporal, path, StandardCopyOption.REPLACE_EXISTING)
//        println("Planta con ID $idPlanta eliminada con éxito.")
//    } else {
//// Si no se encontró, no hace falta hacer nada, borramos el temporal
//        Files.delete(pathTemporal)
//        println("No se encontró ninguna planta con ID $idPlanta.")
//    }
//}
//fun main() {
//    val archivoPath: Path = Paths.get("plantas.bin")
//    val lista = listOf(
//        PlantaBinaria(1, "Rosa", 1.5),
//        PlantaBinaria(2, "Girasol", 3.0),
//        PlantaBinaria(3, "Margarita", 0.6)
//    )
//// vaciar o crear el fichero
//    vaciarCrearFichero(archivoPath)
//// --- Añadir las plantas al fichero
//    lista.forEach { planta ->
//        anadirPlanta(archivoPath, planta.id_planta, planta.nombre_comun,
//            planta.altura_maxima)
//    }
//
//    modificarAlturaPlanta(archivoPath, 2, 5.5)
//    eliminarPlanta(archivoPath, 3)
//
//    val leidasDespuesDeModificar = leerPlantas(archivoPath)
//
//    val leidas = leerPlantas(archivoPath)
//    for (dato in leidas) {
//        println(" - ID: ${dato.id_planta}, Nombre común:${dato.nombre_comun}, Altura: ${dato.altura_maxima} metros")
//    }
//
//
//}