package fis.poo.cinefis.modelo;

public class Funcion {

    private String codigo;
    private Pelicula pelicula;
    private Sala sala;
    private String fecha;
    private String hora;
    private double precioBase;

    public Funcion(String codigo, Pelicula pelicula, Sala sala, String fecha, String hora, double precioBase) {
        this.codigo = codigo;
        this.pelicula = pelicula;
        this.sala = sala;
        this.fecha = fecha;
        this.hora = hora;
        this.precioBase = precioBase;
    }

    public String getCodigo() {
        return codigo;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public Sala getSala() {
        return sala;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    @Override
    public String toString() {
        return pelicula.getTitulo() + " - " + sala.getNombre() + " - " + hora;
    }
}