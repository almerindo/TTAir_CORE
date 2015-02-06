/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ttair.dataaccess.socketHttp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;
import java.util.ArrayList;
//import java.io.PrintWriter;

/**
 *
 * @author TTAir Research Group
 */
public class TreatmentRequest implements Runnable{
	
	
    protected Socket socket;
    
	public TreatmentRequest(Socket socket) {
	  this.socket = socket;
	}


    @Override
    public void run() {
        try {
        	if (socket==null) {
        		return;
        	}
            if (socket != null && !socket.isClosed()) {
                String cmd = readCommand();
                //System.out.println("READ 01: " + cmd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.getOutputStream().close();
            } catch (Exception e) {
            }
            try {
                socket.getInputStream().close();
            } catch (Exception e) {
            }
            try {
                socket.close();
            } catch (Exception e) {
            }
        }

    }

    private String readCommand() throws Exception {
        Reader r = new InputStreamReader(socket.getInputStream());
        //PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader br = new BufferedReader(r);
        StringBuffer sb = new StringBuffer();
        
        
        String userInput = br.readLine();
        //Character userInput = new Character((char) br.read());
        while (userInput!=null ) {
            //System.out.println(userInput);
            toTest(userInput);
            sb.append(userInput);
        	userInput = br.readLine();
            //c = new Character((char) br.read());
        }
        
        
        
        return sb.toString();
    }
    
    private void toTest(String userInput) {
    	
    	for (int i = 0; i < TTAirServerTCP.getListenersCMD().size(); i++) {
    		IListenerServerSocketCmd l = TTAirServerTCP.getListenersCMD().get(i);
    		l.notifyCMDReceived(userInput);
		}
    	
	}


	/*private void writeCommand() throws IOException {
    	String resp = "HTTP/1.1 400 Bad Request\r\n" +
                "Content-Type: text/html\r\n" +
                "Connection: close\r\n" +
                "Content-Length: 35\r\n";    	
    	
    	DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
		if (outToClient!= null) {
			outToClient.writeBytes(resp);
		}
    }*/

    /**
     * @return the socket
     */
    public Socket getSocket() {
        return socket;
    }

}
