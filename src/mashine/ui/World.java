package mashine.ui;

import mashine.utils.Pair;

import java.util.Map;
import java.util.HashMap;

import mashine.engine.*;
import mashine.MaShine;

public class World {

	private Map<Pair<Integer, Integer>, Block> blocks;
	private Map<Pair<Integer, Integer>, Node>  nodes;

	public World(){
		blocks = new HashMap<Pair<Integer, Integer>, Block>();
		nodes = new HashMap<Pair<Integer, Integer>, Node>();
	}

	public Map<Pair<Integer, Integer>, Block> getBlocks(){return blocks;}
	public Map<Pair<Integer, Integer>, Node>  getNodes(){return nodes;}
	
	public Block getBlock(int x, int y){
		Pair coords = new Pair(x, y);
		if(blocks.containsKey(coords)){
			return blocks.get(coords);
		}
		return null;
	}

	public Node getNode(int x, int y){
		Pair coords = new Pair(x, y);
		if(nodes.containsKey(coords)){
			return nodes.get(coords);
		}
		return null;
	}

	public Boolean isBlocked(int x, int y){
		// on a block and not a node
		Pair coords = new Pair(x, y);
		return blocks.containsKey(coords) && !nodes.containsKey(coords);
	}

	public boolean updateBlock(Block b){
		if(canBlockFitAt(b, b.x(), b.y())){

			removeBlock(b);

			int x = b.x();
			int y = b.y();

			for(int i = x; i <= x + b.w(); i++){
				for(int j = y; j <= y + b.h(); j++){
					blocks.put(new Pair(i, j), b);
				}
			}

			int i = 0;
			for(Node n : b.getContentInNodes().values()){
				nodes.put(new Pair<Integer, Integer>(b.x(), b.y()+1+i), n);
				i++;
			}

			i = 0;
			for(Node n : b.getContentOutNodes().values()){
				nodes.put(new Pair<Integer, Integer>(b.x()+b.w(), b.y()+b.h()-1-i), n);
				i++;
			}

			i = 0;
			for(Node n : b.getControlInNodes().values()){
				nodes.put(new Pair<Integer, Integer>(b.x()+1+i, b.y()), n);
				i++;
			}

			i = 0;
			for(Node n : b.getControlOutNodes().values()){
				nodes.put(new Pair<Integer, Integer>(b.x()+b.w()-1-i, b.y()+b.h()), n);
				i++;
			}

			return true;
		}else{
			return false;
		}
	}

	public void growBlockVertical(Block b, int amount){
		for(int a = 0; a < amount; a++){
			int j = b.y()+b.h()+2;
			for(int i = b.x(); i <= b.x() + b.w()+1; i++){
				Pair pair = new Pair(i, j);
				if(blocks.containsKey(pair)){
					moveBlockOneDown(blocks.get(pair));
				}	
			}
		}
	}

	public void moveBlockOneDown(Block b){
		int j = b.y()+b.h()+2;
		for(int i = b.x(); i <= b.y() + b.w()+1; i++){
			Pair pair = new Pair(i, j);
			if(blocks.containsKey(pair)){
				moveBlockOneDown(blocks.get(pair));
			}	
		}
		b.y(b.y()+1);
		updateBlock(b);
	}

	public void removeBlock(Block b){
		for(int i = b.x(); i <= b.x() + b.w(); i++){
			for(int j = b.y(); j <= b.y() + b.h(); j++){
				Pair pair = new Pair(i, j);
				if(blocks.get(pair) != b){
					blocks.remove(pair);
					nodes.remove(pair);
				}
			}
		}
	}

	public boolean canBlockFitAt(Block b, int x, int y){
		for(int i = b.x(); i <= b.x() + b.w()+1; i++){
			for(int j = b.y(); j <= y + b.h()+1; j++){
				Pair pair = new Pair(i, j);
				if(blocks.containsKey(pair)){
					if(blocks.get(pair) != b){
						return false;
					}
				}
			}
		}

		return true;
	}

}