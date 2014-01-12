/*
 *  (c) K.Bryson, Dept. of Computer Science, UCL (2013)
 */
package switched_network;

import java.util.HashMap;

/**
 * 
 * Defines a network switch with a number of LAN Ports.
 * 
 * @author K. Bryson.
 */
public class NetworkSwitch extends Thread {

	// a hashMap to store the ip_address of computer connected to the port
	private HashMap<String, Integer> port_com_ip;

	private final SwitchPort[] ports;

	/*
	 * Create a Network Switch the specified number of LAN Ports.
	 */
	public NetworkSwitch(int numberPorts) {

		ports = new SwitchPort[numberPorts];

		// Create each ports.
		for (int i = 0; i < numberPorts; i++) {
			ports[i] = new SwitchPort(i);
		}

	}

	public int getNumberPorts() {

		return ports.length;

	}

	public SwitchPort getPort(int number) {

		return ports[number];
	}

	/*
	 * Power up the Network Switch so that it starts processing/forwarding
	 * network packet traffic.
	 */
	public synchronized void powerUp() {
		// YOU NEED TO IMPLEMENT THIS METHOD.

		// create a new hashmap to store ip addresses of com that connect to
		// each port
		port_com_ip = new HashMap<String, Integer>();
		// associate the ip address of computer to the connected port
		for (int i = 0; i < ports.length; i++) {
			if (ports[i].getIPAddress() != null) {
				port_com_ip.put(ports[i].getIPAddress().getHostAddress(), i);
			}
		}
		// start the thread
		this.start();
	}

	// This thread is responsible for delivering any current incoming packets.
	public void run() {
		// YOU NEED TO IMPLEMENT THIS METHOD.
		while (true) {
			// reset the temporary string to store ip addresses
			String temp = "";

			try {
				sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			synchronized (this) {
				for (int j = 0; j < ports.length; j++) {
					byte packet[] = ports[j].getIncomingPacket();
					if (packet != null) {
						temp = ""; // reset the "temp" for ip address
						// get the packet destination ip
						for (int i = 4; i < 8; i++) {
							int value = packet[i] & 0xff;
							temp = temp + Integer.toString(value);
							if (i == 7) {
								break;
							}
							temp = temp + ".";
						}
						if (port_com_ip.containsKey(temp)) {
							ports[port_com_ip.get(temp)].sendToComputer(packet);
						}
					}
				}
			}
		}
	}

}
