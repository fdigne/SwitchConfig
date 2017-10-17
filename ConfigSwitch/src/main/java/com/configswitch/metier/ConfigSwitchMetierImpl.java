package com.configswitch.metier;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.OID;
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
	private static final String IFTYPE = ".1.3.6.1.2.1.2.2.1.3.";
	private static final String IFSTATUS = ".1.3.6.1.2.1.2.2.1.8.";
	private static final String VLANID = ".1.3.6.1.4.1.9.9.68.1.2.2.1.2.";

	@Override
	public Switch getSwitchInformations(InetAddress adresseSwitch) {
		SNMPManager client = new SNMPManager("udp:"+adresseSwitch.toString()+"/161");
		try {
			client.start();
			String nomSwitch = client.getAsString(new OID(SWITCHNAME));
			switche = new Switch(nomSwitch, adresseSwitch);
			switche.setAdressSwitchString(adresseSwitch.getHostName());
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
	public Collection<InterfaceSwitch> getListInterfaces(InetAddress adresseSwitch) {

		SNMPManager client = new SNMPManager("udp:"+adresseSwitch.toString()+"/161");
		ArrayList<InterfaceSwitch> liste = new ArrayList<InterfaceSwitch>();

		try {
			client.start();
			List<TableEvent> tableViewSnmp = client.getListInterfaces(new OID(IFDESCR));
			for (TableEvent te : tableViewSnmp) {				
				for(VariableBinding vb : te.getColumns()) {
					String nomInterface = vb.getVariable().toString();
					String typeInterface = getTypeInterface(client.getAsString(new OID(VLANID+te.getIndex())));
					System.out.println(typeInterface);
					boolean statusInterface = getStatusInterface(client.getAsString(new OID(IFSTATUS+te.getIndex())));
					
					if(nomInterface.contains("Ethernet")) {
						InterfaceSwitch interfaceSwitch = new InterfaceSwitch(nomInterface);
						interfaceSwitch.setTypeInterface(typeInterface);
						interfaceSwitch.setStatusInterface(statusInterface);
						liste.add(interfaceSwitch);
					}
					
				}


			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return liste;

	}

	private boolean getStatusInterface(String statusInterfaceSnmp) {
		boolean statusInterface ;
		if (statusInterfaceSnmp.equals("1")) {
			statusInterface = true ;
		}
		else {
			statusInterface = false ;
		}
		return statusInterface;
	}

	private String getTypeInterface(String vlanId) {
		String typeInterface;
		if (vlanId.equals("10")) {
			typeInterface = "Eclairage" ;
		}
		else if(vlanId.equals("20")) {
			typeInterface = "Son" ;

		}
		else if (vlanId.equals("30")){
			typeInterface = "Video" ;
		}
		else {
			typeInterface = "Defaut";
		}

		return typeInterface ;
	}

	@Override
	public Collection<Switch> getListSwitch() {
		InetAddress adresseTestee ;
		ArrayList<Switch> listeSwitch = new ArrayList<Switch>();
		
			try {
				byte[] localAdress = InetAddress.getLocalHost().getAddress();
				
				
				for (int i = 1 ; i<255 ; i++) {
					//localAdress[3] = (byte) i ;
					adresseTestee = InetAddress.getByName("172.31.10."+i);
					try {
						if (adresseTestee.isReachable(12)) {
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
}
