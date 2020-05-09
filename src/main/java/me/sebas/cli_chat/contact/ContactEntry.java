package me.sebas.cli_chat.contact;

public class ContactEntry {
	public String nickname;
	public ContactAddress addr;
	
	/**
	 * Constructor de ContactEntry
	 * @param nickname
	 * @param addr 
	 */
	public ContactEntry(String nickname, ContactAddress addr) {
		super();
		this.nickname = nickname;
		this.addr = addr;
	}
	/**
	 * Constructor de ContactEntry con url y port
	 * @param nickname
	 * @param url
	 * @param port
	 */
	public ContactEntry(String nickname, String url, int port) {
		super();
		this.nickname = nickname;
		this.addr = new ContactAddress(url, port);
	}
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public ContactAddress getAddr() {
		return addr;
	}
	public void setAddr(ContactAddress addr) {
		this.addr = addr;
	}
	
	@Override
	public String toString() {
		return "ContactEntry [nickname=" + nickname + ", addr=" + addr.toString() + "]";
	}
	
}
