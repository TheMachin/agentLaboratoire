package miage.m2.sid.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Laboratoire {

	private String nom;
	private double ca;
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
	}
	
	public void deleteLot(Lot lot){
		this.stocks.remove(lot);
	}
	
}
