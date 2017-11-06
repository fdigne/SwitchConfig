package com.configswitch.metier;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.TableEvent;
import org.springframework.stereotype.Service;
import com.configswitch.entities.InterfaceSwitch;
import com.configswitch.entities.Switch;
import com.configswitch.snmp.SNMPManager;

@Service
public class ConfigSwitchMetierImpl implements IConfigSwitchMetier {

	private Switch switche ;

	public static final String SWITCHNAME = ".1.3.6.1.2.1.1.5.0";
	public static final String IFNAME = ".1.3.6.1.2.1.2.2.1.2.";
	public static final String IFNUMBER = ".1.3.6.1.2.1.2.1.0";
	private static final String IFDESCR = ".1.3.6.1.2.1.2.2.1.2";
	private static final String IFSTATUS = ".1.3.6.1.2.1.2.2.1.8.";
	private static final String VLANID = ".1.3.6.1.4.1.9.9.68.1.2.2.1.2.";
	private static final String IFINDEX = ".1.3.6.1.2.1.2.2.1.1.";


	@Override
	public Switch getSwitchInformations(InetAddress adresseSwitch) {
		SNMPManager client = new SNMPManager("udp:"+adresseSwitch.toString()+"/161");
		try {
			client.start();
			String nomSwitch = client.getAsString(new OID(SWITCHNAME));
			switche = new Switch(nomSwitch, adresseSwitch);
			switche.setAdressSwitchString(adresseSwitch.getHostName());
			switche.setListInterfaces(getListInterfaces(adresseSwitch.getHostName()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return switche;
	}

	@Override
	public boolean isReachable(InetAddress adresseSwitch) throws IOException {
		boolean result = adresseSwitch.isReachable(12);

		return result;
	}


	@Override
	public Collection<InterfaceSwitch> getListInterfaces(String adresseSwitch) {

		SNMPManager client = new SNMPManager("udp:"+adresseSwitch.toString()+"/161");
		ArrayList<InterfaceSwitch> liste = new ArrayList<InterfaceSwitch>();

		try {
			client.start();
			List<TableEvent> tableViewSnmp = client.getListInterfaces(new OID(IFDESCR));
			for (TableEvent te : tableViewSnmp) {				
				for(VariableBinding vb : te.getColumns()) {
					String nomInterface = vb.getVariable().toString();
					String typeInterface = getTypeInterface(client.getAsString(new OID(VLANID+te.getIndex())));
					boolean statusInterface = getStatusInterface(client.getAsString(new OID(IFSTATUS+te.getIndex())));
					String ifIndex = client.getAsString(new OID(IFINDEX+te.getIndex()));
					
					
					if(nomInterface.contains("Ethernet")) {
						HashMap<String, String> nomTypeInterface = new HashMap<String, String>();
						InterfaceSwitch interfaceSwitch = new InterfaceSwitch(nomInterface);
						interfaceSwitch.setTypeInterface(typeInterface);
						interfaceSwitch.setStatusInterface(statusInterface);
						interfaceSwitch.setIfIndex(Integer.parseInt(ifIndex));
						nomTypeInterface.put(nomInterface, typeInterface);
						interfaceSwitch.setNomTypeinterface(nomTypeInterface);
						liste.add(interfaceSwitch);
					}
					
				}


			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return liste;

	}

	@Override
	public boolean getStatusInterface(String statusInterfaceSnmp) {
		boolean statusInterface ;
		if (statusInterfaceSnmp.equals("1")) {
			statusInterface = true ;
		}
		else {
			statusInterface = false ;
		}
		return statusInterface;
	}

	@Override
	public String getTypeInterface(String vlanId) {
		String typeInterface;
		if (vlanId.equals("10")) {
			typeInterface = "eclairage" ;
		}
		else if(vlanId.equals("20")) {
			typeInterface = "son" ;

		}
		else if (vlanId.equals("30")){
			typeInterface = "video" ;
		}
		else {
			typeInterface = "defaut";
		}

		return typeInterface ;
	}
	
	@Override
	public int getVlanId(String typeInterface) {
		int vlanId;
		if(typeInterface.equals("eclairage")) {
			vlanId = 10;
		}
		else if(typeInterface.equals("son")) {
			vlanId = 20;
		}
		else if(typeInterface.equals("video")) {
			vlanId = 30;
		}
		else {
			vlanId=1;
		}
		
		return vlanId;
	}

	@Override
	public Collection<Switch> getListSwitch() {
		InetAddress adresseTestee ;
		ArrayList<Switch> listeSwitch = new ArrayList<Switch>();
		
			try {
				
				// ATTENTION : A MODIFIER LA BOUCLE (METTRE i=1 EN CONDITION REELLE)
				for (int i = 2 ; i<255 ; i++) {
					//localAdress[3] = (byte) i ;
					adresseTestee = InetAddress.getByName("172.31.10."+i);
					try {
						if (adresseTestee.isReachable(15)) {
							listeSwitch.add(getSwitchInformations(adresseTestee));
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		return listeSwitch;
	}

	@Override
	public void setVlanConfiguration(String adresseSwitch, int index, int vlanValue ) {
		SNMPManager client = new SNMPManager("udp:"+adresseSwitch.toString()+"/161");
		try {
			client.start();
			client.set(new OID(VLANID+index), new Integer32(vlanValue));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setSwitchName(String adresseSwitch, String nameSwitch) {
		SNMPManager client = new SNMPManager("udp:"+adresseSwitch.toString()+"/161");
		try {
			client.start();
			client.set(new OID(SWITCHNAME), new OctetString(nameSwitch));	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	}
