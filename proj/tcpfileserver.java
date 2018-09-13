import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

/*
 * TCP file transfer, server side.
 */
class tcpfileserver {
	public static void main(String args[]) {
		try {
			// Open socket channel.
			ServerSocketChannel c = ServerSocketChannel.open();
			Console cons = System.console();
			boolean isint = false;
			int p = 0;

			// Ask for a port number.
			while(!isint) {
				String m = cons.readLine("Give a port number: ");
				if (m.matches("^-?\\d+$")) { 
					p = Integer.parseInt(m);
					isint = true;
				} else System.out.println("Not a number.");
			}

			// Connect.
			c.bind(new InetSocketAddress(p));
			System.out.println("Listening on port* " + p);

			// Infinite loop.
			while (true) {
				SocketChannel sc = c.accept();
				ByteBuffer buffer = ByteBuffer.allocate(5000);

				// Read from incoming client message.
				sc.read(buffer);
				buffer.flip(); 
				byte[] a = new byte[buffer.remaining()];
				buffer.get(a);
				String message = new String(a);

				// Attempt to create and read from a file path given.
				File file = new File(message);
				if (file.isFile()) {
					System.out.println("Reading file " + message + " into bytes.");
					// Read the file into a buffer and send it out to the client.
					FileChannel sbc = FileChannel.open(file.toPath());
					ByteBuffer buff = ByteBuffer.allocate(5000);
					int bytesread = sbc.read(buff);
					while(bytesread != -1) {
						buff.flip();
						sc.write(buff);
						buff.compact();
						bytesread = sbc.read(buff);
					}
				} else {
					System.out.println("File: " + message + " does not exist.");
				}
				
				// Close connection with client.
				sc.close();
			}
		} catch(IOException e) {
			System.out.println("Got an IO Exception");
		}
	}
}