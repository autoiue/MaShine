/**
 *  Mother class to generate a frame from a sequence
 *
 *	@author procsynth - Antoine Pintout
 *	@since  13-02-2016`
 */

package mashine.engine;

import java.io.Serializable;

import mashine.Do;
import mashine.MaShine;
import mashine.scene.Frame;
import mashine.scene.Sequence;

public class Sequencer implements Serializable{

	private static final long serialVersionUID = 0x53050001L;

	private String name;
	private Sequence sequence;
	private boolean tweaking = false;
	private boolean manual = false;
	private boolean loop = true;
	private int index;
	private boolean crossFrameState = true;

	public Sequencer(String name, Sequence sequence){
		this.name = name;
		this.sequence = sequence;
		this.index = 0;

		MaShine.inputs.link("sequencer."+name+".forward.auto", "minim.beat.interpolated");

		MaShine.inputs.registerRange("sequencer."+name+".cross");
		registerActions();
	}

	public void registerActions(){
		MaShine.inputs.registerAction("sequencer."+name+".manual.start", new Do(){public void x(){manual = true;}});
		MaShine.inputs.registerAction("sequencer."+name+".manual.end", new Do(){public void x(){manual = false;}});

		MaShine.inputs.registerAction("sequencer."+name+".reset", new Do(){public void x(){setIndex(0);}});
		MaShine.inputs.registerAction("sequencer."+name+".loop.toggle", new Do(){public void x(){loop = !loop;}});
		MaShine.inputs.registerAction("sequencer."+name+".loop.on", new Do(){public void x(){loop = true;}});
		MaShine.inputs.registerAction("sequencer."+name+".loop.off", new Do(){public void x(){loop = false;}});

		MaShine.inputs.registerAction("sequencer."+name+".forward.manual", new Do(){public void x(){if(manual)setIndex(index +1);}});
		MaShine.inputs.registerAction("sequencer."+name+".backward.manual", new Do(){public void x(){if(manual)setIndex(index -1);}});

		MaShine.inputs.registerAction("sequencer."+name+".forward.auto", new Do(){public void x(){if(!manual)setIndex(index +1);}});
		MaShine.inputs.registerAction("sequencer."+name+".backward.auto", new Do(){public void x(){if(!manual)setIndex(index -1);}});
	}

	public void setIndex(int v){
		index = calcIndex(v);
	};

	private int calcIndex(int v){
		if(v < 0){
			v = sequence.getSize() - 1;
		}else if(v >= sequence.getSize()){
			if(loop)
				v = 0;
			else
				v = sequence.getSize() - 1;
		}
		return v;
	}

	public Frame getFrame(){
		float crossFrame = (float)MaShine.inputs.getRange("sequencer."+name+".cross");

		if(crossFrame == 0f || crossFrame == 1f){
			if(!crossFrameState && crossFrame == 1f || crossFrameState && crossFrame == 0f){
				setIndex(index + 1);
				crossFrameState = !crossFrameState;
			}
			return sequence.getFrame(index);
		}else{
			return Frame.mix((!crossFrameState ? crossFrame : 1f - crossFrame) , sequence.getFrame(index), sequence.getFrame(calcIndex(index + 1)));
		}
	}

	public Sequence getSequence(){
		return sequence;
	}
	public void setSequence(Sequence sequence){
		this.sequence = sequence;
		setIndex(0);
	}

	public String getName(){
		return name;
	}

	public int getIndex(){return index;}

	public void startTweak(){
		MaShine.ui.open("SequenceSelector");
		tweaking = true;
	}
	public void endTweak(){
		MaShine.ui.close("SequenceSelector");
		tweaking = false;
	}

	public boolean isTweaked(){
		return tweaking;
	}

	public boolean isManual(){
		return manual;
	}

}