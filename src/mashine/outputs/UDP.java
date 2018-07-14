/**
 *  Output to Internet
 *
 *	@author procsynth - Antoine Pintout
 *	@since  13-02-2016`
 */

package mashine.outputs;

import java.io.*;
import java.net.*;
import java.util.*;

import mashine.MaShine;
import mashine.scene.Frame;
import mashine.scene.Device;
import mashine.scene.features.EditableFeature;
import mashine.scene.features.Feature;

public class UDP extends Output {

	private InetAddress remote;
	private DatagramSocket socket;
	private final Integer port = 1935;

	public UDP() {
		try{
			remote = InetAddress.getByName("127.0.0.1");
			ports = new HashMap<Integer,String>();
			socket = new DatagramSocket();
			ports.put(port, "UDP:"+port);
		}catch (Exception e) {e.printStackTrace();}
	}

	public void push(Frame frame){
		if(socket != null){	

			HashMap<Integer,short[]> dmxData = new HashMap<Integer,short[]>();
			List<Device> devices = MaShine.scene.getDevices();

			// build arrays

			for(Device d : devices){
				int u = d.getUniverse();
				int i = d.getStartAddress();

				for(Feature f : d.getFeatures()){
					// look in the frame
					if(f instanceof EditableFeature){
						Feature ff = frame.getFeature(d, f);
						if(null == ff){
							for(int j = 0; j < f.getFootprint(); j++){
								sendPacket(u, i-1, (short)0);
								//dmxData.get(u)[i-1] = 0;
								i++;
							}
						}else{
							for(short v : ff.toArray()){
								sendPacket(u, i-1, v);
								//dmxData.get(u)[i-1] = v;
								i++;
							}
						}
						// look in the device
					}else{
						for(short v : f.toArray()){
							sendPacket(u, i-1, v);
							//dmxData.get(u)[i-1] = v;
							i++;
						}
					}
				}
				
			}

			// // send array to OLA
			// try{			
			// 	for(Integer u : dmxData.keySet()){
			// 		ola.streamDmx(u, dmxData.get(u));
			// 	}
			// }catch(Exception e){
			// 	ola = null;
			// 	MaShine.inputs.setState("internal.ola.status", false);
			// 	MaShine.ui.status.set("OLA", "disconnected");
			// }

		}
	}

	private void sendPacket(int universe, int address, short value){
		byte[] buffer = new byte[3];
		buffer[0] = (byte) universe;
		buffer[1] = (byte) address;
		buffer[2] = (byte) value;
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, remote, port);
		try{
			socket.send(packet);
		}catch (Exception e) {e.printStackTrace();}
	}
}
