package com.configswitch.entities;

import java.io.Serializable;

import org.snmp4j.PDU;

public class Trap implements Serializable{
	
	private String message ;
	private PDU pdu ;
	private String sourceAdress ;
	private String interfaceName ;
	private int typeTrap;
	private String sourceSwitch ;
	private String typeInterface;
	

	public Trap() {
		super();
	}
	
	

	public Trap(String message, String sourceAdress, String interfaceName) {
		super();
		this.message = message;
		this.sourceAdress = sourceAdress;
		this.interfaceName = interfaceName;
	}



	public Trap(PDU pdu) {
		super();
		this.pdu = pdu;
	}
	
	

	public int getTypeTrap() {
		return typeTrap;
	}



	public void setTypeTrap(int typeTrap) {
		this.typeTrap = typeTrap;
	}



	public String getTypeInterface() {
		return typeInterface;
	}



	public void setTypeInterface(String typeInterface) {
		this.typeInterface = typeInterface;
	}



	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public PDU getPdu() {
		return pdu;
	}

	public void setPdu(PDU pdu) {
		this.pdu = pdu;
	}

	

	public String getSourceAdress() {
		return sourceAdress;
	}

	public void setSourceAdress(String sourceAdress) {
		this.sourceAdress = sourceAdress;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}



	public String getSourceSwitch() {
		return sourceSwitch;
	}



	public void setSourceSwitch(String sourceSwitch) {
		this.sourceSwitch = sourceSwitch;
	}
	
	

}
