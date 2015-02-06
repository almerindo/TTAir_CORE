/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ttair.dataaccess;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Enumeration;


public class TTAirSerial  implements SerialPortEventListener, Serializable, Runnable {

    private static final long serialVersionUID = 2665400891074496177L;
    private static String portCom = "COM5";
    private static boolean lockStartup = false;
    private static final TTAirSerial INSTANCE = new TTAirSerial();
    
    private String token = "";
    
    private TTAirSerial() {
    }

    private void init(String port){
    	TTAirSerial.setPort(port);
    	initializeCommunication();
    }
    
    
    public static String getPort() {
        return portCom;
    }

    public static void setPort(String port) {
        TTAirSerial.portCom = port;

    }

    private static SerialPort serialPort;

    /**
     * A BufferedReader which will be fed by a InputStreamReader converting the
     * bytes into characters making the displayed results codepage independent
     */
    private static BufferedReader input;
    /**
     * The output stream to the portCom
     */
    private static OutputStream output;
    /**
     * Milliseconds to block while waiting for portCom open
     */
    private static final int TIME_OUT = 2000;
    /**
     * Default bits per second for COM portCom.
     */
    private static final int DATA_RATE = 9600;

    private static CommPortIdentifier portId = null;

    
    private boolean write = true;
    private boolean read = true;
    
    private boolean initialized = false;
    
    
    private final void initializeCommunication() {
    	if (!this.initialized){
    		try {
    			this.getPortID(TTAirSerial.getPort());
    			this.Open();

    			// open the streams
    			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
    			output = serialPort.getOutputStream();

    			// add event listeners
    			//serialPort.addEventListener(this);
    			serialPort.addEventListener(this);
    			serialPort.notifyOnDataAvailable(true);
    			this.initialized = true;

    		} catch (Exception e) {
    			System.err.println(e.toString());
    		}
    		
    	}
    }
    
    @SuppressWarnings("rawtypes")
	private void getPortID(String port){
    	Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
        //First, Find an instance of serial portCom as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();

            if (currPortId.getName().equals(port)) {
                portId = currPortId;
                System.out.println("Comunicação estabelecida na porta: " + port);
                break;
            }
        }
        if (portId == null) {
            System.out.println("Não há¡ comunicação na porta: " + port);
            return;
        }
    }
    
    private void Open() {

        try {
        	// open serial portCom, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT);
            
         // set portCom parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
        } catch (Exception e) {
            System.out.println("Erro abrindo comunicação: " + e);
            System.exit(1);
        }
    }

    /**
     * This should be called when you stop using the portCom. This will prevent
     * portCom locking on platforms like Linux.
     */
    public void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

   /**
     * Handle an event on the serial portCom. Read the data and print it.
     */
	/*@Override
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		  if (this.isRead() && oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
	            try {

	            	byte[] readBuffer = new byte[20];
	            	int numBytes = 0;
	            	InputStream inputStream = serialPort.getInputStream();
	            	while (inputStream.available()>0 && numBytes!=-1) {
		            	
	            		numBytes = inputStream.read(readBuffer);
					}
	            		
	            	String strRead = (new String(readBuffer)).trim();
	            	if (strRead.length()>0){
	            		token = token + strRead;
	            	}
	            	
	            } catch (IOException e) {
	                System.err.println(e.toString());
	            }
	            
	        }
	        // Ignore all the other eventTypes, but you should consider the other ones.
	}*/
    
    @Override
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		  if (this.isRead() && oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
	            try {

	            	InputStream inputStream = serialPort.getInputStream();
	            	while (inputStream.available()>0 ) {
	            		token = token + input.readLine() + ";";		
					}
	            		
	            	token = token.trim();
	            	if (token.length()>0){
	            		//Token tem um valor
	            		//Pode notificar alguns escutadores...
	            	}
	            	
	            } catch (IOException e) {
	                //System.err.println(e.toString());
	            }
	            
	        }
	        // Ignore all the other eventTypes, but you should consider the other ones.
	}
    
    
   public String readLastData() {
	   return this.token;
   }
    

   public  void writeData(int data) {
       this.token = ""; 
	   try {
        	if (this.isWrite()){
        		output.write(data);
        	}
        } catch (Exception e) {
            System.out.println("could not write to port");
        }
    }

    public static TTAirSerial getInstance() {
    	TTAirSerial.INSTANCE.init(TTAirSerial.getPort());
        return TTAirSerial.INSTANCE;
    }


	public boolean isWrite() {
		return write;
	}

	public void setWrite(boolean write) {
		this.write = write;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}


    @Override
    public void run() {
        synchronized (this) {
            while (this.isRead()) {
                try {
                    Thread.sleep(800); //ajustar para melhor velocidde da porta serial
                    if (!lockStartup && !portCom.equals("")) {
                        //initializeCommunication(portCom);
                        lockStartup = true;
                    }
                } catch (InterruptedException ex) {
                    System.out.println("Falha na Thread do Serial communication");
                }
            }
        }
    }

}
