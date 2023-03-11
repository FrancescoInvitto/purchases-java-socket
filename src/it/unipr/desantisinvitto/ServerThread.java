package it.unipr.desantisinvitto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

public class ServerThread implements Runnable{
	private static final int MAX=100;
	private static final long SLEEPTIME = 2000;
	private static final int MAX_PRICE = 200;
	private static final int MIN_PRICE = 10;
	
	private Server server;
	private PriceGeneratorThread generator;
	private Socket socket;
	
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
		
		Random random = new Random();
		int price;
		int client_offer;
		
		while(true) {
			try {
				Thread.sleep(2000);
				price = random.nextInt(MAX_PRICE-MIN_PRICE) + MIN_PRICE;
				System.out.println("Prezzo recuperato dal generatore " + generator.getPrice());
				
				if(os==null) {
					os = new ObjectOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
				}
				
				Price ps = new Price(this.generator.getPrice());
				System.out.format("Thread %s sends: %s to its client%n", id, this.generator.getPrice());
				os.writeObject(ps);
				os.flush();
				
				Object o = is.readObject();
				
				if(o instanceof Offer) {
					Offer oc = (Offer) o;
					client_offer = oc.getOffer();
					System.out.format("Thread %s receives: %s from its client%n", id, client_offer);
					
					if(client_offer>=this.generator.getPrice()) {
						os.writeObject(1);
						os.flush();
					}
					else if (client_offer==0){
						os.writeObject(2);
						os.flush();
					}
					else {
						os.writeObject(0);
						os.flush();
					}
					
				}
				
				o = is.readObject();
				
				if(o instanceof Boolean) {
					if(o.equals(false)) {
						if(this.server.getPool().getActiveCount()==2) { // vuol dire che rimangono l'ultimo client e il thread che genera il prezzo
							this.generator.setStop(true);
							this.server.close();
						}
						this.server.getList().remove(this);
						System.out.println("Clients connected: " + this.server.getList().size());
						this.socket.close();
						return;
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
