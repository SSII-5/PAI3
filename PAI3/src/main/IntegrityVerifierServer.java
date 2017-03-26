package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class IntegrityVerifierServer {
	private ServerSocket serverSocket;

	// Constructor
	public IntegrityVerifierServer() throws Exception {
		// ServerSocketFactory para construir los ServerSockets
		SSLServerSocketFactory socketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		// creación de un objeto ServerSocket escuchando peticiones en el puerto
		// 7070
		serverSocket = (SSLServerSocket) socketFactory.createServerSocket(7070);
		
		
	}

	// ejecución del servidor para escuchar peticiones de los clientes
	private void runServer() {
		while (true) {
			// espera las peticiones del cliente para comprobar mensaje/MAC
			try {
				System.err.println("Esperando conexiones de clientes...");
				Socket socket = (Socket) serverSocket.accept();
				// abre un BufferedReader para leer los datos del cliente
				BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				// abre un PrintWriter para enviar datos al cliente
				PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				// se lee del cliente el mensaje y el macdelMensajeEnviado
				String mensaje = input.readLine();
				
				output.println(mensaje);
				
				output.close();
				input.close();
				socket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	// ejecucion del servidor
	public static void main(String args[]) throws Exception {
		IntegrityVerifierServer server = new IntegrityVerifierServer();
		server.runServer();
	}
}