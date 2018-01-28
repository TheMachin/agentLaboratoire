package miage.m2.sid.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import miage.m2.sid.event.ProposeEvent;


public class InterfaceAgentLaboratoire{

	@FXML
	private Label lblLabo, lblCA;
	@FXML
	private TableView table;
	@FXML
	private TableColumn<ProposeEvent, String> colTime, colAsso, colVaccin, colNb, colDLC,colDateL, colPrice, colStatus;

	ObservableList<ProposeEvent> data= FXCollections.observableArrayList();

	public InterfaceAgentLaboratoire() {

	}

	public void init(){
		colTime.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
		colAsso.setCellValueFactory(cellData -> cellData.getValue().assoProperty());
		colVaccin.setCellValueFactory(cellData -> cellData.getValue().vaccinProperty());
		colNb.setCellValueFactory(cellData -> cellData.getValue().nbProperty());
		colDLC.setCellValueFactory(cellData -> cellData.getValue().dlcProperty());
		colDateL.setCellValueFactory(cellData -> cellData.getValue().dateLivraisonProperty());
		colPrice.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
		colStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
		table.setItems(data);
	}

	public void setLaboratoireName(final String name){
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				lblLabo.setText(name);
			}
		});
	}
	
	public void setCA(final double ca){
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				lblCA.setText(String.valueOf(ca)+" €");
			}
		});
	}

	public void addRow(final ProposeEvent proposeEvent){
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				data.add(proposeEvent);
			}
		});
	}

}
