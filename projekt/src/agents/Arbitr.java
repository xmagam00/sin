package agents;

import jade.core.*; 
import jade.core.behaviours.*;
import jade.lang.acl.*;
import jade.core.Agent;
import jade.core.AID;
import jade.domain.AMSService;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.util.leap.Iterator;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Arbitr extends Agent{
  public static ParallelBehaviour auctionBehav;
    
  protected void setup() { 
    System.out.println(getAID().getName() + " is ready."); 
    
    addBehaviour(new WakerBehaviour(this, 20000) {
      protected void onWake() {
        auctionBehav = new ParallelBehaviour(myAgent, ParallelBehaviour.WHEN_ANY);
        auctionBehav.addSubBehaviour(new RecvAcceptsBehaviour());
        auctionBehav.addSubBehaviour(new PriceControlBehaviour(myAgent, 5000, 100));
        myAgent.addBehaviour(auctionBehav);
      } 
    });
  } 
}


class RecvAcceptsBehaviour extends CyclicBehaviour{
  
  @Override
  public void action() {
    System.out.println("Karel receiving message: ");  
    ACLMessage msg = myAgent.receive();  
    if (msg == null){ 
      block();
    }
    else{
      if(msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){
        System.out.println(msg.getSender().getLocalName() + " won the item for " + msg.getContent());  

        //end auction
        ACLMessage end_msg = new ACLMessage(ACLMessage.CANCEL);  
        end_msg.setContent("Auction won by " + msg.getSender().getName() + " for " + msg.getContent()); 

        for (int i = 1; i < 6; ++i) {
          end_msg.addReceiver(new AID("B"+Integer.toString(i), AID.ISLOCALNAME));
        }
        end_msg.removeReceiver(msg.getSender());
        //System.out.println(myAgent.getAID().getName() + " sent CFPs.");
        myAgent.send(end_msg);
        
        Arbitr.auctionBehav.addSubBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                //System.out.println(myAgent.getAID().getName() + " sent CFPs.");
            }
        });
        
      }
    }
  }
}

class PriceControlBehaviour extends TickerBehaviour{
  long startingPrice;
  double currentPercentage = 1;
    
  public PriceControlBehaviour(Agent a, long period, long _startingPrice) {
    super(a, period);
    startingPrice = _startingPrice;
  }
    
