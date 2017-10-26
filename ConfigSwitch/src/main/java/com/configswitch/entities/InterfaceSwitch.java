package com.configswitch.entities;

import java.io.Serializable;
import java.util.HashMap;

public class InterfaceSwitch implements Serializable {
	
	private String nomInterface ;
	private String typeInterface ;
	private boolean statusInterface;
	private int ifIndex ;
	private HashMap<String, String> nomTypeinterface ;
	
	
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

	
	public HashMap<String, String> getNomTypeinterface() {
		return nomTypeinterface;
	}


	public void setNomTypeinterface(HashMap<String, String> nomTypeinterface) {
		this.nomTypeinterface = nomTypeinterface;
	}


	public boolean getStatusInterface() {
		return statusInterface;
	}


	public void setStatusInterface(boolean statusInterface) {
		this.statusInterface = statusInterface;
	}


	public int getIfIndex() {
		return ifIndex;
	}


	public void setIfIndex(int ifIndex) {
		this.ifIndex = ifIndex;
	}
	
	

}
