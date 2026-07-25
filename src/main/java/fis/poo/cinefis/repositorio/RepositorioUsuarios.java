package fis.poo.cinefis.repositorio;

import fis.poo.cinefis.modelo.Usuario;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class RepositorioUsuarios {

    private final String rutaArchivo =
            "datos/usuarios.txt";

    public ArrayList<Usuario> obtenerUsuarios() {
        ArrayList<Usuario> usuarios =
                new ArrayList<>();

        File archivo = new File(rutaArchivo);

        if (!archivo.exists()) {
            System.out.println(
                    "No existe el archivo de usuarios: "
                    + archivo.getAbsolutePath()
            );

            return usuarios;
        }

        try (BufferedReader br = 
                new BufferedReader(new FileReader(archivo))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();

                if (linea.isEmpty()) {
                    continue;
                }

                String[] datos =
                        linea.split(";", -1);

                if (datos.length != 3) {
                    System.out.println(
                            "Línea de usuario inválida: "
                            + linea
                    );

                    continue;
                }

                String username = datos[0].trim();

                String contrasena = datos[1].trim();

                String rol = datos[2].trim();

                if (username.isEmpty() || contrasena.isEmpty()
                        || rol.isEmpty()) {
                    continue;
                }

                Usuario usuario = new Usuario(
                        username,
                        contrasena,
                        rol
                );

                usuarios.add(usuario);
            }

        } catch (IOException e) {
            System.out.println(
                    "Error al leer usuarios: " + e.getMessage()
            );
        }

        return usuarios;
    }

    public Usuario autenticar(String username, String contrasena) {
        ArrayList<Usuario> usuarios =
                obtenerUsuarios();

        for (Usuario usuario : usuarios) {
            if (usuario.credencialesCoinciden(
                    username,
                    contrasena
            )) {
                return usuario;
            }
        }

        return null;
    }
}
