package miage.m2.sid.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
@Entity
public class Vente {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String nomVaccin;
	private int nombreTotal;
	private double volumeTotal;
	private double prix;
	private Date dataAchat;

	@ManyToOne
	@JoinColumn(name="association_name")
	private Association association;

	@ManyToOne
	@JoinColumn(name="laboratoire_name")
	private Laboratoire laboratoire;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNomVaccin() {
		return nomVaccin;
	}

	public void setNomVaccin(String nomVaccin) {
		this.nomVaccin = nomVaccin;
	}

	public int getNombreTotal() {
		return nombreTotal;
	}

	public void setNombreTotal(int nombreTotal) {
		this.nombreTotal = nombreTotal;
	}

	public double getVolumeTotal() {
		return volumeTotal;
	}

	public void setVolumeTotal(double volumeTotal) {
		this.volumeTotal = volumeTotal;
	}

	public double getPrix() {
		return prix;
	}

	public void setPrix(double prix) {
		this.prix = prix;
	}

	public Date getDataAchat() {
		return dataAchat;
	}

	public void setDataAchat(Date dataAchat) {
		this.dataAchat = dataAchat;
	}

	public Association getAssociation() {
		return association;
	}

	public void setAssociation(Association association) {
		this.association = association;
	}

	public Laboratoire getLaboratoire() {
		return laboratoire;
	}

	public void setLaboratoire(Laboratoire laboratoire) {
		this.laboratoire = laboratoire;
	}
}
