package miage.m2.sid.dummy;

import com.google.gson.Gson;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class DummyInitiatorAgent extends Agent {
    private DummyAssoSequentialBehavior behaviour;
    private DFAgentDescription dfd;
    private DummyController dummyController;

    protected void setup() {
        //initialisation de l'agent
        System.out.println(this.getName() + " " + this.getAID() + " started");

        this.behaviour = new DummyAssoSequentialBehavior(this, (DummyController)getArguments()[1]);
        registerService();
        addBehaviour(behaviour);
    }

    private void registerService() {
        dfd = new DFAgentDescription();
        dfd.setName(this.getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType("assos");
        sd.setName("assoc");

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
