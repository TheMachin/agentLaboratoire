package miage.m2.sid.behaviour;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.swing.AbstractListModel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import miage.m2.sid.EntityManager;
import miage.m2.sid.model.Association;
import miage.m2.sid.model.Echanges;
import miage.m2.sid.model.GrandGroupe;
import miage.m2.sid.model.Laboratoire;
import miage.m2.sid.model.Lot;
import miage.m2.sid.model.Maladie;
import miage.m2.sid.model.Offre;
import miage.m2.sid.ui.InterfaceAgentLaboratoire;

public class BehaviourLaboratoireGrandGroupe extends CyclicBehaviour {
    private Gson gson;
    private javax.persistence.EntityManager em = EntityManager.getInstance();
    private GrandGroupe laboratoire; 
    private InterfaceAgentLaboratoire gui;
    
    public BehaviourLaboratoireGrandGroupe(Agent agent, InterfaceAgentLaboratoire gui, GrandGroupe laboratoire) {
        super(agent);
        this.gson = new Gson();
        this.laboratoire=laboratoire;
        this.gui=gui;
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
                
                gson = new Gson();
                Map<String, String> map = gson.fromJson(message, new TypeToken<Map<String, String>>(){}.getType());
                
                /**
                 * On recoit une demande de vaccin
                 */
                if(aclMessage.getOntology().equals("demande")||aclMessage.getPerformative()==ACLMessage.PROPAGATE){
                	String status = map.get("devis");
                	if(status.equals("demande")){
                		int nombre = Integer.parseInt(map.get("nombre"));
                		Date delais = new Date(Long.parseLong(map.get("delais")));
                		double volume = Double.parseDouble(map.get("volume"));
                		String maladie = map.get("vaccin");
                		String association = map.get("association");
                		
                		Offre offre = new Offre();
                		Association asso = new Association();
                		asso.setNom(association);
                		offre.setAssociation(asso);
                		offre.setDateLimite(delais);
                		offre.setNombre(nombre);
                		offre.setDateDebutOffre(new Date());
                		
                		Maladie m = new Maladie();
                		m.setNom(maladie);
                		
                		Lot lot = null;
                		lot = new Lot();
            			lot.setDateDLU(delais);
            			lot.setMaladie(m);
            			lot.setNombre(nombre);
            			lot.setVolume(volume);
            			lot.setLaboratoire(laboratoire);
            			lot.setPrix(5000.0+nombre);
            			List<Lot> lots = new ArrayList<Lot>();
            			for(int i=0;i<nombre;i++){
            				lots.add(lot);
            			}
            			
            			
            			
                		/*String r = "Select l FROM Lot l WHERE l.volume=:volume and l.maladie_name=:maladie";
                		Query q = em.createQuery(r);
                		q.setParameter("volume", volume);
                		q.setParameter("maladie", m.getNom());
                		lot = (Lot) q.setMaxResults(1).getSingleResult();   */           		
                		
                		/*if(lot==null){
                			JsonObject response = new JsonObject();
                			response.addProperty("Laboratoire", laboratoire.getNom());
                			response.addProperty("devis", "refuse");
                			response.addProperty("vaccin", m.getNom());
                			
                			ACLMessage reply = aclMessage.createReply();
                			reply.setPerformative(ACLMessage.REFUSE);
                			reply.setContent(response.getAsString());
                			
                		}else{*/
                			
            			offre.setLots(lots);
            			Echanges echange = new Echanges();
            			echange.setDate(new Date());
            			echange.setOffre(offre);
            			double prixBase = (lot.getPrix()*nombre);
            			double marge = prixBase * 0.4;
            			echange.setPrix(prixBase+marge);
            			
            			em.getTransaction().begin();
            			em.persist(m);
            			em.persist(asso);
            			em.persist(lot);
            			em.persist(echange);
            			em.persist(offre);
            			em.flush();
            			em.getTransaction().commit();
            			
            			JsonObject response = new JsonObject();
            			response.addProperty("Laboratoire", laboratoire.getNom());
            			response.addProperty("devis", "propose");
            			response.addProperty("vaccin", m.getNom());
            			response.addProperty("nombre", String.valueOf(nombre));
            			response.addProperty("volume", String.valueOf(volume));
            			response.addProperty("prix", echange.getPrix());
            			response.addProperty("dateDLU", String.valueOf(lot.getDateDLU().getTime()));
            			response.addProperty("noDosser", offre.getId());
            			ACLMessage reply = aclMessage.createReply();
            			reply.setPerformative(ACLMessage.PROPOSE);
            			reply.setContent(response.getAsString());
                		
                	}
                }else if(aclMessage.getOntology().equals("negociation")){
                	Offre offre = null;
            		
            		if(map.containsKey("noDossier")){
            			String r = "Select o FROM Offre l WHERE o.id=:id";
                		Query q = em.createQuery(r);
                		q.setParameter("id", map.get("noDossier"));
                		offre = (Offre) q.setMaxResults(1).getSingleResult();
            		}
                	String status = map.get("devis");
                	if(status.equals("propose")&&offre!=null){
                		
                		if(offre.getEchanges().size()>5){
                			//refuser offre
                		}else{
                			Echanges echange = new Echanges();
                			echange.setDate(new Date());
                			echange.setPrix(offre.getEchanges().get(0).getPrix());
                			echange.setOffre(offre);
                			
                			em.getTransaction().begin();
                			em.persist(echange);
                			em.getTransaction().commit();
                			
                			JsonObject response = new JsonObject();
                			response.addProperty("Laboratoire", laboratoire.getNom());
                			response.addProperty("devis", "propose");
                			response.addProperty("vaccin", offre.getLots().get(0).getNom());
                			response.addProperty("nombre", String.valueOf(offre.getNombre()));
                			response.addProperty("volume", String.valueOf(offre.getLots().get(0).getVolume()));
                			response.addProperty("prix", echange.getPrix());
                			response.addProperty("dateDLU", String.valueOf(offre.getLots().get(0).getDateDLU().getTime()));
                			response.addProperty("noDosser", offre.getId());
                			ACLMessage reply = aclMessage.createReply();
                			reply.setPerformative(ACLMessage.PROPOSE);
                			reply.setContent(response.getAsString());
                			
                		}
                		
                		
                	}else if((status.equals("accepte")||aclMessage.getPerformative()==ACLMessage.ACCEPT_PROPOSAL)&&offre!=null){
                		
                		offre.setAccepte(true);
                		offre.setDateAchat(new Date());
                		double prix = offre.getEchanges().get(offre.getEchanges().size()-1).getPrix();
                		offre.setPrix(prix);
                		laboratoire.setCa(laboratoire.getCa()+offre.getPrix());
                		
                		JsonObject response = new JsonObject();
            			response.addProperty("Laboratoire", laboratoire.getNom());
            			response.addProperty("devis", "accepte");
            			response.addProperty("vaccin", offre.getLots().get(0).getNom());
            			response.addProperty("nombre", String.valueOf(offre.getNombre()));
            			response.addProperty("volume", String.valueOf(offre.getLots().get(0).getVolume()));
            			response.addProperty("prix", prix);
            			response.addProperty("dateDLU", String.valueOf(offre.getLots().get(0).getDateDLU().getTime()));
            			response.addProperty("noDosser", offre.getId());
            			ACLMessage reply = aclMessage.createReply();
            			reply.setPerformative(ACLMessage.CONFIRM);
            			reply.setContent(response.getAsString());
                		
                	}else{
                		JsonObject response = new JsonObject();
            			response.addProperty("Laboratoire", laboratoire.getNom());
            			response.addProperty("error", "Pas de numéro du dossier");
            			ACLMessage reply = aclMessage.createReply();
            			reply.setPerformative(ACLMessage.FAILURE);
            			reply.setContent(response.getAsString());
                	}
                }
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            this.block();
        }
    }
}
