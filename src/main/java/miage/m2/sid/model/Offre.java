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
public class Offre {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private double prix;
	private int nombre;
	private Date dateLimite;
	private boolean accepte;
	private Date dateAchat;
	private Date dateDebutOffre;
	
	@OneToOne
	private Lot lot;
	@ManyToOne
	@JoinColumn(name="association_name")
	private Association association;
	
	@OneToMany(mappedBy="offre")
	private List<Echanges> echanges;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public Date getDateLimite() {
		return dateLimite;
	}
	public void setDateLimite(Date dateLimite) {
		this.dateLimite = dateLimite;
	}
	public boolean isAccepte() {
		return accepte;
	}
	public void setAccepte(boolean accepte) {
		this.accepte = accepte;
	}
	public Date getDateAchat() {
		return dateAchat;
	}
	public void setDateAchat(Date dateAchat) {
		this.dateAchat = dateAchat;
	}
	
	public Lot getLot() {
		return lot;
	}
	public void setLot(Lot lot) {
		this.lot = lot;
	}
	public Association getAssociation() {
		return association;
	}
	public void setAssociation(Association association) {
		this.association = association;
	}
	public Date getDateDebutOffre() {
		return dateDebutOffre;
	}
	public void setDateDebutOffre(Date dateDebutOffre) {
		this.dateDebutOffre = dateDebutOffre;
	}
	public List<Echanges> getEchanges() {
		return echanges;
	}
	public void setEchanges(List<Echanges> echanges) {
		this.echanges = echanges;
	}
	
	
}
