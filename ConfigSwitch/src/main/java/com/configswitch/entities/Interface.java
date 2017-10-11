package com.configswitch.entities;

import java.io.Serializable;

public class Interface implements Serializable {
	
	private String nomInterface ;
	private String typeInterface ;
	
	
	public Interface() {
		super();
	}


	public Interface(String nomInterface) {
		super();
		this.nomInterface = nomInterface;
	}


	public String getNomInterface() {
		return nomInterface;
	}


	public void setNomInterface(String nomInterface) {
		this.nomInterface = nomInterface;
	}


	public String getTypeInterface() {
		return typeInterface;
	}


	public void setTypeInterface(String typeInterface) {
		this.typeInterface = typeInterface;
	}
	
	

}
