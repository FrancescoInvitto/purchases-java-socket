package it.unipr.desantisinvitto;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;

public class Server {
	
	private static final int COREPOOL = 3;
	private static final int MAXPOOL = 3;
	private static final int SPORT = 4444;
	private static final long IDLETIME = 5000;
	
	private ServerSocket socket;
	private ThreadPoolExecutor pool;
	private ArrayList<ServerThread> serverThreads;
	private PriceGeneratorThread generator;
	
	public Server() throws IOException{
		this.socket = new ServerSocket(SPORT);
		this.serverThreads = new ArrayList<>();
		this.generator = new PriceGeneratorThread(this);
	}
	
	private void run() {
		this.pool = new ThreadPoolExecutor(COREPOOL, MAXPOOL, IDLETIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		this.pool.execute(generator);
		while(serverThreads.size()<0) {
			System.out.println("Clients connected: " + serverThreads.size());
			try {
				Socket s = this.socket.accept();
				serverThreads.add(new ServerThread(this, s, this.generator));
			}
			catch(Exception e) {
				break;
			}
		}
		for(ServerThread serverThread : serverThreads) {
			this.pool.execute(serverThread);
		}
		while(true) {
			System.out.println("Clients connected: " + serverThreads.size());
			try {
				Socket s = this.socket.accept();
				ServerThread serverThread = new ServerThread(this, s, generator);
				serverThreads.add(serverThread);
				this.pool.execute(serverThread);
			}
			catch(Exception e) {
				break;
			}
		}
		this.pool.shutdown();
	}
	
	public ThreadPoolExecutor getPool() {
		return this.pool;
	}
	
	public ArrayList<ServerThread> getList(){
		return this.serverThreads;
	}
	
	public void close() {
		try {
			this.socket.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException{
		new Server().run();
		System.out.println("Connection closed");
	}
}
