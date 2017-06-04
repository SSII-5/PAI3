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
			socket.setEnabledProtocols(new String[]{"SSLv2Hello",
					"SSLv3",
					"TLSv1",
					"TLSv1.1",
					"TLSv1.2"}); 
			// Todos los cipher suites de ahora son SSL, por tanto no son tan seguros como debieran
			String pickedCipher[] ={
					//"TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA",
					//"TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA",
					"SSL_RSA_WITH_3DES_EDE_CBC_SHA",
					//"TLS_ECDH_ECDSA_WITH_3DES_EDE_CBC_SHA",
					//"TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA",
					"SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA",
					"SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA",
					//"TLS_ECDH_anon_WITH_AES_128_CBC_SHA",
					//"TLS_DH_anon_WITH_AES_128_CBC_SHA",
					//"TLS_ECDH_anon_WITH_3DES_EDE_CBC_SHA",
					"SSL_DH_anon_WITH_3DES_EDE_CBC_SHA",
					"SSL_RSA_WITH_DES_CBC_SHA",
					"SSL_DHE_RSA_WITH_DES_CBC_SHA",
					"SSL_DHE_DSS_WITH_DES_CBC_SHA",
					"SSL_DH_anon_WITH_DES_CBC_SHA",
					"SSL_RSA_EXPORT_WITH_DES40_CBC_SHA",
					"SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA",
					"SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA",
					"SSL_DH_anon_EXPORT_WITH_DES40_CBC_SHA",
					//"TLS_RSA_WITH_NULL_SHA256",
					//"TLS_ECDHE_ECDSA_WITH_NULL_SHA",
					//"TLS_ECDHE_RSA_WITH_NULL_SHA",
					"SSL_RSA_WITH_NULL_SHA",
					//"TLS_ECDH_ECDSA_WITH_NULL_SHA",
					//"TLS_ECDH_RSA_WITH_NULL_SHA",
					//"TLS_ECDH_anon_WITH_NULL_SHA",
					"SSL_RSA_WITH_NULL_MD5",
					//"TLS_KRB5_WITH_3DES_EDE_CBC_SHA",
					//"TLS_KRB5_WITH_3DES_EDE_CBC_MD5",
					//"TLS_KRB5_WITH_DES_CBC_SHA",
					//"TLS_KRB5_WITH_DES_CBC_MD5",
					//"TLS_KRB5_EXPORT_WITH_DES_CBC_40_SHA",
					//"TLS_KRB5_EXPORT_WITH_DES_CBC_40_MD5"
					}; 
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