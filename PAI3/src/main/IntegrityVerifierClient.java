package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JOptionPane;

public class IntegrityVerifierClient {
	public IntegrityVerifierClient() {
		// Constructor que abre una conexión Socket para enviar mensaje/MAC al
		// servidor
		try {
			SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			SSLSocket socket = (SSLSocket) socketFactory.createSocket("localhost", 7070);
			// crea un PrintWriter para enviar mensaje/MAC al servidor
			PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

			//El cliente se presenta
			// El banco le da el user y pass
			// Y el servidor se asegura de que el user y el pass sean verídicos
			
			String userName = JOptionPane.showInputDialog(null, "Introduzca su usuario:");
			String password = JOptionPane.showInputDialog(null, "Introduzca su contraseña:");
			String mensaje = JOptionPane.showInputDialog(null, "Introduzca su usuario:");
			output.println(userName);
			output.println(password);
			output.println(mensaje);
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
		new IntegrityVerifierClient();
	}
}