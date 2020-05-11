package me.sebas.cli_chat;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import me.sebas.cli_chat.ChatClientRunner;
import me.sebas.cli_chat.ChatManager;
import me.sebas.cli_chat.io.FileIO;

/**
 * Hello world!
 *
 */
public class App {
	
	//public final static int PORT = 9988;
	
	public static void main(String[] args) {
		System.out.println("cli-chat v0.1");
		if(args.length < 2) {
			System.out.println("Saliendo ... falta argumentos");
			System.exit(0);
		}
		var port = Integer.parseInt(args[0]);
		var sc = new Scanner(System.in);
		var contacts = FileIO.readContacts(args[1]);
		ChatClientRunner currentChat = null;
		ChatManager chatManager = null;
		try {
			chatManager = new ChatManager(contacts, "me", port);
			(new Thread(chatManager)).start();
		} catch (IOException e) {
			System.err.println("Error escuchando conexiones ...");
			e.printStackTrace();
		}
		System.out.println("Terminando inicialización ... ");
		System.out.println("Linea de comandos...ok");
		for (;;) {
			String comm = sc.nextLine();
			String tokens[] = comm.split("\\s");
//        	for(String s : tokens) {
//        		System.out.println(s);
//        	}
			if (tokens[0].equals("exit")) {
				System.out.println(">exit");
				sc.close();
				System.exit(0);
			} else if (tokens[0].contentEquals("list")) {
				System.out.println(">list");
				for (Map.Entry<String, Boolean> e : chatManager.getConnected().entrySet()) {
					System.out.println("[ " + e.getKey() + " ] := " + e.getValue());
				}
			} else if (tokens[0].contentEquals("tlist")) {
				System.out.println(">thread list");
				for (Map.Entry<String, ChatClientRunner> e : chatManager.getChatThreads().entrySet()) {
					System.out.println("[ " + e.getKey() + " ] := " + e.getValue());
				}	
			} else if (tokens[0].contentEquals("hist")) {
				System.out.println(">chat history");
				for(String m : currentChat.getHistory()) {
					System.out.println(m);
				}
			} else if (tokens[0].equals("cd")) {
				if (chatManager.getConnected().containsKey(tokens[1])) {
					if(currentChat != null)
						currentChat.setFocused(false);
					currentChat = chatManager.getChatThreads().get(tokens[1]);
					currentChat.setFocused(true);
				} else {
					System.out.println("Error cambiando a " + tokens[1]);
				}
			} else {
				if(currentChat != null) {
					currentChat.sendMsg(comm);
				}else {
					System.out.println("No existe sesión activa");
				}
			}
		}
	}
}
