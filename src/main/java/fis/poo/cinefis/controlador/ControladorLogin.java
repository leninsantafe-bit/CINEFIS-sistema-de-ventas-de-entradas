package fis.poo.cinefis.controlador;

import fis.poo.cinefis.modelo.Usuario;
import fis.poo.cinefis.vista.VistaLogin;

public class ControladorLogin {

    private final VistaLogin vista;
    private final ControladorAplicacion aplicacion;
    //Usuario temporal
    private final Usuario usuarioAdministrador;

    public ControladorLogin(
            VistaLogin vista,
            ControladorAplicacion aplicacion
    ) {
        this.vista = vista;
        this.aplicacion = aplicacion;

        usuarioAdministrador =
                new Usuario("admin", "1234", "Administrador");
    }

    public void iniciar() {
        configurarEventos();
        vista.setVisible(true);
    }

    private void configurarEventos() {
        vista.agregarEventoIngresar(e -> iniciarSesion());
        vista.agregarEventoSalir(e -> salir());
    }

    private void iniciarSesion() {
        String nombreUsuario = vista.obtenerUsuario().trim();
        String contrasena = vista.obtenerContrasena();

        if (nombreUsuario.isEmpty() || contrasena.isEmpty()) {
            vista.mostrarMensaje(
                    "Ingrese el usuario y la contraseña."
            );
            return;
        }

        boolean credencialesCorrectas =
                usuarioAdministrador.validarCredenciales(
                        nombreUsuario,
                        contrasena
                );

        if (!credencialesCorrectas) {
            vista.mostrarMensaje(
                    "Usuario o contraseña incorrectos."
            );

            vista.limpiarCampos();
            return;
        }

        vista.mostrarMensaje("Inicio de sesión correcto.");

        vista.dispose();
        aplicacion.mostrarCatalogo();
    }

    private void salir() {
        System.exit(0);
    }
}
