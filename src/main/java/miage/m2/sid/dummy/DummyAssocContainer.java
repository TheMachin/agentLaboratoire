package miage.m2.sid.dummy;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class DummyAssocContainer extends Application{
    private FXMLLoader loader;
    private DummyInitiatorAgent agentAssociation;

    public static void main(String[] args) {
        launch(DummyAssocContainer.class);
        /*DummyAssocContainer dummyAssocContainer = new DummyAssocContainer();
        dummyAssocContainer.startContainer();*/
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
                    .createNewAgent("Agent Dummy Assoc", DummyInitiatorAgent.class.getName(), new Object[]{this, loader.<DummyController>getController()});
            agentLabo.start();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub

        primaryStage.setTitle("Gui Agent laboratoire grand groupe");
        Parent root = null;

        loader = new FXMLLoader(getClass().getResource("/DummyAssocUI.fxml"));

        try {
            root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.show();

            startContainer();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
