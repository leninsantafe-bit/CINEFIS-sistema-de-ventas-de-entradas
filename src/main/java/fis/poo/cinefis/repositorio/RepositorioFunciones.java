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

        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(rutaArchivo))) {
            String linea;
            int numeroLinea = 0;

            // Lee cada función registrada en el archivo
            while ((linea = br.readLine()) != null) {
                numeroLinea++;
                
                // Ignorar líneas vacías
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                // Cada función debe tener 6 datos
                if (datos.length == 6) {
                    try {
                        String codigoFuncion = datos[0].trim();
                        String codigoPelicula = datos[1].trim();
                        String codigoSala = datos[2].trim();
                        String fecha = datos[3].trim();
                        String hora = datos[4].trim();
                        double precioBase = Double.parseDouble(datos[5].trim()); // Blindado con trim()

                        Pelicula pelicula = repositorioPeliculas.buscarPorCodigo(codigoPelicula);
                        Sala sala = repositorioSalas.buscarPorCodigo(codigoSala);

                        if (pelicula != null && sala != null) {
                            Funcion funcion = new Funcion(codigoFuncion, pelicula, sala, fecha, hora, precioBase);
                            funciones.add(funcion);
                        } else {
                            // Este error saldrá si no encuentra la sala o la película específica
                            System.out.println("❌ ERROR (Línea " + numeroLinea + "): Se ignoró " + codigoFuncion 
                                    + ". Película=" + (pelicula == null ? "NO ENCONTRADA" : "OK") 
                                    + " | Sala=" + (sala == null ? "NO ENCONTRADA" : "OK"));
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("❌ ERROR FUNCIONES (Línea " + numeroLinea + "): El precio no es un número válido. Línea: " + linea);
                    }
                } else {
                    System.out.println("❌ ERROR FUNCIONES (Línea " + numeroLinea + "): Faltan o sobran datos (son " + datos.length + " y deben ser 6). Línea: " + linea);
                }
            }
        } catch (java.io.IOException e) {
            System.out.println("Error al leer funciones.txt: " + e.getMessage());
        }

        // ¡Este es nuestro chismoso! Nos dirá cuántas funciones pasaron la prueba.
        System.out.println("✅ TOTAL DE FUNCIONES CARGADAS PARA LA TABLA: " + funciones.size());

        return funciones;
    }

    public Funcion buscarPorCodigo(String codigoBusqueda) {
    ArrayList<Funcion> funciones = obtenerFunciones();

    if (codigoBusqueda == null) {
        return null;
    }

    String codigoLimpiado = codigoBusqueda.trim();

    // Busca una función usando su código de forma segura, ignorando espacios y mayúsculas
    for (Funcion funcion : funciones) {
        if (funcion.getCodigo() != null && funcion.getCodigo().trim().equalsIgnoreCase(codigoLimpiado)) {
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