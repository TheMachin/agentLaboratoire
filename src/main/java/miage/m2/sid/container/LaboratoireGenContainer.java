package miage.m2.sid.container;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import miage.m2.sid.EntityManager;
import miage.m2.sid.agent.AgentLaboratoireGenerique;
import miage.m2.sid.agent.AgentLaboratoireGrandGroupe;
import miage.m2.sid.agent.AgentLogistique;
import miage.m2.sid.model.Generique;
import miage.m2.sid.model.Laboratoire;

import javax.persistence.Query;
import java.util.List;
import java.util.Random;

public class LaboratoireGenContainer {

    private Generique laboGenerique;
    private AgentLaboratoireGenerique agentLaboratoireGenerique;
    private javax.persistence.EntityManager em = EntityManager.getInstance();

    public static void main(String[] args) {
        EntityManager.init();
        LaboratoireGenContainer laboratoireGenContainer = new LaboratoireGenContainer();
        laboratoireGenContainer.startContainer();
    }


    public void startContainer() {

        laboGenerique = getLaboratoire();

        try {
            Runtime runtime = Runtime.instance();
            jade.util.leap.Properties properties = new ExtendedProperties();
            properties.setProperty(Profile.GUI, "false");

            ProfileImpl profileImpl = new ProfileImpl(properties);
            profileImpl.setParameter(ProfileImpl.MAIN_HOST, "192.168.110.1");
            profileImpl.setParameter(ProfileImpl.CONTAINER_NAME, "LaboratoireGenerique");
            AgentContainer agentContainer = runtime.createAgentContainer(profileImpl);
            //agent generique
            AgentController agentLaboGen = agentContainer
                    .createNewAgent(laboGenerique.getNom(), AgentLaboratoireGenerique.class.getName(), new Object[]{this, laboGenerique});

            agentLaboGen.start();
        } catch (ControllerException e) {
            //Logger.log(Level.FATAL,e.toString(),e);
            e.printStackTrace();
        }
    }

    private Generique getLaboratoire(){
        String hql = "SELECT l FROM Generique l";
        Query query = EntityManager.getInstance().createQuery(hql);
        List<Generique> generiques = query.getResultList();
        Random r = new Random();
        int valeur = 0 + r.nextInt(generiques.size()-1 - 0);
        return generiques.get(valeur);
    }
}
