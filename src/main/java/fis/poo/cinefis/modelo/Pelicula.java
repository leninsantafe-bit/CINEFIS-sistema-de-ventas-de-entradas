package fis.poo.cinefis.modelo;

public class Pelicula {

    private String codigo;
    private String titulo;
    private String genero;
    private String clasificacion;
    private int duracion;
    private String imagen;

    public Pelicula(String codigo, String titulo, String genero, String clasificacion, int duracion, String imagen) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.genero = genero;
        this.clasificacion = clasificacion;
        this.duracion = duracion;
        this.imagen = imagen;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getGenero() {
        return genero;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public int getDuracion() {
        return duracion;
    }

    public String getImagen() {
        return imagen;
    }

    @Override
    public String toString() {
        return titulo;
    }
}
