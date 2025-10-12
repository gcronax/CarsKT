# Gestor de Coches
CarsKT es un sistema de gestión de biblioteca desarrollado en Kotlin que permite administrar
un catálogo de coches mediante múltiples formatos de archivo. El sistema soporta operaciones
CRUD para el archivo en formato binario.

# Estructura de Datos
## Data class
```kotlin
data class CocheBinario(
    val id_coche: Int,      // 4 bytes
    val nombre_modelo: String,     // 40 bytes
    val nombre_marca: String,      // 40 bytes
    val consumo: Double,      // 8 bytes
    val HP: Int      // 4 bytes
)
```
## **Estructura del registro binario**
- **id_libro:** Int - 4 bytes
- **modelo:** String - 40 bytes (longitud fija)
- **marca:** String - 40 bytes (longitud fija)
- **consumo:** Double - 8 bytes
- **HP:** Int - 4 bytes
- **Tamaño Total del Registro:** 4 + 40 + 40 + 8 + 4 = 96 bytes

# Instrucciones de ejecución
- **Requisitos previos:** Asegúrate de tener un JDK (ej. versión 17 o superior) instalado.

- **Compilación:** Abre el proyecto en IntelliJ IDEA y deja que Gradle sincronice las dependencias.

- **Ejecución:** Ejecuta la función main del fichero Main.kt. El programa trabaja sobre el fichero coches.bin en el directorio datos_ini, pero para poder
  trabajar con él hay que utilizar primero la función importar(), esta buscará el archivo coches.csv en el directorio
  datos_ini y reescribirá el fichero coches.bin.


# Decisiones de diseño
- Elegí CSV para los datos iniciales porque es un formato muy fácil de crear y sobre todo de manejar.

