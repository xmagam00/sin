package agents.behaviour;


import agents.CarGeneratorAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import model.Car;
import model.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Martin on 17. 11. 2015.
 */
public class CarGeneratorBehaviour extends TickerBehaviour {

    private final static int NUM_CARS = 10;
    private final static int QUEUE_LENGTH = 4;
    private static int CAR_ID = 0;
    private final static int MAX_CAR_ID = 10000;

    public CarGeneratorBehaviour(Agent a, long period) {
        super(a, period);
    }

    public void onTick() {

        List<Car> cars = generateCars();


        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
    }

    private List<Car> generateCars() {
        List<Car> cars = new ArrayList<Car>();
        for (int j = 0; j < QUEUE_LENGTH; j++) {
            for (int i = 0; i < NUM_CARS; i++) {
                Car car = new Car(CAR_ID, directionGenerator());
                sendCar(j, car);
                CAR_ID++;
                if (CAR_ID == MAX_CAR_ID) {
                    CAR_ID = 0;
                }
            }
        }

        return cars;
    }

    private Direction directionGenerator() {
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(1);
        Direction direction = Direction.RIGHT;
        switch (randomInt) {
            case 0:
                direction = Direction.STRAIGHT;
                break;
            case 1:
                direction = Direction.RIGHT;
                break;

        }
        return direction;
    }

    private void sendCar(int queueType, Car car) {
        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.setContent(car.getName());
        request.addReceiver(new AID("B"+Integer.toString(queueType), AID.ISLOCALNAME));
    }
}

