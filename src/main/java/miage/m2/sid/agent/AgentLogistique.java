package miage.m2.sid.agent;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import miage.m2.sid.behaviour.LogistiqueBehaviour;

public class AgentLogistique extends Agent {

    private Behaviour behaviour;

    protected void setup(){
        /**
         * on fixe une période de 10 secondes
         */
        behaviour = new LogistiqueBehaviour(this,10000);

        System.out.println(this.getName()+" "+this.getAID()+" started");
        addBehaviour(behaviour);
    }
}
