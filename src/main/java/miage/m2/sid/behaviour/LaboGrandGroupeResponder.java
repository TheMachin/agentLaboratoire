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
import miage.m2.sid.model.*;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.*;

public class LaboGrandGroupeResponder extends ContractNetResponder {

    private javax.persistence.EntityManager em = EntityManager.getInstance();
    private Laboratoire labo;


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

        Vaccin vaccin = getVaccinByName(cfp.getMaladie());

        if(vaccin==null){
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
        replyMessage.setContent(gson.toJson(proposition));
        replyMessage.setPerformative(ACLMessage.PROPOSE);

        return replyMessage;
    }

    private ACLMessage createFailure(ACLMessage messageReceived){
        ACLMessage replyMessage = messageReceived.createReply();
        replyMessage.setContent("cancel");
        replyMessage.setPerformative(ACLMessage.FAILURE);
        return replyMessage;
    }

    private void acceptPropose(ACLMessage cfp, ACLMessage propose, ACLMessage accept){
        Gson gson = new Gson();
        CFP cfpM = gson.fromJson(cfp.getContent(),CFP.class);
        Propose p = gson.fromJson(propose.getContent(),Propose.class);


        Vaccin vaccin = getVaccinByName(cfpM.getMaladie());
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
        Laboratoire laboratoire = getLaboratoire();
        laboratoire.setCa(laboratoire.getCa()+prix);

        em.getTransaction().begin();
        em.merge(association);
        em.merge(laboratoire);
        em.persist(vente);
        em.getTransaction().commit();

    }

    /*
        Get vaccin by his name
        @name of vaccin
        @return a vaccin or null if not found
     */
    private Vaccin getVaccinByName(String name){
        String hql = "SELECT V FROM Vaccin V WHERE V.nom = :name";
        Query query = EntityManager.getInstance().createQuery(hql);
        query.setParameter("name",name);
        Vaccin vaccin = null;
        try{
            vaccin = (Vaccin) query.getSingleResult();
        }catch (NoResultException no) {

        }
        if(vaccin!=null){
            return vaccin;
        }else{
            return null;
        }
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

    private Laboratoire getLaboratoire(){
        if(this.labo==null) {
            String hql = "SELECT l FROM Laboratoire l WHERE l.nom = :name";
            Query query = EntityManager.getInstance().createQuery(hql);
            //Obtenir le nom du laboratoire sans son adresse
            query.setParameter("name", getNameOfAgentWithoutAddress(this.myAgent.getName()));
            Laboratoire la = null;

            try{
                la = (Laboratoire) query.getSingleResult();
            }catch (NoResultException no){

            }
            if(la!=null) {
                this.labo = la;
                return this.labo;
            }else{
                labo = new Laboratoire();
                labo.setNom(getNameOfAgentWithoutAddress(this.myAgent.getName()));
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
}
