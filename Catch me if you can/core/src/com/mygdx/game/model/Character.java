package com.mygdx.game.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Character {
	public float pos_x = 0, pos_y = 0, stop = 0, size_x = 40, size_y = 80;
	public Rectangle body;
	public Rectangle rect;
	public Rectangle topbox;
	public Rectangle downbox;
	public Rectangle rightbox;
	public Rectangle leftbox;
	public float elapsedTime = 0;
	public TextureAtlas atlas;
	public TextureRegion currentFrame;
	public Animation go_up, go_down, go_right, go_left, stand, confused;
	public String prevkey;
	public boolean checkoverlaps=false, checkbehind=false, 
					checkshoe=false, checkcactus=false, checkbreakboxwalk=false, checkice=false;
	public boolean checkcan = false, checkbone = false;
	public int score = 0;
	public int speedup = 0;
	public float shoedelay = 0;
	public float bananadelay = 0;
	public float candelay = 0;
	public float bonedelay = 0;
	public float cactusdelay = 0;
	public float unjidelay = 0;
	public String holding = "";
	public boolean stuck = false, canstuck=false;
	public boolean slow = false, boneslow=false;
	public boolean speed = false;
	public boolean win = false;
	
	public Character() {
		
	}
	public Character(int pos_x, int pos_y){
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		body = new Rectangle(pos_x, pos_y, size_x, size_y/2);
		rect = new Rectangle(pos_x+7, pos_y+2, size_x-15, size_y/2-15);
		topbox = new Rectangle(pos_x, pos_y+(size_y)/2, size_x, size_y/2);
		downbox = new Rectangle(pos_x, pos_y-(size_y)/2, size_x, size_y/2);
		rightbox = new Rectangle(pos_x+size_x, pos_y, size_x, size_y/2);
		leftbox = new Rectangle(pos_x-size_x, pos_y, size_x, size_y/2);
	}
	public Character(int pos_x, int pos_y, int size_x, int size_y){
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		this.size_x = size_x;
		this.size_y = size_y;
		body = new Rectangle(pos_x, pos_y, size_x, size_y/2);
		rect = new Rectangle(pos_x+7, pos_y+2, size_x-15, size_y/2-15);
		topbox = new Rectangle(pos_x, pos_y+(size_y)/2, size_x, size_y/2);
		downbox = new Rectangle(pos_x, pos_y-(size_y)/2, size_x, size_y/2);
		rightbox = new Rectangle(pos_x+size_x, pos_y, size_x, size_y/2);
		leftbox = new Rectangle(pos_x-size_x, pos_y, size_x, size_y/2);
		
	}
}
