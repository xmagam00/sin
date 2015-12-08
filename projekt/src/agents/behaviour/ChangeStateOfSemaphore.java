/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents.behaviour;

import agents.RouteAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import model.Semaphor;

/**
 *
 * @author Vojtech
 */
public class ChangeStateOfSemaphore extends CyclicBehaviour{

    /*
    @Override
    public void onStart() {
        
    }
    */
    
    @Override
    public void action() {
        

        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPAGATE);
        ACLMessage msg = myAgent.receive(mt);

        
        if (msg != null) {
            //System.out.println("Car [" + myAgent.getLocalName() + "] get message about semaphore");
            //System.out.println("message:" + msg);
            RouteAgent agent = (RouteAgent) myAgent;
            
            Semaphor sem = Semaphor.valueOf(msg.getContent());
            System.out.println("Car " + myAgent.getLocalName() + " have " + sem + " semaphore." );
            agent.setStateOfSemaphor(sem);
            
            //vytvor novou zpravu na generovani novych aut
            ACLMessage reply = msg.createReply();
            
            reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);

            myAgent.send(reply);
        } else {
            block();
        }
    }
    
    /*
    @Override
    public boolean done() {
	return true;
    }
    */
}
