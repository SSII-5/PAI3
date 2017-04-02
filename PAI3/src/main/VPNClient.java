package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class VPNClient {
	public VPNClient() {
		// Constructor que abre una conexión Socket para enviar mensaje/MAC al
		// servidor
		try {
			System.setProperty("javax.net.ssl.trustStore", "C:\\SSLStore");
			System.setProperty("javax.net.ssl.trustStorePassword", "SSII1617");
//			System.setProperty("javax.net.debug", "ssl,handshake");
			SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			
			SSLSocket socket = (SSLSocket) socketFactory.createSocket("localhost", 7070);
			socket.setEnabledProtocols(new String[]{"TLSv1.2"}); 
			String pickedCipher[] ={"TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256", "TLS_RSA_WITH_AES_128_CBC_SHA256", "TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256", "TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256", "TLS_DHE_RSA_WITH_AES_128_CBC_SHA256", "TLS_DHE_DSS_WITH_AES_128_CBC_SHA256", "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256", "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256", "TLS_RSA_WITH_AES_128_GCM_SHA256", "TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256", "TLS_ECDH_RSA_WITH_AES_128_GCM_SHA256", "TLS_DHE_RSA_WITH_AES_128_GCM_SHA256", "TLS_DHE_DSS_WITH_AES_128_GCM_SHA256"}; 
			socket.setEnabledCipherSuites(pickedCipher);
			// crea un PrintWriter para enviar mensaje/MAC al servidor
			
			
			PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

			//El cliente se presenta
			// El banco le da el user y pass
			// Y el servidor se asegura de que el user y el pass sean verídicos
			
			String userName = JOptionPane.showInputDialog(null, "Introduzca su usuario:");
			String password = JOptionPane.showInputDialog(null, "Introduzca su contraseña:");
			String mensaje = JOptionPane.showInputDialog(null, "Introduzca su mensaje:");
			
			output.write(userName + ";;" + password + ";;"  + mensaje + '\n');
			output.flush();
			// habría que calcular el correspondiente MAC con la clave
			// compartida por servidor/cliente

			// crea un objeto BufferedReader para leer la respuesta del servidor
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String respuesta = null;
			while((respuesta = input.readLine()) != null){
				JOptionPane.showMessageDialog(null, respuesta);  // muestra la respuesta al cliente
				break;
			}
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