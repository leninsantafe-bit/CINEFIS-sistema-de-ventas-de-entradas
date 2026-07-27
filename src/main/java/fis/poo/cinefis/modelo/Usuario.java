package fis.poo.cinefis.modelo;

public class Usuario {

    private final String username;
    private final String contrasena;
    private final String rol;

    public Usuario(
            String username,
            String contrasena,
            String rol) {
        this.username = username;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public String getUsername() {
        return username;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getRol() {
        return rol;
    }

    public boolean credencialesCoinciden(String username, String contrasena) {
        return this.username.equals(username) && this.contrasena.equals(contrasena);
    }

    @Override
    public String toString() {
        return username + " - " + rol;
    }
}