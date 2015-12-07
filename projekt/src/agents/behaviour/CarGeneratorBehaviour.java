package agents.behaviour;

import agents.CreatorAgent;
import agents.RouteAgent;
import jade.core.behaviours.OneShotBehaviour;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("serial")
public class CarGeneratorBehaviour extends CyclicBehaviour {

    private static final int MAX_CARS = 1;

    @Override
    public void action() {

        try {

            Random randomGenerator = new Random();

            int randomInt = randomGenerator.nextInt(MAX_CARS);
            int randomInt2 = randomGenerator.nextInt(MAX_CARS);

            ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
            request.addReceiver(new AID("route1", AID.ISLOCALNAME));
            request.setContent("N_" + randomInt + "_" + "S_" + randomInt2);
            myAgent.send(request);

            int randomInt = randomGenerator.nextInt(MAX_CARS);
            int randomInt2 = randomGenerator.nextInt(MAX_CARS);
            ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
            request.addReceiver(new AID("route2", AID.ISLOCALNAME));
            request.setContent("E_" + randomInt + "_" + "W_" + randomInt2);
            myAgent.send(request);

        } catch (StaleProxyException ex) {
            e.printStackTrace();
        }
    }
}