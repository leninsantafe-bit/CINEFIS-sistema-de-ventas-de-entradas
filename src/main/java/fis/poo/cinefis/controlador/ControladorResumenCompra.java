package fis.poo.cinefis.controlador;

import fis.poo.cinefis.modelo.DetalleSnack;
import fis.poo.cinefis.modelo.Entrada;
import fis.poo.cinefis.modelo.Funcion;
import fis.poo.cinefis.modelo.SesionCompra;
import fis.poo.cinefis.modelo.Venta;
import fis.poo.cinefis.repositorio.RepositorioAsientosOcupados;
import fis.poo.cinefis.repositorio.RepositorioVentas;
import fis.poo.cinefis.vista.VistaResumenCompra;
import java.util.ArrayList;

public class ControladorResumenCompra {

    private final VistaResumenCompra vista;
    private final SesionCompra sesionCompra;
    private final ControladorAplicacion aplicacion;
    private final RepositorioVentas repositorioVentas;

    private final RepositorioAsientosOcupados
            repositorioAsientos;

    private boolean ventaRegistrada;

    public ControladorResumenCompra(
            VistaResumenCompra vista,
            SesionCompra sesionCompra,
            ControladorAplicacion aplicacion
    ) {
        this.vista = vista;
        this.sesionCompra = sesionCompra;
        this.aplicacion = aplicacion;

        repositorioAsientos = new RepositorioAsientosOcupados();
        repositorioVentas = new RepositorioVentas();
        
        ventaRegistrada = false;
    }

    public void iniciar() {
        Venta venta = sesionCompra.getVenta();

        if (venta == null || !venta.tieneEntradas()) {
            vista.mostrarMensaje(
                    "No existe una compra calculada."
            );

            vista.dispose();
            aplicacion.mostrarCompra();
            return;
        }

        mostrarVenta(venta);
        configurarEventos();

        vista.setVisible(true);
    }

    private void mostrarVenta(Venta venta) {
        String resumen =
                generarResumen(venta);

        vista.mostrarResumen(resumen);

        vista.mostrarTotales(
                venta.calcularSubtotalEntradas(),
                venta.calcularSubtotalSnacks(),
                venta.calcularTotal()
        );
    }

    private String generarResumen(Venta venta) {
        StringBuilder resumen = new StringBuilder();

        Funcion funcion = venta.getFuncion();

        resumen.append("              CINEFIS\n");
        resumen.append("        RESUMEN DE COMPRA\n");
        resumen.append("----------------------------------------\n\n");

        resumen.append("FUNCIÓN:\n");
        resumen.append("Película: ")
                .append(funcion.getPelicula().getTitulo())
                .append("\n");

        resumen.append("Sala: ")
                .append(funcion.getSala().getNombre())
                .append("\n");

        resumen.append("Fecha: ")
                .append(funcion.getFecha())
                .append("\n");

        resumen.append("Hora: ")
                .append(funcion.getHora())
                .append("\n\n");

        resumen.append("ENTRADAS:\n");

        for (Entrada entrada : venta.getEntradas()) {
            resumen.append("Asiento: ")
                    .append(entrada.getAsiento())
                    .append(" | Edad: ")
                    .append(entrada.getEdad())
                    .append(" | ")
                    .append(entrada.getTipoCliente())
                    .append(" | $")
                    .append(
                            String.format(
                                    "%.2f",
                                    entrada.getPrecio()
                            )
                    )
                    .append("\n");
        }

        resumen.append("\nSNACKS:\n");

        if (!venta.tieneSnacks()) {
            resumen.append(
                    "No se agregaron snacks.\n"
            );

        } else {
            for (DetalleSnack detalle
                    : venta.getDetallesSnacks()) {

                resumen.append(
                        detalle.getSnack().getNombre()
                );

                resumen.append(" x")
                        .append(detalle.getCantidad());

                resumen.append(" - $")
                        .append(
                                String.format(
                                        "%.2f",
                                        detalle.calcularSubtotal()
                                )
                        )
                        .append("\n");
            }
        }

        resumen.append(
                "\n----------------------------------------\n"
        );

        resumen.append(
                String.format(
                        "Subtotal entradas: $%.2f\n",
                        venta.calcularSubtotalEntradas()
                )
        );

        resumen.append(
                String.format(
                        "Subtotal snacks:   $%.2f\n",
                        venta.calcularSubtotalSnacks()
                )
        );

        resumen.append(
                String.format(
                        "TOTAL A PAGAR:     $%.2f\n",
                        venta.calcularTotal()
                )
        );

        return resumen.toString();
    }

    private void configurarEventos() {
        vista.agregarEventoFinalizar(
                e -> finalizarVenta()
        );

        vista.agregarEventoRegresar(
                e -> regresarCompra()
        );
    }

    private void finalizarVenta() {
        if (!vista.confirmarVenta()) {
            return;
        }

        if (!registrarVenta()) {
            volverSeleccionAsientos();
            return;
        }

        vista.mostrarMensaje(
                "Venta registrada correctamente.\nLos asientos quedaron ocupados."
        );

        vista.dispose();
        aplicacion.iniciarNuevaCompra();
    }

    private boolean registrarVenta() {
        if (ventaRegistrada) {
            return true;
        }
        Venta venta = sesionCompra.getVenta();
        
        if (venta == null) {
            vista.mostrarMensaje(
                    "No se encontró la venta."
            );
            return false;
        }

        ArrayList<String> asientos =
                new ArrayList<>();

        for (Entrada entrada : venta.getEntradas()) {
            asientos.add(entrada.getAsiento());
        }
         
        //Se comprueba nuevamente que los asientos continúen disponibles
        for (String asiento : asientos) {
            boolean ocupado =
                    repositorioAsientos.estaOcupado(
                            venta.getFuncion().getCodigo(),
                            asiento
                    );

            if (ocupado) {
                vista.mostrarMensaje(
                        "El asiento "
                        + asiento
                        + " ya fue ocupado."
                );

                return false;
            }
        }

        //Primero se guarda el registro completo de la venta.
        boolean ventaGuardada = repositorioVentas.guardarVenta(venta);

        if (!ventaGuardada) {
            vista.mostrarMensaje(
                    "No fue posible guardar la información "
                    + "de la venta."
            );

            return false;
        }
        
        //Después se bloquean los asientos
        boolean asientosGuardados =
                repositorioAsientos
                        .guardarAsientosOcupados(
                                venta.getFuncion().getCodigo(),
                                asientos
                        );

        if (!asientosGuardados) {
            vista.mostrarMensaje(
                    "La venta fue registrada, pero ocurrió "
                    + "un problema al bloquear los asientos."
            );
            return false;
        }
        ventaRegistrada = true;
        return true;
    }

    private void volverSeleccionAsientos() {
        vista.dispose();
        sesionCompra.reiniciarDesdeAsientos();
        aplicacion.mostrarSeleccionAsientos();
    }
    
    private void regresarCompra() {
        vista.dispose();
        aplicacion.mostrarCompra();
    }
    
}
