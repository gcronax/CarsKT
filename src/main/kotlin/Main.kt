import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.InputMismatchException
import java.util.Scanner

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


    val cocheCsvPath: Path = Paths.get("datos_ini/coches.csv")

    val cochesBinPath: Path = Paths.get("datos_fin/coches.bin")

    vaciarCrearFichero(cochesBinPath)
    importar(cocheCsvPath,cochesBinPath)

    val scanner = Scanner(System.`in`)
    var itera = true

    do {
        println()
        println("   Selecciona una opcion: ")
        println("1. Mostrar todos los registros")
        println("2. Añadir un nuevo registro")
        println("3. Modificar un registro (por ID)")
        println("4. Eliminar un registro (por ID)")
        println("5. Salir")

        try {
            val select = scanner.nextInt()
            scanner.nextLine()
            when (select) {
                1 -> {
                    mostrar(cochesBinPath)
                }
                2 -> {
                    println(" Añadir coche")
                    print("ID: ")
                    val id = scanner.nextInt()
                    scanner.nextLine()
                    print("Modelo: ")
                    val Modelo = scanner.nextLine()
                    print("Marca: ")
                    val Marca = scanner.nextLine()
                    print("Consumo (double): ")
                    val Consumo = scanner.nextDouble()
                    print("Potencia (int): ")
                    val Potencia = scanner.nextInt()
                    scanner.nextLine()

                    anadir(cochesBinPath, id, Modelo, Marca, Consumo, Potencia)
                }
                3 -> {
                    print("ID del coche a modificar: ")
                    val id = scanner.nextInt()
                    print("Nueva Potencia: ")
                    val nuevaPotencia = scanner.nextInt()
                    scanner.nextLine()

                    modificar(cochesBinPath, id, nuevaPotencia)
                }
                4 -> {

                    print("ID del coche a eliminar: ")
                    val id = scanner.nextInt()
                    scanner.nextLine()
                    eliminar(cochesBinPath, id)
                }
                5 -> {
                    itera = false
                }

                else -> {
                    println("Opcion no valida. Por favor, selecciona una opcion del 1 al 5.")
                }
            }

        } catch (e: InputMismatchException) {
            println("Error: Debes introducir un numero valido.")
            scanner.nextLine()
        } catch (e: Exception) {
            println("Error: ${e.message}")
            scanner.nextLine()
        }
    } while (itera)
    scanner.close()

//    pruebasFormatos()
    //pruebas de formatos descomentar para la verificacion de los ejercicios anteriores al 7
}
fun pruebasFormatos(){

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

    val cocheCsvPath: Path = Paths.get("datos_ini/coches.csv")

    val cochesBinPath: Path = Paths.get("datos_fin/coches.bin")

    vaciarCrearFichero(cochesBinPath)
    importar(cocheCsvPath,cochesBinPath)
    mostrar(cochesBinPath)
    anadir(cochesBinPath,6,"modelo","marca",3.2,600)
    mostrar(cochesBinPath)
    eliminar(cochesBinPath,6)
    mostrar(cochesBinPath)
    anadir(cochesBinPath,6,"modelo","marca",3.2,600)
    modificar(cochesBinPath,6,400)
    mostrar(cochesBinPath)



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
