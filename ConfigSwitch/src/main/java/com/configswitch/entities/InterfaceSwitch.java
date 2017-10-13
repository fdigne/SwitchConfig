package com.configswitch.entities;

import java.io.Serializable;

public class InterfaceSwitch implements Serializable {
	
	private String nomInterface ;
	private String typeInterface ;
	private boolean statusInterface;
	
	
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


	public boolean getStatusInterface() {
		return statusInterface;
	}


	public void setStatusInterface(boolean statusInterface) {
		this.statusInterface = statusInterface;
	}
	
	

}
