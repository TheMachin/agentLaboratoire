package miage.m2.sid.dummy;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class DummyInitiatorAgent extends Agent {
    private Behaviour behaviour;
    private DFAgentDescription dfd;

    protected void setup() {
        //initialisation de l'agent
        DummyAssocContainer container = (DummyAssocContainer) getArguments()[0];

        System.out.println(this.getName() + " " + this.getAID() + " started");

        this.behaviour = new DummyInitiator(this, null);
        registerService();
        addBehaviour(behaviour);
    }

    private void registerService() {
        dfd = new DFAgentDescription();
        dfd.setName(this.getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType("Assoc");
        sd.setName("DummyInitiatorAgent");

        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            System.err.println(getLocalName() +
                    " registration with DF unsucceeded. Reason: " + e.getMessage());
            doDelete();
        }

    }

    protected void takeDown() {
        //traitement de fin
        try {
            //deregister from yellow page
            DFService.deregister(this, this.getAID(), dfd);
            System.out.println(this.getName() + " " + this.getAID() + " done");
        } catch (FIPAException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (behaviour != null) {
            removeBehaviour(behaviour);
        }
    }
}
