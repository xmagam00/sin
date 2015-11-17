/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents.behaviour;

import agents.Arbitr;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author Vojtech
 */
public class ArbitrBehaviour extends OneShotBehaviour{
    
    public ArbitrBehaviour(){
        super();
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();  
        
        if (msg == null){ 
            block();
        }
        else{
            if(msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){
                System.out.println(msg.getSender().getLocalName() + " won the item for " + msg.getContent());  

                //end auction
                ACLMessage end_msg = new ACLMessage(ACLMessage.CANCEL);  
                end_msg.setContent("Auction won by " + msg.getSender().getName() + " for " + msg.getContent()); 

                for (int i = 1; i < 6; ++i) {
                    end_msg.addReceiver(new AID("B"+Integer.toString(i), AID.ISLOCALNAME));
                }
                end_msg.removeReceiver(msg.getSender());
                //System.out.println(myAgent.getAID().getName() + " sent CFPs.");
                myAgent.send(end_msg);

                Arbitr.auctionBehav.addSubBehaviour(new OneShotBehaviour() {
                    @Override
                    public void action() {
                        //System.out.println(myAgent.getAID().getName() + " sent CFPs.");
                    }
                });
            }
        }
    }
    
    
    
}