  public void onTick(){
    currentPercentage -= 0.05;
    if(currentPercentage <= 0){
        //end auction
        ACLMessage end_msg = new ACLMessage(ACLMessage.CANCEL);  
        end_msg.setContent("Auction ended due to disinterest."); 

        for (int i = 1; i < 6; ++i) {
          end_msg.addReceiver(new AID("B"+Integer.toString(i), AID.ISLOCALNAME));
        }
        //System.out.println(myAgent.getAID().getName() + " sent CFPs.");
        myAgent.send(end_msg);
        
        Arbitr.auctionBehav.addSubBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                //System.out.println(myAgent.getAID().getName() + " sent CFPs.");
            }
        });
    }
    else{
    
        int  currentPrice = (int) Math.ceil(startingPrice*currentPercentage);

        ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);  
        msg.setContent(Integer.toString(currentPrice)); 

        System.out.println("New offer> " + Integer.toString(currentPrice));

        for (int i = 1; i < 6; ++i) {
            msg.addReceiver(new AID("B"+Integer.toString(i), AID.ISLOCALNAME));
        }
        //System.out.println(myAgent.getAID().getName() + " sent CFPs.");
        myAgent.send(msg);
    }
  }
}
/*
//auction
class AuctionBehaviour extends OneShotBehaviour{
  DFAgentDescription[] candidates; // auction candidates - agents who provide desired service
  int timeout = 20;
  public static RecvOffersBehaviour recvOffersBehaviour;
  
  public static ArrayList<Price> prices;

    
  public void action() {
    try {
        Thread.sleep(20000);
    } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    getProviders();
    
    sendCFPs();
    
    //recieve offers for (timeout) seconds
    prices = new ArrayList<>();
    AuctionBehaviour.recvOffersBehaviour = new RecvOffersBehaviour();
    myAgent.addBehaviour(AuctionBehaviour.recvOffersBehaviour);
    System.out.println(myAgent.getAID().getName() + " started receiving offers."); 
    
    //remove recvOffers behaviour after timeout
    myAgent.addBehaviour(new WakerBehaviour(myAgent, timeout*1000) {
      protected void onWake() {
        myAgent.removeBehaviour(AuctionBehaviour.recvOffersBehaviour);
        System.out.println(myAgent.getAID().getName() + " stopped receiving offers.");
        myAgent.addBehaviour(new SelectWinnerBehaviour());
      } 
    });
    
  }
  //get providers of service called "vypracování projektu z AGS"
  protected void getProviders(){
    try {
        // Build the description used as template for the search
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription templateSd = new ServiceDescription();
        templateSd.setType("Programming");
        templateSd.setName("vypracování projektu z AGS");
        template.addServices(templateSd);

        SearchConstraints sc = new SearchConstraints();
        // We want to receive 10 results at most
        sc.setMaxResults(new Long(10));

        candidates = DFService.search(myAgent, template, sc);
        if (candidates.length > 0) {
            System.out.println("Interspector found the following programming services ("+ candidates.length+"):");
            for (int i = 0; i < candidates.length; ++i) {
                DFAgentDescription dfd = candidates[i];
                AID provider = dfd.getName();
                // The same agent may provide several services; we are only interested
                // in the weather-forcast one
                Iterator it = dfd.getAllServices();
                while (it.hasNext()) {
                    ServiceDescription sd = (ServiceDescription) it.next();
                    if (sd.getType().equals("Programming") && sd.getName().equals("vypracování projektu z AGS")) {
                        System.out.println("- Service \""+sd.getName()+"\" provided by agent "+provider.getName());
                    }
                }
            }
        }	
        else {
                System.out.println("Interspector did not find any weather-forecast service");
        }
        //results[0].getName();
    }
    catch (FIPAException fe) {
  	fe.printStackTrace();
    }
  
  }
  
  protected void sendCFPs(){
    ACLMessage msg = new ACLMessage(ACLMessage.CFP);  
    msg.setContent(Integer.toString(timeout)); 
 
    for (int i = 0; i < candidates.length; ++i) {
        DFAgentDescription dfd = candidates[i];
        AID provider = dfd.getName();
        msg.addReceiver(provider);
    }
    System.out.println(myAgent.getAID().getName() + " sent CFPs.");
    myAgent.send(msg);  
  }
}
  
  

class SelectWinnerBehaviour extends OneShotBehaviour{
    
  public void action() {
    ArrayList<Price> prices = AuctionBehaviour.prices;
    
    Collections.sort(prices);
 
    // remove first and last offer
    if(prices.size() > 2){
        prices.remove(0);
        prices.remove(prices.size()-1);
    }
    
    // tisk jednotlivych nabidek
    for (Price p:prices) {
        System.out.println(p.getAgent().getName()+" : "+p.getPrice());
    }
    
    //send accept and rejections
    if(prices.size() > 0){
        ACLMessage msg = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL); 
        msg.addReceiver(prices.get(0).getAgent()); 
        msg.setContent("Accepting proposal of " + prices.get(0).getPrice() ); 
        myAgent.send(msg);
        
        msg = new ACLMessage(ACLMessage.REJECT_PROPOSAL); 
        msg.setContent("We are sorry to inform you that your proposal has been rejected."); 
        for (Price p:prices.subList(1, prices.size())) {
            msg.addReceiver(p.getAgent());
        }
        myAgent.send(msg);
        
        myAgent.addBehaviour(new AwaitResponseBehaviour());
    }
        
  }
    
}



class AwaitResponseBehaviour extends CyclicBehaviour{
  
  @Override
  public void action() {
    //System.out.println("Karel receiving message: ");  
    ACLMessage msg = myAgent.receive();  
    if (msg == null){ 
      block();
    }
    else{
      if(msg.getPerformative() == ACLMessage.INFORM){
        System.out.println("JOB FINISHED!"); 
      }
      else if(msg.getPerformative() == ACLMessage.FAILURE){
        System.out.println("JOB FAILED! Starting new auction.");
        myAgent.removeBehaviour(this);
        myAgent.addBehaviour(new AuctionBehaviour());
      }
    }
  }
}
*/
/*
//call for proposals
class StartAuctionBehaviour extends WakerBehaviour{
  AID receiver = null;
  int timeout;
  
  public SendCFPBehaviour(AID _receiver, int _timeout){
      receiver = _receiver;
      timeout = _timeout;
  }
    
  public void action() {
    ACLMessage msg = new ACLMessage(ACLMessage.CFP); 
    msg.addReceiver(receiver); 
    
    msg.setContent(Integer.toString(timeout)); 
    myAgent.send(msg);  
    System.out.println("Sender agent " + myAgent.getAID().getName() + " sent msg."); 
  }
}*/

class Price implements Comparable<Object> {
	private AID agent;
	private Integer price;
 
	public Price(AID pAgent, Integer pPrice) {
		this.agent = pAgent;
		this.price = pPrice;
	}
 
	public Integer getPrice() {
		return price;
	}
 
	public AID getAgent() {
		return agent;
	}
 
	@Override
        public int compareTo(Object o) {
		if (o instanceof Price) {
			Price a = (Price)o;
			if (this.price == a.price)
			{
			    return 0;
			}
			else if (this.price > a.price)
			{
			    return 1;
			}
			else
			{
			    return -1;
			}
                }
		return 0;
	}
}



