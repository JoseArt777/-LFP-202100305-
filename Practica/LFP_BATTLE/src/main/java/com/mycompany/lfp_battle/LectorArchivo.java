package com.mycompany.lfp_battle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LectorArchivo {

    // Método para cargar personajes desde el archivo .lfp
    public static List<Personaje> cargarPersonajes(String rutaArchivo) throws IOException {
        List<Personaje> personajes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            int numeroLinea = 0; // Identifica líneas problemáticas
            while ((linea = br.readLine()) != null) {
                numeroLinea++;
                // Ignora líneas vacías o comentarios
                if (linea.trim().isEmpty() || linea.startsWith("#")) {
                    continue;
                    
                    
                }
                // Dividir la línea por |
                String[] datos = linea.split("\\|");
                if (datos.length == 4) {
                    try {
                        String nombre = datos[0].trim();
                        int salud = Integer.parseInt(datos[1].trim());
                        int ataque = Integer.parseInt(datos[2].trim());
                        int defensa = Integer.parseInt(datos[3].trim());
                        // Crea un objeto personaje
                        Personaje personaje = new Personaje(nombre, salud, ataque, defensa);
                        personajes.add(personaje);
                        // Mensaje de confirmación
                        System.out.println("Línea " + numeroLinea + ": Cargado -> " + personaje);
                    } catch (NumberFormatException e) {
                        System.err.println("Error en la línea " + numeroLinea + ": Los valores numéricos no son válidos.");
                    }
                } else {
                    System.err.println("Error en la línea " + numeroLinea + ": La línea no tiene 4 valores.");
                }
            }
        }
        return personajes;
    }

    // Método para generar un archivo HTML con los datos de los personajes
    public static void generarHTML(List<Personaje> personajes, String rutaHTML) throws IOException {
    try (FileWriter writer = new FileWriter(rutaHTML)) {
        // Escribir el encabezado del HTML
        writer.write("<!DOCTYPE html>\n");
        writer.write("<html lang=\"es\">\n");
        writer.write("<head>\n");
        writer.write("    <meta charset=\"UTF-8\">\n");
        writer.write("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        writer.write("    <title>Lista de Jugadores</title>\n");
        writer.write("    <style>\n");
        writer.write("        body {\n");
        writer.write("            font-family: 'Arial', sans-serif;\n");
        writer.write("            background: #222;\n");
        writer.write("            color: #ddd;\n");
        writer.write("            margin: 0;\n");
        writer.write("            padding: 20px;\n");
        writer.write("            background-image: url('https://www.example.com/bg-image.jpg');\n"); // Puedes cambiar la URL por una imagen de fondo
        writer.write("            background-size: cover;\n");
        writer.write("        }\n");
        writer.write("        h1 {\n");
        writer.write("            text-align: center;\n");
        writer.write("            font-size: 36px;\n");
        writer.write("            color: #f4f4f4;\n");
        writer.write("            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.7);\n");
        writer.write("            margin-bottom: 30px;\n");
        writer.write("        }\n");
        writer.write("        table {\n");
        writer.write("            width: 80%;\n");
        writer.write("            margin: 0 auto;\n");
        writer.write("            border-collapse: collapse;\n");
        writer.write("            background-color: rgba(0, 0, 0, 0.6);\n");
        writer.write("            border-radius: 10px;\n");
        writer.write("            box-shadow: 0 0 15px rgba(0, 0, 0, 0.8);\n");
        writer.write("        }\n");
        writer.write("        th, td {\n");
        writer.write("            padding: 12px 15px;\n");
        writer.write("            text-align: center;\n");
        writer.write("            border: 2px solid #555;\n");
        writer.write("            font-size: 18px;\n");
        writer.write("        }\n");
        writer.write("        th {\n");
        writer.write("            background-color: #444;\n");
        writer.write("            color: #f1f1f1;\n");
        writer.write("        }\n");
        writer.write("        tr:nth-child(even) {\n");
        writer.write("            background-color: #333;\n");
        writer.write("        }\n");
        writer.write("        tr:nth-child(odd) {\n");
        writer.write("            background-color: #444;\n");
        writer.write("        }\n");
        writer.write("        tr:hover {\n");
        writer.write("            background-color: #555;\n");
        writer.write("            cursor: pointer;\n");
        writer.write("        }\n");
        writer.write("    </style>\n");
        writer.write("</head>\n");
        writer.write("<body>\n");
        writer.write("    <h1>Lista de Jugadores</h1>\n");
        writer.write("    <table>\n");
        writer.write("        <tr>\n");
        writer.write("            <th>Nombre</th>\n");
        writer.write("            <th>Salud</th>\n");
        writer.write("            <th>Ataque</th>\n");
        writer.write("            <th>Defensa</th>\n");
        writer.write("        </tr>\n");

        // Escribir los datos de los personajes
        for (Personaje personaje : personajes) {
            writer.write("        <tr>\n");
            writer.write("            <td>" + personaje.getNombre() + "</td>\n");
            writer.write("            <td>" + personaje.getSalud() + "</td>\n");
            writer.write("            <td>" + personaje.getAtaque() + "</td>\n");
            writer.write("            <td>" + personaje.getDefensa() + "</td>\n");
            writer.write("        </tr>\n");
        }

        // Cerrar la tabla y el cuerpo del HTML
        writer.write("    </table>\n");
        writer.write("</body>\n");
        writer.write("</html>\n");
    }
}

    // Método principal
    public static void main(String[] args) {
        String rutaArchivo = "personajes.lfp"; // Ruta del archivo .lfp
        String rutaHTML = "jugadores.html"; // Ruta del archivo HTML a generar

        try {
            // Mostrar el directorio de trabajo actual
            System.out.println("Directorio de trabajo: " + System.getProperty("user.dir"));

            // Cargar los personajes desde el archivo
            List<Personaje> personajes = cargarPersonajes(rutaArchivo);

            if (personajes.isEmpty()) {
                System.out.println("No se cargaron personajes desde el archivo.");
            } else {
                System.out.println("Se cargaron " + personajes.size() + " personajes.");
            }

            // Generar el archivo HTML
            generarHTML(personajes, rutaHTML);
            System.out.println("Archivo HTML generado exitosamente: " + rutaHTML);

            // Indicar dónde buscar el archivo
            System.out.println("Busca el archivo en: " + new File(rutaHTML).getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error al procesar el archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
