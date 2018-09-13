import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

class tcpechoserver {
	public static void main(String args[]) {
		try {
			ServerSocketChannel c = ServerSocketChannel.open();
			c.bind(new InetSocketAddress(9877)); // not specifying address... just implying
			// infinite loop
			while (true) {
				SocketChannel sc = c.accept();
				ByteBuffer buffer = ByteBuffer.allocate(5000);
				sc.read(buffer);
				buffer.flip(); // move the pointer in the buffer back to the start.
				byte[] a = new byte[buffer.remaining()];
				buffer.get(a);
				String message = new String(a);
				System.out.println("Got from client: " + message);
				buffer.rewind(); // move pointer back to start of buffer
				sc.write(buffer); // write out to channel
				sc.close();
			}
		} catch(IOException e) {
			System.out.println("Got an IO Exception");
		}
	}
}