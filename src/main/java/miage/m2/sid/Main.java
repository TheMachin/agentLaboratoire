package miage.m2.sid;

import java.util.Date;
import java.util.GregorianCalendar;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import miage.m2.sid.agent.AgentLaboratoireGenerique;
import miage.m2.sid.agent.AgentLaboratoireGrandGroupe;
import miage.m2.sid.model.Generique;
import miage.m2.sid.model.Laboratoire;
import miage.m2.sid.model.Lot;
import miage.m2.sid.model.Maladie;

public class Main {
	
	public static void main (String[] args) 
    {
		EntityManager.init();
		try {
			Runtime runtime = Runtime.instance();
			jade.util.leap.Properties properties = new ExtendedProperties();
			properties.setProperty(Profile.GUI, "true");
			
			ProfileImpl profileImpl = new ProfileImpl(properties);
			profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");
			profileImpl.setParameter(ProfileImpl.MAIN_PORT, "1099");
			AgentContainer agentContainer = runtime.createMainContainer(profileImpl);
			AgentController agentLabo = agentContainer
					.createNewAgent("AgentLabo", AgentLaboratoireGrandGroupe.class.getName(), null);
			Generique[] generique = new Generique[1];
			generique[0]=jeuEssaisGenerique();
			AgentController agentLaboGeneric = agentContainer
					.createNewAgent("AgentLaboGenerique", AgentLaboratoireGenerique.class.getName(), generique);
			
			agentLabo.start();
			agentLaboGeneric.start();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//AgentLaboratoireGrandGroupe agentLabo = new AgentLaboratoireGrandGroupe();
		
    }
	
	
	private static  Generique jeuEssaisGenerique(){
		Laboratoire labo = new Generique(30.0);
		labo.setNom("Le générique de l'ouest");
		Lot l1 = new Lot("Vaccin rage", new GregorianCalendar(2017, 10, 10).getTime(), 100.0, 10, 30.0, new Maladie("Rage"));
		Lot l2 = new Lot("Vaccin rage", new GregorianCalendar(2017, 1, 31).getTime(), 100.0, 10, 30.0, new Maladie("Rage"));
		labo.addLot(l1);
		labo.addLot(l2);
		return (Generique) labo;
	}
	
}
