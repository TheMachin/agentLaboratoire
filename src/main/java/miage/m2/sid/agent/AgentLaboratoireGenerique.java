package miage.m2.sid.agent;

import jade.core.Agent;
import miage.m2.sid.model.Generique;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AgentLaboratoireGenerique extends Agent{

	SimpleDateFormat simpleDateFormat;

	protected void setup(){
		simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Object[] generique = getArguments();
		Generique labo = null;
		if(generique!=null){
			labo=(Generique) generique[0];
			System.out.println(labo.getNom());
			long date = new Date().getTime()-labo.getLots().get(0).getDateDLC().getTime();
			System.out.println(simpleDateFormat.format(labo.getLots().get(0).getDateDLC()));
			System.out.println("Jours diff "+TimeUnit.DAYS.convert(date, TimeUnit.MILLISECONDS));
		}
		//initialisation de l'agent
		System.out.println(this.getName()+" "+this.getAID()+" started");
	}
	
	protected void takeDown(){
		//traitement de fin 
		System.out.println(this.getName()+" "+this.getAID()+" done");
	}
	
}
