package miage.m2.sid.behaviour;

import com.google.gson.Gson;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import miage.m2.sid.EntityManager;
import miage.m2.sid.dummy.CFP;
import miage.m2.sid.dummy.Propose;
import miage.m2.sid.model.Association;
import miage.m2.sid.model.Lot;
import miage.m2.sid.model.Offre;
import miage.m2.sid.model.Vaccin;

import javax.persistence.Query;
import java.util.*;

public class LaboGrandGroupeResponder extends ContractNetResponder {

    private javax.persistence.EntityManager em = EntityManager.getInstance();
    // Take care that if mt is null every message is consumed by this protocol.
    public LaboGrandGroupeResponder(Agent a, MessageTemplate mt) {
        super(a, mt);
    }

    public LaboGrandGroupeResponder(Agent a, MessageTemplate mt, DataStore store) {
        super(a, mt, store);
    }

    /*  When a message arrives that matches the message template passed to the constructor,
        the callback method handleCfp() is executed that must return the wished response,
        for instance the PROPOSE reply message.
        Any other type of returned communicative act is sent and then closes the protocol.

        @param cfp - the initial CFP message to handle.
        @return the reply message to be sent back to the initiator.
        Returning a message different than PROPOSE (or returning null) terminates the protocol.
     */
    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException, FailureException, NotUnderstoodException {
        System.out.println("GRAND GROUPE Called : handleCfp");
        System.out.println("Ontology : "+cfp.getOntology());
        System.out.println("Performative : "+cfp.getPerformative());
        System.out.println("Content : "+cfp.getContent());

        return getACLMessageFromProposition(cfp);
    }

    /*
        Then, if the initiator accepted the proposal, i.e. if an ACCEPT-PROPOSAL message was received,
        the callback method handleAcceptProposal would be executed that must return the message with the result notification,
        i.e. INFORM or FAILURE.
        This method is called when an ACCEPT_PROPOSAL message is received from the initiator.
        This default implementation does nothing and returns null.

        @param cfp - the initial CFP message.
        @param propose - the PROPOSE message sent back as reply to the initial CFP message.
        @param accept - the received ACCEPT_PROPOSAL message.

        @return the reply message to be sent back to the initiator.
     */
    @Override
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
        System.out.println("Called : handleAcceptProposal");
        System.out.println("Ontology : "+cfp.getOntology());
        System.out.println("Performative : "+cfp.getPerformative());
        System.out.println("Content : "+cfp.getContent());

        acceptPropose(cfp,propose,accept);

        return super.handleAcceptProposal(cfp, propose, accept);
    }

    /*
        In alternative, if the initiator rejected the proposal, i.e. if an REJECT-PROPOSAL message was received,
        the callback method handleRejectProposal would be executed and the protocol terminated.
        This method is called when a REJECT_PROPOSAL message is received from the initiator.
        This default implementation does nothing.

        @param cfp - the initial CFP message.
        @param propose - the PROPOSE message sent back as reply to the initial CFP message.
        @param reject - the received REJECT_PROPOSAL message or null if no acceptance message is received from the initiator within the timeout specified in the :reply-by slot of the PROPOSE message.
     */
    @Override
    protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
        System.out.println("Called : handleRejectProposal");
        System.out.println("Ontology : "+cfp.getOntology());
        System.out.println("Performative : "+cfp.getPerformative());
        System.out.println("Content : "+cfp.getContent());

        super.handleRejectProposal(cfp, propose, reject);
    }

    /*
        If a message were received, with the same value of this conversation-id, but that does not comply with the FIPA protocol,
        than the method handleOutOfSequence would be called.
        This method is called whenever a message is received that does not comply to the protocol rules.

        @param cfp - the initial CFP message.
        @param propose - the PROPOSE message sent back as reply to the initial CFP message.
        @param msg - the received out-of-sequence message.
     */
    @Override
    protected void handleOutOfSequence(ACLMessage msg) {
        super.handleOutOfSequence(msg);
    }

    @Override
    protected void sessionTerminated() {
        super.sessionTerminated();
    }


    private ACLMessage getACLMessageFromProposition(ACLMessage messageReceived){

        ACLMessage replyMessage = messageReceived.createReply();
        Gson gson = new Gson();
        CFP cfp = null;
        Propose propose = null;
        try{
            cfp = gson.fromJson(messageReceived.getContent(), CFP.class);
            return createProposition(messageReceived);
        }catch(Exception e){
            try{
                propose = gson.fromJson(messageReceived.getContent(),Propose.class);
                /**
                 * pour dire qu'au bout de 3-5 fois on refuse
                 */
                return messageReceived;
            }catch(Exception ex){
                replyMessage = messageReceived.createReply();
                replyMessage.setContent("cancel");
                replyMessage.setPerformative(ACLMessage.FAILURE);
                return replyMessage;
            }
        }
    }

    private ACLMessage createProposition(ACLMessage messageReceived){
        Gson gson = new Gson();
        CFP cfp = gson.fromJson(messageReceived.getContent(), CFP.class);

        // TODO: 29/11/2017 select prix du vaccin from db
        Vaccin vaccin = getVaccinByName(cfp.getMaladie());
        int prixTotal = (int)(cfp.getNb() * vaccin.getPrix());
        int volumeTotal = (int)(cfp.getNb() * vaccin.getVolume());

        /**
         * Pas de vaccin pour la maladie
         */
        if(vaccin==null){
            // Create reply
            ACLMessage replyMessage = messageReceived.createReply();
            replyMessage.setContent("");
            replyMessage.setPerformative(ACLMessage.REJECT_PROPOSAL);

            return replyMessage;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.YEAR, 1); // Add 1 year to current date

        // Create proposition
        Propose proposition = new Propose(cfp.getNb(), prixTotal, cfp.getDate(), cal.getTime(), volumeTotal);
        System.out.println(proposition);

        // Create reply
        ACLMessage replyMessage = messageReceived.createReply();
        replyMessage.setContent(gson.toJson(proposition));
        replyMessage.setPerformative(ACLMessage.PROPOSE);

        return replyMessage;
    }

    private void acceptPropose(ACLMessage cfp, ACLMessage propose, ACLMessage accept){
        Gson gson = new Gson();
        CFP cfpM = gson.fromJson(cfp.getContent(),CFP.class);
        Propose p = gson.fromJson(propose.getContent(),Propose.class);

        Lot lot = new Lot();
        lot.setVolume(p.getVolume());
        lot.setNombre(p.getNombre());
        lot.setPrix((double) p.getPrix());
        lot.setDateDLC(p.getDatePeremption());
        Vaccin vaccin = getVaccinByName(cfpM.getMaladie());
        lot.setVaccin(vaccin);
        Offre offre = new Offre();
        offre.setAccepte(true);
        offre.setDateDebutOffre(null);
        offre.setDateAchat(new Date());
        offre.setDateLimite(p.getDateLivraison());
        offre.setLot(lot);
        Association association = new Association();
        association.setNom(cfp.getSender().getName());
        offre.setAssociation(association);
        em.getTransaction().begin();
        em.persist(offre);
        em.getTransaction().commit();

    }

    private Vaccin getVaccinByName(String name){
        String hql = "SELECT V FROM Vaccin V WHERE V.nom = :name";
        Query query = EntityManager.getInstance().createQuery(hql);
        query.setParameter("name",name);
        return (Vaccin)query.getSingleResult();
    }
}
