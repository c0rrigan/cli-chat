package me.sebas.cli_chat;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import me.sebas.cli_chat.contact.ContactEntry;
import me.sebas.cli_chat.io.FileIO;

public class ChatClientRunner implements Runnable{
	private Socket so;
	/**** Dispositivos IO Texto ***/
	private PrintWriter wr;
	private BufferedReader br;
	/**** Dispositivos IO Binario ***/
	private InputStream bin;
	private OutputStream bout;
	/**** Variables ****/
	private boolean isFocused;
	private String nickname;
	private ArrayDeque<String> history;
	
	/**
	 * Constructor para crear un hilo de cliente de chat que realizá la conexión para el socket utilizando una entrada de contacto
	 * @param contact
	 * @throws NoRouteToHostException
	 * @throws PortUnreachableException
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public ChatClientRunner(ContactEntry contact) throws NoRouteToHostException, PortUnreachableException, UnknownHostException, IOException {
		//so = new Socket(contact.getAddr().getUrl(), contact.getAddr().getPort());
		so = new Socket();
		so.connect(new InetSocketAddress(contact.getAddr().getUrl(), contact.getAddr().getPort()), 3000);
		System.out.println("Nueva conexión (launcher) : " + so.getInetAddress().toString());		
		nickname = contact.getNickname();
		init(so);
	}
	/**
	 * Constructor para crear un hilo de cliente de chat que utilizá un socket conectado
	 * @param socket
	 * @param contact
	 * @throws NoRouteToHostException
	 * @throws PortUnreachableException
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public ChatClientRunner(Socket socket, String nickname) throws IOException {
		System.out.println("Nuevo socket con conexión ...");
		so = socket;
		this.nickname = nickname;
		init(so);
	}
	/**
	 * Inicializar los objetos de IO e historia del chat
	 * @param so Socket con la conexión del chat
	 * @throws IOException Error creando streams
	 */
	private void init(Socket so) throws IOException {
		wr = new PrintWriter(so.getOutputStream(), true);
		br = new BufferedReader(new InputStreamReader(so.getInputStream()));
		bout = so.getOutputStream();
		bin = so.getInputStream();
		//bout = so.getOutputStream();
		history = new ArrayDeque<String>();		
	}
	/**
	 * Enviar mensagee msg con el socket asignado al hilo
	 * @param msg Mensaje de texto
	 */
	public void sendMsg(String msg) {
		wr.println(msg);
	}
	
	@Override
	public void run() {
		String msg;
		for(;;) {
			try {
				/* Leer recibir mensajes, guardarlos y si esta observando el canal, presentarlos */
				if(br.ready()) {
					msg = br.readLine();
					if(msg.equals("file")) {
						System.out.println("Recibiendo archivo ...");
						var filename  = br.readLine();
						System.out.println("Write file : " + filename);
						var fileSize = Integer.parseInt(br.readLine());
						System.out.println("File size : " + fileSize);
						readFile(filename, fileSize);
					}else {
						history.push(msg);
						if(isFocused) {
							System.out.printf("[%s] %s\n", nickname, msg);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public String toString() {
		return "ChatClientRunner [isFocused=" + isFocused + ", nickname=" + nickname + "]";
	}
	
	private void readFile(String filename, int fileSize) {
		var stream = new ByteArrayOutputStream();
		//var buff = new byte[1000];
		int bytesIn = 0, b = 0;
		try {
//			while((bytesIn = bin.read(buff)) > 0) {
//				System.out.println("Read : " + bytesIn + "bytes");
//				if(bytesIn == buff.length) {
//					stream.write(buff);
//				}else {
//					stream.write(buff, 0, bytesIn);
//				}
//			}
			while(bytesIn < fileSize) {
				//System.out.println("BytesIn : " + bytesIn);
				b = bin.read();
				stream.write(b);
				bytesIn++;
			}
			System.out.println("Stream size " + stream.size());
			FileIO.writeToFile(stream.toByteArray(), filename);
		} catch (IOException e) {
			System.err.println("Error leyendo archivo de socket [file : " + filename + "]");
			e.printStackTrace();
		}
	}
	
	public void sendFile(Path path) {
		System.out.println("Enviado archivo" + path.toString() + "... ");
		try {
			var fileBytes = Files.readAllBytes(path);
			/* Avisar que se manda archivo */
			wr.println("file");
			wr.println(path.getFileName());
			wr.println(fileBytes.length);
			//wr.flush();
//			for(byte b : fileBytes) {
//				bout.write(b);
//			}
			//bout.write(3);
			bout.write(fileBytes);
		} catch (IOException e) {
			System.err.println("Error enviando archivo : " + path.toString());
			e.printStackTrace();
		}
	}
	
	public boolean isFocused() {
		return isFocused;
	}

	public void setFocused(boolean isFocused) {
		this.isFocused = isFocused;
	}
	
	public BufferedReader getInputStream() {
		return br;
	}


	public ArrayDeque<String> getHistory() {
		return history;
	}
}
