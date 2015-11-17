package agents.behaviour;


import agents.CarGeneratorAgent;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import model.Car;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 17. 11. 2015.
 */
public class CarGeneratorBehaviour extends TickerBehaviour {

    private static int NUM_CARS = 40;

    public CarGeneratorBehaviour(Agent a, long period) {
        super(a, period);
    }

    public void onTick() {

        List<Car> cars = generateCars();


        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
    }

    private List<Car> generateCars() {
        List<Car> cars = new ArrayList<Car>();

        for (int i = 0; i <= NUM_CARS; i++ ) {
            Car car = new Car();
            

            cars.add(car);
        }

        return cars;
    }
}

