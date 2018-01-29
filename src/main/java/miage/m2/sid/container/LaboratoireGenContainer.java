package miage.m2.sid.container;

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
import miage.m2.sid.EntityManager;
import miage.m2.sid.agent.AgentLaboratoireGenerique;
import miage.m2.sid.agent.AgentLaboratoireGrandGroupe;
import miage.m2.sid.model.Generique;
import miage.m2.sid.query.Requetes;
import miage.m2.sid.ui.InterfaceAgentLaboratoire;

import java.io.IOException;

public class LaboratoireGenContainer extends Application{

    private Generique laboGenerique;
    private AgentLaboratoireGenerique agentLaboratoireGenerique;
    private javax.persistence.EntityManager em = EntityManager.getInstance();
    private Stage primaryStage;
    private InterfaceAgentLaboratoire gui;

    public static void main(String[] args) {
        EntityManager.init();
        launch(LaboratoireGenContainer.class);
    }

    public void startContainer() {

        try {
            Runtime runtime = Runtime.instance();
            jade.util.leap.Properties properties = new ExtendedProperties();
            properties.setProperty(Profile.GUI, "false");

            ProfileImpl profileImpl = new ProfileImpl(properties);
            profileImpl.setParameter(Profile.EXPORT_HOST, "192.168.43.229");
            profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");
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

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub

        laboGenerique = Requetes.getRandomLaboratoire();

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Gui Agent laboratoire "+laboGenerique.getNom());
        Parent root = null;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AgentLaboratoire.fxml"));
        gui = new InterfaceAgentLaboratoire();
        loader.setController(gui);
        try {
            root = loader.load();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Scene scene = new Scene(root);

        this.primaryStage.setScene(scene);
        this.primaryStage.show();
        gui.init();
        startContainer();
        gui.setLaboratoireName(laboGenerique.getNom());
        gui.setCA(laboGenerique.getCa());
        //ProposeEvent proposeEvent = new ProposeEvent("asso","boulot",4,new Date(),new Date(), 14.0,"En cours");
        //gui.addRow(proposeEvent);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public InterfaceAgentLaboratoire getControllerInterface() {
        return this.gui;
    }

    public void setAgentGen(AgentLaboratoireGenerique agentGen) {
        this.agentLaboratoireGenerique = agentGen;
    }
}
