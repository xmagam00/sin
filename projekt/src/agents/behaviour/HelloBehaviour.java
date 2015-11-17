package agents.behaviour;

import agents.HelloAgent;
import jade.core.behaviours.Behaviour;

/**
 * Created by Martin on 17. 11. 2015.
 */
public class HelloBehaviour extends Behaviour {
    public HelloBehaviour(HelloAgent helloAgent) {
    }

    public void action()
    {
        //...this is where the real programming goes !!
    }

    private boolean finished = false;

    public boolean done() {
        return finished;
    }
}
