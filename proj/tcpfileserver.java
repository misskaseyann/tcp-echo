import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

class tcpfileserver {
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
				File file = new File(message);

				if (file.isFile()) {
					System.out.println("Reading file " + message + " into bytes.");
					FileChannel sbc = FileChannel.open(file.toPath());
					ByteBuffer buff = ByteBuffer.allocate(10000000);
					int bytesread = sbc.read(buff);
					while(bytesread != -1) {
						buff.flip();
						sc.write(buff);
						buff.compact();
						bytesread = sbc.read(buff);
					}
					
					// byte[] b = new byte[(int) file.length()];
					// FileInputStream fis = new FileInputStream(file);
					// fis.read(b);
					// //fis.close();
					// sc.write(b);
				} else {
					System.out.println("File: " + message + " does not exist.");
				}

				//buffer.rewind(); // move pointer back to start of buffer
				//sc.write(buffer); // write out to channel
				sc.close();
			}
		} catch(IOException e) {
			System.out.println("Got an IO Exception");
		}
	}
}