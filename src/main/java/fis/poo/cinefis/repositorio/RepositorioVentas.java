package fis.poo.cinefis.repositorio;

import fis.poo.cinefis.modelo.DetalleSnack;
import fis.poo.cinefis.modelo.Entrada;
import fis.poo.cinefis.modelo.Usuario;
import fis.poo.cinefis.modelo.Venta;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RepositorioVentas {

    private final String rutaArchivo =
            "datos/ventas.txt";

    private final DateTimeFormatter formatoFecha =
            DateTimeFormatter.ofPattern(
                    "yyyy-MM-dd HH:mm:ss"
            );

    public RepositorioVentas() {
        crearArchivoSiNoExiste();
    }

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
                    "No se pudo crear el archivo de ventas: "
                    + e.getMessage()
            );
        }
    }

    public boolean guardarVenta(Venta venta, Usuario usuario) {
        if (venta == null
                || venta.getFuncion() == null
                || venta.getEntradas().isEmpty()) {

            return false;
        }

        try (BufferedWriter bw =
                     new BufferedWriter(new FileWriter(rutaArchivo, true))) {

            bw.write(construirRegistroVenta(venta, usuario));
            bw.newLine();

            return true;

        } catch (IOException e) {
            System.out.println(
                    "Error al guardar la venta: "
                    + e.getMessage()
            );

            return false;
        }
    }

    private String construirRegistroVenta(Venta venta, Usuario usuario) {
        String fechaVenta = LocalDateTime.now().format(formatoFecha);
        
        String username = usuario.getUsername();
        
        String rol = usuario.getRol();
        
        String codigoFuncion = venta.getFuncion().getCodigo();

        String pelicula =
                venta.getFuncion()
                        .getPelicula()
                        .getTitulo();

        String sala =
                venta.getFuncion()
                        .getSala()
                        .getNombre();

        String entradas =
                construirTextoEntradas(venta);

        String snacks =
                construirTextoSnacks(venta);

        return fechaVenta
                + ";"
                + username
                + ";"
                + rol
                + ";"
                + limpiarTexto(codigoFuncion)
                + ";"
                + limpiarTexto(pelicula)
                + ";"
                + limpiarTexto(sala)
                + ";"
                + entradas
                + ";"
                + snacks
                + ";"
                + String.format(
                        "%.2f",
                        venta.calcularSubtotalEntradas()
                )
                + ";"
                + String.format(
                        "%.2f",
                        venta.calcularSubtotalSnacks()
                )
                + ";"
                + String.format(
                        "%.2f",
                        venta.calcularTotal()
                );
    }

    private String construirTextoEntradas(Venta venta) {
        StringBuilder texto = new StringBuilder();

        for (Entrada entrada : venta.getEntradas()) {
            if (texto.length() > 0) {
                texto.append("|");
            }

            texto.append(limpiarTexto(entrada.getAsiento()));

            texto.append(",").append(entrada.getEdad());

            texto.append(",").append(limpiarTexto(entrada.getTipoCliente()));

            texto.append(",").append(String.format("%.2f",entrada.getPrecio()));
        }

        return texto.toString();
    }

    private String construirTextoSnacks(Venta venta) {
        if (!venta.tieneSnacks()) {
            return "SIN_SNACKS";
        }

        StringBuilder texto = new StringBuilder();

        for (DetalleSnack detalle
                : venta.getDetallesSnacks()) {

            if (texto.length() > 0) {
                texto.append("|");
            }

            texto.append(limpiarTexto(detalle.getSnack().getNombre()));

            texto.append(",").append(detalle.getCantidad());

            texto.append(",").append(String.format("%.2f",detalle.calcularSubtotal()));
        }

        return texto.toString();
    }

    private String limpiarTexto(String texto) {
        if (texto == null) {
            return "";
        }
        return texto
                .replace(";", " ")
                .replace("|", " ")
                .replace(",", " ")
                .trim();
    }
}
