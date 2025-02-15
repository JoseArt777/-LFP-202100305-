/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */



import java.io.IOException;
import static java.time.Clock.system;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author iosea
 */
public class LFP_BATTLE {

    public static void main(String[] args) {
        Scanner scanner= new Scanner(System.in);
        List<Personaje> personajes=new ArrayList<>();
        boolean salir=false;
        
        while(!salir){
        
    
        System.out.println("=== MENÚ PRINCIPAL ===");
        System.out.println("1. Cargar archivo");
        System.out.println("2. Jugar");
        System.out.println("3. Generar Reporte de mayor ataque");
        System.out.println("4. Generar reporte de mayor defensa");
        System.out.println("Mostrar Información del desarrollador");
        System.out.println("6. Salir");
        
        int opcion= scanner.nextInt();
        scanner.nextLine();
        
        switch(opcion){
            case 1:
                System.out.println("Ingrese la ruta del archivo");
                String ruta=scanner.nextLine();
                try{
                   // personajes=LectorArchivo.cargarPersonaje(ruta);
                    System.out.println("Archivo cargado correctamente...");
                    
                }catch (IOException e){
                    System.out.println("Error al cargar el archivo");
                    
                }
                break;
            case 2:
                break;
            case 3:
                break;
                
            case 4: 
                break;
                
            case 5: 
                System.out.println("Nombre: Tu nombre");
                System.out.println("Carné: Tu carné");
                break;
            case 6: 
                salir=true;
                System.out.println("Saliendo...");
                break;
            default:
                System.out.println("Opción inválida");
        }
        }
scanner.close();
    }
    
}



