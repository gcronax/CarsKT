import java.awt.Color
import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import javax.imageio.ImageIO
fun pruebas() {
    val originalPath = Path.of("multimedia/jpg/amanecer1.jpg")
    val copiaPath = Path.of("multimedia/jpg/amanecer1_copia.jpg")
    val grisPath = Path.of("multimedia/jpg/amanecer1_escala_de_grises.png")
    if (!Files.exists(originalPath)) {
        println("No se encuentra la imagen original: $originalPath")
    } else {
        Files.copy(originalPath, copiaPath, StandardCopyOption.REPLACE_EXISTING)
        println("Imagen copiada a: $copiaPath")
        val imagen: BufferedImage = ImageIO.read(copiaPath.toFile())
        for (x in 0 until imagen.width) {
            for (y in 0 until imagen.height) {
                val color = Color(imagen.getRGB(x, y))
                val gris = (color.red * 0.299 + color.green * 0.587 + color.blue *
                        0.114).toInt()
                val colorGris = Color(gris, gris, gris)
                imagen.setRGB(x, y, colorGris.rgb)
            }
        }
        ImageIO.write(imagen, "png", grisPath.toFile())
        println("Imagen convertida a escala de grises y guardada como: $grisPath")
    }
}