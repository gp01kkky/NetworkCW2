/*
 *  (c) K.Bryson, Dept. of Computer Science, UCL (2013)
 */

package switched_network;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 
 * This represents a 'Switch Port' on the front of a Network Switch.
 * 
 * For instance, a Network Switch may have 4 ports which are Ethernet sockets at
 * the front of the Network Switch. Each Ethernet socket is physically connected
 * to the network card of a computer using an Ethernet cable.
 * 
 * @author K. Bryson.
 */
public class SwitchPort {

	private final int portNumber;
	private NetworkCard connectedNetworkCard = null;
	private InetAddress ipAddress = null;

	// to store byte_packet;
	private Queue<byte[]> packet_queue = new LinkedList<byte[]>();

	public SwitchPort(int number) {
		portNumber = number;
	}

	public int getNumber() {
		return portNumber;
	}

	public InetAddress getIPAddress() {
		return ipAddress;
	}

	/*
	 * This method is USED BY THE COMPUTER to send a packet of data to this Port
	 * on the Switch.
	 * 
	 * The packet of data should follow the simplified header format as
	 * specified in the coursework descriptions.
	 */

	// need to synchronize because packet is a shared variable to avoid reading
	// and writing at the same time
	public synchronized void sendToNetwork(byte[] packet) {
		// YOU NEED TO IMPLEMENT THIS METHOD.
		packet_queue.add(packet);
	}

	public synchronized byte[] getIncomingPacket() {
		// YOU NEED TO IMPLEMENT THIS METHOD.
		return packet_queue.poll();
	}

	void sendToComputer(byte[] packet) {
		// YOU NEED TO IMPLEMENT THIS METHOD.
		connectedNetworkCard.sendToComputer(packet);
	}

	void connectNetworkCard(NetworkCard networkCard) {
		connectedNetworkCard = networkCard;
		ipAddress = networkCard.getIPAddress();
	}

}
