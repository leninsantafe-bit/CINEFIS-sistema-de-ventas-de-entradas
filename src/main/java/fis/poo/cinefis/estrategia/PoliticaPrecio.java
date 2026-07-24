package fis.poo.cinefis.estrategia;

public interface PoliticaPrecio {

    double calcularPrecio(double precioBase);

    String obtenerTipoCliente();
}