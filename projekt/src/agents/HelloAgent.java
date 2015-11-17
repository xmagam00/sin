package agents;

import agents.behaviour.HelloBehaviour;
import jade.core.Agent;

/**
 * Created by Martin on 17. 11. 2015.
 */
public class HelloAgent extends Agent
{
    protected void setup()
    {
        addBehaviour(new HelloBehaviour( this ) );
        System.out.println("Hello World. ");
        System.out.println("My name is "+ getLocalName());
    }
}
