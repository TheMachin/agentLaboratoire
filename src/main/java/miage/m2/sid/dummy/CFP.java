package miage.m2.sid.dummy;

import java.io.Serializable;
import java.util.Date;

public class CFP implements Serializable {

    private String maladie;
    private int nb;
    private Date date;

    public CFP(String maladie, int nb, Date date) {
        this.maladie = maladie;
        this.nb = nb;
        this.date = date;
    }

    public String getMaladie() {
        return maladie;
    }

    public void setMaladie(String maladie) {
        this.maladie = maladie;
    }

    public int getNb() {
        return nb;
    }

    public void setNb(int nb) {
        this.nb = nb;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
