package mashine.ui;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import processing.core.*;

import mashine.engine.*;
import mashine.utils.*;
import mashine.MaShine;

public class Grid{

	PApplet p;

	private World world;

	private List<Path> paths;
	private List<Block> blocks;

	private int gridSize = 30;
	private PVector pxOffset = new PVector(0,0);

	private PVector start = null;
	private PVector end = null;

	private boolean click = false;
	private int gmousex, gmousey;

	public Grid(){
		this.p = MaShine.m;
		paths = new ArrayList<Path>();
		blocks = new ArrayList<Block>();

		world = new World();

		blocks = MaShine.engine.getBlocks();

		int offset = Math.round(p.width/gridSize)-2;
		int middle = Math.round(p.height/gridSize/2);

		for(Block block : MaShine.engine.getBlocks()){
			block.x(offset-block.w());
			block.y(middle-block.h()/2);
			offset -= 2+block.w();
			world.updateBlock(block);
		}
	}

	public void draw(){

		blocks = MaShine.engine.getBlocks();

		p.pushMatrix();
		p.translate(pxOffset.x, pxOffset.y);

		gmousex = (int) Math.round((p.mouseX-pxOffset.x)/gridSize);
		gmousey = (int) Math.round((p.mouseY-pxOffset.y)/gridSize);

		for(Path path : paths){		
			drawPath(path.get(), 105,240,174, 25);
		}

		if(start != null){
			drawPath(AStar((int)Math.floor(start.x), (int)Math.floor(start.y), gmousex, gmousey), 0,230,118, 255); 
		}else if(end != null){
			drawPath(AStar(gmousex, gmousey, (int)Math.floor(end.x), (int)Math.floor(end.y)), 0,230,118, 255); 
		}

		for(Block b : blocks){
			FlatColor.fill(p.g, Colors.MATERIAL.BLUE_GREY._800); 
			p.noStroke();
			p.rect(b.x()*gridSize, b.y()*gridSize, b.w()*gridSize+1, b.h()*gridSize+1);
		}

		for(Pair coord : world.getNodes().keySet()){
			Node n = world.getNodes().get(coord);
			drawNode(n, (Integer) coord.getFirst(), (Integer) coord.getSecond());
			checkClickedNode(n, (Integer) coord.getFirst(), (Integer) coord.getSecond(), gmousex, gmousey);
		}

		click = false;
		p.strokeWeight(1);
		p.noFill();
		p.stroke(255,0,255);
		p.line(gmousex*gridSize-20, gmousey*gridSize, gmousex*gridSize+20, gmousey*gridSize);
		p.line(gmousex*gridSize, gmousey*gridSize-20, gmousex*gridSize, gmousey*gridSize+20);

		p.popMatrix();
	}

	private void drawNode(Node n, int x, int y){
		
		FlatColor.stroke(p.g, Colors.MATERIAL.BLUE_GREY._900);

		p.strokeWeight(4);

		if(n instanceof InNode){
			FlatColor.fill(p.g, Colors.MATERIAL.TEAL.A200);
		}else{
			FlatColor.fill(p.g, Colors.MATERIAL.YELLOW.A200);
		}
		p.rectMode(p.RADIUS);

		p.strokeJoin(p.ROUND);
		if(n.getType().equals("Number")){
			p.ellipse(x*gridSize, y*gridSize, gridSize/2.5f, gridSize/2.5f);
		}else if(n.getType().equals("Boolean")){
			p.rect(x*gridSize, y*gridSize, gridSize/5, gridSize/5);
		}else if(n.getType().equals("Frame")){
			p.triangle((x-0.15f)*gridSize, (y-0.25f)*gridSize, (x-0.15f)*gridSize, (y+0.25f)*gridSize, (x+0.23f)*gridSize, y*gridSize);
		}else if(n.getType().equals("Sequence")){
			p.triangle((x-0.15f)*gridSize, (y-0.25f)*gridSize, (x-0.15f)*gridSize, (y+0.25f)*gridSize, (x+0.23f)*gridSize, y*gridSize);
			p.ellipse((x-0.1f)*gridSize, y*gridSize, gridSize/6, gridSize/6);
		}else if(n.getType().equals("DeviceGroup")){
			p.rect(x*gridSize, y*gridSize, gridSize/5, gridSize/5);
			p.strokeWeight(3);
			p.line((x-0.25f)*gridSize, y*gridSize, (x+0.25f)*gridSize, y*gridSize);
			p.line(x*gridSize, (y-0.25f)*gridSize, x*gridSize, (y+0.25f)*gridSize);
			p.strokeWeight(4);
		}

		p.rectMode(p.CORNER);
	}

