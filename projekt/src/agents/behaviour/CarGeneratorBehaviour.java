package agents.behaviour;

import agents.CreatorAgent;
import agents.RouteAgent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("serial")
public class CarGeneratorBehaviour extends CyclicBehaviour {

    private static final int MAX_CARS = 2;

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
        ACLMessage msg = myAgent.receive(mt);
        
        if (msg != null){
            CreatorAgent creator = (CreatorAgent) myAgent;
            //tedy uz se ozval 2 agent
            if(creator.getFinished() == 1){
                creator.setFinished(0);
                Random randomGenerator = new Random();
                int randomInt = randomGenerator.nextInt(MAX_CARS);
                int randomInt2 = randomGenerator.nextInt(MAX_CARS);
                //System.out.println("prvni random:" + randomInt);
                //System.out.println("druhz random:" + randomInt2);

                ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                request.addReceiver(new AID("route1", AID.ISLOCALNAME));
                request.setContent("N_" + randomInt + "_" + "S_" + randomInt2);
                myAgent.send(request);
                int randomInt3 = randomGenerator.nextInt(MAX_CARS);
                int randomInt4 = randomGenerator.nextInt(MAX_CARS);
                ACLMessage request1 = new ACLMessage(ACLMessage.REQUEST);
                request1.addReceiver(new AID("route2", AID.ISLOCALNAME));
                request1.setContent("E_" + randomInt3 + "_" + "W_" + randomInt4);
                myAgent.send(request1);
            }
            else{
                creator.setFinished(1);
            }
                    
        }
         else {
            block();
        }
        
        
    }
}