package miage.m2.sid.model;

import java.util.Date;

public class Lot {
	private String nom;
	private Date dateDLU;
	private double prix;
	private int nombre;
	private double volume;
	
	private Laboratoire laboratoire;
	private Maladie maladie;
}
