package org.ttair.proccess;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ttair.TTAirControl;
import org.ttair.TTAirObject;
import org.ttair.proccess.architecture.ANodeRecognizer;



public  class RecognizerControl extends TTAirControl implements Serializable, Runnable{

	private static final long serialVersionUID = -1730429596458134960L;
	private static final RecognizerControl INSTANCE = new RecognizerControl();	

	private  List<TTAirObject> listRecognizers = new ArrayList<TTAirObject>();
	private  List<Thread> listCacheRecognizers = new ArrayList<Thread>();
	private final ExecutorService executor = Executors.newFixedThreadPool(1);



	private boolean start = false;


	public static RecognizerControl getINSTANCE() {
		return INSTANCE;
	}

	

	//TODO: PROBLEMA DE CONCORRÊNCIA - Com Threads lendo listCacheRecognizers e ao mesmo tempo modificando e escrevendo.
	//captura todos que estão com o start ligado e adiciona no cache para executar. 
	//remove todos que estejam desligado, do cache para executar
	public synchronized void init(boolean start){
		this.start = start;
		
		//Limpa o cache e para as threads
		for (int j = 0; j < listCacheRecognizers.size(); j++) {
			Thread tAux =  listCacheRecognizers.get(j);
			tAux.interrupt();
			listCacheRecognizers.remove(j);
		}
		
		if(this.start) {
			
			this.listRecognizers = this.getList();
			//Cria novas Threads e adiciona no cacche
			for (int i = 0; i < this.listRecognizers.size(); i++) {
				ANodeRecognizer recognizer = (ANodeRecognizer) this.listRecognizers.get(i);
				if (recognizer.isStart()) {
					listCacheRecognizers.add(new Thread(recognizer,recognizer.getID()));

				}
			}
		}else{
			//Para todos os reconhecedores que estão sendo executados.
			this.listRecognizers = this.getList();
			for (int i = 0; i < this.listRecognizers.size(); i++) {
				ANodeRecognizer recognizer = (ANodeRecognizer) this.listRecognizers.get(i);
				recognizer.start(false);
			}
		
			//executor.shutdown(); 
		}
	}


	//Executa os reconhecedores que estão no Cache  //Problema de concorrencia para ser resolvido.
	public    void execute (){
		while (this.start) {
			synchronized (this.listCacheRecognizers) {
				try {
					Thread.sleep(20); // Caso não coloque esse tempo dá erro de concorrência.
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				Thread t = null;
				for (int i = 0; i < this.listCacheRecognizers.size(); i++) {
					t = this.listCacheRecognizers.get(i);
					try {
						executor.execute(t);
					} catch (Exception e) {
						System.out.println("Parou");
					}
				}
			}
		}
	}

	@Override
	public void run() {
		this.execute();
	}

	public void shutdown(){
		this.clear();
		executor.shutdown(); 
	}
	
	@Override
	public void clear() {
		this.listCacheRecognizers.clear();
		this.listRecognizers.clear();
		super.clear();
	}
		
	@Override
	public void testObjType(TTAirObject obj) throws Exception {
		if (!(obj instanceof ANodeRecognizer)) {
			throw new Exception("Objeto não é um Recognizer");
		}	
	}

	


}
