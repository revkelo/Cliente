/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.unbosque;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 * @author englinx
 */
public class Cliente extends Thread{
    // initialize socket and input output streams 
    private Socket socket;
    private ServerSocket server; 
    private Scanner sn;
    private DataOutputStream out;
    private DataInputStream in; //Input stream from server
    private String address;
    private int port;
  
    // constructor to put ip address and port 
    public Cliente(String address, int port){ 
    	// initialize socket and input output streams 
        this.socket= null;
        this.server=null;
        this.sn=new Scanner(System.in);
        this.out= null;
        this.address=address;
        this.port=port;
        
        
      
    }
    
    @Override
    public void run() {
    	
    	// string to read message from input 
        String line = ""; 
  
    	// keep reading until "Over" is input 
        while (!line.equals("Over")) 
        { 
        	 // establish a connection 
        	try
            { 
        		this.socket = new Socket(this.address, this.port); 
                System.out.println("Connected"); 
            
             // sets timeout for server response
                this.socket.setSoTimeout(2000); // 5 seconds
                // sends output to the socket 
                this.out = new DataOutputStream(socket.getOutputStream()); 
        		
        		//line = this.input.readLine(); 
                line=sn.next();
                this.out.writeUTF(line);
                //close socket and output stream
                this.out.close(); 
                this.socket.close(); 
                //Create a serverSocket to wait message from server
                this.server = new ServerSocket(this.port+1);
    	        this.socket = server.accept(); 
    	        System.out.println("Received message:"); 
    	        // takes input from the client socket 
    	        this.in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                //Print in server the client message
    	        
    	        
                System.out.println(in.readUTF());
                this.in.close();
                this.server.close();
            } catch (SocketTimeoutException ste) {
                System.out.println("Server is not responding. Closing connection.");
               line = "Over"; 
                try {
                    this.socket.close();
                } catch (IOException e) {
                    System.out.println("Error closing socket: " + e.getMessage());
                }
            } catch (IOException i) {
                System.out.println("Error: " + i.getMessage());
            }
        }
        
        // close the connection 
        try
        { 
            out.close(); 
            socket.close(); 
        } 
        catch(IOException i) 
        { 
            System.out.println(i); 
        } 
    	
    }
    
    public static void main(String args[]) 
    { 
    	//Servidor server = new Servidor(5001); 
    	Cliente client = new Cliente("127.0.0.1", 7000); 
    	client.start();
    	
    } 
    
}