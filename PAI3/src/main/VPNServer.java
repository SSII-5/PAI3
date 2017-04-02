package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class VPNServer {
	private SSLServerSocket serverSocket;

	protected String usuario = "user";
	protected String password = "pass";
	private String executionPath;
	protected static String hash;

	// Constructor
	public VPNServer() throws Exception {
		System.setProperty("javax.net.ssl.keyStore", "C:\\SSLStore");
		System.setProperty("javax.net.ssl.keyStorePassword", "SSII1617");
		// ServerSocketFactory para construir los ServerSockets
		SSLServerSocketFactory socketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		// creación de un objeto ServerSocket escuchando peticiones en el puerto
		// 7070
//		System.out.println( "Default cipher Suites: " + Arrays.toString(socketFactory.getDefaultCipherSuites()));
//		System.out.println("Supported: "+ Arrays.toString(socketFactory.getSupportedCipherSuites()));
		serverSocket = (SSLServerSocket) socketFactory.createServerSocket(7070);
		


	}

	// ejecución del servidor para escuchar peticiones de los clientes
	private void runServer() {
		while (true) {
			// espera las peticiones del cliente para comprobar mensaje/MAC
			try {
				System.err.println("Esperando conexiones de clientes...");
				SSLSocket socket = (SSLSocket) serverSocket.accept();
				
				InputStream inputstream = socket.getInputStream();
				InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
				BufferedReader input = new BufferedReader(inputstreamreader);
				
				
				PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				String text;
				while ((text = input.readLine()) != null) {
					String[] message = text.trim().split(";;");
					String details;
					if (!(details = message[0].trim()).equals(this.usuario)) {
						output.write("Usuario incorrecto" + "\n");
						output.flush();
					} else if (!(details = message[1].trim()).equals(this.password)){
						output.write("Contraseña incorrecta" + '\n');
						output.flush();
					}else{
						output.write("Mensaje recibido" + "\n");
						output.flush();
						// Si se ha recibido el mensaje, se hace la comprobación de la integridad
						// previamente a guardar el mensaje.
						
						String hashCheck = hashFile("mensajes.properties");
						if (VPNServer.hash != null && !VPNServer.hash.equals(hashCheck)) {
							createError("Se ha perdido la integridad del fichero de transacciones");
						}
						// Creamos un fichero properties para guardar las transacciones
						// asociadas con el usuario.
						// Esto se hace como sustituto a una base de datos que tendrían
						// los clientes, por lo que es útil para testear

						Properties prop = new Properties();
						InputStream in = new FileInputStream("mensajes.properties");
						OutputStream out = new FileOutputStream("mensajes.properties");
						prop.load(in);
						prop.setProperty(message[0], message[2]);
						prop.store(out, null);

						VPNServer.hash = hashFile("mensajes.properties");
					}
					// Aseguramos la integridad del fichero de transacciones, si no,
					// creamos un mensaje de error
					
					output.close();
					input.close();
					socket.close();
					break;
				}
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	// ejecucion del servidor
	public static void main(String args[]) throws Exception {
		VPNServer server = new VPNServer();
		server.setExecutionPath();
		server.runServer();
	}

	public String hashFile(String file) {
		String hashed = null;
		String result;
		Path filepath = Paths.get(this.executionPath, file);
		try {
			InputStream fis = new FileInputStream(filepath.toFile());

			byte[] buffer = new byte[8192];
			MessageDigest complete;
			byte[] hash;

			complete = MessageDigest.getInstance("SHA-256");

			int numRead;
			do {
				numRead = fis.read(buffer);
				if (numRead > 0) {
					complete.update(buffer, 0, numRead);
				}
			} while (numRead != -1);
			fis.close();
			hash = complete.digest();
			hashed = this.byteArrayToHexString(hash);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {

		}
		result = file + ": " + hashed;
		return result;

	}

	public static void createError(String error) {

		List<String> lines = new ArrayList<String>();
		Date moment;
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat formatter2 = new SimpleDateFormat("hh:mm");
		String date;
		String time;

		moment = new Date();
		date = formatter.format(moment);
		time = formatter2.format(moment);

		String filename = "errors.txt";
		String executionPath = System.getProperty("user.dir");
		Path file = Paths.get(executionPath, filename);

		try {
			lines = Files.lines(file).collect(Collectors.toList());
		} catch (IOException e) {
			lines.add("Día 				Hora				Tipo de error");
		}
		lines.add(date + "			|	" + time + "			|	" + error);

		try {
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			createError(e.getMessage());
		}

	}

	private static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e",
			"f" };

	// Convierte un byte a una cadena hexadecimal
	public String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	// Convierte un array de bytes a una cadena hexadecimal
	public String byteArrayToHexString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; ++i)
			result += this.byteToHexString(b[i]);
		return result;
	}

	public void setExecutionPath() {
		this.executionPath = System.getProperty("user.dir");
	}
}