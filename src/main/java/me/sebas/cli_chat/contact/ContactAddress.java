package me.sebas.cli_chat.contact;

public class ContactAddress {
	public String url;
	public int port;

	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public ContactAddress(String url, int port) {
		this.url = url;
		this.port = port;
	}

	public ContactAddress() {
		super();
	}

	@Override
	public String toString() {
		return "ContactAddress [url=" + url + ", port=" + port + "]";
	}
}
