package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JOptionPane;

public class VPNClient {
	public VPNClient() {
		// Constructor que abre una conexión Socket para enviar mensaje/MAC al
		// servidor
		try {
			System.setProperty("javax.net.ssl.trustStore", "SSLStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "SSII1617");
			SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			System.out.println(socketFactory.getDefaultCipherSuites());
			System.out.println(socketFactory.getSupportedCipherSuites());
			SSLSocket socket = (SSLSocket) socketFactory.createSocket("localhost", 7070);
			System.out.println(socket.getEnabledCipherSuites());
			socket.startHandshake();
			// crea un PrintWriter para enviar mensaje/MAC al servidor
			PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

			//El cliente se presenta
			// El banco le da el user y pass
			// Y el servidor se asegura de que el user y el pass sean verídicos
			
			String userName = JOptionPane.showInputDialog(null, "Introduzca su usuario:");
			String password = JOptionPane.showInputDialog(null, "Introduzca su contraseña:");
			String mensaje = JOptionPane.showInputDialog(null, "Introduzca su mensaje:");
			
			output.println(userName + "/%%/" + password + "/%%/" + mensaje);
			// habría que calcular el correspondiente MAC con la clave
			// compartida por servidor/cliente

			// crea un objeto BufferedReader para leer la respuesta del servidor
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String respuesta = input.readLine(); // lee la respuesta del servidor
			JOptionPane.showMessageDialog(null, respuesta);
			// muestra la respuesta al cliente
			output.close();
			input.close();
			socket.close();
		} // end try
		catch (IOException ioException) {
			ioException.printStackTrace();
		}
		// Salida de la aplicacion
		finally {
			System.exit(0);
		}
	}


	public static void main(String args[]) {
		new VPNClient();
	}
}