/*
 *  (c) K.Bryson, Dept. of Computer Science, UCL (2013)
 */

package switched_network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This simulates a computer network consisting of two computers
 * with server application running on one machine and a
 * client application running on the other machine.
 *
 * The code should work with this setup ... although it should
 * also work with different network structures ...
 * (so 10 computers each running 10 applications!)
 *
 * @author K. Bryson.
 */
public class Main {


    public static void main(String[] args) {

        try {

            // Create 2 computers named 'A' and 'B' with specific IP addresses.
            Computer computerA = new Computer("A", InetAddress.getByName("1.2.3.4"));
            Computer computerB = new Computer("B", InetAddress.getByName("1.2.3.7"));
            
            //8 More computers
            Computer computerC = new Computer("C", InetAddress.getByName("1.2.3.1"));
            Computer computerD = new Computer("D", InetAddress.getByName("1.2.3.2"));
            Computer computerE = new Computer("E", InetAddress.getByName("1.2.3.3"));
            Computer computerF = new Computer("F", InetAddress.getByName("1.2.3.5"));
            Computer computerG = new Computer("G", InetAddress.getByName("1.2.3.6"));
            Computer computerH = new Computer("H", InetAddress.getByName("1.2.3.8"));
            Computer computerI = new Computer("I", InetAddress.getByName("1.2.3.9"));
            Computer computerJ = new Computer("J", InetAddress.getByName("1.2.3.10"));
            
            /*
            // Create a network switch with 4 ports.
            NetworkSwitch networkSwitch = new NetworkSwitch(4);*/
            
            //Create a network switch with 10 ports.
            NetworkSwitch networkSwitch = new NetworkSwitch(10);            

            // This connects network card of ComputerA
            // to Port 0 on the Network Switch.
            SwitchPort port0 = networkSwitch.getPort(0);
            port0.connectNetworkCard((NetworkCard) computerA);
            computerA.connectPort(port0);
            
            // This connects network card of ComputerB
            // to Port 1 on the Network Switch.
            SwitchPort port1 = networkSwitch.getPort(1);
            port1.connectNetworkCard((NetworkCard) computerB);
            computerB.connectPort(port1);
            
            //
            SwitchPort port2 = networkSwitch.getPort(2);
            port2.connectNetworkCard((NetworkCard) computerC);
            computerC.connectPort(port2);
            
            SwitchPort port3 = networkSwitch.getPort(3);
            port3.connectNetworkCard((NetworkCard) computerD);
            computerD.connectPort(port3);
            
            SwitchPort port4 = networkSwitch.getPort(4);
            port4.connectNetworkCard((NetworkCard) computerE);
            computerE.connectPort(port4);
            
            SwitchPort port5 = networkSwitch.getPort(5);
            port5.connectNetworkCard((NetworkCard) computerF);
            computerF.connectPort(port5);
            
            SwitchPort port6 = networkSwitch.getPort(6);
            port6.connectNetworkCard((NetworkCard) computerG);
            computerG.connectPort(port6);
            
            SwitchPort port7 = networkSwitch.getPort(7);
            port7.connectNetworkCard((NetworkCard) computerH);
            computerH.connectPort(port7);
            
            SwitchPort port8 = networkSwitch.getPort(8);
            port8.connectNetworkCard((NetworkCard) computerI);
            computerI.connectPort(port8);
            
            SwitchPort port9 = networkSwitch.getPort(9);
            port9.connectNetworkCard((NetworkCard) computerJ);
            computerJ.connectPort(port9);

            
            // Start the switch operating.
            // Essentially the switch starts forwarding network packets.
            networkSwitch.powerUp();

            // Create a 'ParrotServer' running on Computer B listening to port number 9999.
            Application parrotServer = new ParrotServer((ComputerOS) computerB, 9999);
            parrotServer.start();
            
            Application parrotServer1 = new ParrotServer((ComputerOS) computerB, 10000);
            parrotServer1.start();
            
            Application parrotServer2 = new ParrotServer((ComputerOS) computerB, 20000);
            parrotServer2.start();
            
            //Create 'CustomClient' running on all the other computers sending at port 9999
            for (int i = 0; i < 10; i++) {
	            CustomClient client = new CustomClient("A"+ i, (ComputerOS) computerA, InetAddress.getByName("1.2.3.7"), 9999);
	            client.start();
            }
            
            for (int i = 0; i < 10; i++) {
	            CustomClient client = new CustomClient("C"+ i, (ComputerOS) computerC, InetAddress.getByName("1.2.3.7"), 9999);
	            client.start();
            }
            for (int i = 0; i < 10; i++) {
	            CustomClient client = new CustomClient("D"+ i, (ComputerOS) computerD, InetAddress.getByName("1.2.3.7"), 9999);
	            client.start();
            }
            for (int i = 0; i < 10; i++) {
	            CustomClient client = new CustomClient("E"+ i, (ComputerOS) computerE, InetAddress.getByName("1.2.3.7"), 9999);
	            client.start();
            }
            for (int i = 0; i < 10; i++) {
	            CustomClient client = new CustomClient("F"+ i, (ComputerOS) computerF, InetAddress.getByName("1.2.3.7"), 9999);
	            client.start();
            }
            for (int i = 0; i < 10; i++) {
	            CustomClient client = new CustomClient("G"+ i, (ComputerOS) computerG, InetAddress.getByName("1.2.3.7"), 10000);
	            client.start();
            }
            for (int i = 0; i < 10; i++) {
	            CustomClient client = new CustomClient("H"+ i, (ComputerOS) computerH, InetAddress.getByName("1.2.3.7"), 10000);
	            client.start();
            }
            for (int i = 0; i < 10; i++) {
	            CustomClient client = new CustomClient("I"+ i, (ComputerOS) computerI, InetAddress.getByName("1.2.3.7"), 9999);
	            client.start();
            }
            for (int i = 0; i < 10; i++) {
	            CustomClient client = new CustomClient("J"+ i, (ComputerOS) computerJ, InetAddress.getByName("1.2.3.7"), 9999);
	            client.start();
            }
            
            
        } catch (UnknownHostException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void addMoreComputers() {
    	
    }
}
