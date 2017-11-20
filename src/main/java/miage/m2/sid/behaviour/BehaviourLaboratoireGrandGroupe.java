package miage.m2.sid.behaviour;

import com.google.gson.Gson;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class BehaviourLaboratoireGrandGroupe extends CyclicBehaviour {
    private Gson gson;

    public BehaviourLaboratoireGrandGroupe(Agent agent) {
        super(agent);
        this.gson = new Gson();
    }

    @Override
    public void action() {
        ACLMessage aclMessage = myAgent.receive();
        
        if (aclMessage != null) {
            try {
            	System.out.println("Réception du nouveau message");
                System.out.println("Acte de communication");
                System.out.println("Language "+aclMessage.getLanguage());
                System.out.println("Onthology "+aclMessage.getOntology());
                String message = aclMessage.getContent();
                System.out.println(myAgent.getLocalName() + ": I receive message\n" +
                        aclMessage + "\nwith content\n" + message);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            this.block();
        }
    }
}
