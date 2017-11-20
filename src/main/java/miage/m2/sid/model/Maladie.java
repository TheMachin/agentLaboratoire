package miage.m2.sid.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Maladie {
	@Id
	@Column(name="maladie_name")
	private String nom;
	@OneToOne(fetch=FetchType.LAZY, mappedBy="maladie")
	private Lot lot;
	/**
	 * @param nom
	 */
	public Maladie(String nom) {
		this.nom = nom;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public Lot getLot() {
		return lot;
	}
	public void setLot(Lot lot) {
		this.lot = lot;
	}
	
	
	
}
