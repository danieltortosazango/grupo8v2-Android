package com.example.primeravistaapp;

public class Trayectos {

    private double idBici;
    private String email;
    private boolean movimiento;
    private String base;




    public  Trayectos(String email, double idBici, boolean movimiento, String base) {
        this.email = email;
        this.movimiento = movimiento;
        this.base = base;
        this.idBici = idBici;
    }


    public void setBase(String base) {
        this.base = base;
    }

    public double getIdBici() {
        return idBici;
    }

    public void setIdBici(double idBici) {
        this.idBici = idBici;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isMovimiento() {
        return movimiento;
    }

    public void setMovimiento(boolean movimiento) {
        this.movimiento = movimiento;
    }

    public String getBase() {
        return base;
    }

    @Override
    public String toString() {
        return "Trayectos{" +
                "idBici=" + idBici +
                ", email='" + email + '\'' +
                ", movimiento=" + movimiento +
                ", base='" + base + '\'' +
                '}';
    }
}
