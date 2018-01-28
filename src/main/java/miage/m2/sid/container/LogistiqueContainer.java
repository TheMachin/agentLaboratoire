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
import miage.m2.sid.agent.AgentLogistique;

public class LogistiqueContainer {

    public static void main(String[] args) {
        EntityManager.init();
        LogistiqueContainer logistiqueContainer = new LogistiqueContainer();
        logistiqueContainer.startContainer();
    }


    public void startContainer() {

        try {
            Runtime runtime = Runtime.instance();
            jade.util.leap.Properties properties = new ExtendedProperties();
            properties.setProperty(Profile.GUI, "false");

            ProfileImpl profileImpl = new ProfileImpl(properties);
            profileImpl.setParameter(ProfileImpl.MAIN_HOST, "192.168.110.1");
            profileImpl.setParameter(ProfileImpl.CONTAINER_NAME, "LogistiqueLaboratoire");
            AgentContainer agentContainer = runtime.createAgentContainer(profileImpl);
            //agent logistique
            AgentController agentLogistique = agentContainer.createNewAgent("Agent logistique", AgentLogistique.class.getName(),null);

            agentLogistique.start();
        } catch (ControllerException e) {
            //Logger.log(Level.FATAL,e.toString(),e);
            e.printStackTrace();
        }
    }
}
