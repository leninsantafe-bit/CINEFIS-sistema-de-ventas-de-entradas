package fis.poo.cinefis.modelo;

public class Entrada {

    private String asiento;
    private int edad;
    private String tipoCliente;
    private double precio;

    public Entrada(String asiento, int edad, String tipoCliente, double precio) {
        this.asiento = asiento;
        this.edad = edad;
        this.tipoCliente = tipoCliente;
        this.precio = precio;
    }

    public String getAsiento() {
        return asiento;
    }

    public int getEdad() {
        return edad;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public double getPrecio() {
        return precio;
    }

    @Override
    public String toString() {
        return asiento
                + " - "
                + tipoCliente
                + " - $"
                + String.format("%.2f", precio);
    }
    
    
}