package it.unipr.desantisinvitto;

import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

/**
 * The {@code Client} class defines the behavior of the clients.
 * Each client connects to the server and waits the current price of the product.
 * Than it randomly generates its maximum offer and if the current price does not 
 * exceed such value, the client will make an offer and wait for the outcome.
 * 
 * The client interrupts its execution as soon as it has made a certain number of purchases (10 by default).
 * @author De Santis Fabrizio, Invitto Francesco
 *
 */
public class Client {
	
	private static final int SPORT = 4444;
	private static final String SHOST = "localhost";
	
	private static final int MIN_PRICE = 10;
	private static final int MAX_PRICE = 200;
	private static final int PURCHASES = 10;
	
	public void run(){
		try {
			Socket client = new Socket(SHOST, SPORT);
			ObjectOutputStream os = new ObjectOutputStream(client.getOutputStream());
			ObjectInputStream is = null;
			
			Random random = new Random();
			int maxOffer;
			int currPrice;
			int purchCount = 0;
			
			while(true) { 
				
				if(is==null) {
					is = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
				}
				
				Object o = is.readObject();
				
				System.out.println("---------------------------------------");
				
				if(o instanceof Price) {
					Price p = (Price) o;
					currPrice = p.getValue();	//gets the current price
					
					System.out.format("Current price: %s%n", currPrice);
					
					if(currPrice==0) {
						break;
					}
					
					maxOffer = random.nextInt(MAX_PRICE - MIN_PRICE) + MIN_PRICE;	//random generation of maximum offer
					
					System.out.format("My maximum offer: %s%n", maxOffer);
					
					Offer oc;
					
					if(maxOffer >= currPrice) {
						oc = new Offer(maxOffer);
						System.out.format("Product at affordable price! I make an offer.%n");
					}
					else {
						System.out.println("Price too high!");
						oc = new Offer(0);
					}
					os.writeObject(oc);
					os.flush();
				}		
								
				o = is.readObject();
				
				if(o instanceof Integer) {
					int outcome = ((Integer) o).intValue();	//gets the outcome of its offer
					
					if(outcome == 1) {
						System.out.println("Offer accepted!");
						purchCount++;
					}
					else if(outcome == 0) {
						System.out.println("Offer rejected!");
					}
				}
				
				System.out.println("---------------------------------------");
				
				if(purchCount == PURCHASES) {
					os.writeObject(false);	//the client informs the server it has completed all its purchases
					os.flush();
					break;
				}
				
				os.writeObject(true);
				os.flush();
				
				Thread.sleep(1000);
			}
			
			client.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(final String[] args) {
		new Client().run();
		System.out.println("Connection closed");
	}
}
