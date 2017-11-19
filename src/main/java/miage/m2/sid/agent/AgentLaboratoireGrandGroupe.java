package miage.m2.sid.agent;

import jade.core.Agent;

public class AgentLaboratoireGrandGroupe extends Agent{

	protected void setup(){
		//initialisation de l'agent
		System.out.println(this.getName()+" "+this.getAID()+" started");
	}
	
	protected void takeDown(){
		//traitement de fin 
		System.out.println(this.getName()+" "+this.getAID()+" done");
	}
	
}
