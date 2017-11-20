package miage.m2.sid;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManager {
    private static javax.persistence.EntityManager em;
    private static  EntityManagerFactory emf;

    public static void init(){
        emf = Persistence.createEntityManagerFactory("laboratoire");
        em = emf.createEntityManager();
    }

    public static javax.persistence.EntityManager getInstance(){
        return em;
    }
    
    public static void close(){
    	em.close();
    	emf.close();
    }
    
}
