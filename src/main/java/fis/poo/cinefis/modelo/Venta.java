package fis.poo.cinefis.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Venta {

    private final Funcion funcion;
    private final ArrayList<Entrada> entradas;
    private final ArrayList<DetalleSnack> detallesSnacks;

    public Venta(Funcion funcion) {
        this.funcion = funcion;
        this.entradas = new ArrayList<>();
        this.detallesSnacks = new ArrayList<>();
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public List<Entrada> getEntradas() {
        return Collections.unmodifiableList(entradas);
    }

    public List<DetalleSnack> getDetallesSnacks() {
        return Collections.unmodifiableList(detallesSnacks);
    }

    public void agregarEntrada(Entrada entrada) {
        if (entrada != null) {
            entradas.add(entrada);
        }
    }

    public void agregarDetalleSnack(DetalleSnack detalleSnack) {
        if (detalleSnack != null
                && detalleSnack.getCantidad() > 0) {
            detallesSnacks.add(detalleSnack);
        }
    }

    public double calcularSubtotalEntradas() {
        double subtotal = 0;
        for (Entrada entrada : entradas) {
            subtotal += entrada.getPrecio();
        }
        return subtotal;
    }

    public double calcularSubtotalSnacks() {
        double subtotal = 0;
        for (DetalleSnack detalle : detallesSnacks) {
            subtotal += detalle.calcularSubtotal();
        }
        return subtotal;
    }

    public double calcularTotal() {
        return calcularSubtotalEntradas()
                + calcularSubtotalSnacks();
    }

    public boolean tieneEntradas() {
        return !entradas.isEmpty();
    }

    public boolean tieneSnacks() {
        return !detallesSnacks.isEmpty();
    }
}