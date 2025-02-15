import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LectorArchivo {
    public static List<Personaje> cargarPersonajes(String rutaArchivo) throws IOException {
        List<Personaje> personajes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                String nombre = datos[0].trim();
                int salud = Integer.parseInt(datos[1].trim());
                int ataque = Integer.parseInt(datos[2].trim());
                int defensa = Integer.parseInt(datos[3].trim());
                personajes.add(new Personaje(nombre, salud, ataque, defensa));
            }
        }
        return personajes;
    }
}