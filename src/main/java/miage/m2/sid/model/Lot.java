package miage.m2.sid.model;

import java.util.Date;

public class Lot {
	private String nom;
	private Date dateDLU;
	private double prix;
	private int nombre;
	private double volume;
	
	private Laboratoire laboratoire;
	private Maladie maladie;
	/**
	 * @param nom
	 * @param dateDLU
	 * @param prix
	 * @param nombre
	 * @param volume
	 * @param maladie
	 */
	public Lot(String nom, Date dateDLU, double prix, int nombre, double volume, Maladie maladie) {
		this.nom = nom;
		this.dateDLU = dateDLU;
		this.prix = prix;
		this.nombre = nombre;
		this.volume = volume;
		this.laboratoire = laboratoire;
		this.maladie = maladie;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public Date getDateDLU() {
		return dateDLU;
	}
	public void setDateDLU(Date dateDLU) {
		this.dateDLU = dateDLU;
	}
	public double getPrix() {
		return prix;
	}
	public void setPrix(double prix) {
		this.prix = prix;
	}
	public int getNombre() {
		return nombre;
	}
	public void setNombre(int nombre) {
		this.nombre = nombre;
	}
	public double getVolume() {
		return volume;
	}
	public void setVolume(double volume) {
		this.volume = volume;
	}
	public Laboratoire getLaboratoire() {
		return laboratoire;
	}
	public Maladie getMaladie() {
		return maladie;
	}
	public void setMaladie(Maladie maladie) {
		this.maladie = maladie;
	}


	public void setLaboratoire(Laboratoire laboratoire) {
		this.laboratoire = laboratoire;
	}
}
