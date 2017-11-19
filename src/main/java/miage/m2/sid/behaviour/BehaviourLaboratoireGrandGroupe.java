package miage.m2.sid.behaviour;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class BehaviourLaboratoireGrandGroupe extends CyclicBehaviour{


	@Override
	public void action() {
		// TODO Auto-generated method stub
		ACLMessage message = null;
		while(message==null){
			message = this.getAgent().receive();
		}
		//action
	}

}
