/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents.behaviour;

import agents.RouteAgent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import model.Semaphor;

/**
 *
 * @author Vojtech
 */
public class ChangeStateOfSemaphore extends Behaviour{

    @Override
    public void onStart() {
        
    }
    
    @Override
    public void action() {
        System.out.println("Car [" + myAgent.getLocalName() + "] get message about semaphore");

        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPAGATE);
        ACLMessage msg = myAgent.receive(mt);

        if (msg != null) {
            RouteAgent agent = (RouteAgent) myAgent;
            Semaphor sem = Semaphor.valueOf(msg.getContent());
            agent.setStateOfSemaphor(sem);
        } else {
            block();
        }
    }
    
    @Override
    public boolean done() {
	return true;
    }
}
