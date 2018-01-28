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

    /**
     * Send response and accept proposal with proba 1/2
     * @param response
     */
    private void sendResponse(ACLMessage response) {
        Gson gson = new Gson();

        if(response.getPerformative()==ACLMessage.PROPOSE){
            Propose propose = gson.fromJson(response.getContent(),Propose.class);
            //proba acceptation
            int accepte = 0 + (int)(Math.random() * ((1 - 0) + 1));
            if(accepte==1){
                ACLMessage agree = response.createReply();
                agree.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                System.out.println("-----------Accept_proposal-------------");
                this.myAgent.send(agree);
            }else{
                ACLMessage reject = response.createReply();
                reject.setPerformative(ACLMessage.REJECT_PROPOSAL);
                System.out.println("-----------Reject_proposal-------------");
                this.myAgent.send(reject);
            }
        }
    }

    /**
     * Reset cette behaviour
     */
    private void resetBehaviour() {
        System.out.println(myAgent.getLocalName() + " -------- Labo behaviour is reset");
        CommunicationBehaviour parent = (CommunicationBehaviour) this.parent;
        parent.init();
        this.reset(parent.startLabo());
    }

    public void reset(ACLMessage message){
        this.reset(message);
    }
}
