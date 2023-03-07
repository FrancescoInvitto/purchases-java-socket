package it.unipr.desantisinvitto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class ClientHandler implements Runnable{
	
	public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private static final int MAX_PRICE = 200;
	private static final int MIN_PRICE = 10;
	
	public ClientHandler(Socket socket) {
		try {
			this.socket = socket;
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			clientHandlers.add(this);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Random random = new Random();
		String messageFromClient;
		int price;
		while(socket.isConnected()) {
			try {
				Thread.sleep(2000);
				price = random.nextInt(MAX_PRICE-MIN_PRICE) + MIN_PRICE;
				broadcastMessage(price);
				messageFromClient = bufferedReader.readLine();
				System.out.println(messageFromClient + " " + socket);
			}
			catch(Exception e) {
				close(socket, bufferedWriter, bufferedReader);
				break;
			}
		}
		
	}
	
	public void broadcastMessage(int price){
		for(ClientHandler clientHandler : clientHandlers) {
			try {
				clientHandler.bufferedWriter.write(Integer.toString(price));
				clientHandler.bufferedWriter.newLine();
				clientHandler.bufferedWriter.flush();
			}
			catch(Exception e) {
				close(socket, bufferedWriter, bufferedReader);
			}
		}
	}
	
	public void removeClientHandler() {
		clientHandlers.remove(this);
	}
	
	public void close(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
		removeClientHandler();
		try {
			if(bufferedReader!=null) {
				bufferedReader.close();
			}
			if(bufferedWriter!=null) {
				bufferedWriter.close();
			}
			if(socket!=null) {
				socket.close();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
