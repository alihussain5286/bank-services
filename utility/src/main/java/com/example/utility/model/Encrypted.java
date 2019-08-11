package com.example.utility.model;

public class Encrypted {

	private String encryptedValue;
	public Encrypted() {
		super();
	}

	public Encrypted(String encryptedvalue) {
		this.encryptedValue= encryptedvalue;
	}

	public String getEncryptedValue() {
		return encryptedValue;
	}

	public void setEncryptedValue(String encryptedValue) {
		this.encryptedValue = encryptedValue;
	}

}
