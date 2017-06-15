/**
 *  Instance to generate a filtered frame from sequencer
 *
 *	@author procsynth - Antoine Pintout
 *	@since  13-02-2016`
 */

package mashine.engine;

import java.io.Serializable;
import java.util.ArrayList;

import mashine.MaShine;
import mashine.Do;
import mashine.scene.Frame;

public class Track implements Serializable{

	private static final long serialVersionUID = 0x1A0F0002L;

	private String name;
	public Sequencer sequencer;
	private ArrayList<Filter> filters;
	private int filterIndex;
	private float opacity;

	private boolean tweaked = false;

	public Track(String name){
		this.name = name;
		sequencer = new Sequencer(name, MaShine.bank.getSequence(0));
		filters = new ArrayList<Filter>();
		opacity = 1f;

		
		MaShine.inputs.registerRange("track."+name+".opacity");
		MaShine.inputs.range("track."+name+".opacity", "_100");

		registerActions();
	}

	public void registerActions(){
		MaShine.inputs.registerAction("track."+name+".tweak.start", new Do(){public void x(){startTweak();}});
		MaShine.inputs.registerAction("track."+name+".tweak.end", new Do(){public void x(){endTweak();}});
		sequencer.registerActions();
	}

	public Frame getFrame(){
		Frame frame = new Frame(sequencer.getFrame());
		for(Filter f : filters){
			if(f.isEnabled()){
				frame = f.filter(frame);
			}
		}
		return frame;
	}

	public float getOpacity(){ 
		opacity = (float)MaShine.inputs.getRange("track."+name+".opacity");
		return opacity;
	}


	public String addFilter(String type){
		if(MaShine.bank.getFilter(type) != null){
			filterIndex = (int) Math.round(MaShine.m.random(0, 1023));
			String name = "track."+this.name+".filter."+hex(filterIndex);
			Filter f = new Filter(name, MaShine.bank.getFilter(type));
			filters.add(f);
			return name+"."+type;
		}
		return null;
	}

	public ArrayList<Filter> getFilters(){return filters;}
	public String getName(){return name;}

	public boolean isTweaked(){return tweaked;}
	public void startTweak(){sequencer.startTweak(); tweaked = true;}
	public void endTweak(){sequencer.endTweak();tweaked = false;}

	public static String hex(int n) {
		return String.format("%5s", Integer.toHexString(n)).replace(' ', '0');
	}
}