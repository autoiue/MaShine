/**
 *  Instance for handling the Korg NanoKontrol2
 *
 *  @author procsynth - Antoine Pintout
 *  @since  11-01-2018`
 */

package mashine.inputs.midi_devices;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class KorgNanoPad2 extends MidiDevice{

    public KorgNanoPad2(){
    	super();

    	deviceName = "nanoPAD2";

        Map<Integer, String> pad = new HashMap<Integer, String>();
        pad.put(36, "scene1.1");
        pad.put(37, "scene1.2");
        pad.put(38, "scene1.3");
        pad.put(39, "scene1.4");
        pad.put(40, "scene1.5");
        pad.put(41, "scene1.6");
        pad.put(42, "scene1.7");
        pad.put(43, "scene1.8");
        pad.put(44, "scene1.9");
        pad.put(45, "scene1.10");
        pad.put(46, "scene1.11");
        pad.put(47, "scene1.12");
        pad.put(48, "scene1.13");
        pad.put(49, "scene1.14");
        pad.put(50, "scene1.15");
        pad.put(51, "scene1.16");

        pad.put(36+16, "scene2.1");
        pad.put(37+16, "scene2.2");
        pad.put(38+16, "scene2.3");
        pad.put(39+16, "scene2.4");
        pad.put(40+16, "scene2.5");
        pad.put(41+16, "scene2.6");
        pad.put(42+16, "scene2.7");
        pad.put(43+16, "scene2.8");
        pad.put(44+16, "scene2.9");
        pad.put(45+16, "scene2.10");
        pad.put(46+16, "scene2.11");
        pad.put(47+16, "scene2.12");
        pad.put(48+16, "scene2.13");
        pad.put(49+16, "scene2.14");
        pad.put(50+16, "scene2.15");
        pad.put(51+16, "scene2.16");

        pad.put(36+32, "scene3.1");
        pad.put(37+32, "scene3.2");
        pad.put(38+32, "scene3.3");
        pad.put(39+32, "scene3.4");
        pad.put(40+32, "scene3.5");
        pad.put(41+32, "scene3.6");
        pad.put(42+32, "scene3.7");
        pad.put(43+32, "scene3.8");
        pad.put(44+32, "scene3.9");
        pad.put(45+32, "scene3.10");
        pad.put(46+32, "scene3.11");
        pad.put(47+32, "scene3.12");
        pad.put(48+32, "scene3.13");
        pad.put(49+32, "scene3.14");
        pad.put(50+32, "scene3.15");
        pad.put(51+32, "scene3.16");

        pad.put(36+48, "scene4.1");
        pad.put(37+48, "scene4.2");
        pad.put(38+48, "scene4.3");
        pad.put(39+48, "scene4.4");
        pad.put(40+48, "scene4.5");
        pad.put(41+48, "scene4.6");
        pad.put(42+48, "scene4.7");
        pad.put(43+48, "scene4.8");
        pad.put(44+48, "scene4.9");
        pad.put(45+48, "scene4.10");
        pad.put(46+48, "scene4.11");
        pad.put(47+48, "scene4.12");
        pad.put(48+48, "scene4.13");
        pad.put(49+48, "scene4.14");
        pad.put(50+48, "scene4.15");
        pad.put(51+48, "scene4.16");
       
        PAD = Collections.unmodifiableMap(pad);

        Map<Integer, String> range = new HashMap<Integer, String>();
      	range.put(1, "x");
        range.put(2, "y");
        range.put(3, "z");
    
        RANGE = Collections.unmodifiableMap(range);
    }
}