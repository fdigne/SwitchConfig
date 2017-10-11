package com.configswitch.snmp;


import java.io.IOException;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.PDUFactory;
import org.snmp4j.util.TableUtils;

public class SNMPManager implements Runnable {

	Snmp snmp = null;
	String address = null;

	/**
	* Constructor
	* @param add
	*/
	public SNMPManager(String add)
	{
	address = add;
	//System.out.println(address);
	}

	
	/**
	* Start the Snmp session. If you forget the listen() method you will not
	* get any answers because the communication is asynchronous
	* and the listen() method listens for answers.
	* @throws IOException
	*/
	public void start() throws IOException {
	TransportMapping transport = new DefaultUdpTransportMapping();
	snmp = new Snmp(transport);
	transport.listen();
	}

	/**
	* Method which takes a single OID and returns the response from the agent as a String.
	* @param oid
	* @return
	* @throws IOException
	*/
	public String getAsString(OID oid) throws IOException  {
		
	 ResponseEvent event;
	 String eventString ;
	
		
					event = get(new OID[] { oid });
					if (event.getResponse() != null){
						eventString = event.getResponse().get(0).getVariable().toString();
					}
					else {
						eventString = new String("Information non disponible");
					}
		
	return eventString;
	}
	
	/**
	* This method is capable of handling multiple OIDs
	* @param oids
	* @return
	* @throws IOException
	*/
	public ResponseEvent get(OID oids[]) throws IOException {
	PDU pdu = new PDU();
	for (OID oid : oids) {
	pdu.add(new VariableBinding(oid));
	}
	pdu.setType(PDU.GET);
	ResponseEvent event = snmp.send(pdu, getTarget(), null);
	if(event != null) {
	return event;
	}
	throw new RuntimeException("GET timed out");
	}

	/**
	* This method returns a Target, which contains information about
	* where the data should be fetched and how.
	* @return
	*/
	private Target getTarget() {
	Address targetAddress = GenericAddress.parse(address);
	CommunityTarget target = new CommunityTarget();
	target.setCommunity(new OctetString("public"));
	target.setAddress(targetAddress);
	target.setRetries(2);
	target.setTimeout(150);
	target.setVersion(SnmpConstants.version2c);
	return target;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
				try {
					this.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

	

	}
