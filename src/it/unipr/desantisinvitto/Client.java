package it.unipr.desantisinvitto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Client {
	private static final int MAX_PRICE = 220;
	private static final int MIN_PRICE = 10;
	
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	
	public Client(Socket socket) {
		try {
			this.socket = socket;
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void listenForMessage() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String msgFromServer;
				while(socket.isConnected()) {
					try {
						msgFromServer = bufferedReader.readLine();
						System.out.println(msgFromServer);
					}
					catch(Exception e) {
						close(socket, bufferedWriter, bufferedReader);
					}
				}
			}
		}).start();
	}
	
	public void close(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
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
	
	public static void main(final String[] args) throws Exception {
		Random random = new Random();
		Socket socket = new Socket("localhost", 1000);
		Client client = new Client(socket);
		client.listenForMessage();
	}
}
