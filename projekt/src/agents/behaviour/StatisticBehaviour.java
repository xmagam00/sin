/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents.behaviour;

import agents.CreatorAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.regex.Pattern;

/**
 *
 * @author Vojtech
 */
@SuppressWarnings("serial")
public class StatisticBehaviour extends CyclicBehaviour{
    
    @Override
	public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            ACLMessage msg = myAgent.receive(mt);

            if (msg != null) {
                //System.out.println("statistic: " + myAgent.getLocalName());
                CreatorAgent creator = (CreatorAgent) myAgent;

                String msgContent = msg.getContent();
                
                // process message                                      1.fronta                      2.fronta
                //ziskame casti zpravy "nazevagenta"-"stavSemaforu"-"smer"-"1.auto"-"velikost"-smer-1.auto-velikostFronty
                String partsOfMessage[] = msgContent.split(Pattern.quote("-"));

                if (partsOfMessage.length < 2) {
                    //System.err.println("Error: Unknown incoming message:" + msgContent);
                    return;
                }
                
                //jednali se o prvniho agenta
                if(partsOfMessage[0].equals("route1")){
                    //a pokud 1 data jsou prazdne, tak je napnime
                    if(creator.getDataOfFristAgent().isEmpty()){
                        creator.setDataOfFristAgent(msgContent);
                    }
                    //neco jako, zmeny neprovadej
                    else{
                        //System.err.println("Error: 1 agent se znovu zeptal pred druhym agenterm drive:" + msgContent);
                    }
                }
                else if(partsOfMessage[0].equals("route2")){
                    //a pokud 1 data jsou prazdne, tak je napnime
                    if(creator.getDataOfSecondAgent().isEmpty()){
                        creator.setDataOfSecondAgent(msgContent);
                    }
                    //neco jako, zmeny neprovadej
                    else{
                        //System.err.println("Error: 2 agent se znovu zeptal pred druhym agenterm drive:" + msgContent);
                    }
                }
                
                if(!creator.getDataOfSecondAgent().isEmpty() && !creator.getDataOfFristAgent().isEmpty()){
                    creator.printStatistic();
                }
                else{
                    
                }
            }

            else {
                    block();
            }
	}
    
}
