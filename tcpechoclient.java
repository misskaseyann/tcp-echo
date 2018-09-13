import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

class tcpechoclient {
	public static void main(String args[]) {
		try {
			// most modern way of doing things.. socket channel is our interface to the network
			SocketChannel sc = SocketChannel.open();
			sc.connect(new InetSocketAddress("127.0.0.1", 9877));
			Console cons = System.console();
			String m = cons.readLine("Enter your message: ");
			// the only thing we can send over a socket channel is a byte buffer.
			// so we need to turn a string into a byte buffer.
			ByteBuffer buf = ByteBuffer.wrap(m.getBytes());
			// send using the write method
			sc.write(buf);
			// recieve with an empty byte buffer of 5000 bytes
			ByteBuffer buf2 = ByteBuffer.allocate(5000);
			sc.read(buf2);
			// now to print the message......
			sc.read(buf2);
			buf2.flip();
			byte[] a = new byte[buf2.remaining()]; // byte array for buffer
			buf2.get(a); // empty contents into the array
			String message = new String(a); // convert to string
			System.out.println("Got from server: " + message);
			sc.close();
		} catch(IOException e) {
			System.out.println("Got an IO Exception");
		}
	}
}