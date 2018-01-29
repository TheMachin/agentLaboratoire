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
import miage.m2.sid.util.Utils;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LaboGenResponder extends ContractNetResponder {

    private javax.persistence.EntityManager em = EntityManager.getInstance();
    private Laboratoire labo = null;
    private InterfaceAgentLaboratoire gui;

    // Take care that if mt is null every message is consumed by this protocol.
    public LaboGenResponder(Agent a, MessageTemplate mt, InterfaceAgentLaboratoire gui) {
        super(a, mt);
        this.gui=gui;
    }

    public LaboGenResponder(Agent a, MessageTemplate mt, DataStore store) {
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
        System.out.println("Laboratoire generique "+ getLaboratoire(this.myAgent)+" ---------------- handleCfp");
        System.out.println("Called : handleCfp");
        System.out.println("Ontology : "+cfp.getOntology());
        System.out.println("Performative : "+cfp.getPerformative());
        System.out.println("Content : "+cfp.getContent());

        return createProposition(cfp);
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
        System.out.println("Laboratoire generique "+ getLaboratoire(this.myAgent)+" ---------------- AcceptProposal");
        System.out.println("Called : handleAcceptProposal");
        System.out.println("Ontology : "+cfp.getOntology());
        System.out.println("Performative : "+cfp.getPerformative());
        System.out.println("Content : "+cfp.getContent());
        System.out.println("Propose : "+propose.toString() );
        return acceptPropose(cfp,propose,accept);
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
                "Rejeté par l'association");
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



    private ACLMessage createProposition(ACLMessage messageReceived){
        Gson gson = new Gson();
        CFP cfp = gson.fromJson(messageReceived.getContent(), CFP.class);

        Vaccin vaccin = Requetes.getVaccinByName(cfp.getMaladie());
        double prixTotal = 0;
        double volumeTotal = 0;
        int nombre = 0 ;
        /**
         * Pas de vaccin pour la maladie
         */
        if(vaccin==null){
            // Create reply
            return rejectProposal(messageReceived);
        }
        /*
        On vérifie si ya assez de vaccin
         */
        List<Lot> lots = getALlLotsWithEnoughVaccin(vaccin,cfp.getNb(), getLaboratoire(this.myAgent), cfp.getDate());

        //Si on a a pas assez de vaccins
        if(lots==null || lots.size()==0){
            return rejectProposal(messageReceived);
        }

        /**
         * on récupère le prix et le volume cumulés de tous les lots
         */
        volumeTotal = getVolumeLotsTotal(lots);
        prixTotal = getPriceByStrategies(lots, cfp.getDate(), getLaboratoire(this.myAgent));

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, 1); // Add 1 month to current date

        // Create proposition
        Propose proposition = new Propose(nombre, prixTotal, cfp.getDate(), cal.getTime(), volumeTotal);
        System.out.println("--------Proposition Labo generique "+proposition);

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


        // Create reply
        ACLMessage replyMessage = messageReceived.createReply();
        replyMessage.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        replyMessage.setContent(gson.toJson(proposition));
        replyMessage.setPerformative(ACLMessage.PROPOSE);
        replyMessage.setOntology("Laboratoire");

        return replyMessage;
    }

    private ACLMessage acceptPropose(ACLMessage cfp, ACLMessage propose, ACLMessage accept){
        Gson gson = new Gson();
        CFP cfpM = gson.fromJson(cfp.getContent(),CFP.class);
        Propose p = gson.fromJson(propose.getContent(),Propose.class);

        //on vérifie si on a tjr du stock
        Vaccin vaccin = Requetes.getVaccinByName(cfpM.getMaladie());
        List<Lot> lots = getALlLotsWithEnoughVaccin(vaccin,cfpM.getNb(), getLaboratoire(this.myAgent), p.getDateLivraison());

        //Si on a a pas assez de vaccins
        //On informe l'association
        if(lots==null || lots.size()==0){
            return rejectProposal(accept);
        }

        /**
         * obtenir le nom de l'association sans son adresse
         */
        String nameAsso = getNameOfAgentWithoutAddress(cfp.getSender().getName());

        //On enregistre tous les infos
        Vente vente = new Vente();
        vente.setPrix(p.getPrix());
        vente.setVolumeTotal(p.getVolume());
        vente.setNomVaccin(cfpM.getMaladie());
        vente.setDataAchat(new Date());
        vente.setNombreTotal(lots.size());
        Association association = new Association();
        association.setNom(nameAsso);
        vente.setAssociation(association);
        Laboratoire laboratoire = getLaboratoire(this.myAgent);
        laboratoire.setCa(laboratoire.getCa()+p.getPrix());
        vente.setLaboratoire(laboratoire);


        //on supprime les lots
        for(Lot l : lots){
            laboratoire.removeLot(l);
        }


            //on met le tout à jour
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

        //on informe l'association que c'est ok
        ACLMessage reply = accept.createReply();
        reply.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        reply.setOntology("Laboratoire");
        reply.setPerformative(ACLMessage.INFORM);
        return reply;

    }

    private ACLMessage rejectProposal(ACLMessage messageReceived){
        ACLMessage replyMessage = messageReceived.createReply();
        replyMessage.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        replyMessage.setOntology("Laboratoire");
        replyMessage.setContent("");
        replyMessage.setPerformative(ACLMessage.REJECT_PROPOSAL);
        System.out.println("Laboratoire generique "+ getLaboratoire(this.myAgent)+" ---------------- RejectProposal----------");
        return replyMessage;
    }

    private double getPriceLotsTotal(List<Lot> lots){
        double price = 0;
        for(Lot lot : lots){
            price+=lot.getPrix();
        }
        return price;
    }

    private double getVolumeLotsTotal(List<Lot> lots){
        double volume = 0;
        for(Lot lot : lots){
            volume+=lot.getVolume();
        }
        return Utils.round(volume,2);
    }

    /**
     * Permet de récupérer l'objet laboratoire à partir du nom de l'agent
     * @return
     */
    private Laboratoire getLaboratoire(Agent a){
        if(this.labo==null) {
            this.labo = Requetes.getLaboratoireByName(getNameOfAgentWithoutAddress(a.getName()));
            return this.labo;
        }else{
            return this.labo;
        }
    }

    private List<Lot> getALlLotsWithEnoughVaccin(Vaccin vaccin,int nombre, Laboratoire labo, Date dateLivraison){
        //on recupere tous les lots du vaccin concerné
        List<Lot> stocks = Requetes.getStock(vaccin, labo, dateLivraison);
        List<Lot> lots = new ArrayList<Lot>();
        //on vérifie si on a assez de vaccin
        for(Lot lot : stocks){
            //on soustrait
            nombre-=lot.getNombre();
            lots.add(lot);
        }
        /**
         * si on a assez de vaccin on retourne les lots
         * sinon on retourne un null
         */
        if(nombre<=0){
            return lots;
        }else{
            return null;
        }
    }

    /**
     * on propose un prix en fonction de deux stratégies (DLC ou stock)
     * @param lots
     * @param dateLivraison
     * @return le prix la plus faible
     */
    private double getPriceByStrategies(List<Lot> lots, Date dateLivraison, Laboratoire labo){

        double priceByDLCStrategy = getPriceByDLCStrategy(lots, dateLivraison);
        double priceByStockStrategy = getPriceByStockStrategy(lots.get(0), dateLivraison, labo, getPriceLotsTotal(lots));

        if(priceByDLCStrategy>priceByStockStrategy){
            return Utils.round(priceByStockStrategy,2);
        }else{
            return Utils.round(priceByDLCStrategy, 2);
        }
    }

    /**
     * Reduit prix du lot si dlc inférieur à 1 semaine avant la date de livraison
     * Le rabais dépend de la DLC pour chaque lot
     * @param lots
     * @param dateLivraison
     * @return price
     */
    private double getPriceByDLCStrategy(List<Lot> lots, Date dateLivraison){
        double price = 0;
        for(Lot lot : lots) {
            long date = dateLivraison.getTime() - lot.getDateDLC().getTime();
            //System.out.println(simpleDateFormat.format(labo.getLots().get(0).getDateDLC()));
            long jourDiff = TimeUnit.DAYS.convert(date, TimeUnit.MILLISECONDS);
            System.out.println("Jours diff " + jourDiff);
            if (jourDiff < 7) {
                price+= lot.getPrix() - (lot.getPrix() * 0.3);
            } else {
                price+= lot.getPrix();
            }
        }
        return price;
    }

    /**
     * Obtenir un prix selon la stratégie du stock
     * Si on a trop de stock, on diminue le prix
     * Le rabais dépend du stock du laboratoire
     * @param lot
     * @param dateLivraison
     * @param labo
     * @return
     */
    private double getPriceByStockStrategy(Lot lot, Date dateLivraison, Laboratoire labo, double price){
        Vaccin v = new Vaccin();
        v.setNom(lot.getNom());
        List<Lot> stocks = Requetes.getStock(v, labo, dateLivraison);
        double rabais = 0;
        int nbVaccin = 0;
        //on récupere le nombre total de vaccin
        for(Lot l : stocks){
            nbVaccin+=l.getNombre();
        }
        //si on a trop de stock, on diminue le prix
        if(stocks.size()>800){
            rabais = 0.3 * lot.getPrix();
        }else if(stocks.size()>500){
            rabais = 0.2 * lot.getPrix();
        }else if(stocks.size()>300){
            rabais = 0.15 * lot.getPrix();
        }else if(stocks.size()>200){
            rabais = 0.1 * lot.getPrix();
        }else if(stocks.size()>100){
            rabais = 0.05 * lot.getPrix();
        }
        return price - rabais;
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

}
