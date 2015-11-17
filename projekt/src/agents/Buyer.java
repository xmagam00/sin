package agents;

import jade.core.*; 
import jade.core.behaviours.*;
import jade.lang.acl.*;
import jade.core.Agent;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.Property;
import java.util.Date;
import java.util.Random;


public class Buyer extends Agent{

  private static final String STATE_WAIT = "WAIT";
  private static final String STATE_BUY = "BUY";
  private static final String STATE_END = "END";
    
  int maxPrice;
  protected void setup() { 
    Random rand = new Random();
    maxPrice = rand.nextInt(101);
    
    System.out.println("Buyer " + getAID().getLocalName() + " is ready. Max price: " + Integer.toString(maxPrice)); 
    //addBehaviour(new RecvMsgBehaviourPa());
    
    FSMBehaviour fsm = new FSMBehaviour(this) {
		public int onEnd() {
		    System.out.println(myAgent.getLocalName() + ": FSM behaviour completed.");
		    //myAgent.doDelete();
		    return super.onEnd();
		  }
		};
    
    fsm.registerFirstState(new WaitForMessage(maxPrice), STATE_WAIT);
    fsm.registerState(new BuyItem(), STATE_BUY);
    fsm.registerLastState(new OneShotBehaviour() {
        @Override
        public void action() {
            System.out.println(myAgent.getLocalName() + " ending.");
        }
    }, STATE_END);
    
    fsm.registerTransition(STATE_WAIT, STATE_WAIT, 0);
    fsm.registerTransition(STATE_WAIT, STATE_END, -1);
    //whenever WaitForMessage returns anything higher than 0, the number returned is payment
    fsm.registerDefaultTransition(STATE_WAIT, STATE_BUY);
    fsm.registerDefaultTransition(STATE_BUY, STATE_END);
    
    addBehaviour(fsm);
  } 
}

class WaitForMessage extends OneShotBehaviour{
  private int exitValue = 0;
  private final int maxPrice;
  
  public WaitForMessage(int _maxPrice){
      super();
      maxPrice = _maxPrice;
  }
  
  @Override
  public void action() {
    ACLMessage msg = myAgent.blockingReceive();
    if(msg == null){
      exitValue = 0;
     System.out.println(myAgent.getLocalName() + " no message.");
    }
    else{
      if(msg.getPerformative() == ACLMessage.CANCEL){
        exitValue = -1; //cancel auction
      }
      else if(msg.getPerformative() == ACLMessage.PROPOSE){
        int price = Integer.valueOf(msg.getContent());
        //buy item if the price is below or equal to maxPrice
        exitValue = (price <= maxPrice) ? price : 0; 
        System.out.println(myAgent.getLocalName() + " exitValue> " + Integer.toString(exitValue));
      }
    }
  }
  
  public int onEnd() {
    return exitValue;
  }
  
}

class BuyItem extends OneShotBehaviour{
  
  @Override
  public void action() {
    FSMBehaviour fsm = (FSMBehaviour) this.getParent();
    int finalPrice = fsm.getLastExitValue();
    
    ACLMessage msg = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
    msg.setContent(Integer.toString(finalPrice));
    msg.addReceiver(new AID("Rudolf", AID.ISLOCALNAME));
    myAgent.send(msg);
  }

}