package miage.m2.sid;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;
import miage.m2.sid.excel.model.PoiVaccin;
import miage.m2.sid.model.Generique;
import miage.m2.sid.model.Laboratoire;

public class Main {

	public static void main (String[] args) 
    {
		EntityManager.init();
		//EntityManager.close();
        //getAllSickFormSheet("/sheet/maladie.xlsx");
        //jeuEssaisGenerique();
		try {
			Runtime runtime = Runtime.instance();
			jade.util.leap.Properties properties = new ExtendedProperties();
			properties.setProperty(Profile.GUI, "true");
			
			ProfileImpl profileImpl = new ProfileImpl(properties);
			profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");
			profileImpl.setParameter(ProfileImpl.MAIN_PORT, "1099");
			AgentContainer agentContainer = runtime.createMainContainer(profileImpl);
			agentContainer.start();
			
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
	
	
	private static  void jeuEssaisGenerique(){

        javax.persistence.EntityManager em = EntityManager.getInstance();

        Laboratoire l1 = new Laboratoire();
		l1.setNom("Le générique de l'ouest");
		l1.setCa(0);
		l1.setLots(null);
		l1.setLots(null);
        Laboratoire l2 = new Laboratoire();
        l2.setNom("La contrefaçon chinoise");
        l2.setCa(0);
        l2.setLots(null);
        l2.setLots(null);
        Laboratoire l3 = new Laboratoire();
        l3.setNom("GeneriqueLabo");
        l3.setCa(0);
        l3.setLots(null);
        l3.setLots(null);
        Laboratoire l4 = new Laboratoire();
        l4.setNom("El famoso labo");
        l4.setCa(0);
        l4.setLots(null);
        l4.setLots(null);
        Laboratoire l5 = new Laboratoire();
        l5.setNom("Le petit labo");
        l5.setCa(0);
        l5.setLots(null);
        l5.setLots(null);
		em.getTransaction().begin();
		em.persist(l1);
        em.persist(l2);
        em.persist(l3);
        em.persist(l4);
        em.persist(l5);
        em.getTransaction().commit();
	}

	private static void getAllSickFormSheet(String fileName){
        PoiVaccin poiVaccin = new PoiVaccin(fileName);
        poiVaccin.setAllMaladie();
    }
	
}
