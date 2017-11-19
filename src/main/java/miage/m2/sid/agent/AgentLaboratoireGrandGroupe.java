package miage.m2.sid.agent;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import miage.m2.sid.behaviour.BehaviourLaboratoireGrandGroupe;

public class AgentLaboratoireGrandGroupe extends Agent{

	private BehaviourLaboratoireGrandGroupe behaviour;

	protected void setup(){
		//initialisation de l'agent
		System.out.println(this.getName()+" "+this.getAID()+" started");
		this.behaviour = new BehaviourLaboratoireGrandGroupe(this);
		registerService();
		addBehaviour(behaviour);
	}

	private void registerService() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(this.getAID());

		ServiceDescription sd = new ServiceDescription();
		sd.setType("Labo");
		sd.setName("LaboGrandGroupe");

		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			System.err.println(getLocalName() +
					" registration with DF unsucceeded. Reason: " + e.getMessage());
			doDelete();
		}

	}
	
	protected void takeDown(){
		//traitement de fin
		removeBehaviour(behaviour);
		System.out.println(this.getName()+" "+this.getAID()+" done");
	}
	
}
