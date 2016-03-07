/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoAlpha;

/**
 *
 * @author Santiago
 */

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


import java.util.Date;

public class server {
    
    public static void main (String args[]) {
        //MULTICAST PARA MANDAR MONSTRUO
        MulticastSocket s =null;
   	try {
            InetAddress group = InetAddress.getByName("228.13.11.91"); // destination multicast group 
            s = new MulticastSocket(6789);
            while(true){
                s.joinGroup(group); 
                s.setTimeToLive(10);
                //System.out.println("Messages' TTL (Time-To-Live): "+ s.getTimeToLive());
                Date hora = new Date();
                String myMessage=hora.toString();
                byte [] m = myMessage.getBytes();
                DatagramPacket messageOut = 
                        new DatagramPacket(m, m.length, group, 6789);
                s.send(messageOut);
                System.out.println("Se envió la hora: "+myMessage);
                s.leaveGroup(group);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MulticastSenderPeer.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
                //
 	} catch (SocketException e){
            System.out.println("Socket: " + e.getMessage());
	} catch (IOException e){
            System.out.println("IO: " + e.getMessage());
        } finally {
            if(s != null) s.close();
        }
        
        //TCP ESCUCHA QUIEN PEGO
	try{
            int serverPort = 7896;
            ServerSocket listenSocket = new ServerSocket(serverPort);
            while(true) {
                System.out.println("Waiting for messages...");
                Socket clientSocket = listenSocket.accept();  // Listens for a connection to be made to this socket and accepts it. The method blocks until a connection is made. 
                Connection c = new Connection(clientSocket);
                c.start();
            }
	} catch(IOException e) {
            System.out.println("Listen :" + e.getMessage());
        }
    }
}

class Connection extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    public Connection (Socket aClientSocket) {
        try {
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out =new DataOutputStream(clientSocket.getOutputStream());
        } catch(IOException e)  {System.out.println("Connection:"+e.getMessage());}
    }
        
    @Override
    public void run(){
        try {			                 // an echo server
            String data = in.readUTF();	     
            System.out.println("Message received from: " + clientSocket.getRemoteSocketAddress());
            out.writeUTF(data);
        } 
        catch(EOFException e) {
            System.out.println("EOF:"+e.getMessage());
        } 
        catch(IOException e) {
            System.out.println("IO:"+e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e){
                System.out.println(e);
            }
        }
    }
}