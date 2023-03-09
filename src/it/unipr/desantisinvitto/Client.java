package it.unipr.desantisinvitto;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Client {
	
	private static final int SPORT = 4444;
	private static final String SHOST = "localhost";
	private static final int MAX_PRICE = 200;
	private static final int MIN_PRICE = 10;
	private int COUNT = 3;
	
	public void run() {
		try {
			Socket client = new Socket(SHOST, SPORT);
			ObjectOutputStream os = new ObjectOutputStream(client.getOutputStream());
			ObjectInputStream is = null;
			
			Random random = new Random();
			int offer;
			int currPrice;
			
			while(COUNT>0) {
				
				if(is==null) {
					is = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
				}
				
				Object o = is.readObject();
				
				if(o instanceof Price) {
					Price ps = (Price) o;
					currPrice = ps.getPrice();
					System.out.format("Received  %s from Server%n", currPrice);
					if(currPrice==0) {
						break;
					}
					
					offer = random.nextInt(MAX_PRICE-MIN_PRICE) + MIN_PRICE;
					
					Offer oc;
					
					if(offer>=currPrice) {
						oc = new Offer(offer);
						System.out.format("Client sends %s to the Server%n", offer);
					}
					else {
						System.out.println("Price too high");
						oc = new Offer(0);
					}
					os.writeObject(oc);
					os.flush();
				}
				
								
				o = is.readObject();
				
				if(o instanceof Integer) {
					int result = ((Integer) o).intValue();
					if(result==1) {
						System.out.println("Offer accepted");
						COUNT--;
					}
					else if(result==0) {
						System.out.println("Offer declined");
					}
				}
				
				if(COUNT==0) {
					os.writeObject(false);
					os.flush();
					break;
				}
				os.writeObject(true);
				os.flush();
			}
			
			client.close();
		}
		catch(IOException | ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	
	public static void main(final String[] args) {
		new Client().run();
		System.out.println("Connection closed");
	}
}
