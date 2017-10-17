package com.configswitch.metier;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;

import com.configswitch.entities.InterfaceSwitch;
import com.configswitch.entities.Switch;

public interface IConfigSwitchMetier {
	
	public boolean isReachable(InetAddress adresseSwitch) throws IOException;
	public Switch getSwitchInformations(InetAddress adresseSwitch);
	public Collection<InterfaceSwitch> getListInterfaces(InetAddress adresseSwitch) ;
	public Collection<Switch> getListSwitch();

}
