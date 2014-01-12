/*
 *  (c) K.Bryson, Dept. of Computer Science, UCL (2013)
 */

package switched_network;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Models a computer system which can have a number of applications running on
 * it and provides these applications with operating system services including
 * networking.
 * 
 * The Computer provides the operating system services by implementing the
 * ComputerOS interface.
 * 
 * The Computer also handles network traffic to/from switch ports by
 * implementing a NetworkCard interface.
 * 
 * @author K. Bryson.
 */
public class Computer implements ComputerOS, NetworkCard {

	private final String hostname;
	private final InetAddress ipAddress;

	private Lock lock = new ReentrantLock();
	private Queue<byte[]> payload_queue = new LinkedList<byte[]>();

	// This is the switch port which the computer is attached to.
	private SwitchPort port = null;

	private final static int MAX_PORTS = 65536;

	// To store the packet with port number specified
	private HashMap<Integer, Queue<byte[]>> received_packet_table = new HashMap<Integer, Queue<byte[]>>();

	public Computer(String hostname, InetAddress ipAddress) {

		this.hostname = hostname;
		this.ipAddress = ipAddress;

	}

	/**********************************************************************************
	 * The following methods provide the 'Operating System Services' of the
	 * computer which are directly used by applications.
	 **********************************************************************************/

	/*
	 * Get the host name of the computer.
	 */
	public String getHostname() {
		return hostname;
	}

	/*
	 * Ask the operating system to send this message (byte array as the payload)
	 * to the computer with the given IP Address.
	 * 
	 * The message goes from a 'port' on this machine to the specified port on
	 * the other machine.
	 */

	public void send(byte[] payload, InetAddress ip_address_to, int port_from,
			int port_to) {

		// YOU NEED TO IMPLEMENT THIS METHOD.

		// get the source and destination ip addresses in String
		String source_ip = this.ipAddress.getHostAddress();
		String destination_ip = ip_address_to.getHostAddress();

		// create byte array to encapsulate packet
		byte src_ip[] = new byte[4];
		byte dest_ip[] = new byte[4];
		byte src_port[] = new byte[2];
		byte dest_port[] = new byte[2];

		// convert from string ip to integer and to byte
		String source_ip_part[] = source_ip.split("\\.");
		String destination_ip_part[] = destination_ip.split("\\.");
		for (int i = 0; i < 4; i++) {
			src_ip[i] = (byte) Integer.parseInt(source_ip_part[i]);
			dest_ip[i] = (byte) Integer.parseInt(destination_ip_part[i]);
		}
		// store port from in byte
		src_port[1] = (byte) ((port_from >> 8) & 0xff);// store higher 8 bit
		src_port[0] = (byte) ((port_from) & 0xff);

		// store port to in byte
		dest_port[1] = (byte) ((port_to >> 8) & 0xff);
		dest_port[0] = (byte) ((port_to) & 0xff);

		// create the packet of the size header + payload size
		byte[] packet = new byte[payload.length + 12];

		// put source ip to packet
		for (int i = 0; i < 4; i++) {
			packet[i] = src_ip[i];
		}
		// put destination ip to packet
		for (int i = 4; i < 8; i++) {
			packet[i] = dest_ip[i - 4];
		}
		// put source port into packet
		for (int i = 8; i < 10; i++) {
			packet[i] = src_port[i - 8];
		}
		// put destination port into packet
		for (int i = 10; i < 12; i++) {
			packet[i] = dest_port[i - 10];
		}
		// put payload into packet
		for (int i = 12; i < packet.length; i++) {
			packet[i] = payload[i - 12];
		}

		// send the packet to the network card
		this.port.sendToNetwork(packet);
	}

	/*
	 * This asks the operating system to check whether any incoming messages
	 * have been received on the given port on this machine.
	 * 
	 * If a message is pending then the 'payload' is returned as a byte array.
	 * (i.e. without any UDP/IP header information)
	 */
	public  byte[] recv(int port) {
		// YOU NEED TO IMPLEMENT THIS METHOD.
		lock.lock();
		Queue<byte[]> payloads = received_packet_table.get(port);
		if(payloads!=null)
		{
		byte[]payload = payloads.poll();
		lock.unlock();
		return payload;
		}
		else
		{
			lock.unlock();
			return null;
		}
	}

	/**********************************************************************************
	 * The following methods implement the Network Card interface for this
	 * computer.
	 * 
	 * They are used by the 'operating system' to send and recv packets.
	 ***********************************************************************************/

	/*
	 * Get the IP Address of the network card.
	 */
	public InetAddress getIPAddress() {
		return ipAddress;
	}

	/*
	 * This allows a port of a network switch to be attached to this computers
	 * network card.
	 */
	public void connectPort(SwitchPort port) {
		this.port = port;
	}

	/*
	 * This method is used by the Network Switch (SwitchPort) to send a packet
	 * of data from the network to this computer.
	 */
	public void sendToComputer(byte[] packet) {
		// YOU NEED TO IMPLEMENT THIS METHOD.
		int payload_size = packet.length - 12;

		byte payload[] = new byte[payload_size];

		for (int i = 12; i < packet.length; i++) {
			payload[i - 12] = packet[i];
		}

		int dest_port_no = (packet[11] & 0xFF) << 8 | packet[10] & 0xFF;
		lock.lock();
		try {
			payload_queue.add(payload);
			received_packet_table.put(dest_port_no, payload_queue);
		} finally {
			lock.unlock();
		}
	}

}
