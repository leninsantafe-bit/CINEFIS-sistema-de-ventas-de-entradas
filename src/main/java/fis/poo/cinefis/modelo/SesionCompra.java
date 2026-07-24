package fis.poo.cinefis.modelo;

import java.util.ArrayList;


public class SesionCompra {

    private Funcion funcionSeleccionada;
    private ArrayList<String> asientosSeleccionados;
    private Venta venta;

    public SesionCompra() {
        asientosSeleccionados = new ArrayList<>();
    }

    public Funcion getFuncionSeleccionada() {
        return funcionSeleccionada;
    }

    public void setFuncionSeleccionada(Funcion funcionSeleccionada) {
        this.funcionSeleccionada = funcionSeleccionada;
    }

    public ArrayList<String> getAsientosSeleccionados() {
        return new ArrayList<>(asientosSeleccionados);
    }

    public void setAsientosSeleccionados(ArrayList<String> asientosSeleccionados) {
        this.asientosSeleccionados = new ArrayList<>(asientosSeleccionados);
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }
    
    //Limpia toda la compra actual
    public void reiniciar() {
        funcionSeleccionada = null;
        asientosSeleccionados.clear();
        venta = null;
    }

    //limpia lo que se seleccionó despues de elegir la funcion
    //se conserva la funcion seleccionada
    public void reiniciarDesdeAsientos() {
        asientosSeleccionados.clear();
        venta = null;
    }
}
