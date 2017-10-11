package com.configswitch.metier;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;

import org.snmp4j.smi.OID;
import org.springframework.stereotype.Service;

import com.configswitch.entities.InterfaceSwitch;
import com.configswitch.entities.Switch;
import com.configswitch.snmp.SNMPManager;

@Service
public class ConfigSwitchMetierImpl implements IConfigSwitchMetier {

	private Switch switche ;

	public static final String IFNAME = ".1.3.6.1.2.1.2.2.1.2.";
	public static final String IFNUMBER = ".1.3.6.1.2.1.2.1.0";

	@Override
	public Switch getSwitchInformations(InetAddress adresseSwitch) {

		switche = new Switch(adresseSwitch.getHostName(), adresseSwitch);
		return switche;
	}

	@Override
	public boolean isReachable(InetAddress adresseSwitch) throws IOException {
		boolean result = adresseSwitch.isReachable(10);

		return result;
	}


	@Override
	public Collection<InterfaceSwitch> getListInterfaces(InetAddress adresseSwitch) {

		SNMPManager client = new SNMPManager("udp:"+adresseSwitch.toString()+"/161");
		ArrayList<InterfaceSwitch> liste = new ArrayList<InterfaceSwitch>();

		try {
			client.start(); 
			int ifNumber = Integer.parseInt(client.getAsString(new OID(IFNUMBER)));
			System.out.println(ifNumber);

			for (int i=0 ; i<= ifNumber ; i++) {
				liste.add(new InterfaceSwitch(client.getAsString(new OID(IFNAME+i))));

			}

			int compteur = 0;
			for (InterfaceSwitch i : liste) {
				if(compteur <= liste.size()/2) {
					i.setTypeInterface("Eclairage");
					compteur ++;
				}
				else {
					i.setTypeInterface("Video");
					compteur ++ ;
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return liste;

	}

}
