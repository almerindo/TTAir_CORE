package org.ttair.dataaccess.socketHttp;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TTAirServerTCP implements Runnable{

	public static ExecutorService executor ;
	ArrayList<ExecutorService> listExecutorRequest = new ArrayList<ExecutorService>();
	private static final TTAirServerTCP INSTANCE = new TTAirServerTCP();
	private boolean start;
	private int port = 40000;
	private ServerSocket socket = null;
	private Socket connectionSocket = null;
	private static final ArrayList<IListenerServerSocketCmd> listenersCMD = new ArrayList<IListenerServerSocketCmd>();

	public TTAirServerTCP() {

	}

	public void start() throws IOException {
		TTAirServerTCP.executor = Executors.newFixedThreadPool(1);
		this.start=true;
		// Cria um SocketServer (Socket característico de um servidor)
		TTAirServerTCP.executor.execute(this);
	}

	public void stop() {
		this.start = false;

		//Tenta parar o fluxo;
		try {
			if (connectionSocket!=null){
				if (!connectionSocket.isClosed()){
					connectionSocket.getInputStream().close();
				}
			}
		} catch (IOException e1) {
			//e1.printStackTrace();
		}

		/*
		//Simula um cliente para liberar o fluxo preso.
		try {
			Socket cliente = new Socket("127.0.0.1",this.port);
			PrintStream saida = new PrintStream(cliente.getOutputStream());
			saida.println("BYE\r\n");
			saida.close();
			cliente.close();

		} catch (Exception e) {

		}*/

		//Para a conexao
		try {
			if (connectionSocket!=null){
				if (!connectionSocket.isClosed()) {
					connectionSocket.close();
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}


		//Para o server
		try {
			if (this.socket!= null) {
				if (!this.socket.isClosed()){
					this.socket.close();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}	

		//Para o executor de thread
		try {
			if (TTAirServerTCP.executor!=null) {
				TTAirServerTCP.executor.shutdownNow();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			for (int i = 0; i < listExecutorRequest.size(); i++) {
				ExecutorService exs = listExecutorRequest.get(i);
				exs.shutdownNow();
			}
		} catch (Exception e) {
		}

	}


	@Override
	public void run() {


		try {
			// Cria um SocketServer (Socket característico de um servidor)
			socket = new ServerSocket(this.port );
			
			while(this.start) {    

				/* Cria um objeto Socket, mas passando informações características de um servidor,
				 *no qual somente abre uma porta e aguarda a conexão de um cliente 
				 */
				if (!socket.isClosed()) {
					connectionSocket = socket.accept();
				}
				// Cria uma buffer que irá armazenar as informações enviadas pelo cliente
				//BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

				// Cria uma stream de sáida para retorno das informações ao cliente
				//DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

				//ExecutorService ex = Executors.newFixedThreadPool(1);
				//listExecutorRequest.add(ex);
				//ex.execute(new TreatmentRequest(connectionSocket));
				TreatmentRequest  tr = new TreatmentRequest(connectionSocket);
				Thread t = new Thread(tr);
				t.start();
				
				//this.treatmentRequest(inFromClient, outToClient);
				
				
			} 

		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	
	public void addListenerCMD(IListenerServerSocketCmd listenerCMD){
		
		if (!this.listenersCMD.contains(listenerCMD)) {
			this.listenersCMD.add(listenerCMD);
		}
		
	}
	
	public void removeListenerCMD(IListenerServerSocketCmd listenerCMD) {
		if (this.listenersCMD == null || listenerCMD==null) {
			return;
		}
		this.listenersCMD.remove(listenersCMD);
	}
    
	public static TTAirServerTCP getINSTANCE() {
		return INSTANCE;
	}
	

	public static void main(String[] args) {
		TTAirServerTCP server = new TTAirServerTCP();
		System.out.println("INICIANDO");
		try {
			server.start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		
		//Socket cliente;
		/*PrintStream saida;
		for (int i = 0; i < 10; i++) {
			try {
				cliente = new Socket("127.0.0.1",40000);
				saida = new PrintStream(cliente.getOutputStream());
				saida.println("C2_TV_OFF"+i+"\r\n");
				saida.close();
				cliente.close();
			
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}*/
		
		
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
		}

		System.out.println("Parando");
		server.stop();
		System.out.println("INICIANDO");
		try {
			server.start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
		}
		
		System.out.println("Parando");
		server.stop();

	}

	public boolean isStarted() {
		return start;
	}

	public boolean isStopped() {
		return !start;
	}

	public static ArrayList<IListenerServerSocketCmd> getListenersCMD() {
		return listenersCMD;
	}

	
}


