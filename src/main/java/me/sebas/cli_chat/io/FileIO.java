package me.sebas.cli_chat.io;

import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import me.sebas.cli_chat.contact.ContactEntry;

public class FileIO {
	private static Gson gson = new Gson();
	/**
	 * Escribir la lista de contacts en el archivo {@literal contacts.json}
	 * @param contacts Lista con objectos ContactEntry
	 */
	public static void writeContacts(ArrayList<ContactEntry> contacts) {
		String json = gson.toJson(contacts);
		writePlainText(Paths.get(System.getProperty("user.dir"), "contacts.json"), json);
	}
	/**
	 * Lee los contactos del archivo {@literal contacts.json} y los deserializa en una lista de objetos ContactEntry
	 * @return ArrayList de ContactEntry
	 */
	public static ArrayList<ContactEntry> readContacts(String file) {
		String json = readPlainText(Paths.get(System.getProperty("user.dir"), file));
		ArrayList<ContactEntry> contacts = gson.fromJson(json, new TypeToken<ArrayList<ContactEntry>>() {}.getType());
		return contacts;
	}
	/**
	 * Escribe la cadena text en un archivo localizado en la dirección representada por path
	 * @param path Una dirección para guardar el archivo de texto
	 * @param text Una cadena de texto
	 */
	private static void writePlainText(Path path, String text) {
		try {
			var writer = Files.newBufferedWriter(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
			writer.write(text);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Leer la cadena de texto simple del archivo path
	 * @param path Dirección del archivo de texto
	 * @return Cadena de texto de path
	 */
	private static String readPlainText(Path path) {
		String line, text = "";
		try {
			var reader = Files.newBufferedReader(path);
			while((line = reader.readLine()) != null) {
				text = text + line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}
}
