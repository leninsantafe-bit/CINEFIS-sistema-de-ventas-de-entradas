package fis.poo.cinefis.repositorio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class RepositorioAsientosOcupados {

    private final String rutaArchivo = "datos/asientos_ocupados.txt";

    public RepositorioAsientosOcupados() {
        crearArchivoSiNoExiste();
    }

     //Crea la carpeta datos y el archivo de asientos ocupados
     //cuando todavía no existen
     
    private void crearArchivoSiNoExiste() {
        try {
            File archivo = new File(rutaArchivo);
            File carpeta = archivo.getParentFile();

            if (carpeta != null && !carpeta.exists()) {
                carpeta.mkdirs();
            }

            if (!archivo.exists()) {
                archivo.createNewFile();
            }

        } catch (IOException e) {
            System.out.println(
                    "No se pudo crear el archivo de asientos: "
                    + e.getMessage()
            );
        }
    }

    /*obtiene todos los asientos ocupados de una función
     @param codigoFuncion código de la función
     @return lista de códigos de asientos ocupados
    */
    public ArrayList<String> obtenerAsientosOcupados(String codigoFuncion) {
        Set<String> asientos = new LinkedHashSet<>();

        if (codigoFuncion == null || codigoFuncion.isBlank()) {
            return new ArrayList<>();
        }

        try (BufferedReader br =
                     new BufferedReader(
                             new FileReader(rutaArchivo)
                     )) {

            String linea;

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();

                if (linea.isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";");

                if (datos.length != 2) {
                    continue;
                }

                String codigoFuncionArchivo =
                        datos[0].trim();

                String codigoAsiento =
                        datos[1].trim();

                if (codigoFuncionArchivo.equals(codigoFuncion)) {
                    asientos.add(codigoAsiento);
                }
            }

        } catch (IOException e) {
            System.out.println(
                    "Error al leer asientos ocupados: "
                    + e.getMessage()
            );
        }

        return new ArrayList<>(asientos);
    }

     //Comprueba si un asiento ya fue vendido para una función.

    public boolean estaOcupado(String codigoFuncion,String asiento) {
        ArrayList<String> ocupados =
                obtenerAsientosOcupados(codigoFuncion);

        return ocupados.contains(asiento);
    }

    /*
    Guarda varios asientos como ocupados.
    Antes de escribir comprueba que ninguno haya sido vendido
    previamente.
    @return true si se guardaron; false si alguno estaba ocupado
    */
    public boolean guardarAsientosOcupados(
            String codigoFuncion,
            ArrayList<String> nuevosAsientos
    ) {
        if (codigoFuncion == null
                || codigoFuncion.isBlank()
                || nuevosAsientos == null
                || nuevosAsientos.isEmpty()) {

            return false;
        }

        ArrayList<String> ocupadosActuales =
                obtenerAsientosOcupados(codigoFuncion);

        for (String asiento : nuevosAsientos) {
            if (ocupadosActuales.contains(asiento)) {
                return false;
            }
        }

        try (BufferedWriter bw =
                     new BufferedWriter(
                             new FileWriter(rutaArchivo, true)
                     )) {

            for (String asiento : nuevosAsientos) {
                bw.write(
                        codigoFuncion
                        + ";"
                        + asiento
                );

                bw.newLine();
            }

            return true;

        } catch (IOException e) {
            System.out.println(
                    "Error al guardar asientos ocupados: "
                    + e.getMessage()
            );

            return false;
        }
    }
}
