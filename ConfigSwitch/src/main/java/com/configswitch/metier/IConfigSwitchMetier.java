package com.configswitch.metier;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;

import org.snmp4j.PDU;

import com.configswitch.entities.InterfaceSwitch;
import com.configswitch.entities.Switch;

public interface IConfigSwitchMetier {
	
	public boolean isReachable(InetAddress adresseSwitch) throws IOException;
	public Switch getSwitchInformations(InetAddress adresseSwitch);
	public Collection<InterfaceSwitch> getListInterfaces(InetAddress adresseSwitch) ;
	public Collection<Switch> getListSwitch();
	public int getVlanId(String typeInterface);
	public void setVlanConfiguration(String adresseSwitch, int index, int vlanValue);

}
