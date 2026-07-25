package fis.poo.cinefis.controlador;

import fis.poo.cinefis.estrategia.PoliticaPrecio;
import fis.poo.cinefis.estrategia.PrecioFactory;
import fis.poo.cinefis.modelo.DetalleSnack;
import fis.poo.cinefis.modelo.Entrada;
import fis.poo.cinefis.modelo.Funcion;
import fis.poo.cinefis.modelo.SesionCompra;
import fis.poo.cinefis.modelo.Snack;
import fis.poo.cinefis.modelo.Venta;
import fis.poo.cinefis.vista.VistaCompra;

public class ControladorCompra {

    private final VistaCompra vista;
    private final SesionCompra sesionCompra;
    private final ControladorAplicacion aplicacion;

    private final Snack snackCanguil;
    private final Snack snackGaseosa;
    private final Snack snackNachos;

    public ControladorCompra(
            VistaCompra vista,
            SesionCompra sesionCompra,
            ControladorAplicacion aplicacion
    ) {
        this.vista = vista;
        this.sesionCompra = sesionCompra;
        this.aplicacion = aplicacion;

        /*
        Estos snacks permanecen fijos porque actualmente
        la vista tiene solamente estos tres productos.
         */
        snackCanguil = new Snack(
                "S001",
                "Canguil",
                2.50,
                "canguil.png"
        );

        snackGaseosa = new Snack(
                "S002",
                "Gaseosa",
                1.50,
                "gaseosa.png"
        );

        snackNachos = new Snack(
                "S003",
                "Nachos",
                3.00,
                "nachos.png"
        );
    }

    public void iniciar() {
        if (sesionCompra.getFuncionSeleccionada() == null) {
            vista.mostrarMensaje("No existe una función seleccionada.");
            vista.dispose();
            aplicacion.mostrarCatalogo();
            return;
        }

        if (sesionCompra
                .getAsientosSeleccionados()
                .isEmpty()) {

            vista.mostrarMensaje("No existen asientos seleccionados.");
            vista.dispose();
            aplicacion.mostrarSeleccionAsientos();
            return;
        }

        configurarEventos();
        vista.setVisible(true);
    }

    private void configurarEventos() {
        vista.agregarEventoCalcular(
                e -> calcularCompra()
        );

        vista.agregarEventoContinuar(
                e -> continuarResumen()
        );

        vista.agregarEventoVolver(
                e -> volverAsientos()
        );
    }

    //Construye una Venta nueva usando los datos de la vista.
    
    private boolean calcularCompra() {
        Funcion funcion = sesionCompra.getFuncionSeleccionada();

        Venta venta = new Venta(funcion);

        vista.limpiarResultadosEntradas();

        boolean entradasValidas =
                crearEntradas(venta);

        if (!entradasValidas) {
            sesionCompra.setVenta(null);
            return false;
        }

        agregarSnacks(venta);

        sesionCompra.setVenta(venta);

        vista.mostrarTotales(
                venta.calcularSubtotalEntradas(),
                venta.calcularSubtotalSnacks(),
                venta.calcularTotal()
        );

        return true;
    }

    private boolean crearEntradas(Venta venta) {
        int cantidadEntradas =
                vista.obtenerCantidadEntradas();

        double precioBase =
                venta.getFuncion().getPrecioBase();

        for (int fila = 0;
                fila < cantidadEntradas;
                fila++) {

            String asiento =
                    vista.obtenerAsiento(fila);

            String textoEdad =
                    vista.obtenerEdad(fila);

            if (textoEdad.isEmpty()) {
                vista.mostrarMensaje(
                        "Ingrese la edad correspondiente "
                        + "al asiento "
                        + asiento
                        + "."
                );

                return false;
            }

            int edad;

            try {
                edad = Integer.parseInt(textoEdad);

            } catch (NumberFormatException e) {
                vista.mostrarMensaje(
                        "La edad del asiento "
                        + asiento
                        + " debe ser un número entero."
                );

                return false;
            }

            if (edad < 0) {
                vista.mostrarMensaje(
                        "La edad del asiento "
                        + asiento
                        + " no puede ser negativa."
                );

                return false;
            }

            PoliticaPrecio politica =
                    PrecioFactory.obtenerPolitica(edad);

            String tipoCliente =
                    politica.obtenerTipoCliente();

            double precioEntrada =
                    politica.calcularPrecio(precioBase);

            Entrada entrada = new Entrada(
                    asiento,
                    edad,
                    tipoCliente,
                    precioEntrada
            );

            venta.agregarEntrada(entrada);

            vista.mostrarResultadoEntrada(
                    fila,
                    tipoCliente,
                    precioEntrada
            );
        }

        return true;
    }

    private void agregarSnacks(Venta venta) {
        if (vista.tieneCanguilSeleccionado()) {
            int cantidad =
                    vista.obtenerCantidadCanguil();

            if (cantidad > 0) {
                venta.agregarDetalleSnack(
                        new DetalleSnack(
                                snackCanguil,
                                cantidad
                        )
                );
            }
        }

        if (vista.tieneGaseosaSeleccionada()) {
            int cantidad =
                    vista.obtenerCantidadGaseosa();

            if (cantidad > 0) {
                venta.agregarDetalleSnack(
                        new DetalleSnack(
                                snackGaseosa,
                                cantidad
                        )
                );
            }
        }

        if (vista.tieneNachosSeleccionados()) {
            int cantidad =
                    vista.obtenerCantidadNachos();

            if (cantidad > 0) {
                venta.agregarDetalleSnack(
                        new DetalleSnack(
                                snackNachos,
                                cantidad
                        )
                );
            }
        }
    }

    private void continuarResumen() {
        boolean compraCorrecta =
                calcularCompra();

        if (!compraCorrecta) {
            return;
        }

        Venta venta = sesionCompra.getVenta();

        if (venta == null || !venta.tieneEntradas()) {
            vista.mostrarMensaje(
                    "No se pudo construir la venta."
            );

            return;
        }

        vista.dispose();
        aplicacion.mostrarResumenCompra();
    }

    private void volverAsientos() {
        /*
        Se eliminan los cálculos de la compra, pero se mantienen
        la función y los asientos seleccionados.
         */
        sesionCompra.setVenta(null);

        vista.dispose();
        aplicacion.mostrarSeleccionAsientos();
    }
}