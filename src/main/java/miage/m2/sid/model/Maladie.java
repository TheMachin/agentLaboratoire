package miage.m2.sid.model;

public class Maladie {
	private String nom;
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
