package miage.m2.sid.dummy;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;


public class DummyAssocContainer {
    private DummyInitiatorAgent agentAssociation;

    public static void main(String[] args) {
        DummyAssocContainer dummyAssocContainer = new DummyAssocContainer();
        dummyAssocContainer.startContainer();
    }

    public void startContainer() {
        try {
            Runtime runtime = Runtime.instance();
            jade.util.leap.Properties properties = new ExtendedProperties();
            properties.setProperty(Profile.GUI, "true");

            ProfileImpl profileImpl = new ProfileImpl(properties);
            profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");
            profileImpl.setParameter(ProfileImpl.CONTAINER_NAME, "association");
            AgentContainer agentContainer = runtime.createAgentContainer(profileImpl);
            AgentController agentLabo = agentContainer
                    .createNewAgent("Agent Dummy Assoc", DummyInitiatorAgent.class.getName(), new Object[]{this});
            agentLabo.start();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }
}
