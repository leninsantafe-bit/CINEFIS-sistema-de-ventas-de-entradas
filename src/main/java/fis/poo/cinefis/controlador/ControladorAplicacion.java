package fis.poo.cinefis.controlador;

import fis.poo.cinefis.modelo.SesionCompra;
import fis.poo.cinefis.vista.VistaCatalogo;
import fis.poo.cinefis.vista.VistaCompra;
import fis.poo.cinefis.vista.VistaLogin;
import fis.poo.cinefis.vista.VistaResumenCompra;
import fis.poo.cinefis.vista.VistaSeleccionAsientos;

public class ControladorAplicacion {

    private final SesionCompra sesionCompra;

    public ControladorAplicacion() {
        sesionCompra = new SesionCompra();
    }

    
    //Inicia el flujo de la aplicación mostrando el login.
    
    public void iniciar() {
        mostrarLogin();
    }

    public void mostrarLogin() {
        sesionCompra.reiniciar();

        VistaLogin vistaLogin = new VistaLogin();

        ControladorLogin controladorLogin =
                new ControladorLogin(vistaLogin, this);

        controladorLogin.iniciar();
    }

    public void mostrarCatalogo() {
        VistaCatalogo vistaCatalogo = new VistaCatalogo();

        ControladorCatalogo controladorCatalogo =
                new ControladorCatalogo(
                        vistaCatalogo,
                        sesionCompra,
                        this
                );

        controladorCatalogo.iniciar();
    }

    public void mostrarSeleccionAsientos() {
        VistaSeleccionAsientos vistaAsientos =
                new VistaSeleccionAsientos();

        ControladorSeleccionAsientos controladorAsientos =
                new ControladorSeleccionAsientos(
                        vistaAsientos,
                        sesionCompra,
                        this
                );

        controladorAsientos.iniciar();
    }

    public void mostrarCompra() {
        VistaCompra vistaCompra =
                new VistaCompra(sesionCompra.getAsientosSeleccionados());

        ControladorCompra controladorCompra =
                new ControladorCompra(
                        vistaCompra,
                        sesionCompra,
                        this
                );

        controladorCompra.iniciar();
    }

    public void mostrarResumenCompra() {
        VistaResumenCompra vistaResumen = new VistaResumenCompra();

        ControladorResumenCompra controladorResumen = new ControladorResumenCompra(
                        vistaResumen,
                        sesionCompra,
                        this
                );

        controladorResumen.iniciar();
    }

    
    //Inicia una nueva compra sin cerrar la sesión.
    
    public void iniciarNuevaCompra() {
        sesionCompra.reiniciar();
        mostrarCatalogo();
    }

    public SesionCompra getSesionCompra() {
        return sesionCompra;
    }
}
