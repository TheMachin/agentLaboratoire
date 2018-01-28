package miage.m2.sid.agent;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import miage.m2.sid.behaviour.LaboGenResponder;
import miage.m2.sid.container.LaboratoireGenContainer;
import miage.m2.sid.model.Generique;

public class AgentLaboratoireGenerique extends Agent{

	private LaboratoireGenContainer container;
	private Behaviour behaviour;
	private DFAgentDescription dfd;
    Generique labo;

	protected void setup(){
		container = (LaboratoireGenContainer) getArguments()[0];
		Object[] generique = getArguments();
		labo = (Generique) getArguments()[1];
		if(generique==null){
			System.out.println("fail");
		}
		//initialisation de l'agent
		System.out.println(this.getName()+" "+this.getAID()+" started");
		//putain dérreur humaine
		this.behaviour = new LaboGenResponder(this, null);
		registerService();
		addBehaviour(behaviour);
	}

	private void registerService() {
		dfd = new DFAgentDescription();
		dfd.setName(this.getAID());

		ServiceDescription sd = new ServiceDescription();
		sd.setType("Labo");
		sd.setName("LaboGenerique");

		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			System.err.println(getLocalName() +
					" registration with DF unsucceeded. Reason: " + e.getMessage());
			doDelete();
		}

	}

	/*
		Traitement de fin
	 */
	protected void takeDown(){
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

    public Generique getLabo() {
        return labo;
    }

    public void setLabo(Generique labo) {
        this.labo = labo;
    }
}
