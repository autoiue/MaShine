/**
 *  Instance to visualize devices of the scene
 *
 *	@author procsynth - Antoine Pintout
 *	@since  13-02-2016`
 */

package mashine.ui.boxes;

import mashine.*;
import mashine.ui.*;
import mashine.ui.elements.*;
import mashine.scene.*;
import mashine.scene.features.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.ArrayList;

public class SceneVisualizer extends Drawable {

	LinkedHashMap<String,DeviceElement> deviceElements;
	HashMap<String,Device> selectedDevices;

	public SceneVisualizer(MaShine m){
		super(m, 10, 50, m.width - 10, m.height - 50);
		deviceElements = new LinkedHashMap<String, DeviceElement>();
		selectedDevices = new HashMap<String, Device>();
	}

	public void drawContent(){

		canvas.background(55, 71, 79);

		Frame frame = new Frame();

		HashMap<String,Device> devices = M.scene.getDevices();
		HashMap<String,EditableFeature> frameFeatures = frame.getFeatures();

		for(String deviceIdentifier : devices.keySet()){
			// add new DeviceElement if not already present
			if(!deviceElements.containsKey(deviceIdentifier)){
				deviceElements.put(deviceIdentifier, new DeviceElement(this, devices.get(deviceIdentifier)));
				M.println("Adding "+deviceIdentifier);
			}

			DeviceElement de = deviceElements.get(deviceIdentifier);

			// selected it maybe
			boolean selected = selectedDevices.containsKey(deviceIdentifier);

			if(M.inputs.getState("keyboard.16.hold") && de.isClicked()){
				if(selected){
					selectedDevices.remove(deviceIdentifier);
					selected = false;
				}else{
					selectedDevices.put(deviceIdentifier, devices.get(deviceIdentifier));
					selected = true;				
				}
			}else if(de.isClicked()){
				selectedDevices.clear();
				selected = true;
				selectedDevices.put(deviceIdentifier, devices.get(deviceIdentifier));
			}

			// draw it
			de.setSelected(selected);
			de.setFrameFeatures(frameFeatures);
			de.draw();
		}

		// remove DeviceElement if Device not here anymore

		// Iterator<String> dei = deviceElements.keySet().iterator();

		// while(dei.hasNext()){
		// 	String deviceIdentifier = dei.next();
		// 	if(!devices.containsKey(deviceIdentifier)){
		// 		deviceElements.remove(deviceIdentifier);
		// 	}
		// }

		for(Iterator<Map.Entry<String, DeviceElement>> it = deviceElements.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<String, DeviceElement> entry = it.next();
			if(!devices.containsKey(entry.getKey())) {
				it.remove();
			}
		}

	}

	public HashMap<String,Device> getSelectedDevices(){
		return selectedDevices;
	}

	public void setSelectedDevices(HashMap<String, Device> newSelection){
		selectedDevices = newSelection;
	}


	public void renameDevice(String oldId, String newId){
		deviceElements.put(newId, deviceElements.remove(oldId));
	}

}