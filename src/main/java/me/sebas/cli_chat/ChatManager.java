package me.sebas.cli_chat;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import me.sebas.cli_chat.contact.ContactEntry;

public class ChatManager implements Runnable{
	
	private ServerSocket servSo;
	
	public HashMap<String, Boolean> connected;
	//public HashMap<String, ChatBean> chatData;
	private HashMap<String, ChatClientRunner> chatThreads;


	public ChatManager(ArrayList<ContactEntry> contacts,String nickname, int bindPort) throws IOException {
		connected = new HashMap<String,Boolean>();
		chatThreads = new HashMap<String,ChatClientRunner>();
		/* Tratar de conectarse con todos los usuaros en 'contacts' */
		launchClients(contacts);
		/* Inicializar el socket del serivdor */
		servSo = new ServerSocket(bindPort);
		//chatData = new HashMap<String, ChatBean>();
		//chatThreads = new HashMap<String,ChatClientRunner>();
		
	}
	
	public void launchClients(ArrayList<ContactEntry> contacts) {
		ChatClientRunner runner;
		for(ContactEntry c : contacts) {
			try {
				/* Lanzar un hilo por cada contacto en la lista */
				//connectedContacts.put(c.getNickname(), true);
				//(new Thread(new ChatClientRunner(c.getAddr().getUrl(), c.getAddr().getPort()))).start();
				runner = new ChatClientRunner(c);
				(new Thread(runner)).start();
				chatThreads.put(c.getNickname(), runner);
				connected.put(c.getNickname(), true);
			} catch (NoRouteToHostException e) {
				System.err.println("La dirección no puede ser accesada");
				connected.put(c.getNickname(), false);
			} catch(PortUnreachableException e) {
				System.err.println("El puerto no puede ser accesado");
				connected.put(c.getNickname(), false);
			} catch(UnknownHostException e) {
				System.err.println("La dirección IP no puede ser accesada");
				connected.put(c.getNickname(), false);
			} catch(IOException e) {
				connected.put(c.getNickname(), false);
				e.printStackTrace();
			}
		}
	}
	
	public HashMap<String, ChatClientRunner> getChatThreads() {
		return chatThreads;
	}
	
	public HashMap<String, Boolean> getConnected() {
		return connected;
	}
	
	/*	Esperar conexiones de otros clientes */
	@Override
	public void run() {
		Socket so;
		ChatClientRunner runner;
		String nick;
		System.out.println("Accepting connections ...");
		for(;;) {
			try {
				so = servSo.accept();
				nick = so.getInetAddress().getHostAddress();
				runner = new ChatClientRunner(so, nick);
				connected.put(nick, true);
			} catch (IOException e) {
				System.err.println("Error while listening ...");
				e.printStackTrace();
			}
		}
	}
}
