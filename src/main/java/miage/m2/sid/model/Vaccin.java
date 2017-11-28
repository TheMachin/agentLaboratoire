package miage.m2.sid.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Vaccin {
	
	@Id
	private String nom;
	private double prix;
	private double volume;
	
	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public double getPrix() {
		return prix;
	}
	public void setPrix(double prix) {
		this.prix = prix;
	}
	public double getVolume() {
		return volume;
	}
	public void setVolume(double volume) {
		this.volume = volume;
	}
	
	

}
