package com.configswitch.entities;

import java.io.Serializable;

public class InterfaceSwitch implements Serializable {
	
	private String nomInterface ;
	private String typeInterface ;
	
	
	public InterfaceSwitch() {
		super();
	}


	public InterfaceSwitch(String nomInterface) {
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
