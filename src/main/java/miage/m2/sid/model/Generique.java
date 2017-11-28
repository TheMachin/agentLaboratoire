package miage.m2.sid.model;

import javax.persistence.Entity;

@Entity
public class Generique extends Laboratoire{
	private double maxRabais = 30.0;

	
	public double getMaxRabais() {
		return maxRabais;
	}

	public void setMaxRabais(double maxRabais) {
		this.maxRabais = maxRabais;
	}
	
	
	
}
