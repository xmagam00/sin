package agents.behaviour;

import agents.CreatorAgent;
import agents.RouteAgent;
import jade.core.behaviours.Behaviour;
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
public class CarReceiverBehaviour extends CyclicBehaviour {

    private int firstQueue;
    private int secondQueue;

    @Override
    public void action() {
        System.out.println("Car [" + myAgent.getLocalName() + "] add car");
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        ACLMessage msg = myAgent.receive(mt);

        if (msg != null) {
            RouteAgent routeAgent = (RouteAgent)myAgent;
            String content = msg.getContent();
            String[] splitString = content.split("_");
            if ((splitString[3].equals("0") || splitString[3].equals("1")) && (splitString[1].equals("0") || splitString[1].equals("1"))) {
                setFirstQueue(Integer.parseInt(splitString[3]));
                setSecondQueue(Integer.parseInt(splitString[3]));
                routeAgent.addNewCarToList(getFirstQueue(), getSecondQueue(), splitString[0]);

            } else {
                System.err.println("Wrong message type" + content);
            }


        } else {
            block();
        }

    }

    public int getFirstQueue() {
        return firstQueue;
    }

    public void setFirstQueue(int firstQueue) {
        this.firstQueue = firstQueue;
    }

    public int getSecondQueue() {
        return secondQueue;
    }

    public void setSecondQueue(int secondQueue) {
        this.secondQueue = secondQueue;
    }
}