package miage.m2.sid.dummy;

import java.io.Serializable;
import java.util.Date;

public class Propose implements Serializable {

    private Integer nombre;
    private Double prix;
    private Date dateLivraison;
    private Date datePeremption;
    private double volume;

    public Propose() {
    }

    public Propose(Integer nombre, Double prix, Date dateLivraison, Date datePeremption, int volume) {
        this.nombre = nombre;
        this.prix = prix;
        this.dateLivraison = dateLivraison;
        this.datePeremption = datePeremption;
        this.volume = volume;
    }

    public Integer getNombre() {
        return nombre;
    }

    public void setNombre(Integer nombre) {
        this.nombre = nombre;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public Date getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public Date getDatePeremption() {
        return datePeremption;
    }

    public void setDatePeremption(Date datePeremption) {
        this.datePeremption = datePeremption;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "Propose{" +
                "nombre=" + nombre +
                ", prix=" + prix +
                ", dateLivraison=" + dateLivraison +
                ", datePeremption=" + datePeremption +
                ", volume=" + volume +
                '}';
    }
}
