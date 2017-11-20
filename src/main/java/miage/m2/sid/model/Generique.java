package miage.m2.sid.model;

import javax.persistence.Entity;

@Entity
public class Generique extends Laboratoire{
	private double maxRabais;

	/**
	 * @param maxRabais
	 */
	public Generique(double maxRabais) {
		this.maxRabais = maxRabais;
	}

	public double getMaxRabais() {
		return maxRabais;
	}

	public void setMaxRabais(double maxRabais) {
		this.maxRabais = maxRabais;
	}
	
	
	
}
