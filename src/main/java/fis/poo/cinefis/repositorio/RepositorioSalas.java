package fis.poo.cinefis.repositorio;

import fis.poo.cinefis.modelo.Sala;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class RepositorioSalas {

    // Ruta del archivo donde están guardadas las salas
    private final String rutaArchivo = "datos/salas.txt";

    public ArrayList<Sala> obtenerSalas() {
        ArrayList<Sala> salas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;

            // Lee cada línea del archivo salas.txt
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");

                // Cada sala debe tener 3 datos
                if (datos.length == 3) {
                    String codigo = datos[0];
                    String nombre = datos[1];
                    int capacidad = Integer.parseInt(datos[2]);

                    Sala sala = new Sala(codigo, nombre, capacidad);
                    salas.add(sala);
                }
            }

        } catch (IOException e) {
            System.out.println("Error al leer salas.txt: " + e.getMessage());
        }

        return salas;
    }

    public Sala buscarPorCodigo(String codigoBusqueda) {
    ArrayList<Sala> salas = obtenerSalas();

    if (codigoBusqueda == null) {
        return null;
    }

    String codigoLimpiado = codigoBusqueda.trim();

    // Busca una sala usando su código de forma segura, ignorando espacios y mayúsculas
    for (Sala sala : salas) {
        if (sala.getCodigo() != null && sala.getCodigo().trim().equalsIgnoreCase(codigoLimpiado)) {
            return sala;
        }
    }

    return null;
}
}