/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;

import agents.behaviour.CarGeneratorBehaviour;
import agents.behaviour.CreateCrossroadsBehaviour;
import agents.behaviour.StatisticBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
//import java.util.concurrent.Semaphore;
import java.util.regex.Pattern;

import model.Semaphor;

/**
 * @author Vojtech
 */
@SuppressWarnings("serial")
public class CreatorAgent extends Agent {

    private AgentContainer routeAgentContainer;

    private String dataOfFristAgent;
    private String dataOfSecondAgent;
    private int countCarInFirstQueues;
    private int countCarInSecondQueues;
    private int finished;
    private int cyclingOfGreen = 5;
    private int currentGreenUsees;


    @Override
    protected void setup() {
        System.out.println("Agent Creator is ready");

        setDataOfFristAgent("");
        setDataOfSecondAgent("");
        countCarInFirstQueues = 0;
        countCarInSecondQueues = 0;
        setFinished(0);
        
        setCurrentGreenUsees(0);

        // vytvoreni kontejneru na auta, ktery si budeme pamatovat
        Profile p = new ProfileImpl();
        setRouteAgentContainer(jade.core.Runtime.instance().createAgentContainer(p));

        addBehaviour(new CreateCrossroadsBehaviour());

        addBehaviour(new StatisticBehaviour());
        
        addBehaviour(new CarGeneratorBehaviour());


    }

    public void sendDecisionSemaphor(Semaphor first, Semaphor second) {

        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                ACLMessage request = new ACLMessage(ACLMessage.PROPAGATE);
                request.setContent(first.toString());
                request.addReceiver(new AID("route1", AID.ISLOCALNAME));
                myAgent.send(request);

                ACLMessage request1 = new ACLMessage(ACLMessage.PROPAGATE);
                request1.setContent(second.toString());
                request1.addReceiver(new AID("route2", AID.ISLOCALNAME));
                myAgent.send(request1);
            }

        });
    }

    /**
     * @return the routeAgentContainer
     */
    public AgentContainer getRouteAgentContainer() {
        return routeAgentContainer;
    }

    /**
     * @param routeAgentContainer the routeAgentContainer to set
     */
    public void setRouteAgentContainer(AgentContainer routeAgentContainer) {
        this.routeAgentContainer = routeAgentContainer;
    }

    /**
     * @return the dataOfFristAgent
     */
    public String getDataOfFristAgent() {
        return dataOfFristAgent;
    }

    /**
     * @param dataOfFristAgent the dataOfFristAgent to set
     */
    public void setDataOfFristAgent(String dataOfFristAgent) {
        this.dataOfFristAgent = dataOfFristAgent;
    }

    /**
     * @return the dataOfSecondAgent
     */
    public String getDataOfSecondAgent() {
        return dataOfSecondAgent;
    }

    /**
     * @param dataOfSecondAgent the dataOfSecondAgent to set
     */
    public void setDataOfSecondAgent(String dataOfSecondAgent) {
        this.dataOfSecondAgent = dataOfSecondAgent;
    }

    public void printStatistic() {
        
        String part1[] = dataOfFristAgent.split(Pattern.quote("-"));
        String part2[] = dataOfSecondAgent.split(Pattern.quote("-"));
        System.out.println("Current state of crossroad:");
        prepareOfPrintingStatistic(part1);
        prepareOfPrintingStatistic(part2);

        //smaz vsechny data
        dataOfFristAgent = "";
        dataOfSecondAgent = "";

        Semaphor redOrGreenFirst ;
        Semaphor redOrGreenSecond;
        
        //pokud se nebude menit barva semaforu
        if(currentGreenUsees < cyclingOfGreen){
            //predame prvni p
            redOrGreenFirst = Semaphor.valueOf(part1[1]);
            redOrGreenSecond = Semaphor.valueOf(part2[1]);
            ++currentGreenUsees;
        }
        else{
            currentGreenUsees=0;
            redOrGreenFirst = Semaphor.RED;
            redOrGreenSecond = Semaphor.RED;
            if (countCarInFirstQueues > countCarInSecondQueues)
                redOrGreenFirst = Semaphor.GREEN;
            else
                redOrGreenSecond = Semaphor.GREEN;

            
        }
        
        countCarInFirstQueues = 0;
        countCarInSecondQueues = 0;
        sendDecisionSemaphor(redOrGreenFirst, redOrGreenSecond);

    }

    public void prepareOfPrintingStatistic(String[] route) {
        int countFirstQueue = 0;
        int countSecondQueue = 0;

        //System.out.println("aha: " + route.length );
        
        if (route.length == 3) {
            if (route[0].equals("route1")) {
                System.out.println("North:\nSouth:");
            } else {
                System.out.println("East:\nWest:");
            }
        }
        //je pouze jedna fronta plna
        else if (route.length == 5) {
            countFirstQueue = Integer.parseInt(route[4]);
            switch(route[2]){
                case "E" : {
                    printDataStateOfRoute(route[2], Integer.parseInt(route[3]), countFirstQueue);
                    System.out.println("West:");
                    break;
                } 
                case "N" :{
                    printDataStateOfRoute(route[2], Integer.parseInt(route[3]), countFirstQueue);
                    System.out.println("South:"); 
                    break;}
                case "W" :{
                    System.out.println("East:");
                    printDataStateOfRoute(route[2], Integer.parseInt(route[3]), countFirstQueue);
                    break;
                } 
                
                case "S" : {
                    System.out.println("North:");
                    printDataStateOfRoute(route[2], Integer.parseInt(route[3]), countFirstQueue);
                    break;
                }
            }
            
            

        } else if (route.length == 8) {
            countFirstQueue = Integer.parseInt(route[4]);
            countSecondQueue = Integer.parseInt(route[7]);
            //vytiskni prvni semafor
            printDataStateOfRoute(route[2], Integer.parseInt(route[3]), countFirstQueue);
            printDataStateOfRoute(route[5], Integer.parseInt(route[6]), countSecondQueue);
        }

        //secteme vsechny auta ve frontach
        countFirstQueue += countSecondQueue;

        if (route[0].equals("route1")) {
            countCarInFirstQueues = countFirstQueue;
        } else {
            countCarInSecondQueues = countFirstQueue;
        }
    }


    public void printDataStateOfRoute(String Direction, int firstIndex, int countOfCar) {
        int countOfCyrcles = firstIndex + countOfCar;
        switch (Direction) {
            case "N":
                System.out.println("North:");
                break;
            case "S":
                System.out.println("South:");
                break;
            case "E":
                System.out.println("East:");
                break;
            case "W":
                System.out.println("West:");
                break;
        }
        for (int i = firstIndex; i < countOfCyrcles; ++i) {
            System.out.println("car-" + Direction + "-" + Integer.toString(i));
        }
    }

    /**
     * @return the finished
     */
    public int getFinished() {
        return finished;
    }

    /**
     * @param finished the finished to set
     */
    public void setFinished(int finished) {
        this.finished = finished;
    }

    /**
     * @return the currentGreenUsees
     */
    public int getCurrentGreenUsees() {
        return currentGreenUsees;
    }

    /**
     * @param currentGreenUsees the currentGreenUsees to set
     */
    public void setCurrentGreenUsees(int currentGreenUsees) {
        this.currentGreenUsees = currentGreenUsees;
    }

}
