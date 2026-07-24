package fis.poo.cinefis.estrategia;

public class PrecioFactory {

    public static PoliticaPrecio obtenerPolitica(int edad) {
        if (edad <= 12) {
            return new PrecioNino();
        } else if (edad >= 65) {
            return new PrecioAdultoMayor();
        } else {
            return new PrecioAdulto();
        }
    }
}