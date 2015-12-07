/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;

import agents.behaviour.CreateCrossroadsBehaviour;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import java.util.regex.Pattern;

/**
 *
 * @author Vojtech
 */
@SuppressWarnings("serial")
public class CreatorAgent extends Agent{
    
    private AgentContainer routeAgentContainer;
    
    private String dataOfFristAgent;
    private String dataOfSecondAgent;
    private int countCarIn;
    
    
    @Override
	protected void setup() {
		System.out.println("Creator " + getAID().getName() + " is ready");

		setDataOfFristAgent("");
                setDataOfSecondAgent("");

		// vytvoreni kontejneru na auta, ktery si budeme pamatovat
		Profile p = new ProfileImpl();
		setRouteAgentContainer(jade.core.Runtime.instance().createAgentContainer(p));

                addBehaviour(new CreateCrossroadsBehaviour());
                
                //addBehaviour(new StatisticBehaviour());
                
                
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
    
    public void printStatistic(){
        String part1[] = dataOfFristAgent.split(Pattern.quote("-"));
        String part2[] = dataOfSecondAgent.split(Pattern.quote("-"));
        prepareOfPrintingStatistic(part1);
        prepareOfPrintingStatistic(part2);
        
        //smaz vsechny data
        dataOfFristAgent = "";
        dataOfSecondAgent = "";
        
    }
    
    public void prepareOfPrintingStatistic(String[] route){
        if(route.length < 3){
            System.out.println("North\nSouth");
        }
        //je pouze jedna fronta plna
        else if(route.length < 5){
            printDataStateOfRoute(route[2], Integer.parseInt(route[3]), Integer.parseInt(route[4]));
        }
        else {
            //vytiskni prvni semafor
            printDataStateOfRoute(route[2], Integer.parseInt(route[3]), Integer.parseInt(route[4]));
            printDataStateOfRoute(route[5], Integer.parseInt(route[6]), Integer.parseInt(route[7]));
        }
    }
    
    
    public void printDataStateOfRoute(String Direction,int firstIndex,int countOfCar){
        int countOfCyrcles = firstIndex + countOfCar;
        for(int i = firstIndex ; i < countOfCyrcles ; ++i){
            System.out.println("car-"+Direction+"-"+Integer.toString(i));
        }
    }
    
}
