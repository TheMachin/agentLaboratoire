package miage.m2.sid.event;

import javafx.beans.property.SimpleStringProperty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProposeEvent {
    private SimpleStringProperty time;
    private SimpleStringProperty asso;
    private SimpleStringProperty vaccin;
    private SimpleStringProperty nb;
    private SimpleStringProperty dlc;
    private SimpleStringProperty dateLivraison;
    private SimpleStringProperty price;
    private SimpleStringProperty status;

    public ProposeEvent(String asso, String vaccin, int nb, Date dlc, Date dateL, double price, String status) {
        time = new SimpleStringProperty(convertDateToString(new Date()));
        this.asso = new SimpleStringProperty(asso);
        this.vaccin = new SimpleStringProperty(vaccin);
        this.nb = new SimpleStringProperty(String.valueOf(nb));
        this.dlc = new SimpleStringProperty(convertDateToString(dlc));
        this.dateLivraison = new SimpleStringProperty(convertDateToString(dateL));
        this.price = new SimpleStringProperty(String.valueOf(price));
        this.status = new SimpleStringProperty(status);
    }

    public String getTime() {
        return time.get();
    }

    public SimpleStringProperty timeProperty() {
        return time;
    }

    public String getAsso() {
        return asso.get();
    }

    public SimpleStringProperty assoProperty() {
        return asso;
    }

    public String getVaccin() {
        return vaccin.get();
    }

    public SimpleStringProperty vaccinProperty() {
        return vaccin;
    }

    public String getNb() {
        return nb.get();
    }

    public SimpleStringProperty nbProperty() {
        return nb;
    }

    public String getDlc() {
        return dlc.get();
    }

    public SimpleStringProperty dlcProperty() {
        return dlc;
    }

    public String getDateLivraison() {
        return dateLivraison.get();
    }

    public SimpleStringProperty dateLivraisonProperty() {
        return dateLivraison;
    }

    public String getPrice() {
        return price.get();
    }

    public SimpleStringProperty priceProperty() {
        return price;
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public String convertDateToString(Date date){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return df.format(date);
    }

}
