package it.unipr.desantisinvitto;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.net.Socket;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * The {@code Server} class defines the behavior of the server.
 * The server simply accepts the connection of the clients and delegates
 * separated threads to manage the logic of the purchases.
 * 
 * The server creates a new thread each time a new client connects, but
 * all the procedures begin as soon as at least 3 (by default) clients have connected.
 * 
 * @author De Santis Fabrizio, Invitto Francesco
 *
 */
public class Server {
	
	private static final int COREPOOL = 100;
	private static final int MAXPOOL = 100;
	private static final int SPORT = 4444;
	private static final long IDLETIME = 5000;
	
	private static final int MIN_CLIENTS = 3;
	
	private ServerSocket socket;
	private ThreadPoolExecutor pool;
	private PriceGeneratorThread generator;
	private volatile boolean start;
	
	/**
	 * Class constructor.
	 * 
	 * @throws IOException
	 */
	public Server() throws IOException{
		this.socket = new ServerSocket(SPORT);
		this.generator = new PriceGeneratorThread();
		this.start = false;
	}
	
	private void run() {
		this.pool = new ThreadPoolExecutor(COREPOOL, MAXPOOL, IDLETIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		this.pool.execute(generator);
		int clientsCount = 0;
		
		while(true) {
			System.out.println("Clients connected: " + clientsCount + ". Minimum required: " + MIN_CLIENTS);
			try {
				Socket s = this.socket.accept();
				ServerThread serverThread = new ServerThread(this, s, this.generator);
				clientsCount++;
				this.pool.execute(serverThread);
				
				// if the minimum number of clients has been reached, threaded servers can communicate with connected clients
				
				if(clientsCount == MIN_CLIENTS) { 
					this.start = true;
				}
				
			}
			catch(Exception e) {
				break;
			}
		}
		this.pool.shutdown();
	}
	
	/**
	 * This method is used to retrieve the pool of threads.
	 * 
	 * @return the pool of threads
	 */
	public ThreadPoolExecutor getPool() {
		return this.pool;
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
	
	/**
	 * This method is called by the server threads in order to check
	 * if they can start reading the price from the generator.
	 * 
	 * @return	true if they can start
	 */
	public boolean getStart() {
		return this.start;
	}
}
