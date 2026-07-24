package fis.poo.cinefis.controlador;

import fis.poo.cinefis.modelo.Funcion;
import fis.poo.cinefis.modelo.SesionCompra;
import fis.poo.cinefis.repositorio.RepositorioAsientosOcupados;
import fis.poo.cinefis.vista.VistaSeleccionAsientos;
import java.util.ArrayList;

public class ControladorSeleccionAsientos {

    private final VistaSeleccionAsientos vista;
    private final SesionCompra sesionCompra;
    private final ControladorAplicacion aplicacion;

    private final RepositorioAsientosOcupados
            repositorioAsientos;

    public ControladorSeleccionAsientos(
            VistaSeleccionAsientos vista,
            SesionCompra sesionCompra,
            ControladorAplicacion aplicacion
    ) {
        this.vista = vista;
        this.sesionCompra = sesionCompra;
        this.aplicacion = aplicacion;

        repositorioAsientos =
                new RepositorioAsientosOcupados();
    }

    public void iniciar() {
        Funcion funcion =
                sesionCompra.getFuncionSeleccionada();

        if (funcion == null) {
            vista.mostrarMensaje(
                    "No se ha seleccionado una función."
            );

            vista.dispose();
            aplicacion.mostrarCatalogo();
            return;
        }

        vista.mostrarFuncion(funcion);

        cargarAsientosOcupados(funcion);

        vista.establecerAsientosSeleccionados(
                sesionCompra.getAsientosSeleccionados()
        );

        configurarEventos();

        vista.setVisible(true);
    }

    private void cargarAsientosOcupados(
            Funcion funcion
    ) {
        ArrayList<String> ocupados =
                repositorioAsientos
                        .obtenerAsientosOcupados(
                                funcion.getCodigo()
                        );

        vista.establecerAsientosOcupados(ocupados);
    }

    private void configurarEventos() {
        vista.agregarEventoContinuar(
                e -> continuarCompra()
        );

        vista.agregarEventoVolverCatalogo(
                e -> volverCatalogo()
        );
    }

    private void continuarCompra() {
        ArrayList<String> asientos =
                vista.obtenerAsientosSeleccionados();

        if (asientos.isEmpty()) {
            vista.mostrarMensaje(
                    "Seleccione al menos un asiento."
            );
            return;
        }

        Funcion funcion =
                sesionCompra.getFuncionSeleccionada();

        //Se comprueba nuevamente por seguridad que ninguno
        //haya sido vendido antes de continuar.    
        for (String asiento : asientos) {
            boolean ocupado =
                    repositorioAsientos.estaOcupado(
                            funcion.getCodigo(),
                            asiento
                    );

            if (ocupado) {
                vista.mostrarMensaje(
                        "El asiento "
                        + asiento
                        + " acaba de ser ocupado."
                );

                cargarAsientosOcupados(funcion);
                return;
            }
        }

        sesionCompra.setAsientosSeleccionados(asientos);

        vista.dispose();
        aplicacion.mostrarCompra();
    }

    private void volverCatalogo() {
        sesionCompra.reiniciarDesdeAsientos();

        vista.dispose();
        aplicacion.mostrarCatalogo();
    }
}