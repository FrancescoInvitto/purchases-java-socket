package it.unipr.desantisinvitto;

import java.util.ArrayList;
import java.util.Random;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;

public class Server {
	private static final int PORT = 1000;
	private static final int MAX_PRICE = 200;
	private static final int MIN_PRICE = 10;
	private ServerSocket sSocket;
	
	public Server(ServerSocket sSocket) {
		this.sSocket = sSocket;
	}
	
	public void startServer() {
		System.out.println("Server in ascolto...");
		try {
			while(!sSocket.isClosed()) {
				Socket socket = sSocket.accept();
				System.out.println("Nuovo client connesso: " + socket);
				ClientHandler clientHandler = new ClientHandler(socket);
				Thread thread = new Thread(clientHandler);
				thread.start();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void closeServerSocket() {
		try {
			if(sSocket!=null) {
				sSocket.close();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException{
		ServerSocket serverSocket = new ServerSocket(PORT);
		Server server = new Server(serverSocket);
		server.startServer();
	}
}
