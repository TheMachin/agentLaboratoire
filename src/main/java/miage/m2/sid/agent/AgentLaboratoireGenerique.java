package miage.m2.sid.agent;

import jade.core.Agent;
import miage.m2.sid.model.Generique;

public class AgentLaboratoireGenerique extends Agent{

	protected void setup(){
		Object[] generique = getArguments();
		Generique labo = null;
		if(generique!=null){
			labo=(Generique) generique[0];
			System.out.println(labo.getNom());
		}
		//initialisation de l'agent
		System.out.println(this.getName()+" "+this.getAID()+" started");
	}
	
	protected void takeDown(){
		//traitement de fin 
		System.out.println(this.getName()+" "+this.getAID()+" done");
	}
	
}
