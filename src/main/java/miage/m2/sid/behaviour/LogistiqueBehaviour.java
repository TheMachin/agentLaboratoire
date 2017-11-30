package miage.m2.sid.behaviour;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import miage.m2.sid.EntityManager;
import miage.m2.sid.model.Generique;
import miage.m2.sid.model.Lot;
import miage.m2.sid.model.Vaccin;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LogistiqueBehaviour extends TickerBehaviour{

    private javax.persistence.EntityManager em = EntityManager.getInstance();
    private List<Vaccin> vaccins;

    public LogistiqueBehaviour(Agent a, long period) {
        super(a, period);
        vaccins=getAllVaccins();
    }



    @Override
    protected void onTick() {
        System.out.println("start");
        String r = "SELECT g FROM Generique g";
        Query q = em.createQuery(r);
        List<Generique> generiques = q.getResultList();

        for(Generique g : generiques){
            addStock(g);
        }

    }

    private void addStock(Generique generique){
        int min = 0;
        int max = vaccins.size()-1;
        int nb = min + (int)(Math.random() * ((max - min) + 1));
        int nb2=0;
        int nbVaccin = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, 1); // Add 1 month to current date
        Vaccin vaccin = null;
        Lot lot = new Lot();
        lot.setNom(vaccin.getNom());
        lot.setDateDLC(cal.getTime());
        for(int i=0;i<nb;i++){
            nb2 = min + (int)(Math.random() * ((max - min) + 1));
            //on prend un vaccin de maniere aleatoire
            vaccin = vaccins.get(nb2);
            //on ajoute entre 100 et 200 vaccin
            nbVaccin = 100 + (int)(Math.random() * ((200 - 100) + 1));
            for(int x = 0 ; x<nbVaccin;x++){
                lot.addVaccin(vaccin);
            }
            //on ajoute un nombre aléatoire de lot
            for(int j=0;j<nb2;j++){
                generique.addLot(lot);
            }
        }
        em.getTransaction().begin();
        em.merge(generique);
        em.getTransaction().commit();
    }

    private List<Vaccin> getAllVaccins(){
        String r = "Select v from Vaccin v";
        Query q = em.createQuery(r);
        return q.getResultList();
    }

}
