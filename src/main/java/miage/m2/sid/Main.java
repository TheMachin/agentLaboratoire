package miage.m2.sid;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;
import miage.m2.sid.excel.model.PoiVaccinDao;
import miage.m2.sid.model.Generique;
import miage.m2.sid.model.Laboratoire;

public class Main {
	
	public static void main (String[] args) 
    {
		EntityManager.init();
		//EntityManager.close();
        getAllSickFormSheet("/sheet/maladie.xlsx");
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
	
	
	private static  Generique jeuEssaisGenerique(){
		Laboratoire labo = new Generique();
		labo.setNom("Le générique de l'ouest");
		/*Lot l1 = new Lot("Vaccin rage", new GregorianCalendar(2017, 10, 10).getTime(), 100.0, 10, 30.0, m);
		Lot l2 = new Lot("Vaccin rage", new GregorianCalendar(2017, 1, 31).getTime(), 100.0, 10, 30.0, m);
		labo.addLot(l1);
		labo.addLot(l2);*/
		return (Generique) labo;
	}

	private static void getAllSickFormSheet(String fileName){
        PoiVaccinDao poiVaccinDao = new PoiVaccinDao(fileName);
        poiVaccinDao.setAllMaladie();

    }
	
}
