package com.configswitch.entities;

import java.io.File;
import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.context.annotation.Bean;


public class Switch implements Serializable {
	
	
	private String nameSwitch ;
	private InetAddress adressSwitch;
	private File configSwitch;
	
	
	public Switch() {
		super();
	}


	public Switch(String nameSwitch, InetAddress adresse) {
		super();
		this.nameSwitch = nameSwitch;
		this.adressSwitch = adresse;
	}


	public String getNameSwitch() {
		return nameSwitch;
	}


	public void setNameSwitch(String nameSwitch) {
		this.nameSwitch = nameSwitch;
	}


	public InetAddress getAdressSwitch() {
		return adressSwitch;
	}


	public void setAdressSwitch(InetAddress adressSwitch) {
		this.adressSwitch = adressSwitch;
	}


	public File getConfigSwitch() {
		return configSwitch;
	}


	public void setConfigSwitch(File configSwitch) {
		this.configSwitch = configSwitch;
	}
	
	
	

}
