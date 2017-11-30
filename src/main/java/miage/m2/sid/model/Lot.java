package miage.m2.sid.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
public class Lot {
	@Id
	private String nom;
	private Date dateDLC;
	private double prix;
	private int nombre;
	private double volume;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="laboratoire_id")
	private Laboratoire laboratoire;
	@OneToMany
	private List<Vaccin> vaccins;
	
	
	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public Date getDateDLC() {
		return dateDLC;
	}
	public void setDateDLC(Date dateDLU) {
		this.dateDLC = dateDLU;
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

	public List<Vaccin> getVaccins() {
		return vaccins;
	}

	public void setVaccins(List<Vaccin> vaccins) {
		this.vaccins = vaccins;
	}

	public void addVaccin(Vaccin v){
		//si le nom de vaccin est la meme
		if(v.getNom().equals(this.nom)) {
			//on ajoute vaccin + prix + nb + volume
			vaccins.add(v);
			this.nombre++;
			this.volume+=v.getVolume();
			this.prix+=v.getPrix();
		}
	}

	public void setLaboratoire(Laboratoire laboratoire) {
		this.laboratoire = laboratoire;
	}
}
