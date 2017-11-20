package miage.m2.sid.model.container;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import miage.m2.sid.agent.AgentLaboratoireGenerique;
import miage.m2.sid.agent.AgentLaboratoireGrandGroupe;

public class LaboratoireContainer {

	public static void main (String[] args) 
    {
		try {
			Runtime runtime = Runtime.instance();
			jade.util.leap.Properties properties = new ExtendedProperties();
			properties.setProperty(Profile.GUI, "true");
			
			ProfileImpl profileImpl = new ProfileImpl(properties);
			profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");
			profileImpl.setParameter(ProfileImpl.CONTAINER_NAME, "Laboratoire");
			AgentContainer agentContainer = runtime.createAgentContainer(profileImpl);
			AgentController agentLabo = agentContainer
					.createNewAgent("AgentLabo", AgentLaboratoireGrandGroupe.class.getName(), null);
			/*AgentController agentLaboGeneric = agentContainer
					.createNewAgent("AgentLaboGenerique", AgentLaboratoireGenerique.class.getName(), null);*/
			
			agentLabo.start();
			//agentLaboGeneric.start();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//AgentLaboratoireGrandGroupe agentLabo = new AgentLaboratoireGrandGroupe();
    }
}
