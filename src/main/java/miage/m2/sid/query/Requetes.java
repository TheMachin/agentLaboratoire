package miage.m2.sid.query;

import miage.m2.sid.EntityManager;
import miage.m2.sid.model.Generique;
import miage.m2.sid.model.Laboratoire;
import miage.m2.sid.model.Lot;
import miage.m2.sid.model.Vaccin;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Requetes {

    private static javax.persistence.EntityManager em = EntityManager.getInstance();

    public static Generique getRandomLaboratoire(){
        String hql = "SELECT l FROM Generique l";
        Query query = EntityManager.getInstance().createQuery(hql);
        List<Generique> generiques = query.getResultList();
        Random r = new Random();
        int valeur = 0 + r.nextInt(generiques.size()-1 - 0);
        return generiques.get(valeur);
    }

    /**
     * recupere un stock de lot de vaccin d'un labo donné dont la date DLC est > à la date de livraison
     * @param vaccin
     * @param labo
     * @param dateLivraison
     * @return
     */
    public static List<Lot> getStock(Vaccin vaccin, Laboratoire labo, Date dateLivraison){
        //on recupere tous les lots du vaccin concerné
        String r = "SELECT lo FROM Lot lo WHERE DATEDIFF(dateDLC,:dateLivraison)>0 AND lo.nom=:name AND laboratoire_id=:labo";
        Query q = em.createQuery(r);
        q.setParameter("name",vaccin.getNom());
        q.setParameter("labo",labo.getNom());
        q.setParameter("dateLivraison", dateLivraison);
        System.out.println("taille des résultats getStock : "+q.getResultList().size());
        return q.getResultList();
    }

    /**
     * Obtenir le nom du vaccin qui soigne la maladie
     * @param name
     * @return
     */
    public static Vaccin getVaccinByName(String name){
        String hql = "SELECT V FROM Vaccin V WHERE V.nom = :name";
        Query query = EntityManager.getInstance().createQuery(hql);
        query.setParameter("name",name);
        Vaccin vaccin = null;
        try{
            vaccin = (Vaccin) query.getSingleResult();
        }catch (NoResultException no){

        }
        if(vaccin!=null){
            return vaccin;
        }else{
            return null;
        }
    }

    public static Laboratoire getLaboratoireByName(String name){
        String hql = "SELECT l FROM Laboratoire l WHERE l.nom = :name";
        Query query = EntityManager.getInstance().createQuery(hql);
        //Obtenir le nom du laboratoire sans son adresse
        query.setParameter("name", name);
        Laboratoire la = null;

        try{
            la = (Laboratoire) query.getSingleResult();
            return la;
        }catch (NoResultException no){

        }
        return null;
    }

    public static String getRandomVaccinName(){
        EntityManager.init();
        String hql = "SELECT v FROM Vaccin v";
        Query query = EntityManager.getInstance().createQuery(hql);
        List<Vaccin> vaccins = query.getResultList();
        Random r = new Random();
        int index = r.nextInt(vaccins.size());
        EntityManager.close();
        return vaccins.get(index).getNom();
    }

}
