
/**
 *  Handle OSC inputs sing OSCP5
 *
 *	@author procsynth - Antoine Pintout
 *	@since  19-10-2016
 */

package mashine.inputs;

import java.util.HashMap;
import java.util.Map;

import mashine.MaShine;
import mashine.Do;

import oscP5.*;
import netP5.*;

public class OSC extends InputSource implements Learnable{

	
	private String lastState;
	private String lastRange;
	private boolean init = false;

	private NetAddress broadcast; 
	private OscP5 oscP5;

	public OSC () {
		super();
		oscP5 = new OscP5(this, 12000);
	}


	public void tick(){
		if(!init){
			init = true;
			// MaShine.inputs.registerAction("mashine.inputs.reload_midi", new Do(){public void x(){rescanDevices(); registerOutputs();}});
			// registerOutputs();
		}

	}

	public void clear(){
		// for(String s : states.keySet()){
		// 	states.put(s, false);
		// }
		// lastRange = null;
		// lastState = null;
	}

	private void registerOutputs(){
		
	}

	public void oscEvent(OscMessage theOscMessage) {
		/* get and print the address pattern and the typetag of the received OscMessage */
		MaShine.m.println("### received an osc message with addrpattern "+theOscMessage.addrPattern()+" and typetag "+theOscMessage.typetag());
		theOscMessage.print();
	}

	public String getLastRange(){

		return lastRange;
	}
	public String getLastState(){
		return lastState;
	}

}