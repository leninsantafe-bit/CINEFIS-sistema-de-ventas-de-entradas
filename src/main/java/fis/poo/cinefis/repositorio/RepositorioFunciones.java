package fis.poo.cinefis.repositorio;

import fis.poo.cinefis.modelo.Funcion;
import fis.poo.cinefis.modelo.Pelicula;
import fis.poo.cinefis.modelo.Sala;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class RepositorioFunciones {

    // Ruta del archivo donde están guardadas las funciones
    private final String rutaArchivo = "datos/funciones.txt";

    // Se usan para buscar la película y la sala de cada función
    private RepositorioPeliculas repositorioPeliculas = new RepositorioPeliculas();
    private RepositorioSalas repositorioSalas = new RepositorioSalas();

    public ArrayList<Funcion> obtenerFunciones() {
        ArrayList<Funcion> funciones = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;

            // Lee cada función registrada en el archivo
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");

                // Cada función debe tener 6 datos
                if (datos.length == 6) {
                    String codigoFuncion = datos[0];
                    String codigoPelicula = datos[1];
                    String codigoSala = datos[2];
                    String fecha = datos[3];
                    String hora = datos[4];
                    double precioBase = Double.parseDouble(datos[5]);

                    // Busca los objetos reales usando sus códigos
                    Pelicula pelicula = repositorioPeliculas.buscarPorCodigo(codigoPelicula);
                    Sala sala = repositorioSalas.buscarPorCodigo(codigoSala);

                    // Solo crea la función si encontró la película y la sala
                    if (pelicula != null && sala != null) {
                        Funcion funcion = new Funcion(codigoFuncion, pelicula, sala, fecha, hora, precioBase);
                        funciones.add(funcion);
                    }else {
                       
                        System.out.println("=========================================");
                        System.out.println("ERROR SILENCIOSO: Se ignoró la función " + codigoFuncion);
                        if (pelicula == null) {
                            System.out.println("-> CULPABLE: No se encontró la película con código: " + codigoPelicula);
                        }
                        if (sala == null) {
                            System.out.println("-> CULPABLE: No se encontró la sala con código: " + codigoSala);
                        }
                        System.out.println("=========================================");
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Error al leer funciones.txt: " + e.getMessage());
        }

        return funciones;
    }

    public Funcion buscarPorCodigo(String codigo) {
        ArrayList<Funcion> funciones = obtenerFunciones();

        // Busca una función usando su código
        for (Funcion funcion : funciones) {
            if (funcion.getCodigo().equals(codigo)) {
                return funcion;
            }
        }

        return null;
    }
    
    public void guardarNuevaFuncion(Funcion nuevaFuncion) {
        // El 'true' en FileWriter significa que va a agregar texto al final del archivo sin borrar lo anterior
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            String linea = nuevaFuncion.getCodigo() + ";" +
                           nuevaFuncion.getPelicula().getCodigo() + ";" +
                           nuevaFuncion.getSala().getCodigo() + ";" +
                           nuevaFuncion.getFecha() + ";" +
                           nuevaFuncion.getHora() + ";" +
                           nuevaFuncion.getPrecioBase();
            
            bw.write(linea);
            bw.newLine(); 
            
        } catch (IOException e) {
            System.out.println("Error al guardar la nueva función: " + e.getMessage());
        }
    }
    
    public String generarSiguienteCodigoFuncion() {
        // 1. Obtenemos todas las funciones actuales
        RepositorioFunciones repo = new RepositorioFunciones();
        ArrayList<Funcion> listaActual = repo.obtenerFunciones();
     
        
        // 2. Si el archivo está vacío, empezamos desde F001 por defecto
        if (listaActual.isEmpty()) {
            return "F001";
        }
        
        // 3. Obtenemos el código de la ÚLTIMA función registrada en la lista
        Funcion ultimaFuncion = listaActual.get(listaActual.size() - 1);
        String ultimoCodigo = ultimaFuncion.getCodigo(); // Ej: "F012" o "F999"
        
        // 4. Desarmamos el código
        char letra = ultimoCodigo.charAt(0); // Extrae la 'F'
        int numero = Integer.parseInt(ultimoCodigo.substring(1)); // Extrae el "012" y lo vuelve el número 12
        
        // 5. Aumentamos el número en 1
        numero++;
        
        // 6. Lógica de cambio de letra
        if (numero > 999) {
            numero = 0; // Reinicia a 000
            letra++;    // Pasa al siguiente caracter en el abecedario (De 'F' a 'G')
        }
        
        // 7. Volvemos a armar el texto
        // %c coloca la letra. %03d asegura que el número siempre tenga 3 cifras rellenando con ceros
        return String.format("%c%03d", letra, numero);
    }
}