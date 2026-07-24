package fis.poo.cinefis.modelo;

public class DetalleSnack {

    private Snack snack;
    private int cantidad;

    public DetalleSnack(Snack snack, int cantidad) {
        this.snack = snack;
        this.cantidad = cantidad;
    }

    public Snack getSnack() {
        return snack;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double calcularSubtotal() {
        return snack.getPrecio() * cantidad;
    }
}