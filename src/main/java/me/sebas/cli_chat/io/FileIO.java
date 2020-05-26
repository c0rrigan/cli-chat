package me.sebas.cli_chat.io;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
	/**
	 * Leer el archivo ubicado en la dirección 'path' y regresar sus contenidos en un arreglo de bytes
	 * @param path Dirección del archivo
	 * @return Arreglo de bytes
	 */
	public static byte[] readFile(Path path) {
		byte[] barray = null;
		try {
			barray = Files.readAllBytes(path);
		} catch (IOException e) {
			System.err.println("Error leyendo el archivo : " + path.toString());
			e.printStackTrace();
		}
		return barray;
	}
	public static InputStream readFileStream(Path path) {
		InputStream is = null;
		try {
			is = Files.newInputStream(path, StandardOpenOption.READ);
		} catch (IOException e) {
			System.err.println("Error leyendo el archivo : " + path.toString());
			e.printStackTrace();
		}
		return is;
	}
	/**
	 * Escribir el arreglo de bytes data en un archivo con el nombre filename
	 * @param data Arreglo de bytes
	 * @param path Cadena con el nombre del archivo
	 */
	public static void writeToFile(byte[] data, String filename) {
		try {
			var path = Paths.get(System.getProperty("user.dir"), "rec_copy_" + filename);
			var writer = new BufferedOutputStream(Files.newOutputStream(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE));
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			System.err.println("Erro escribiendo : " + filename);
			e.printStackTrace();
		}
		
	}
}
