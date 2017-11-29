package miage.m2.sid.dummy;

import com.google.gson.Gson;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DummyInitiator extends ContractNetInitiator {
    public DummyInitiator(Agent a, ACLMessage cfp) {
        super(a, cfp);
    }

    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {
        System.out.println("--------" + responses.size() + "responses");
        System.out.println("--------" + acceptances.size() + "acceptances");
        List<ACLMessage> proposeResponse = new ArrayList<ACLMessage>();
        for (Object response : responses) {
            ACLMessage reponseMessage = (ACLMessage) response;
            System.out.println("Response : " + response.toString());
            if (reponseMessage.getPerformative() == ACLMessage.PROPOSE) {
                proposeResponse.add(reponseMessage);
                sendResponse(reponseMessage);

            }
        }
    }

    private void sendResponse(ACLMessage response) {
        Gson gson = new Gson();
        ACLMessage agree = response.createReply();
        agree.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
        this.myAgent.send(agree);
    }
}
