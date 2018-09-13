import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

class tcpfileclient {
	public static void main(String args[]) {
		try {
			SocketChannel sc = SocketChannel.open();
			sc.connect(new InetSocketAddress("127.0.0.1", 9877));
			Console cons = System.console();
			
			String m = cons.readLine("Enter your message: ");
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