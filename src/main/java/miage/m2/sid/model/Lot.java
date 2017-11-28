package miage.m2.sid.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Lot {
	@Id
	private String nom;
	private Date dateDLU;
	private double prix;
	private int nombre;
	private double volume;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="laboratoire_id")
	private Laboratoire laboratoire;
	@OneToOne
	@JoinColumn(name="maladie_name")
	private Maladie maladie;
	
	
	
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
