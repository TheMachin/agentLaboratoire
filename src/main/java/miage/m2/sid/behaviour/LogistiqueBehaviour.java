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

    private List<Vaccin> getAllVaccins(){
        String r = "Select v from Vaccin v";
        Query q = em.createQuery(r);
        return q.getResultList();
    }

}
