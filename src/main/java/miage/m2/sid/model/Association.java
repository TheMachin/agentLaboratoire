package miage.m2.sid.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class Association {
	@Id
	private String nom;
	@OneToMany(mappedBy="association")
	private List<Offre> offres;
}
