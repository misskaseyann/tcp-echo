import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

class tcpfileclient {
	public static void main(String args[]) {
		try {
			SocketChannel sc = SocketChannel.open();
			Console cons = System.console();

			boolean isint = false;
			int p = 0;
			String a = "127.0.0.1";
			String m = cons.readLine("Give an address: ");
			if (m.matches("(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)")) a = m;
			else System.out.println("Not an address. Using default.");

			System.out.println(a);

			while(!isint) {
				m = cons.readLine("Give a port number: ");
				if (m.matches("^-?\\d+$")) { 
					p = Integer.parseInt(m);
					isint = true;
				} else System.out.println("Not a number.");
			}

			sc.connect(new InetSocketAddress(a, p));

			m = cons.readLine("Enter your message: ");
			String type = "";
			String[] arr = m.split("\\.");
			if (arr.length > 0) {
				type = arr[arr.length - 1];
			}

			ByteBuffer buf = ByteBuffer.wrap(m.getBytes());
			sc.write(buf);

			ByteBuffer buf2 = ByteBuffer.allocate(5000);
			int bytesRead = sc.read(buf2);
			FileOutputStream bout = new FileOutputStream("download." + type);
			FileChannel sbc = bout.getChannel();

			while(bytesRead != -1) {
				buf2.flip();
				sbc.write(buf2);
				buf2.compact();
				bytesRead = sc.read(buf2);
			}

			System.out.println("Downloaded file!");
			sc.close();
		} catch(IOException e) {
			System.out.println("Got an IO Exception");
		}
	}
}