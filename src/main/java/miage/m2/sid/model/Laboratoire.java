package miage.m2.sid.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class Laboratoire {

	@Id
	private String nom;
	private double ca;
	@OneToMany(mappedBy="laboratoire")
	private List<Lot> lots;
	/**
	 * 
	 */
	public Laboratoire() {
		lots = new ArrayList<Lot>();
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public double getCa() {
		return ca;
	}
	public void setCa(double ca) {
		this.ca = ca;
	}
	public List<Lot> getLots() {
		return lots;
	}
	public void setLots(List<Lot> stocks) {
		this.lots = stocks;
	}
	
	public void addLot(Lot lot){
		this.lots.add(lot);
		lot.setLaboratoire(this);
	}

	public void removeLot(Lot lot){
		if(lots.contains(lot)){
			lots.remove(lot);
		}
	}


}
