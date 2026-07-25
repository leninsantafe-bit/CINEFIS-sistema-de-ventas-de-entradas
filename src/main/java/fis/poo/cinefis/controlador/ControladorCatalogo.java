package fis.poo.cinefis.controlador;

import fis.poo.cinefis.modelo.Funcion;
import fis.poo.cinefis.modelo.SesionCompra;
import fis.poo.cinefis.repositorio.RepositorioFunciones;
import fis.poo.cinefis.vista.VistaCatalogo;
import java.util.ArrayList;

public class ControladorCatalogo {

    private final VistaCatalogo vista;
    private final SesionCompra sesionCompra;
    private final ControladorAplicacion aplicacion;
    private final RepositorioFunciones repositorioFunciones;

    public ControladorCatalogo(
            VistaCatalogo vista,
            SesionCompra sesionCompra,
            ControladorAplicacion aplicacion
    ) {
        this.vista = vista;
        this.sesionCompra = sesionCompra;
        this.aplicacion = aplicacion;

        repositorioFunciones = new RepositorioFunciones();
    }

    public void iniciar() {
        cargarFunciones();
        configurarEventos();
        vista.setVisible(true);
    }

    private void cargarFunciones() {
        ArrayList<Funcion> funciones =
                repositorioFunciones.obtenerFunciones();

        if (funciones.isEmpty()) {
            vista.mostrarMensaje(
                    "No existen funciones disponibles."
            );
        }

        vista.mostrarFunciones(funciones);
    }

    private void configurarEventos() {
        vista.agregarEventoSeleccionarFuncion(
                e -> seleccionarFuncion()
        );

        vista.agregarEventoCerrarSesion(
                e -> cerrarSesion()
        );

        vista.agregarEventoSalir(
                e -> salir()
        );
    }

    private void seleccionarFuncion() {
        Funcion funcionSeleccionada =
                vista.obtenerFuncionSeleccionada();

        if (funcionSeleccionada == null) {
            vista.mostrarMensaje(
                    "Seleccione una función de la tabla."
            );
            return;
        }

        sesionCompra.setFuncionSeleccionada(
                funcionSeleccionada
        );

        sesionCompra.reiniciarDesdeAsientos();

        vista.dispose();
        aplicacion.mostrarSeleccionAsientos();
    }

    private void cerrarSesion() {
        vista.dispose();
        aplicacion.cerrarSesion();
    }

    private void salir() {
        System.exit(0);
    }
}
