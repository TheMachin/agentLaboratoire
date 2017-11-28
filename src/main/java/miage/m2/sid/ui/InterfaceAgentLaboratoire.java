package miage.m2.sid.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class InterfaceAgentLaboratoire{

	@FXML
	private Label lblLabo, lblCA;
	
	
	
	public void setLaboratoireName(String name){
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				lblLabo.setText(name);
			}
		});
	}
	
	public void setCA(double ca){
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				lblCA.setText(String.valueOf(ca)+" €");
			}
		});
	}

}
