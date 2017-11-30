package miage.m2.sid.behaviour;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import miage.m2.sid.EntityManager;
import miage.m2.sid.model.Generique;
import miage.m2.sid.model.Lot;
import miage.m2.sid.model.Vaccin;

import javax.persistence.Query;
import java.util.*;

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
        for(Generique laboGenerique : generiques){
            generateStock(laboGenerique);
        }

    }

    private void generateStock(Generique generique){
        Vaccin vaccin = getRandomVaccin();
        if(vaccin != null){
            int quantity = getRandomNumber(200,100);
            Lot lot = new Lot();
            lot.setLaboratoire(generique);
            lot.setNom(vaccin.getNom());
            lot.setVolume(quantity * vaccin.getVolume());
            lot.setPrix(quantity * vaccin.getPrix());
            lot.setNombre(quantity);

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.MONTH, 1);
            lot.setDateDLC(cal.getTime());

            em.getTransaction().begin();
            em.persist(lot);
            em.getTransaction().commit();
        }
    }

    private Vaccin getRandomVaccin(){
        int min = 0;
        int max = vaccins.size()-1;
        if(max <= 0){
            return null;
        }else{
            return vaccins.get(getRandomNumber(max,min));
        }
    }

    private static int getRandomNumber(int max, int min){
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    /*private void addStock(Generique generique){
        int min = 0;
        int max = vaccins.size()-1;
        int nb = min + (int)(Math.random() * ((max - min) + 1));
        int nb2=0;
        int nbVaccin = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, 1); // Add 1 month to current date
        Vaccin vaccin = vaccins.get(nb);
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
    }*/

    private List<Vaccin> getAllVaccins(){
        String r = "Select v from Vaccin v";
        Query q = em.createQuery(r);
        return q.getResultList();
    }

}
