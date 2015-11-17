package agents;

import jade.core.Agent;
import agents.behaviour.CarGeneratorBehaviour;
/**
 * Created by Martin on 17. 11. 2015.
 */
public class CarGeneratorAgent extends Agent {
    private static long generator_period = 100l;

    protected void setup()
    {
        addBehaviour(new CarGeneratorBehaviour(this, generator_period ) );

    }
}