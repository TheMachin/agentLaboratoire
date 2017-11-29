package miage.m2.sid.behaviour;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import miage.m2.sid.EntityManager;
import miage.m2.sid.model.Generique;
import miage.m2.sid.model.Vaccin;

import javax.persistence.Query;
import java.util.ArrayList;
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
        Vaccin vaccin = null;
        for(int i=0;i<nb;i++){
            nb2 = min + (int)(Math.random() * ((max - min) + 1));
            vaccin = vaccins.get(nb2);
            for(int j=0;j<nb2;j++){
                generique.addVaccin(vaccin);
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
