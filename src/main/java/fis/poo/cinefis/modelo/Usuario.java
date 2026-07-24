package fis.poo.cinefis.modelo;

public class Usuario {

    private String nombreUsuario;
    private String contrasena;
    private String rol;

    public Usuario(String nombreUsuario, String contrasena, String rol) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getRol() {
        return rol;
    }

    public boolean validarCredenciales(String usuario, String contrasena) {
        return this.nombreUsuario.equals(usuario) && this.contrasena.equals(contrasena);
    }
}