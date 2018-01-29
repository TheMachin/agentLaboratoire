package miage.m2.sid.dummy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import miage.m2.sid.query.Requetes;

import java.util.*;

public class DummyAssoSequentialBehavior extends SequentialBehaviour{
    final Gson gson = new GsonBuilder().create();
    public final String labosType = "labo";

    DummyController dummyController;

    DummyInitiator dummyInitiator;


    public DummyAssoSequentialBehavior(Agent a, DummyController dummyController) {
        super(a);
        dummyInitiator = new DummyInitiator(myAgent, initiate());
        this.addSubBehaviour(dummyInitiator);
        this.dummyController = dummyController;
        dummyController.agentAssoc = this;
        dummyController.displayLabos();
    }

    ACLMessage initiate(){
        Gson gson = new Gson();
        String vaccinName = Requetes.getRandomVaccinName();
        int nombre = getRandomNumber(300, 100);

        CFP cfp = new CFP(vaccinName,nombre, new Date());

        ACLMessage message = new ACLMessage(ACLMessage.CFP);
        message.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        message.setContent(gson.toJson(cfp));
        message.setSender(this.myAgent.getAID());
        for(AID aid : getLabos()){
            message.addReceiver(aid);
        }

        return message;
    }

    void sendCFP(AID receiverAid){
        System.out.println("receverAid : "+receiverAid);
        dummyInitiator.reset();
        Gson gson = new Gson();
        CFP cfp = new CFP("varicelle",100, new Date());

        ACLMessage message = new ACLMessage(ACLMessage.CFP);
        message.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        message.setContent(gson.toJson(cfp));
        message.setSender(this.myAgent.getAID());
        message.addReceiver(receiverAid);
    }

    List<AID> getLabos() {
        List<AID> labos = new ArrayList<AID>();
        DFAgentDescription dfd = new DFAgentDescription();
        try {
            DFAgentDescription[] result = DFService.search(myAgent, dfd);
            for (DFAgentDescription desc : result) {
                Iterator iter = desc.getAllServices();
                while (iter.hasNext()) {
                    ServiceDescription sd = (ServiceDescription) iter.next();
                    if (sd.getType().equals("labo")) {
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

    private static int getRandomNumber(int max, int min){
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}
