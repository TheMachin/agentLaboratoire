package miage.m2.sid.dummy;

import jade.core.AID;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DummyController implements Initializable {

    DummyAssoSequentialBehavior agentAssoc;

    @FXML
    ComboBox cbLabo;

    @FXML
    Button btSendCfp;

    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("initialize");

        btSendCfp.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if(cbLabo.getValue() != null){
                    System.out.println("Selected : "+cbLabo.getValue());
                    agentAssoc.sendCFP(getSelectedAid(cbLabo.getValue().toString()));
                }
            }
        });

    }

    void displayLabos(){
        System.out.println(agentAssoc.getLabos());
        cbLabo.getItems().addAll(agentAssoc.getLabos());
    }

    private AID getSelectedAid(String value){
        List<AID> aid = agentAssoc.getLabos();
        int pos = 0;
        while(!value.equals(aid.get(pos).toString())){
            pos++;
        }
        return aid.get(pos);
    }


}
