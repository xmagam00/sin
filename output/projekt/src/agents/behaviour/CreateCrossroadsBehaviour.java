/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents.behaviour;

import agents.CreatorAgent;
import agents.RouteAgent;
import jade.core.behaviours.OneShotBehaviour;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.Semaphor;

/**
 *
 * @author Vojtech
 */
@SuppressWarnings("serial")
public class CreateCrossroadsBehaviour extends OneShotBehaviour {
    
    private int NUM_CARS = 10;
    
    
    @Override
	public void action() {
            CreatorAgent creator = (CreatorAgent) myAgent;
            
            // spawn
            try {
                    
                    //for North
                    List<String> listOfCar = createCars("N");
                    listOfCar.addAll(createCars("S"));
                    
                    //zadame info o tom, ze se jedna o prvniho cestu
                    listOfCar.add(0,"1");
                    
                    int countOfCar1 = listOfCar.size();
                    
                    String[] args1 = listOfCar.toArray(new String[listOfCar.size()]);
                    
                    AgentController agent = creator.getRouteAgentContainer().createNewAgent("route1", RouteAgent.class.getCanonicalName(), args1);
                    agent.start();
                    
                    //smazeme vsechny 
                    listOfCar.clear();
                    listOfCar = createCars("E");
                    listOfCar.addAll(createCars("W"));
                    
                    int countOfCar2 = listOfCar.size();
                    
                    //zadame info o tom, ze se jedna o prvniho cestu
                    listOfCar.add(0,"2");
                    
                    String[] args2 = listOfCar.toArray(new String[listOfCar.size()]);
                    
                    AgentController agent2 = creator.getRouteAgentContainer().createNewAgent("route2", RouteAgent.class.getCanonicalName(), args2);
                    agent2.start();
                    
                    Semaphor redOrGreenFirst = Semaphor.RED;
                    Semaphor redOrGreenSecond = Semaphor.RED;
                    if(countOfCar1 > countOfCar2)
                        redOrGreenFirst = Semaphor.GREEN;
                    else
                        redOrGreenSecond = Semaphor.GREEN;
                    creator.sendDecisionSemaphor(redOrGreenFirst, redOrGreenSecond);

            } catch (StaleProxyException e) {
                    System.err.println("Error creating car agents");
                    e.printStackTrace();
            }
	}
        
        private List<String> createCars(String distance){
            List<String> cars = new ArrayList<>();
            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(NUM_CARS);
            for(int i = 0 ; i < randomInt ; ++i){
                cars.add("car-"+ distance +"-"+i);
            }
            return cars;
        }
}
