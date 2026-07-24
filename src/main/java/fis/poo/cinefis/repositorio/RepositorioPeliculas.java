package fis.poo.cinefis.repositorio;

import fis.poo.cinefis.modelo.Pelicula;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class RepositorioPeliculas {

    // Ruta del archivo donde están guardadas las películas
    private final String rutaArchivo = "datos/peliculas.txt";

    public ArrayList<Pelicula> obtenerPeliculas() {
        ArrayList<Pelicula> peliculas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;

            // Lee el archivo línea por línea
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");

                // Cada película debe tener 6 datos
                if (datos.length == 6) {
                    String codigo = datos[0];
                    String titulo = datos[1];
                    String genero = datos[2];
                    String clasificacion = datos[3];
                    int duracion = Integer.parseInt(datos[4]);
                    String imagen = datos[5];

                    Pelicula pelicula = new Pelicula(codigo, titulo, genero, clasificacion, duracion, imagen);
                    peliculas.add(pelicula);
                }
            }

        } catch (IOException e) {
            System.out.println("Error al leer peliculas.txt: " + e.getMessage());
        }

        return peliculas;
    }

    public Pelicula buscarPorCodigo(String codigo) {
        ArrayList<Pelicula> peliculas = obtenerPeliculas();

        // Busca una película usando su código
        for (Pelicula pelicula : peliculas) {
            if (pelicula.getCodigo().equals(codigo)) {
                return pelicula;
            }
        }

        return null;
    }
}