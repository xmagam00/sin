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
import model.Route;
import model.Semaphor;

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
            //agent cesty informuje o zmene
            if(msg.getPerformative() == ACLMessage.REQUEST){
                
                System.out.println(msg.getSender().getLocalName() + " won the item for " + msg.getContent()); 
                Route r = this.parseContentMessage(msg.getContent());
                if (r != null){
                    ////nastav agentovi v jeho rade vztvorenou cestu, kterou yiskal od agenta, ktery chce zmenu
                    
                }

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
    
    public Route parseContentMessage(String msg){
        Route route = null;
        String items[] = msg.split("\\s");
        int id = 0;
        int number = -1;
        Semaphor s = null;
        
        
        
        for(int i = 0 ; i < items.length ; ++i){
            String item[] = items[i].split(":");
            id = ("id".equals(item[0])) ?  Integer.parseInt(item[1]) : id ;
            number = ("numbersCar".equals(item[0])) ? Integer.parseInt(item[1]) : number;
            s = ("semaphor".equals(item[0])) ? Semaphor.valueOf(item[1]) : s;
        }
        
        //pokud jsme ziskali odpoved od agenta, ktery posila ve zpave ve tvaru protokolu, tak je to ok
        if (id > 0 && number > 0 && s != null){
            route = new Route(id, number, s);
        }
        
        return route;
    }
    
    
    
}
