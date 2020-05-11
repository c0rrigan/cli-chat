package me.sebas.cli_chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import me.sebas.cli_chat.contact.ContactEntry;

public class ChatClientRunner implements Runnable{
	private Socket so;
	private PrintWriter wr;
	private BufferedReader br;
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
		System.out.println("Nueva conexión : " + so.getInetAddress().toString());
		wr = new PrintWriter(so.getOutputStream(), true);
		br = new BufferedReader(new InputStreamReader(so.getInputStream()));
		history = new ArrayDeque<String>();		
		nickname = contact.getNickname();
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
		so = socket;
		wr = new PrintWriter(so.getOutputStream(), true);
		br = new BufferedReader(new InputStreamReader(so.getInputStream()));
		history = new ArrayDeque<String>();		
		this.nickname = nickname;
	}
	/**
	 * Enviar mensagee msg con el socket asignado al hilo
	 * @param msg Mensaje de texto
	 */
	public void sendMsg(String msg) {
		wr.println(msg);
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
	
	@Override
	public void run() {
		String msg;
		for(;;) {
			try {
				/* Leer recibir mensajes, guardarlos y si esta observando el canal, presentarlos */
				if(br.ready()) {
					msg = br.readLine();
					history.push(msg);
					if(isFocused) {
						System.out.printf("[%s] %s\n", nickname, msg);
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
}
