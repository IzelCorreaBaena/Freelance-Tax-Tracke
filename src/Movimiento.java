package src;
public class Movimiento {

    private String concepto;
    private double costo; 
    private boolean esGasto;
    
    public Movimiento(String concepto, double costo, boolean esGasto) {
        this.concepto = concepto;
        this.costo = costo;
        this.esGasto = esGasto;
    }

    public double getCosto() {
        return this.costo;
    }

    public String getConcepto() {
        return this.concepto;
    }

    public boolean isGasto() {
        return this.esGasto;
    }



}