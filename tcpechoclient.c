#include <sys/socket.h> // program interface to any network socket (generic)
#include <netinet/in.h> // specific to internet protocols (tcp, udp, idp)
#include <arpa/inet.h> // specific to internet protocols (tcp, udp, idp)
#include <stdio.h>
#include <string.h>
#include <unistd.h>

// making a client and server
int main(int argc, char** argv) {
	// function call creates a socket
	// three params specifies the type of socket we are creating.
	// SOCK_STREAM creates simple tcp packet and automatically breaks the data up into packets.
	// we cant control how many packets the data is sent and recieved in.
	// sometimes if we call for our data, we only get part and so we need to keep calling
	// function returns an integer (but with special meaning.. its an identifier)
	int sockfd = socket(AF_INET, SOCK_STREAM, 0);
	// check for errors.
	// should be incredibly rare for this to occur
	if (sockfd < 0) {
		printf("There was an error creating the socket\n");
		return 1;
	}
	// set up the specific place the socket goes to.. ->socket address internet
	struct sockaddr_in serveraddr;
	serveraddr.sin_family=AF_INET; // family of INET
	// what application we should hand a packet to.. the server is sending data to this port. 
	// h to ns, different representations of integers. 
	// always use htons() so you never have to worry about remembering. htons means: host to network short
	serveraddr.sin_port=htons(9876); 
	// address is acting like you are sending over the network but *really* you are sending back to yourself.
	// useful to use when you don't want to be specifying your IP
	serveraddr.sin_addr.s_addr=inet_addr("127.0.0.1");

	// casting down to generic type sockaddr since we are not looking for the internet socket address
	int e = connect(sockfd, (struct sockaddr*)&serveraddr, sizeof(serveraddr));
	// check for errors
	if (e < 0) {
		printf("There was an error connecting\n");
		return 2;
	}

	// at this point our socket is connected to a remote server and we can start communicating

	printf("Enter a line: ");
	char line[5000];
	char line2[5000];
	fgets(line, 5000, stdin);
	// sending to the address we specified, what we want to send, and the size of our data (if we dont send a string, use bytes)
	send(sockfd, line, strlen(line) + 1, 0);
	// getting data over the network
	// where we are recieving (address), where we want to store it, the amount we are willing to store
	recv(sockfd, line2, 5000, 0);
	printf("%s\n", line2);
	// close the socket
	close(sockfd);

	return 0;
}