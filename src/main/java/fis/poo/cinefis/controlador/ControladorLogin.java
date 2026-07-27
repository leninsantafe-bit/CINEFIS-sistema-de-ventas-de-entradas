package fis.poo.cinefis.controlador;

import fis.poo.cinefis.modelo.Usuario;
import fis.poo.cinefis.repositorio.RepositorioUsuarios;
import fis.poo.cinefis.vista.VistaLogin;

public class ControladorLogin {

    private final VistaLogin vista;
    private final ControladorAplicacion aplicacion;
    private final RepositorioUsuarios repositorioUsuarios;

    public ControladorLogin(
            VistaLogin vista,
            ControladorAplicacion aplicacion
    ) {
        this.vista = vista;
        this.aplicacion = aplicacion;

        repositorioUsuarios = new RepositorioUsuarios();
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
        String username = vista.obtenerUsuario().trim();

        String contrasena = vista.obtenerContrasena();

        if (username.isEmpty()|| contrasena.isEmpty()) {
            vista.mostrarMensaje("Ingrese el usuario y la contraseña.");
            return;
        }

        Usuario usuario = repositorioUsuarios.autenticar(username, contrasena);
        
        if (usuario.getRol().equals("Administrador")) {
            vista.dispose(); 
            aplicacion.mostrarRegistroPeliculas(); 
        } 
        
        if (usuario == null) {
            vista.mostrarMensaje("Usuario o contraseña incorrectos.");
            return;
        }
        if (usuario.getRol().equals("Cajero")) {
            vista.dispose(); 
            aplicacion.mostrarCatalogo();
            vista.dispose();
        } 
        vista.mostrarMensaje(
                "Bienvenido, "
                + usuario.getUsername()
                + "\nRol: "
                + usuario.getRol()
        );
        aplicacion.establecerUsuarioAutenticado(usuario);
        
        
        
    }

    private void salir() {
        System.exit(0);
    }
}
