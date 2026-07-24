package fis.poo.cinefis.estrategia;

public class PrecioAdulto implements PoliticaPrecio {

    @Override
    public double calcularPrecio(double precioBase) {
        return precioBase;
    }

    @Override
    public String obtenerTipoCliente() {
        return "Adulto";
    }
}