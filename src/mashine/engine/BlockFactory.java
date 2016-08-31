
package mashine.engine;

public interface BlockFactory{

	public Block getBlock(String type, String id);
	public Block getBlock(String type);


}