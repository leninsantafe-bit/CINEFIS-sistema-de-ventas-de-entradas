package fis.poo.cinefis.repositorio;

import fis.poo.cinefis.modelo.Funcion;
import fis.poo.cinefis.modelo.Pelicula;
import fis.poo.cinefis.modelo.Sala;
import java.io.BufferedReader;
import java.io.FileReader;
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
}