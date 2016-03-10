package mashine.ui.elements;

import mashine.*;
import mashine.ui.*;
import java.util.HashMap;
import java.util.regex.Pattern;

public class TextInput extends Element{

	private String value;
	private static final String validChar = "[a-zA-Z0-9_ \\-\\@\\#]+";

	public TextInput(Drawable parent, String defaultValue, int x, int y, int width){
		super(parent, x, y, width, 15);
		value = defaultValue;
	}

	public void drawContent(){
		P.canvas.noStroke();


		if(enabled && hasFocus() && P.hasFocus()){
			FlatColor.stroke(P.canvas, Colors.MATERIAL.ORANGE.A700);
			if(M.inputs.getState("keyboard.8.press")){
				if(value.length() > 0)
					value = value.substring(0, value.length() - 1);
			}else if(M.inputs.getState("keyboard.9.press") || M.inputs.getState("keyboard.10.press")){
				focus = false;
			}else if(M.inputs.getState("keyboard.147.press")){
				value = "";
			}else if(Pattern.matches(validChar, M.inputs.getLastKey())){
				value += M.inputs.getLastKey();
			}
		}

		if(enabled)
			FlatColor.fill(P.canvas, Colors.WHITE);
		else
			FlatColor.fill(P.canvas, Colors.MATERIAL.GREY._400);
		P.canvas.rect(x, y, width, height);
		FlatColor.fill(P.canvas, Colors.MATERIAL.BLUE_GREY._900);
		P.canvas.textAlign(P.canvas.LEFT, P.canvas.CENTER);
		P.canvas.text(value + (M.millis() % 1200 > 600 && enabled && hasFocus() ? "_" : ""), x + 3, y +height/2);
	}

	public void setValue(String v){
		value = v;
	}

	public String value(){
		return value;
	}

}