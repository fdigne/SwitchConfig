package com.configswitch.entities;

import java.io.Serializable;

import org.snmp4j.PDU;

public class Trap implements Serializable{
	
	String message ;
	PDU pdu ;

	public Trap() {
		super();
	}

	public Trap(PDU pdu) {
		super();
		this.pdu = pdu;
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
	
	

}
