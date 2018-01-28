package miage.m2.sid.behaviour;

import com.google.gson.Gson;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import miage.m2.sid.EntityManager;
import miage.m2.sid.dummy.CFP;
import miage.m2.sid.dummy.Propose;
import miage.m2.sid.event.ProposeEvent;
import miage.m2.sid.model.*;
import miage.m2.sid.query.Requetes;
import miage.m2.sid.ui.InterfaceAgentLaboratoire;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.*;

public class LaboGrandGroupeResponder extends ContractNetResponder {

    private javax.persistence.EntityManager em = EntityManager.getInstance();
    private Laboratoire labo;
    private InterfaceAgentLaboratoire gui;


    // Take care that if mt is null every message is consumed by this protocol.
    public LaboGrandGroupeResponder(Agent a, MessageTemplate mt, InterfaceAgentLaboratoire gui) {
        super(a, mt);
        setInterface(gui);
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
        System.out.println("Laboratoire Grand Groupe ---------------- handleCfp");
        System.out.println("GRAND GROUPE Called : handleCfp");
        System.out.println("Ontology : "+cfp.getOntology());
        System.out.println("Performative : "+cfp.getPerformative());
        System.out.println("Content : "+cfp.getContent());


        try{
            return createProposition(cfp);
        }catch(Exception ex){
            ex.printStackTrace();
            return createFailure(cfp);
        }
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
        System.out.println("Laboratoire Grand Groupe ---------------- ACCEPTPROPOSAL--------------------");
        System.out.println("Called : handleAcceptProposal");
        System.out.println("Ontology : "+cfp.getOntology());
        System.out.println("Performative : "+cfp.getPerformative());
        System.out.println("Content : "+cfp.getContent());

        /**
         * enregistrement des infos
        */
        acceptPropose(cfp,propose,accept);

        ACLMessage inform = accept.createReply();
        inform.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        inform.setOntology("Laboratoire");
        inform.setPerformative(ACLMessage.INFORM);
        return inform;
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

        Gson gson = new Gson();
        CFP cfpM = gson.fromJson(cfp.getContent(), CFP.class);
        Propose p = gson.fromJson(propose.getContent(),Propose.class);

        /**
         * affichage proposition sur l'interface graphique
         */
        ProposeEvent proposeEvent = new ProposeEvent(
                getNameOfAgentWithoutAddress(cfp.getSender().getName()),
                cfpM.getMaladie(),
                p.getNombre(),
                p.getDatePeremption(),
                cfpM.getDate(),
                p.getPrix(),
                "Refusé par l'association");
        gui.addRow(proposeEvent);

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

    private ACLMessage createProposition(ACLMessage messageReceived) throws Exception{
        Gson gson = new Gson();
        CFP cfp = gson.fromJson(messageReceived.getContent(), CFP.class);

        Vaccin vaccin = Requetes.getVaccinByName(cfp.getMaladie());

        if(vaccin==null){
            ProposeEvent proposeEvent = new ProposeEvent(getNameOfAgentWithoutAddress(messageReceived.getSender().getName()),cfp.getMaladie(),cfp.getNb(),new Date(),cfp.getDate(), 0.0,"Pas de vaccin");
            gui.addRow(proposeEvent);
            throw new Exception("Vaccin : "+cfp.getMaladie()+" doesn't exist");
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, 1); // Add 1 month to current date

        // Create proposition
        double prixTotal = cfp.getNb() * vaccin.getPrix();
        int volumeTotal = (int)(cfp.getNb() * vaccin.getVolume());
        Propose proposition = new Propose(cfp.getNb(), prixTotal, cfp.getDate(), cal.getTime(), volumeTotal);
        System.out.println(proposition);

        // Create reply
        ACLMessage replyMessage = messageReceived.createReply();
        replyMessage.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        replyMessage.setOntology("Laboratoire");
        replyMessage.setContent(gson.toJson(proposition));
        replyMessage.setPerformative(ACLMessage.PROPOSE);
        /**
         * affichage proposition sur l'interface graphique
         */
        ProposeEvent proposeEvent = new ProposeEvent(
                getNameOfAgentWithoutAddress(messageReceived.getSender().getName()),
                cfp.getMaladie(),
                proposition.getNombre(),
                proposition.getDatePeremption(),
                cfp.getDate(),
                proposition.getPrix(),
                "Envoie de proposition");
        gui.addRow(proposeEvent);

        return replyMessage;
    }

    private ACLMessage createFailure(ACLMessage messageReceived){
        ACLMessage replyMessage = messageReceived.createReply();
        replyMessage.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        replyMessage.setOntology("Laboratoire");
        replyMessage.setContent("cancel");
        replyMessage.setPerformative(ACLMessage.FAILURE);
        return replyMessage;
    }

    private void acceptPropose(ACLMessage cfp, ACLMessage propose, ACLMessage accept){
        Gson gson = new Gson();
        CFP cfpM = gson.fromJson(cfp.getContent(),CFP.class);
        Propose p = gson.fromJson(propose.getContent(),Propose.class);


        Vaccin vaccin = Requetes.getVaccinByName(cfpM.getMaladie());
        double volume = vaccin.getVolume() * cfpM.getNb();
        double prix = vaccin.getPrix() * cfpM.getNb();

        Vente vente = new Vente();
        vente.setNomVaccin(vaccin.getNom());
        vente.setNombreTotal(cfpM.getNb());
        vente.setDataAchat(new Date());
        vente.setPrix(prix);
        vente.setVolumeTotal(volume);

        Association association = new Association();
        association.setNom(getNameOfAgentWithoutAddress(cfp.getSender().getName()));
        vente.setAssociation(association);
        Laboratoire laboratoire = getLaboratoire(this.myAgent);
        laboratoire.setCa(laboratoire.getCa()+prix);

        em.getTransaction().begin();
        em.merge(association);
        em.merge(laboratoire);
        em.persist(vente);
        em.getTransaction().commit();

        /**
         * affichage proposition sur l'interface graphique
         */
        ProposeEvent proposeEvent = new ProposeEvent(
                association.getNom(),
                cfpM.getMaladie(),
                p.getNombre(),
                p.getDatePeremption(),
                cfpM.getDate(),
                p.getPrix(),
                "Accepté par l'association");
        gui.addRow(proposeEvent);
        gui.setCA(laboratoire.getCa());
    }

    /**
     * Permet d'obtenir le nom de l'agent sans son adresse
     * @param a
     * @return
     */
    private String getNameOfAgentWithoutAddress(String a){
        String[] parts = a.split("@");
        return parts[0];
    }

    private Laboratoire getLaboratoire(Agent a){
        if(this.labo==null) {
            Laboratoire la = null;
            la = Requetes.getLaboratoireByName(getNameOfAgentWithoutAddress(a.getName()));
            if(la!=null) {
                this.labo = la;
                return this.labo;
            }else{
                labo = new Laboratoire();
                labo.setNom(getNameOfAgentWithoutAddress(a.getName()));
                labo.setCa(0);
                em.getTransaction().begin();
                em.persist(labo);
                em.getTransaction().commit();
                return this.labo;
            }
        }else{
            return this.labo;
        }
    }

    public void setInterface(InterfaceAgentLaboratoire gui){
        this.gui=gui;
    }
}
