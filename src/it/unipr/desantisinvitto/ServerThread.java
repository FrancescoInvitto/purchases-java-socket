package it.unipr.desantisinvitto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * The {@code ServerThread} class defines the behavior of the threads
 * created by the server to manage the communication with each client.
 * 
 * Each server thread retrieves the current price from the generator, 
 * sends it to the client and waits until the client makes an offer.
 * 
 * Then it checks if the offer is at least as high as the current price;
 * in the positive case the offer is accepted, otherwise it is rejected.
 * 
 * @author De Santis Fabrizio, Invitto Francesco
 */
public class ServerThread implements Runnable{
	
	private static final int NOT_SET = 2;
	private static final int ACCEPTED = 1;
	private static final int REJECTED = 0;

	private Server server;
	private PriceGeneratorThread generator;
	private Socket socket;

	/**
	 * Class constructor.
	 * 
	 * @param s	the server reference
	 * @param c	the client socket
	 * @param g	the reference to the price generator thread
	 */
	public ServerThread(final Server s, final Socket c, final PriceGeneratorThread g) {
		this.server = s;
		this.socket = c;
		this.generator = g;
	}

	@Override
	public void run() {
		ObjectInputStream is = null;
		ObjectOutputStream os = null;

		try {
			is = new ObjectInputStream(new BufferedInputStream(this.socket.getInputStream()));
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}

		String id = String.valueOf(this.hashCode());

		int price;
		int clientOffer;

		while(true) {
			try {
				if(this.server.getStart()) {
					System.out.println("-----------------------------------------");
					
					price = this.generator.getPrice();	//retrieves the current price
					System.out.println("Current product price: " + price);

					if(os==null) {
						os = new ObjectOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
					}

					Price p = new Price(price);
					
					System.out.format("Thread %s sends the price: %s to its client%n", id, price);
					
					os.writeObject(p);	//informs the client about the price
					os.flush();

					Object o = is.readObject();

					if(o instanceof Offer) {
						Offer oc = (Offer) o;
						clientOffer = oc.getValue();	//retrieves the value of the client's offer
						
						System.out.format("Thread %s receives offer: %s from its client%n", id, clientOffer);
						
						price = this.generator.getPrice(); //gets again the price, in order to check if it has not changed

						if(clientOffer >= price) {
							os.writeObject(ACCEPTED);
							os.flush();
						}
						else if (clientOffer == 0){	//the client has not made an offer
							os.writeObject(NOT_SET);
							os.flush();
						}
						else {
							os.writeObject(REJECTED);
							os.flush();
						}
						
						System.out.println("-----------------------------------------");

					}

					o = is.readObject();

					if(o instanceof Boolean) {
						
						if(o.equals(false)) {
							if(this.server.getPool().getActiveCount() == 2) { // it means that the last client and the thread that generates the price remain
								this.generator.setStop(true);
								this.server.close();
							}
							this.socket.close();
							return;
						}
						
					}
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				System.exit(0);
			}

		}
	}

	public void close() {
		try {
			this.socket.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
