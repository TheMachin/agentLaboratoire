package miage.m2.sid.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class Laboratoire {

	@Id
	private String nom;
	private double ca;
	@OneToMany(mappedBy="laboratoire")
	private List<Lot> lots;
	@OneToMany
	private List<Vaccin> stocks;
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

	public List<Vaccin> getStocks() {
		return stocks;
	}

	public void setStocks(List<Vaccin> stocks) {
		this.stocks = stocks;
	}

	public void addVaccin(Vaccin vaccin){
		this.stocks.add(vaccin);
	}

	public void removeVaccin(Vaccin vaccin){
		if(stocks.contains(vaccin)){
			stocks.remove(vaccin);
		}
	}

}
