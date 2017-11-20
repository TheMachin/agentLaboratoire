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
	
	@OneToMany
	private List<Lot> lots;
	@ManyToOne
	@JoinColumn(name="association_name")
	private Association association;
}
