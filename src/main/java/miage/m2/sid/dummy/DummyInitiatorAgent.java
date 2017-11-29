package miage.m2.sid.dummy;

import com.google.gson.Gson;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import java.util.*;

public class DummyInitiatorAgent extends Agent {
    private DummyInitiator behaviour;
    private DFAgentDescription dfd;
    private DummyController dummyController;

    protected void setup() {
        //initialisation de l'agent
        //DummyAssocContainer container = (DummyAssocContainer) getArguments()[0];
        dummyController = (DummyController)getArguments()[1];
        dummyController.agentAssoc = this;
        dummyController.displayLabos();

        System.out.println(this.getName() + " " + this.getAID() + " started");

        this.behaviour = new DummyInitiator(this, initiate());
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

    ACLMessage initiate(){
        Gson gson = new Gson();
        CFP cfp = new CFP("rage",15, new Date());

        ACLMessage message = new ACLMessage(ACLMessage.CFP);
        message.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        message.setContent(gson.toJson(cfp));
        message.setSender(this.getAID());
        message.addReceiver(getLabos().get(0));

        return message;
    }

    void sendCFP(AID receiverAid){
        Gson gson = new Gson();
        CFP cfp = new CFP("sida",80, new Date());

        ACLMessage message = new ACLMessage(ACLMessage.CFP);
        message.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        message.setContent(gson.toJson(cfp));
        message.setSender(this.getAID());
        message.addReceiver(receiverAid);

        behaviour.reset(message);
        //send(message);
    }

    /*public ACLMessage startLabo() {
        DummyInitiatorAgent assocAgent = (DummyInitiatorAgent) this.myAgent;
        Objectif objectif = assocAgent.enCours;

        ACLMessage message = new ACLMessage(ACLMessage.CFP);
        message.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);

        CFP content = new CFP("rage", 80, new Date());
        Gson gson = new Gson();
        message.setContent(gson.toJson(content));

        AssocAgent agent = (AssocAgent) this.myAgent;
        message.setSender(agent.getAID());
        for (AID aid : agent.getLabos()) {
            message.addReceiver(aid);
        }
        return message;
    }*/

    List<AID> getLabos() {
        List<AID> labos = new ArrayList<AID>();
        DFAgentDescription dfd = new DFAgentDescription();
        try {
            DFAgentDescription[] result = DFService.search(this, dfd);
            for (DFAgentDescription desc : result) {
                Iterator iter = desc.getAllServices();
                while (iter.hasNext()) {
                    ServiceDescription sd = (ServiceDescription) iter.next();
                    if (sd.getType().equals("Labo")) {
                        labos.add(desc.getName());
                    }
                }
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        System.out.println("--------There are " + labos.size() + " labos");
        return labos;
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
