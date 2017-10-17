package com.configswitch.entities;

import java.io.File;
import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Collection;

import org.springframework.context.annotation.Bean;


public class Switch implements Serializable {
	
	
	private String nameSwitch ;
	private InetAddress adressSwitch;
	private Collection<InterfaceSwitch> listInterfaces ;
	private File configSwitch;
	private String adressSwitchString ;
	
	
	public Switch() {
		super();
	}


	public Switch(String nameSwitch, InetAddress adresse) {
		super();
		this.nameSwitch = nameSwitch;
		this.adressSwitch = adresse;
	}


	public Switch(String nameSwitch, String adressSwitchString) {
		super();
		this.nameSwitch = nameSwitch;
		this.adressSwitchString = adressSwitchString;
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


	public Collection<InterfaceSwitch> getListInterfaces() {
		return listInterfaces;
	}


	public void setListInterfaces(Collection<InterfaceSwitch> listInterfaces) {
		this.listInterfaces = listInterfaces;
	}


	public String getAdressSwitchString() {
		return adressSwitchString;
	}


	public void setAdressSwitchString(String adressSwitchString) {
		this.adressSwitchString = adressSwitchString;
	}
	
	
	

}
