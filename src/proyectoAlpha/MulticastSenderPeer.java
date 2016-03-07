/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package proyectoAlpha;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JGUTIERRGARC
 */
public class MulticastSenderPeer {
    	public static void main(String args[]){ 
  	 
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
 	    }
         catch (SocketException e){
             System.out.println("Socket: " + e.getMessage());
	 }
         catch (IOException e){
             System.out.println("IO: " + e.getMessage());
         }
	 finally {
            if(s != null) s.close();
        }
    }		     
}
