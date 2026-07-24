package fis.poo.cinefis.estrategia;

public class PrecioAdultoMayor implements PoliticaPrecio {

    @Override
    public double calcularPrecio(double precioBase) {
        return precioBase * 0.50;
    }

    @Override
    public String obtenerTipoCliente() {
        return "Adulto mayor";
    }
}