	void checkClickedNode(Node n, int x, int y, int gx, int gy){
		if(x == gx && y == gy && click){
			if(n instanceof OutNode){
				start = new PVector(x,y);
				MaShine.println(n.getType());
			}else if(n instanceof InNode){
				end = new PVector(x,y);
				MaShine.println(n.getType());
			}
			if(start != null && end != null){
				//paths.add(AStar((int)Math.floor(start.x), (int)Math.floor(start.y), (int)Math.floor(end.x), (int)Math.floor(end.y)));
				start = end = null;
			}
		}
	}

	private void drawPath(List<PVector> path, int r, int g, int b, int a){
		if(path == null) return;
		p.strokeWeight(3);

		PVector prev = null;
		for(PVector current : path){
			if(prev != null){
				p.stroke(r,g,b, a);
				p.line(prev.x*gridSize,prev.y*gridSize,current.x*gridSize,current.y*gridSize);
			}
			prev = current;
		}
	}

	public void zoom(float amount){
		gridSize += amount;
		gridSize = Math.min(200, Math.max(20, gridSize));
	}
	public void drag(PVector amount){

		if(world.getBlock(gmousex, gmousey) == null){
			pxOffset = PVector.add(pxOffset, amount);
		}else if(false){

		}

	}

	public void click(){
		click = true;
	}

	// private Path getPath(InNode in, OutNode out){}
	// private Path getPath(int x, int y, OutNode out){}
	// private Path getPath(InNode in, int x, int y){}


	private List<PVector> AStar(int x1, int y1, int x2, int y2){

		// Helper class remembering scores, parents, coords.
		class Square{
			public int x, y, px, py, f, g, h;

			Square(int x_, int y_, int px_, int py_, int f_, int g_, int h_){
				x = x_; y = y_; px = px_; py = py_; f = f_; g = g_; h = h_;
			} 
		}

		Node startNode = world.getNode(x1, y1);
		Node endNode = world.getNode(x2, y2);
		boolean startIsNode = startNode != null;
		boolean endIsNode = endNode != null;



		if(startIsNode && endIsNode){
			if(startNode.getClass() == endNode.getClass()){ return null; }
			if(!startNode.getType().equals(endNode.getType())){ return null; }
			//if(instanceof startNode == instanceof);
		}

		// A* constants (tweaked to avoid zigzaging) (also, no diagonal paths)
		int G = 10;
		boolean under = x1 == x2 ? false : Math.abs(y2-y1)/Math.abs(x2-x1) < 1;
		int Hxf = under ? 10 : 7;
		int Hyf = under ? 7 : 10;

		// open/closed lists
		Map<String,Square> open = new HashMap<String,Square>();
		Map<String,Square> closed = new HashMap<String,Square>();

		// put the starting point
		open.put(x1+":"+y1, new Square(x1, y1, x1, y1, 0, 0, 0));
		Square current = null;

		// check start/end validity, exit with null
		if(world.isBlocked(x1, y1) || world.isBlocked(x2, y2)){return null;}

		while(true){

			// select minimum score in open squares
			Integer min = null;
			current = null;

			for(Square s : open.values()){if(current == null || s.f < min){current = s; min = s.f;}}
				if(current ==  null) return null;
			
			// remove it from the open list and make it current
			open.remove(current.x+":"+current.y);
			closed.put(current.x+":"+current.y, current);

			// if this is the target square : exit loop
			if(current.x == x2 && current.y == y2){
				break; // FOUND
			}else{
			// else find adjacent squares
				for(int i = 0; i < 4; i++){
					int x = (i == 0 ? current.x - 1 : (i == 2 ? current.x + 1 : current.x));
					int y = (i == 3 ? current.y - 1 : (i == 1 ? current.y + 1 : current.y));
			// process it :
					if(closed.containsKey(x+":"+y)){ // treated block, end laps
					}else if(world.isBlocked(x, y)){  // if blocked at x,y
					}else if(!(x == x2 && y == y2) && world.getNode(x, y) != null){ // except 
					//}else if(!(x == x2 && y == y2) && world.getNode(x, y) != null){ // except 
					}else{
						// heuristic score
						int h = Math.abs(x2-x)*Hxf+Math.abs(y2-y)*Hyf;
						// record processed sqaure and it's parent
						open.put(x+":"+y, new Square(x, y, current.x, current.y, G+h, G, h));
					}

				}
			}
		}

		List<PVector> path = new ArrayList<PVector>();

		// follow the path to starting square, records path without offsets :
		while(true){
			path.add(new PVector(current.x, current.y));
			current = closed.get(current.px+":"+current.py);
			if(current.x == x1 && current.y == y1){
				path.add(new PVector(current.px, current.py));
				break;
			}
		}

		return path;
	}

}