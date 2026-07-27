package fis.poo.cinefis.repositorio;

import fis.poo.cinefis.modelo.Pelicula;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class RepositorioPeliculas {

    // Ruta del archivo donde están guardadas las películas
    private final String rutaArchivo = "datos/peliculas.txt";

    public ArrayList<Pelicula> obtenerPeliculas() {
        ArrayList<Pelicula> peliculas = new ArrayList<>();

        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(rutaArchivo))) {
            String linea;
            int numeroLinea = 0; 

            while ((linea = br.readLine()) != null) {
                numeroLinea++;
                
                if (linea.trim().isEmpty()) {
                    continue; 
                }

                String[] datos = linea.split(";", -1);

                if (datos.length == 7) {
                    try {
                        String codigo = datos[0].trim();
                        String titulo = datos[1].trim();
                        String genero = datos[2].trim();
                        String clasificacion = datos[3].trim();
                        int duracion = Integer.parseInt(datos[4].trim());
                        String imagen = datos[5].trim();
                        String sinopsis = datos[6].trim();

                        Pelicula pelicula = new Pelicula(codigo, titulo, genero, 
                                            clasificacion, duracion, imagen, sinopsis);
                        peliculas.add(pelicula);
                        
                    } catch (NumberFormatException ex) {
                        System.out.println("❌ ERROR PELÍCULA (Línea " + numeroLinea + "): La duración no es un número válido. Línea: " + linea);
                    }
                } else {
                    System.out.println("❌ ERROR PELÍCULA (Línea " + numeroLinea + "): Faltan o sobran datos (no son 7). Línea: " + linea);
                }
            }
        } catch (java.io.IOException e) {
            System.out.println("Error al leer peliculas.txt: " + e.getMessage());
        }

        return peliculas;
    }

    public Pelicula buscarPorCodigo(String codigoBusqueda) {
        ArrayList<Pelicula> peliculas = obtenerPeliculas();

        if (codigoBusqueda == null) {
            return null;
        }

        String codigoLimpiado = codigoBusqueda.trim();

        // Busca una película usando su código de forma estricta pero limpia
        for (Pelicula pelicula : peliculas) {
            if (pelicula.getCodigo() != null && pelicula.getCodigo().trim().equalsIgnoreCase(codigoLimpiado)) {
                return pelicula;
            }
        }

        return null;
    }
    public String generarSiguienteCodigoPelicula() {
        // 1. Obtenemos todas las funciones actuales
        RepositorioPeliculas repo = new RepositorioPeliculas();
        ArrayList<Pelicula> listaActual = repo.obtenerPeliculas();
     
        
        // 2. Si el archivo está vacío, empezamos desde F001 por defecto
        if (listaActual.isEmpty()) {
            return "P001";
        }
        
        // 3. Obtenemos el código de la ÚLTIMA función registrada en la lista
        Pelicula ultimaPelicula = listaActual.get(listaActual.size() - 1);
        String ultimoCodigo = ultimaPelicula.getCodigo(); // Ej: "P012" o "P999"
        
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
    
    
    
    
    public void guardarNuevaPelicula(Pelicula nuevaPelicula) {
        // El 'true' indica que vamos a agregar texto al final, sin borrar las películas anteriores
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            
            // Construimos la línea uniendo los 7 atributos separados por ";"
            String linea = nuevaPelicula.getCodigo() + ";" +
                           nuevaPelicula.getTitulo() + ";" +
                           nuevaPelicula.getGenero() + ";" +
                           nuevaPelicula.getClasificacion() + ";" +
                           nuevaPelicula.getDuracion() + ";" +
                           nuevaPelicula.getImagen() + ";" +
                           nuevaPelicula.getSinopsis();
            
            bw.write(linea);
            bw.newLine(); // Hacemos un salto de línea para que la siguiente no se pegue
            
        } catch (IOException e) {
            System.out.println("Error al guardar la nueva película: " + e.getMessage());
        }
    }
}
