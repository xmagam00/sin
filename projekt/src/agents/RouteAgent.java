/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;

import agents.behaviour.ChangeStateOfSemaphore;
import jade.core.Agent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import model.Semaphor;

/**
 *
 * @author Vojtech
 */
@SuppressWarnings("serial")
public class RouteAgent extends Agent{
    
    private List<String> firstCarQueue;
    private List<String> secondCarQueue;
    
    private String name;
    
    private Semaphor stateOfSemaphor;
    
    @Override
    protected void setup() {
        System.out.println("route " + getAID().getName() + " is ready");

        firstCarQueue = new ArrayList<>();
        secondCarQueue = new ArrayList<>();
        //nejede zadne auto
        setStateOfSemaphor(Semaphor.RED);

        Object args[] = getArguments();
        if (args.length < 1) {
                System.err.println("Unexpected arguments for CarAgent. Call with <src> <dst>");
                doDelete();
        }

        name = "route"+args[0];
        int sizeArgs = args.length ;

        
        String tempCar;
        //jedna li se o prvni cestu 
        if (args[0].toString().equals("1")){
            
            for(int i = 1 ; i < sizeArgs ; ++i){
                tempCar = args[i].toString();
                String parts[] = tempCar.split(Pattern.quote("-"));
                //pokud se jedna o auto prijizdejici ze severu, tak vloz do prvniho listu
                if(parts[1].equals("N")){
                    firstCarQueue.add(tempCar);
                }
                else{
                    secondCarQueue.add(tempCar);
                }
            }
        }
        //jedna se o druhy typ agenta 
        else{
            for(int i = 1 ; i < sizeArgs ; ++i){
                tempCar = args[i].toString();
                String parts[] = tempCar.split(Pattern.quote("-"));
                //pokud se jedna o auto prijizdejici z vychodu, tak vloz do prvniho listu
                if(parts[1].equals("E")){
                    firstCarQueue.add(tempCar);
                }
                else{
                    secondCarQueue.add(tempCar);
                }
            }
        }

        printAllCarsQueue();
        
        addBehaviour(new ChangeStateOfSemaphore());
    }
    
        
    public void printAllCarsQueue(){
        System.out.println("agent " + getAID().getName() );
        System.out.println("prrvni list");
        for(int i=0;i<firstCarQueue.size();i++){
            System.out.println(firstCarQueue.get(i));
        }
        System.out.println("druhy list");
        for(int i=0;i<secondCarQueue.size();i++){
            System.out.println(secondCarQueue.get(i));
        }
        
    }
    
    public String createMessageOfStateRoute(){
        String message = name+"-"+getStateOfSemaphor()+"-";
        if(firstCarQueue.size() > 0){
            String parts[] = firstCarQueue.get(0).split(Pattern.quote("-"));
            String direstion = parts[1];
            int smallestID = Integer.parseInt(parts[2]);
            //indexujeme od 0
            int countOfAllCars = firstCarQueue.size();
            message += direstion + "-" + Integer.toString(smallestID) + "-" + Integer.toString(countOfAllCars)+"-"; 
        }
        if(secondCarQueue.size() > 0){
            String parts[] = secondCarQueue.get(0).split(Pattern.quote("-"));
            String direstion = parts[1];
            int smallestID = Integer.parseInt(parts[2]);
            //indexujeme od 0
            int countOfAllCars = secondCarQueue.size();
            message += direstion + "-" + Integer.toString(smallestID) + "-" + Integer.toString(countOfAllCars); 
        }
        
        return message;
    }

    /**
     * @return the stateOfSemaphor
     */
    public Semaphor getStateOfSemaphor() {
        return stateOfSemaphor;
    }

    /**
     * @param stateOfSemaphor the stateOfSemaphor to set
     */
    public void setStateOfSemaphor(Semaphor stateOfSemaphor) {
        this.stateOfSemaphor = stateOfSemaphor;
    }
    
}
