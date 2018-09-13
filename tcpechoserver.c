#include <sys/socket.h> // program interface to any network socket (generic)
#include <netinet/in.h> // specific to internet protocols (tcp, udp, idp)
#include <arpa/inet.h> // specific to internet protocols (tcp, udp, idp)
#include <stdio.h>
#include <string.h>
#include <unistd.h>

int main(int argc, char** argv) {
	int sockfd = socket(AF_INET, SOCK_STREAM, 0);
	// check for error
	if (sockfd < 0) {
		printf("Problem creating socket\n");
		return 1;
	}

	// server needs its own address but should also store client address
	struct sockaddr_in serveraddr, clientaddr;
	serveraddr.sin_family=AF_INET;
	serveraddr.sin_port=htons(9876);
	// dont have to program the server to know what device it is running on. 
	// this constant has you use any address available.. 
	// as long as it has that same port number.
	serveraddr.sin_addr.s_addr=INADDR_ANY;

	// opposite of connect. 
	int b = bind(sockfd, (struct sockaddr*)&serveraddr, sizeof(serveraddr));
	// error check very important here
	if (b < 0) {
		printf("Bind error\n");
		return 3;
	}

	// 10 is the backlog for buffer of connection requests that we havent handled.
	// handles multiple.. but not at the same time...
	listen(sockfd, 10);

	// we want the server to keep accepting clients. an infinite loop is most common
	while(1) {
		int len = sizeof(clientaddr);
		// accept is a blocking call. it will wait until it can accept something.
		// gives us a new socket back to specifically talk to the client
		int clientsocket = accept(sockfd, (struct sockaddr*)&clientaddr, &len);
		char line[5000];
		// uses client socket to talk to that client.
		// client socket allows us to distinguish between clients.
		recv(clientsocket, line, 5000, 0);
		printf("Got from client: %s\n", line);
		send(clientsocket, line, strlen(line) + 1, 0);
		close(clientsocket);
	}

	return 0;
}