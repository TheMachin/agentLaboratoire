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
import miage.m2.sid.agent.AgentLaboratoireGrandGroupe;
import miage.m2.sid.event.ProposeEvent;
import miage.m2.sid.model.Laboratoire;
import miage.m2.sid.ui.InterfaceAgentLaboratoire;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.Date;

public class LaboratoireGrandGroupeContainer extends Application {

    private Stage primaryStage;
    private AgentLaboratoireGrandGroupe agentGrandGroupe;
    private InterfaceAgentLaboratoire gui;
    private javax.persistence.EntityManager em = EntityManager.getInstance();
    private Laboratoire laboratoire;

    public static void main(String[] args) {
        EntityManager.init();
        launch(LaboratoireGrandGroupeContainer.class);
    }

    public void startContainer() {
        laboratoire = new Laboratoire();
        laboratoire.setCa(0);
        laboratoire.setNom("Un grand labo");

        try {
            Runtime runtime = Runtime.instance();// cest peut etre ça
            jade.util.leap.Properties properties = new ExtendedProperties();
            properties.setProperty(Profile.GUI, "true");

            ProfileImpl profileImpl = new ProfileImpl(properties);
            profileImpl.setParameter("host", "192.168.43.229");
            profileImpl.setParameter(Profile.EXPORT_HOST, "192.168.43.178");
            profileImpl.setParameter("main", "false");
            profileImpl.setParameter(ProfileImpl.CONTAINER_NAME, "LaboratoireGrandGroupes");
            AgentContainer agentContainer = runtime.createAgentContainer(profileImpl);
            //agent grand groupe
            AgentController agentLabo = agentContainer.createNewAgent(
                    "Agent " + laboratoire.getNom(),
                    AgentLaboratoireGrandGroupe.class.getName(),
                    new Object[]{this, laboratoire});


            agentLabo.start();
        } catch (ControllerException e) {
            //Logger.log(Level.FATAL,e.toString(),e);
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Gui Agent laboratoire grand groupe");
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
        gui.setLaboratoireName(laboratoire.getNom());
        gui.setCA(laboratoire.getCa());
        //ProposeEvent proposeEvent = new ProposeEvent("asso","boulot",4,new Date(),new Date(), 14.0,"En cours");
        //gui.addRow(proposeEvent);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public void setAgentGrandGroupe(AgentLaboratoireGrandGroupe agent) {
        this.agentGrandGroupe = agent;
    }

    public InterfaceAgentLaboratoire getControllerInterface() {
        return this.gui;
    }



}


