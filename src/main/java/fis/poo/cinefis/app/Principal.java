package fis.poo.cinefis.app;

import fis.poo.cinefis.controlador.ControladorAplicacion;
import javax.swing.SwingUtilities;

public class Principal {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ControladorAplicacion aplicacion = new ControladorAplicacion();
            aplicacion.iniciar();
        });
    }
}