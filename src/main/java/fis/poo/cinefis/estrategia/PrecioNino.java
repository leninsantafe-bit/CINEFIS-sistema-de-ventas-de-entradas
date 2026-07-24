package fis.poo.cinefis.estrategia;

public class PrecioNino implements PoliticaPrecio {

    @Override
    public double calcularPrecio(double precioBase) {
        return precioBase * 0.60;
    }

    @Override
    public String obtenerTipoCliente() {
        return "Niño";
    }
}