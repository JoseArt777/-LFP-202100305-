package com.mycompany.lfp_battle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Reporte {
    public static void generarReporteAtaque(List<Personaje> personajes, String ruta) throws IOException {
        // Crear la carpeta "output" si no existe
        File carpetaOutput = new File("output");
        if (!carpetaOutput.exists()) {
            carpetaOutput.mkdir(); // Crea la carpeta
        }

        // Ordenar los personajes por ataque descendente
        Collections.sort(personajes, Comparator.comparingInt(Personaje::getAtaque).reversed());

        // Escribir el archivo HTML con estilo y tabla
        try (FileWriter writer = new FileWriter(ruta)) {
            writer.write("<html>\n");
            writer.write("<head>\n");
            writer.write("<style>\n");
            writer.write("body {\n");
            writer.write("    font-family: Arial, sans-serif;\n");
            writer.write("    background-color: #f4f4f9;\n");
            writer.write("    color: #333;\n");
            writer.write("    margin: 20px;\n");
            writer.write("}\n");
            writer.write("h1 {\n");
            writer.write("    color: #007BFF;\n");
            writer.write("    text-align: center;\n");
            writer.write("}\n");
            writer.write("table {\n");
            writer.write("    width: 80%;\n");
            writer.write("    margin: 20px auto;\n");
            writer.write("    border-collapse: collapse;\n");
            writer.write("    background-color: #ffffff;\n");
            writer.write("    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);\n");
            writer.write("}\n");
            writer.write("th, td {\n");
            writer.write("    padding: 12px;\n");
            writer.write("    text-align: left;\n");
            writer.write("    border: 1px solid #ddd;\n");
            writer.write("}\n");
            writer.write("th {\n");
            writer.write("    background-color: #007BFF;\n");
            writer.write("    color: white;\n");
            writer.write("}\n");
            writer.write("tr:nth-child(even) {\n");
            writer.write("    background-color: #f9f9f9;\n");
            writer.write("}\n");
            writer.write("</style>\n");
            writer.write("</head>\n");
            writer.write("<body>\n");
            writer.write("<h1>Top 5 Ataque</h1>\n");
            writer.write("<table>\n");
            writer.write("    <tr>\n");
            writer.write("        <th>#</th>\n");
            writer.write("        <th>Nombre</th>\n");
            writer.write("        <th>Ataque</th>\n");
            writer.write("    </tr>\n");
            for (int i = 0; i < Math.min(5, personajes.size()); i++) {
                writer.write("    <tr>\n");
                writer.write("        <td>" + (i + 1) + "</td>\n");
                writer.write("        <td>" + personajes.get(i).getNombre() + "</td>\n");
                writer.write("        <td>" + personajes.get(i).getAtaque() + "</td>\n");
                writer.write("    </tr>\n");
            }
            writer.write("</table>\n");
            writer.write("</body>\n");
            writer.write("</html>");
        }
    }

    public static void generarReporteDefensa(List<Personaje> personajes, String ruta) throws IOException {
        // Crear la carpeta "output" si no existe
        File carpetaOutput = new File("output");
        if (!carpetaOutput.exists()) {
            carpetaOutput.mkdir(); // Crea la carpeta
        }

        // Ordenar los personajes por defensa descendente
        Collections.sort(personajes, Comparator.comparingInt(Personaje::getDefensa).reversed());

        // Escribir el archivo HTML con estilo y tabla
        try (FileWriter writer = new FileWriter(ruta)) {
            writer.write("<html>\n");
            writer.write("<head>\n");
            writer.write("<style>\n");
            writer.write("body {\n");
            writer.write("    font-family: Arial, sans-serif;\n");
            writer.write("    background-color: #f4f4f9;\n");
            writer.write("    color: #333;\n");
            writer.write("    margin: 20px;\n");
            writer.write("}\n");
            writer.write("h1 {\n");
            writer.write("    color: #28a745;\n");
            writer.write("    text-align: center;\n");
            writer.write("}\n");
            writer.write("table {\n");
            writer.write("    width: 80%;\n");
            writer.write("    margin: 20px auto;\n");
            writer.write("    border-collapse: collapse;\n");
            writer.write("    background-color: #ffffff;\n");
            writer.write("    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);\n");
            writer.write("}\n");
            writer.write("th, td {\n");
            writer.write("    padding: 12px;\n");
            writer.write("    text-align: left;\n");
            writer.write("    border: 1px solid #ddd;\n");
            writer.write("}\n");
            writer.write("th {\n");
            writer.write("    background-color: #28a745;\n");
            writer.write("    color: white;\n");
            writer.write("}\n");
            writer.write("tr:nth-child(even) {\n");
            writer.write("    background-color: #f9f9f9;\n");
            writer.write("}\n");
            writer.write("</style>\n");
            writer.write("</head>\n");
            writer.write("<body>\n");
            writer.write("<h1>Top 5 Defensa</h1>\n");
            writer.write("<table>\n");
            writer.write("    <tr>\n");
            writer.write("        <th>#</th>\n");
            writer.write("        <th>Nombre</th>\n");
            writer.write("        <th>Defensa</th>\n");
            writer.write("    </tr>\n");
            for (int i = 0; i < Math.min(5, personajes.size()); i++) {
                writer.write("    <tr>\n");
                writer.write("        <td>" + (i + 1) + "</td>\n");
                writer.write("        <td>" + personajes.get(i).getNombre() + "</td>\n");
                writer.write("        <td>" + personajes.get(i).getDefensa() + "</td>\n");
                writer.write("    </tr>\n");
            }
            writer.write("</table>\n");
            writer.write("</body>\n");
            writer.write("</html>");
        }
    }
}