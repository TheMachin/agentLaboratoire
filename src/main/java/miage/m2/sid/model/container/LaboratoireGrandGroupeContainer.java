package miage.m2.sid.model.container;

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
import miage.m2.sid.agent.AgentLogistique;
import miage.m2.sid.model.Laboratoire;
import miage.m2.sid.ui.InterfaceAgentLaboratoire;

import java.io.IOException;

public class LaboratoireGrandGroupeContainer extends Application {

    private Stage primaryStage;
    private AgentLaboratoireGrandGroupe agentGrandGroupe;
    private InterfaceAgentLaboratoire gui;
    private Laboratoire laboratoire;

    public static void main(String[] args) {
        EntityManager.init();
        launch(LaboratoireGrandGroupeContainer.class);
    }

    public void startContainer() {
        laboratoire = null;
        /*javax.persistence.EntityManager em = EntityManager.getInstance();
		String r = "Select l fron GrandGroupe l where l.nom=:nom";
		Query q = em.createQuery(r);
		q.setParameter("nom", "Un grand labo");
		laboratoire = (GrandGroupe) q.getSingleResult();*/
        laboratoire = new Laboratoire();
        laboratoire.setCa(0);
        laboratoire.setNom("Un grand labo");

        try {
            Runtime runtime = Runtime.instance();
            jade.util.leap.Properties properties = new ExtendedProperties();
            properties.setProperty(Profile.GUI, "true");

            ProfileImpl profileImpl = new ProfileImpl(properties);
            profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");
            profileImpl.setParameter(ProfileImpl.CONTAINER_NAME, "Laboratoire");
            AgentContainer agentContainer = runtime.createAgentContainer(profileImpl);
            AgentController agentLabo = agentContainer
                    .createNewAgent("Agent " + laboratoire.getNom(), AgentLaboratoireGrandGroupe.class.getName(), new Object[]{this, laboratoire});

            AgentController agentLogistique = agentContainer.createNewAgent("Agent logistique", AgentLogistique.class.getName(),null);

            agentLabo.start();
            agentLogistique.start();
        } catch (ControllerException e) {
            //Logger.log(Level.FATAL,e.toString(),e);
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

        startContainer();
        gui.setLaboratoireName(laboratoire.getNom());
        gui.setCA(laboratoire.getCa());

    }

    public void setAgentGrandGroupe(AgentLaboratoireGrandGroupe agent) {
        this.agentGrandGroupe = agent;
    }

    public InterfaceAgentLaboratoire getControllerInterface() {
        return this.gui;
    }

}


