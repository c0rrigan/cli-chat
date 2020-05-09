package me.sebas.cli_chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatServerRunner implements Runnable{
	private PrintWriter sockOut;
	private BufferedReader sockIn;
	
	public ChatServerRunner(Socket sock) throws IOException {
		this.sockOut = new PrintWriter(sock.getOutputStream(), true);
		this.sockIn = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	}
	
	@Override
	public void run() {
		try {
			for(;;) {
				if(sockIn.ready()) {
					System.out.println(sockIn.readLine());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
