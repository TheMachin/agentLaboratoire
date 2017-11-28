package miage.m2.sid.agent;


import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import miage.m2.sid.behaviour.BehaviourLaboratoireGrandGroupe;
import miage.m2.sid.model.Laboratoire;
import miage.m2.sid.model.container.LaboratoireGrandGroupeContainer;
import miage.m2.sid.ui.InterfaceAgentLaboratoire;

public class AgentLaboratoireGrandGroupe extends Agent{

	private BehaviourLaboratoireGrandGroupe behaviour;
	private LaboratoireGrandGroupeContainer container;
	private InterfaceAgentLaboratoire gui;
	private Laboratoire laboratoire = null;
	private DFAgentDescription dfd;

	protected void setup(){
		//initialisation de l'agent
		container = (LaboratoireGrandGroupeContainer) getArguments()[0];
		laboratoire = (Laboratoire) getArguments()[1];
		if(laboratoire==null){
			System.out.println("fail");
		}
		container.setAgentGrandGroupe(this);
		gui = container.getControllerInterface();
		System.out.println(laboratoire.getNom());
		System.out.println(this.getName()+" "+this.getAID()+" started");
		
		this.behaviour = new BehaviourLaboratoireGrandGroupe(this, gui, laboratoire);
		registerService();
		addBehaviour(behaviour);		
	}

	private void registerService() {
		dfd = new DFAgentDescription();
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
		if(behaviour!=null){
			try {
				//deregister from yellow page
				DFService.deregister(this, this.getAID(), dfd);
			} catch (FIPAException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			removeBehaviour(behaviour);
			System.out.println(this.getName()+" "+this.getAID()+" done");
		}
	}
	
}
