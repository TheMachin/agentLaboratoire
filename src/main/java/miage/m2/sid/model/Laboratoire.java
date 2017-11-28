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
	private List<Lot> stocks;
	/**
	 * 
	 */
	public Laboratoire() {
		stocks = new ArrayList<Lot>();
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
	public List<Lot> getStocks() {
		return stocks;
	}
	public void setStocks(List<Lot> stocks) {
		this.stocks = stocks;
	}
	
	public void addLot(Lot lot){
		this.stocks.add(lot);
		lot.setLaboratoire(this);
	}
	
	public void deleteLot(Lot lot){
		this.stocks.remove(lot);
	}
	
}
