/**
 *  UI managing instance
 *
 *	@author procsynth - Antoine Pintout
 *	@since  13-02-2016`
 */

package mashine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

import mashine.scene.Device;
import mashine.scene.Frame;
import mashine.scene.Sequence;
import mashine.ui.FlatColor;
import mashine.ui.Focusable;
import mashine.ui.Status;
import mashine.ui.boxes.ColorPalette;
import mashine.ui.boxes.DataViewer;
import mashine.ui.boxes.DeviceEditor;
import mashine.ui.boxes.EngineView;
import mashine.ui.boxes.EventViewer;
import mashine.ui.boxes.Linker;
import mashine.ui.boxes.Menu;
import mashine.ui.boxes.SceneView;
import mashine.ui.boxes.SequenceEditor;
import mashine.ui.boxes.SequenceSelector;
import mashine.ui.boxes.FilterSelector;
import processing.core.PFont;

public class UI{

	private Menu menu;
	private HashMap<String,Focusable> uiElements;
	private LinkedList<Focusable> openedUiElements;
	private ArrayList<Focusable> toBeOpenedUiElements;
	private Frame displayFrame;
	public SceneView sceneView;
	public EngineView engineView;
	public SequenceSelector sequenceSelector;
	public ColorPalette colorPalette;
	public Linker linker;
	public Status status;
	public PFont TEXTFONT;
	public PFont TITLEFONT;
	public int TEXTSIZE;
	public int TITLESIZE;

	public static final class SortByFocus implements Comparator<Focusable> {
		public int compare(Focusable a, Focusable b) {
			boolean mouseInA = a.mouseIn();
			boolean mouseInB = b.mouseIn();
			if(a.hasFocus() && MaShine.m.mousePressed)
				return 1; // b get drawn first, a on top
			else if (b.hasFocus() && MaShine.m.mousePressed)
				return -1; // b on top
			else
				if(mouseInA == mouseInB)
					return 0;
				else 
					if(mouseInA)
						return 1; // b get drawn first, a on top
					else
						return -1; // b on top
		}
	}

	public UI(){
		MaShine.m.colorMode(MaShine.RGB);

		TEXTFONT = MaShine.m.loadFont("RobotoMono-Light-11.vlw");
		TEXTSIZE = 11;

		MaShine.m.textFont(TEXTFONT);
		MaShine.m.textSize(TEXTSIZE);

		status = new Status();
		menu = new Menu();
		sceneView = new SceneView();
		engineView = new EngineView();
		sequenceSelector = new SequenceSelector();
		colorPalette = new ColorPalette();
		linker = new Linker();

		uiElements = new HashMap<String,Focusable>();
		uiElements.put("EventViewer", new EventViewer());
		uiElements.put("DataViewer", new DataViewer());
		uiElements.put("DeviceEditor", new DeviceEditor());
		uiElements.put("SequenceSelector", sequenceSelector);
		uiElements.put("ColorPalette", colorPalette);
		uiElements.put("SequenceEditor", new SequenceEditor());
		uiElements.put("Linker", linker);
		uiElements.put("FilterSelector", new FilterSelector());
		openedUiElements = new LinkedList<Focusable>();
		toBeOpenedUiElements = new ArrayList<Focusable>();

		displayFrame = new Frame();
	}

	public void close(String uiElementName){
		if(uiElements.containsKey(uiElementName)){
			Focusable el = uiElements.get(uiElementName);
			openedUiElements.remove(el);
			el.defocus();
		}
	}

	public void close(Focusable el){
		if(uiElements.containsValue(el)){
			openedUiElements.remove(el);
			el.defocus();
		}
	}

	public void open(String uiElementName){
		if(uiElements.containsKey(uiElementName)){
			Focusable el = uiElements.get(uiElementName);
			if(!openedUiElements.contains(el)){
				toBeOpenedUiElements.add(el);
			}
		}
	}

	public void draw(){
		//M.strokeWeight((float)0.5);
		//M.strokeJoin(M.MITER);

		menu.draw();
		if(null == displayFrame){
			displayFrame = new Frame();
		}
		sceneView.setFrame(displayFrame);
		displayFrame = null;
		sceneView.draw();
		engineView.draw();

		for(Focusable f : toBeOpenedUiElements){
			openedUiElements.add(f);
		}

		toBeOpenedUiElements.clear();

		openedUiElements.sort(new SortByFocus());

		for(Focusable el : openedUiElements){
			el.tick();
			el.draw();
			el.defocus();
		}

		sceneView.defocus();

		if(!openedUiElements.isEmpty()){
			if(openedUiElements.getLast().mouseIn()){
				openedUiElements.getLast().focus();
			}else if(sceneView.mouseIn()){
				sceneView.focus();
			}
		}else if(sceneView.mouseIn()){
			sceneView.focus();
		}
		
		status.draw();
	}


	public ArrayList<Device> getSelectedDevices(){return sceneView.getSelectedDevices();}
	public void setSelectedDevices(ArrayList<Device> newSelection){sceneView.setSelectedDevices(newSelection);}
	public void clearSelectedDevices(){sceneView.clearSelectedDevices();}
	public void reloadElements(){sceneView.reloadElements();}
	
	public Sequence getSelectedSequence(){return sequenceSelector.getSelectedSequence();}
	public void setSelectedSequence(Sequence s){sequenceSelector.setSelectedSequence(s);}

	public FlatColor getSelectedColor(){return colorPalette.getSelectedColor();}
	public void setSelectedColor(FlatColor s){colorPalette.setSelectedColor(s);}

	public void setDisplayedFrame(Frame frame){if(null == displayFrame)displayFrame = frame;}
}

