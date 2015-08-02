package com.softserve.edu.dto.application;

public class ClientMailDTO {
	private String verifID;

	private String msg;
	private String name;

	private String surname;
	private String email;

	public String getName() {
		return name;
	}



	public ClientMailDTO(){}

	public ClientMailDTO(String verifID, String msg) {
		super();
		this.verifID = verifID;
		this.msg = msg;
	}

	public ClientMailDTO(String verifID, String msg, String name, String surname, String email) {
		super();
		this.verifID = verifID;
		this.msg = msg;
		this.name = name;
		this.surname = surname;
		this.email = email;
	}

	public String getVerifID() {
		return verifID;
	}

	public void setVerifID(String verifID) {
		this.verifID = verifID;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setName(String name) {this.name = name;}

	public String getSurname() {return surname;	}

	public void setSurname(String surname) {this.surname = surname;	}

	public String getEmail() { 	return email;}

	public void setEmail(String email) {this.email = email;	}
}
