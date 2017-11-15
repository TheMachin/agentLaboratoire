package model;

import java.util.Date;
import java.util.List;

public class Offre {
	private int id;
	private double prix;
	private int nombre;
	private Date dateLimite;
	private boolean accepte;
	private Date dateAchat;
	
	private List<Lot> lots;
	private Association association;
}
