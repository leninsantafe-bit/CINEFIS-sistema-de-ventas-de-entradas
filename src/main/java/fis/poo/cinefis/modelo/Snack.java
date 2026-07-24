package fis.poo.cinefis.modelo;

public class Snack {

    private String codigo;
    private String nombre;
    private double precio;
    private String imagen;

    public Snack(String codigo, String nombre, double precio, String imagen) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.imagen = imagen;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public String getImagen() {
        return imagen;
    }

    @Override
    public String toString() {
        return nombre + " - $" + precio;
    }
}
