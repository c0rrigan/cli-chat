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
	
	public final static int PORT = 9988;
	
	public static void main(String[] args) {
		var sc = new Scanner(System.in);
		var contacts = FileIO.readContacts();
		ChatClientRunner currentChat = null;
		ChatManager chatManager = null;
		try {
			chatManager = new ChatManager(contacts, "me");
			(new Thread(chatManager)).start();
		} catch (IOException e) {
			System.err.println("Error escuchando conexiones ...");
			e.printStackTrace();
		}
		System.out.println("Terminando inicialización ... ");
		for (;;) {
			String comm = sc.nextLine();
			String tokens[] = comm.split("\\s");
//        	for(String s : tokens) {
//        		System.out.println(s);
//        	}
			if (tokens[0].equals("exit")) {
				System.out.println(">exit");
				System.exit(0);
			} else if (tokens[0].contentEquals("list")) {
				System.out.println(">list");
				for (Map.Entry<String, Boolean> e : chatManager.getConnected().entrySet()) {
					System.out.println("[ " + e.getKey() + " ] := " + e.getValue());
				}
			} else if (tokens[0].equals("cd")) {
				if (chatManager.getConnected().containsKey(tokens[1])) {
					currentChat = chatManager.getChatThreads().get(tokens[1]);
				} else {
					System.out.println("Error cambiando a " + tokens[1]);
				}
			} else {
				currentChat.sendMsg(comm);
			}
		}
	}
}
