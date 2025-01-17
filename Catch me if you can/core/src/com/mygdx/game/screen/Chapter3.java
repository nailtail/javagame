package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Hud;
import com.mygdx.game.Main;
import com.mygdx.game.item.Banana;
import com.mygdx.game.item.Bone;
import com.mygdx.game.item.Can;
import com.mygdx.game.item.Pickaxe;
import com.mygdx.game.item.Shoes;
import com.mygdx.game.item.Unji;
import com.mygdx.game.model.Character;
import com.mygdx.game.tools.Clipping;

public class Chapter3 implements Screen{
	final Main main;
	
	//Batch
	public SpriteBatch batch;
	//Image
	public Texture grid, img_shoe, img_banana, img_bananapeel, hunterwin, runnerwin;
	public Texture img_unjisack, img_unji, img_pickaxe, img_stat1, img_stat2;
	public Texture lasthunterwin, lastrunnerwin;
	//Character
	public Character player1;
	public Character player2;
	//TiledMap
	public TiledMap tiledMap;
	public TiledMapRenderer tiledMapRenderer;
	public OrthographicCamera camera;
	//Objects Array
	public int boxescount = 0, behindcount = 0, breakboxcount = 0, breakboxwalkcount = 0, icecount = 0;
	public Array<Rectangle> boxes = new Array<Rectangle>();
	public Array<Rectangle> behind = new Array<Rectangle>();
	public Array<Rectangle> breakbox = new Array<Rectangle>();
	public Array<Rectangle> breakboxwalk = new Array<Rectangle>();
	public Array<Rectangle> ice = new Array<Rectangle>();
	//Exit
	public int escapeint = MathUtils.random(2);
	public Rectangle escape;
	public Array<Rectangle> exit = new Array<Rectangle>();
	//Sound
	public Sound wallcrash, music, boost, throwcan, throwbone, hitcan, takeitem, hitbone, broken, slow, stun;
	public Sound winning, escapesound;
	//item
	public Shoes shoes;
	public Banana banana;
	public Unji unji;
	public Pickaxe pickaxe;
	public Can can;
	public Bone bone;
	//ViewPort, HUD
	public Viewport gamePort;
	public Hud hud;
	public float w, h;
	//WinTime
	public float wintime = 0;
	public int escapecount = 0;
	public boolean end = false;
	
	public Chapter3(Main main, int p1oldscore, int p2oldscore) {
		this.main = main;
		float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        this.w = w;
        this.h = h;
		
		//Sprite, Batch, Texture, Atlas
		batch = main.batch;
		grid = new Texture("test/0001.jpg");
		//item
		img_shoe = new Texture(Gdx.files.internal("item/Shoe1.png"));
		img_banana = new Texture(Gdx.files.internal("item/Banana.png"));
		img_bananapeel = new Texture(Gdx.files.internal("item/Banana-Drop.png"));
		img_unjisack = new Texture(Gdx.files.internal("item/Unji-Sack.png"));
		img_unji = new Texture(Gdx.files.internal("item/Unji.png"));
		img_pickaxe = new Texture(Gdx.files.internal("item/Pickaxe.png"));
		img_stat1 = new Texture(Gdx.files.internal("hud/item.png"));
		img_stat2 = new Texture(Gdx.files.internal("hud/status.png"));
		//victory
		hunterwin = new Texture(Gdx.files.internal("victory/huntwinsnow.png"));
		runnerwin = new Texture(Gdx.files.internal("victory/runwinsnow.png"));
		
		lasthunterwin = new Texture(Gdx.files.internal("hunterwin.png"));
		lastrunnerwin = new Texture(Gdx.files.internal("runnerwin.png"));
		//TiledMap
		tiledMap = new TmxMapLoader().load("tilemap/winter/winter.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		//TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
		
		//Sound
		wallcrash = Gdx.audio.newSound(Gdx.files.internal("sound/effect/wallcrash.ogg"));
		boost = Gdx.audio.newSound(Gdx.files.internal("sound/effect/boost.mp3"));
		throwcan = Gdx.audio.newSound(Gdx.files.internal("sound/effect/throwcan.mp3"));
		throwbone = Gdx.audio.newSound(Gdx.files.internal("sound/effect/throwbone.mp3"));
		hitcan = Gdx.audio.newSound(Gdx.files.internal("sound/effect/hitcan.mp3"));
		hitbone = Gdx.audio.newSound(Gdx.files.internal("sound/effect/hitbone.mp3"));
		takeitem = Gdx.audio.newSound(Gdx.files.internal("sound/effect/takeitem.mp3"));
		broken = Gdx.audio.newSound(Gdx.files.internal("sound/effect/breakfire.mp3"));
		slow = Gdx.audio.newSound(Gdx.files.internal("sound/effect/slow.mp3"));
		stun = Gdx.audio.newSound(Gdx.files.internal("sound/effect/Stun.mp3"));
		winning = Gdx.audio.newSound(Gdx.files.internal("sound/effect/winning.mp3"));
		escapesound = Gdx.audio.newSound(Gdx.files.internal("sound/effect/escapesound.mp3"));
		music = Gdx.audio.newSound(Gdx.files.internal("sound/music/mapsnow.mp3"));
		music.loop();

		//Camera, Viewport
		
		camera = new OrthographicCamera();
        camera.setToOrtho(false,w,h);
        gamePort = new StretchViewport(Main.V_WIDTH, Main.V_HEIGHT, camera);
		
        //Objects
		player1 = new Character(100, 40);
		player2 = new Character(1280-100, 500);
		//Stack score
		player1.score += p1oldscore;
		player2.score += p2oldscore;
		//item
		shoes = new Shoes(300, 300);
		banana = new Banana(500, 500);
		unji = new Unji(300, 350);
		pickaxe = new Pickaxe(550, 500);
		can = new Can(600, 50);
		bone = new Bone(900, 70);
		
		//HUD
        hud = new Hud(batch, player1.score, player2.score);
		
		
		/*-------------Texture,Animation of Objects-----------*/
        
        //BreakBox
        for (MapObject object : tiledMap.getLayers().get("campblock1").getObjects().getByType(RectangleMapObject.class)) {
        	Rectangle rect = ((RectangleMapObject) object).getRectangle();
        	breakbox.add(rect);
			++breakboxcount;
			break;
		}
        for (MapObject object : tiledMap.getLayers().get("campblock2").getObjects().getByType(RectangleMapObject.class)) {
        	Rectangle rect = ((RectangleMapObject) object).getRectangle();
        	breakbox.add(rect);
			++breakboxcount;
			break;
		}
        for (MapObject object : tiledMap.getLayers().get("campblock3").getObjects().getByType(RectangleMapObject.class)) {
        	Rectangle rect = ((RectangleMapObject) object).getRectangle();
        	breakbox.add(rect);
			++breakboxcount;
			break;
		}
        for (MapObject object : tiledMap.getLayers().get("campblock4").getObjects().getByType(RectangleMapObject.class)) {
        	Rectangle rect = ((RectangleMapObject) object).getRectangle();
        	breakbox.add(rect);
			++breakboxcount;
			break;
		}
        for (MapObject object : tiledMap.getLayers().get("campblock5").getObjects().getByType(RectangleMapObject.class)) {
        	Rectangle rect = ((RectangleMapObject) object).getRectangle();
        	breakbox.add(rect);
			++breakboxcount;
			break;
		}
        for (MapObject object : tiledMap.getLayers().get("campblock6").getObjects().getByType(RectangleMapObject.class)) {
        	Rectangle rect = ((RectangleMapObject) object).getRectangle();
        	breakbox.add(rect);
			++breakboxcount;
			break;
		}
        for (MapObject object : tiledMap.getLayers().get("campblock7").getObjects().getByType(RectangleMapObject.class)) {
        	Rectangle rect = ((RectangleMapObject) object).getRectangle();
        	breakbox.add(rect);
			++breakboxcount;
			break;
		}
        for (MapObject object : tiledMap.getLayers().get("campblock8").getObjects().getByType(RectangleMapObject.class)) {
        	Rectangle rect = ((RectangleMapObject) object).getRectangle();
        	breakbox.add(rect);
			++breakboxcount;
			break;
		}
        
        
        
        //Behind
        for (MapObject object : tiledMap.getLayers().get("treewalk").getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			behind.add(rect);
			++behindcount;
		}
        for (MapObject object : tiledMap.getLayers().get("rockwalk").getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			behind.add(rect);
			++behindcount;
		}
        for (MapObject object : tiledMap.getLayers().get("housewalk").getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			behind.add(rect);
			++behindcount;
		}
        
        
        //IceObjects
        for (MapObject object : tiledMap.getLayers().get("iceobject").getObjects().getByType(RectangleMapObject.class)) {
     	   Rectangle rect = ((RectangleMapObject) object).getRectangle();
         	ice.add(rect);
         	icecount++;
 		}
        
        
        //exit
        for (MapObject object : tiledMap.getLayers().get("oexit1").getObjects().getByType(RectangleMapObject.class)) {
    	   Rectangle rect = ((RectangleMapObject) object).getRectangle();
        	exit.add(rect);
        	break;
		}
        for (MapObject object : tiledMap.getLayers().get("oexit2").getObjects().getByType(RectangleMapObject.class)) {
        	Rectangle rect = ((RectangleMapObject) object).getRectangle();
        	exit.add(rect);
        	break;
		}
        for (MapObject object : tiledMap.getLayers().get("oexit3").getObjects().getByType(RectangleMapObject.class)) {
        	Rectangle rect = ((RectangleMapObject) object).getRectangle();
        	exit.add(rect);
        	break;
		}
        
        
        
        //boxes
		for (MapObject object : tiledMap.getLayers().get("wall").getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			boxes.add(rect);
			++boxescount;
		}
		
		for (MapObject object : tiledMap.getLayers().get("rockcrash").getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			boxes.add(rect);;
			++boxescount;
		}
		
		for (MapObject object : tiledMap.getLayers().get("treecrash").getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			boxes.add(rect);;
			++boxescount;
		}
		
		for (MapObject object : tiledMap.getLayers().get("frameobject").getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			boxes.add(rect);;
			++boxescount;
		}
		
		for (MapObject object : tiledMap.getLayers().get("housecrash").getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			boxes.add(rect);;
			++boxescount;
		}
		
		//EscapeWay
      	escape = exit.get(escapeint);
		//Player1
		player1.atlas = new TextureAtlas(Gdx.files.internal("player/winter/hunter/yeti.atlas"));
		player1.go_up = new Animation(1/2f, player1.atlas.findRegion("yu1"), player1.atlas.findRegion("yu2"));
		player1.go_down = new Animation(1/2f, player1.atlas.findRegion("yd1"), player1.atlas.findRegion("yd2"));
		player1.go_right = new Animation(1/2f, player1.atlas.findRegion("tl1"), player1.atlas.findRegion("tl2"), player1.atlas.findRegion("tl3"), player1.atlas.findRegion("tl4"), player1.atlas.findRegion("tl5"));
		player1.go_left = new Animation(1/2f, player1.atlas.findRegion("yfr1"), player1.atlas.findRegion("yfr2"), player1.atlas.findRegion("yfr3"), player1.atlas.findRegion("yfr4"), player1.atlas.findRegion("yfr5"));
		player1.stand = new Animation(1/2f, player1.atlas.findRegion("yfd"), player1.atlas.findRegion("ybd"), player1.atlas.findRegion("tlf"), player1.atlas.findRegion("yfr"));
		player1.confused = new Animation(1/2f, player1.atlas.findRegion("yc1"), player1.atlas.findRegion("yc2"), player1.atlas.findRegion("yc3"));
		
		//Player2
		player2.atlas = new TextureAtlas(Gdx.files.internal("player/winter/runner/ninja.atlas"));
		player2.go_up = new Animation(1/2f, player2.atlas.findRegion("nu1"), player2.atlas.findRegion("nu2"));
		player2.go_down = new Animation(1/2f, player2.atlas.findRegion("nd1"), player2.atlas.findRegion("nd2"));
		player2.go_right = new Animation(1/2f, player2.atlas.findRegion("nl1"), player2.atlas.findRegion("nl2"), player2.atlas.findRegion("nl3"), player2.atlas.findRegion("nl4"), player2.atlas.findRegion("nl5"));
		player2.go_left = new Animation(1/2f, player2.atlas.findRegion("nr1"), player2.atlas.findRegion("nr2"), player2.atlas.findRegion("nr3"), player2.atlas.findRegion("nr4"), player2.atlas.findRegion("nr5"));
		player2.stand = new Animation(1/2f, player2.atlas.findRegion("nfd"), player2.atlas.findRegion("nbu"), player2.atlas.findRegion("nl"), player2.atlas.findRegion("nr"));
		player2.confused = new Animation(1/2f, player2.atlas.findRegion("nc1"), player2.atlas.findRegion("n2"), player2.atlas.findRegion("nc3"));
		
		
		//can
		can.atlas = new TextureAtlas(Gdx.files.internal("item/can/Can Action/canact.atlas"));
		can.anime = new Animation(1/5f, can.atlas.findRegion("1"), can.atlas.findRegion("2"),can.atlas.findRegion("3")
				,can.atlas.findRegion("4"),can.atlas.findRegion("5"),can.atlas.findRegion("6"),can.atlas.findRegion("7")
				,can.atlas.findRegion("8"));
				
		//bone
		bone.atlas = new TextureAtlas(Gdx.files.internal("item/bone/Bone Action/boneact.atlas"));
		bone.anime = new Animation(1/5f, bone.atlas.findRegion("1"), bone.atlas.findRegion("2"),bone.atlas.findRegion("3")
				,bone.atlas.findRegion("4"),bone.atlas.findRegion("5"),bone.atlas.findRegion("6"),bone.atlas.findRegion("7"));
		
		
	}
	
	
	public void check_bone(float delta) {
		//Can overlaps and Holding
		if (player1.rect.overlaps(bone.rect) && bone.slow == false) {
			player1.holding = "Bone";
			bone.pickup = true;
			bone.slow = true;
			bone.rect.x += 100000;
			takeitem.play();
		}
		if (player2.rect.overlaps(bone.rect) && bone.slow == false) {
			player2.holding = "Bone";
			bone.pickup = true;
			bone.slow = true;
			bone.rect.x += 100000;
			takeitem.play();
		}
		// When touch bone
		if (player1.rect.overlaps(bone.rect) && bone.slow == true) {
			bone.rect.x += 100000;
			player1.boneslow= true;
			player1.bonedelay = TimeUtils.nanoTime();
			hitbone.play();
			slow.play();
		}
		if (player2.rect.overlaps(bone.rect) && bone.slow == true) {
			bone.rect.x += 100000;
			player2.boneslow = true;
			player2.bonedelay = TimeUtils.nanoTime();
			hitbone.play();
			slow.play();
		}
		
		//Slow
		if (player1.boneslow == true) {
			if (TimeUtils.nanoTime()- player1.bonedelay < 2000000000f) {
				player1.elapsedTime += 0.7*delta;
				player1.speedup = -2;
				player1.boneslow = true;
			}
			else {
				player1.bonedelay = 0;
				player1.boneslow = false;
				player1.speedup = 0;
			}
		}
		if (player2.boneslow == true) {
			if (TimeUtils.nanoTime()- player2.bonedelay < 2000000000f) {
				player2.elapsedTime += 0.7*delta;
				player2.boneslow = true;
				player2.speedup = -2;
			}
			else {
				player2.bonedelay = 0;
				player2.boneslow = false;
				player2.speedup = 0;
			}
		}
		
		//Player1 throw
		if (player1.holding == "Bone") {
			if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player1.prevkey == "UP"){
				bone.rect.x = player1.topbox.x;
				bone.rect.y = player1.topbox.y;
				player1.holding = "";
				bone.throwup = true;
				throwbone.play();
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player1.prevkey == "DOWN"){
				bone.rect.x = player1.downbox.x;
				bone.rect.y = player1.downbox.y;
				player1.holding = "";
				bone.throwdown = true;
				throwbone.play();
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player1.prevkey == "RIGHT"){
				bone.rect.x = player1.rightbox.x;
				bone.rect.y = player1.rightbox.y;
				player1.holding = "";
				bone.throwright = true;
				throwbone.play();
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player1.prevkey == "LEFT"){
				bone.rect.x = player1.leftbox.x;
				bone.rect.y = player1.leftbox.y;
				player1.holding = "";
				bone.throwleft = true;
				throwbone.play();
			}
		}
		
		//Player2 throw
		if (player2.holding == "Bone") {
			if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_0) && player2.prevkey == "UP"){
				bone.rect.x = player2.topbox.x;
				bone.rect.y = player2.topbox.y;
				player2.holding = "";
				bone.throwup = true;
				throwbone.play();
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_0) && player2.prevkey == "DOWN"){
				bone.rect.x = player2.downbox.x;
				bone.rect.y = player2.downbox.y;
				player2.holding = "";
				bone.throwdown = true;
				throwbone.play();
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_0) && player2.prevkey == "RIGHT"){
				bone.rect.x = player2.rightbox.x;
				bone.rect.y = player2.rightbox.y;
				player2.holding = "";
				bone.throwright = true;
				throwbone.play();
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_0) && player2.prevkey == "LEFT"){
				bone.rect.x = player2.leftbox.x;
				bone.rect.y = player2.leftbox.y;
				player2.holding = "";
				bone.throwleft = true;
				throwbone.play();
			}
		}
		
}
	
	public void check_pickaxe(float delta) {
		//pickaxe overlaps
		if (player1.rect.overlaps(pickaxe.rect)) {
			pickaxe.rect.x += 100000;
			player1.holding = "Pickaxe";
			takeitem.play();
					
		}
		if (player2.rect.overlaps(pickaxe.rect)) {
			pickaxe.rect.x += 100000;
			player2.holding = "Pickaxe";
			takeitem.play();
		}
		
		//player1 destroy
		for (int i=0 ; i < breakbox.size ; ++i) {
			Rectangle rect = breakbox.get(i);
			if (player1.holding == "Pickaxe") {
				if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player1.prevkey == "UP"
						&& player1.topbox.overlaps(rect)){
					broken.play();
					breakbox.get(i).y += 100000;
					if (i == 0)
						tiledMap.getLayers().get("camp1").setVisible(false);
					else if (i == 1)
						tiledMap.getLayers().get("camp2").setVisible(false);
					else if (i == 2)
						tiledMap.getLayers().get("camp3").setVisible(false);
					else if (i == 3)
						tiledMap.getLayers().get("camp4").setVisible(false);
					else if (i == 4)
						tiledMap.getLayers().get("camp5").setVisible(false);
					else if (i == 5)
						tiledMap.getLayers().get("camp6").setVisible(false);
					else if (i == 6)
						tiledMap.getLayers().get("camp7").setVisible(false);
					else if (i == 7)
						tiledMap.getLayers().get("camp8").setVisible(false);
					
					player1.holding = "";
				}
				else if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player1.prevkey == "DOWN"
						&& player1.downbox.overlaps(rect)){
					broken.play();
					breakbox.get(i).y += 100000;
					if (i == 0)
						tiledMap.getLayers().get("camp1").setVisible(false);
					else if (i == 1)
						tiledMap.getLayers().get("camp2").setVisible(false);
					else if (i == 2)
						tiledMap.getLayers().get("camp3").setVisible(false);
					else if (i == 3)
						tiledMap.getLayers().get("camp4").setVisible(false);
					else if (i == 4)
						tiledMap.getLayers().get("camp5").setVisible(false);
					else if (i == 5)
						tiledMap.getLayers().get("camp6").setVisible(false);
					else if (i == 6)
						tiledMap.getLayers().get("camp7").setVisible(false);
					else if (i == 7)
						tiledMap.getLayers().get("camp8").setVisible(false);
					
					player1.holding = "";
					
				}
				else if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player1.prevkey == "RIGHT"
						&& player1.rightbox.overlaps(rect)){
					broken.play();
					breakbox.get(i).y += 100000;
					if (i == 0)
						tiledMap.getLayers().get("camp1").setVisible(false);
					else if (i == 1)
						tiledMap.getLayers().get("camp2").setVisible(false);
					else if (i == 2)
						tiledMap.getLayers().get("camp3").setVisible(false);
					else if (i == 3)
						tiledMap.getLayers().get("camp4").setVisible(false);
					else if (i == 4)
						tiledMap.getLayers().get("camp5").setVisible(false);
					else if (i == 5)
						tiledMap.getLayers().get("camp6").setVisible(false);
					else if (i == 6)
						tiledMap.getLayers().get("camp7").setVisible(false);
					else if (i == 7)
						tiledMap.getLayers().get("camp8").setVisible(false);
					
					player1.holding = "";
					
				}
				else if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player1.prevkey == "LEFT"
						&& player1.leftbox.overlaps(rect)){
					broken.play();
					breakbox.get(i).y += 100000;
					if (i == 0)
						tiledMap.getLayers().get("camp1").setVisible(false);
					else if (i == 1)
						tiledMap.getLayers().get("camp2").setVisible(false);
					else if (i == 2)
						tiledMap.getLayers().get("camp3").setVisible(false);
					else if (i == 3)
						tiledMap.getLayers().get("camp4").setVisible(false);
					else if (i == 4)
						tiledMap.getLayers().get("camp5").setVisible(false);
					else if (i == 5)
						tiledMap.getLayers().get("camp6").setVisible(false);
					else if (i == 6)
						tiledMap.getLayers().get("camp7").setVisible(false);
					else if (i == 7)
						tiledMap.getLayers().get("camp8").setVisible(false);
					
					player1.holding = "";
					
				}
			}
		}
		
		//player2 destroy
		for (int i=0 ; i < breakbox.size ; ++i) {
			Rectangle rect = breakbox.get(i);
			if (player2.holding == "Pickaxe") {
				if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_0) && player2.prevkey == "UP"
						&& player2.topbox.overlaps(rect)){
					broken.play();
					breakbox.get(i).y += 100000;
					if (i == 0)
						tiledMap.getLayers().get("camp1").setVisible(false);
					else if (i == 1)
						tiledMap.getLayers().get("camp2").setVisible(false);
					else if (i == 2)
						tiledMap.getLayers().get("camp3").setVisible(false);
					else if (i == 3)
						tiledMap.getLayers().get("camp4").setVisible(false);
					else if (i == 4)
						tiledMap.getLayers().get("camp5").setVisible(false);
					else if (i == 5)
						tiledMap.getLayers().get("camp6").setVisible(false);
					else if (i == 6)
						tiledMap.getLayers().get("camp7").setVisible(false);
					else if (i == 7)
						tiledMap.getLayers().get("camp8").setVisible(false);
					
					player2.holding = "";
				}
				else if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_0) && player2.prevkey == "DOWN"
						&& player2.downbox.overlaps(rect)){
					broken.play();
					breakbox.get(i).y += 100000;
					if (i == 0)
						tiledMap.getLayers().get("camp1").setVisible(false);
					else if (i == 1)
						tiledMap.getLayers().get("camp2").setVisible(false);
					else if (i == 2)
						tiledMap.getLayers().get("camp3").setVisible(false);
					else if (i == 3)
						tiledMap.getLayers().get("camp4").setVisible(false);
					else if (i == 4)
						tiledMap.getLayers().get("camp5").setVisible(false);
					else if (i == 5)
						tiledMap.getLayers().get("camp6").setVisible(false);
					else if (i == 6)
						tiledMap.getLayers().get("camp7").setVisible(false);
					else if (i == 7)
						tiledMap.getLayers().get("camp8").setVisible(false);
					
					player2.holding = "";
					
				}
				else if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_0) && player2.prevkey == "RIGHT"
						&& player2.rightbox.overlaps(rect)){
					broken.play();
					breakbox.get(i).y += 100000;
					if (i == 0)
						tiledMap.getLayers().get("camp1").setVisible(false);
					else if (i == 1)
						tiledMap.getLayers().get("camp2").setVisible(false);
					else if (i == 2)
						tiledMap.getLayers().get("camp3").setVisible(false);
					else if (i == 3)
						tiledMap.getLayers().get("camp4").setVisible(false);
					else if (i == 4)
						tiledMap.getLayers().get("camp5").setVisible(false);
					else if (i == 5)
						tiledMap.getLayers().get("camp6").setVisible(false);
					else if (i == 6)
						tiledMap.getLayers().get("camp7").setVisible(false);
					else if (i == 7)
						tiledMap.getLayers().get("camp8").setVisible(false);
					
					player2.holding = "";
					
				}
				else if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_0) && player2.prevkey == "LEFT"
						&& player2.leftbox.overlaps(rect)){
					broken.play();
					breakbox.get(i).y += 100000;
					if (i == 0)
						tiledMap.getLayers().get("camp1").setVisible(false);
					else if (i == 1)
						tiledMap.getLayers().get("camp2").setVisible(false);
					else if (i == 2)
						tiledMap.getLayers().get("camp3").setVisible(false);
					else if (i == 3)
						tiledMap.getLayers().get("camp4").setVisible(false);
					else if (i == 4)
						tiledMap.getLayers().get("camp5").setVisible(false);
					else if (i == 5)
						tiledMap.getLayers().get("camp6").setVisible(false);
					else if (i == 6)
						tiledMap.getLayers().get("camp7").setVisible(false);
					else if (i == 7)
						tiledMap.getLayers().get("camp8").setVisible(false);
					
					player2.holding = "";
					
				}
			}
		}
		
	}
	
	public void check_can(float delta) {
		//Can overlaps and Holding
		if (player1.rect.overlaps(can.rect) && can.stunt == false) {
			player1.holding = "Can";
			can.pickup = true;
			can.stunt = true;
			can.rect.x += 100000;
			takeitem.play();
		}
		if (player2.rect.overlaps(can.rect) && can.stunt == false) {
			player2.holding = "Can";
			can.pickup = true;
			can.stunt = true;
			can.rect.x += 100000;
			takeitem.play();
		}
		// When touch can
		if (player1.rect.overlaps(can.rect) && can.stunt == true) {
			can.rect.x += 100000;
			player1.canstuck = true;
			player1.candelay = TimeUtils.nanoTime();
			hitcan.play();
			stun.play();
		}
		if (player2.rect.overlaps(can.rect) && can.stunt == true) {
			can.rect.x += 100000;
			player2.canstuck = true;
			player2.candelay = TimeUtils.nanoTime();
			hitcan.play();
			stun.play();
		}
		
		//Stunt
		if (player1.canstuck == true) {
			if (TimeUtils.nanoTime()- player1.candelay < 1500000000f) {
				player1.currentFrame = (TextureRegion) player1.confused.getKeyFrame(player1.elapsedTime, true);
				player1.elapsedTime += 3*delta;
				player1.canstuck = true;
			}
			else {
				player1.candelay = 0;
				player1.canstuck = false;
			}
		}
		if (player2.canstuck == true) {
			if (TimeUtils.nanoTime()- player2.candelay < 1500000000f) {
				player2.currentFrame = (TextureRegion) player2.confused.getKeyFrame(player2.elapsedTime, true);
				player2.elapsedTime += 3*delta;
				player2.canstuck = true;
			}
			else {
				player2.candelay = 0;
				player2.canstuck = false;
			}
		}
		
		//Player1 throw
		if (player1.holding == "Can") {
			if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player1.prevkey == "UP"){
				can.rect.x = player1.topbox.x;
				can.rect.y = player1.topbox.y;
				player1.holding = "";
				can.throwup = true;
				throwcan.play();
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player1.prevkey == "DOWN"){
				can.rect.x = player1.downbox.x;
				can.rect.y = player1.downbox.y;
				player1.holding = "";
				can.throwdown = true;
				throwcan.play();
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player1.prevkey == "RIGHT"){
				can.rect.x = player1.rightbox.x;
				can.rect.y = player1.rightbox.y;
				player1.holding = "";
				can.throwright = true;
				throwcan.play();
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player1.prevkey == "LEFT"){
				can.rect.x = player1.leftbox.x;
				can.rect.y = player1.leftbox.y;
				player1.holding = "";
				can.throwleft = true;
				throwcan.play();
			}
		}
		
		//Player2 throw
		if (player2.holding == "Can") {
			if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_0) && player2.prevkey == "UP"){
				can.rect.x = player2.topbox.x;
				can.rect.y = player2.topbox.y;
				player2.holding = "";
				can.throwup = true;
				throwcan.play();
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_0) && player2.prevkey == "DOWN"){
				can.rect.x = player2.downbox.x;
				can.rect.y = player2.downbox.y;
				player2.holding = "";
				can.throwdown = true;
				throwcan.play();
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_0) && player2.prevkey == "RIGHT"){
				can.rect.x = player2.rightbox.x;
				can.rect.y = player2.rightbox.y;
				player2.holding = "";
				can.throwright = true;
				throwcan.play();
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_0) && player2.prevkey == "LEFT"){
				can.rect.x = player2.leftbox.x;
				can.rect.y = player2.leftbox.y;
				player2.holding = "";
				can.throwleft = true;
				throwcan.play();
			}
		}
		
		
	}
	
	public void check_unji(float delta) {
		//Unji
		if (player1.slow == true) {
			if (TimeUtils.nanoTime()- player1.unjidelay < 2000000000f) {
				player1.elapsedTime += 0.7*delta;
				player1.slow = true;
				player1.speedup = -2;
			}
			else {
				player1.unjidelay = 0;
				player1.slow = false;
				player1.speedup = 0;
			}
		}
		
		if (player2.slow == true) {
			if (TimeUtils.nanoTime()- player2.unjidelay < 2000000000f) {
				player2.elapsedTime += 0.7*delta;
				player2.slow = true;
				player2.speedup = -2;
			}
			else {
				player2.unjidelay = 0;
				player2.slow = false;
				player2.speedup = 0;
			}
		}
		//unji sack overlaps
		if (player1.rect.overlaps(unji.rect) && unji.unsack == false) {
			unji.rect.x += 100000;
			player1.holding = "Unji";
			unji.unsack = true;
			takeitem.play();
			
		}
		if (player2.rect.overlaps(unji.rect) && unji.unsack == false) {
			unji.rect.x += 100000;
			player2.holding = "Unji";
			unji.unsack = true;
			takeitem.play();
		}
		
		//unji unsack overlaps
		if (player1.rect.overlaps(unji.rect) && unji.unsack == true) {
			unji.rect.x += 100000;
			player1.slow = true;
			player1.unjidelay = TimeUtils.nanoTime();
			slow.play();
		}
		if (player2.rect.overlaps(unji.rect) && unji.unsack == true) {
			unji.rect.x += 100000;
			player2.slow = true;
			player2.unjidelay = TimeUtils.nanoTime();
			slow.play();
		}
		
		//unji holding
		if (player1.holding == "Unji") {
			if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player1.prevkey == "UP"){
				unji.rect.x = player1.topbox.x;
				unji.rect.y = player1.topbox.y;
				player1.holding = "";
				takeitem.play();
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player1.prevkey == "DOWN"){
				unji.rect.x = player1.downbox.x;
				unji.rect.y = player1.downbox.y;
				player1.holding = "";
				takeitem.play();
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player1.prevkey == "RIGHT"){
				unji.rect.x = player1.rightbox.x;
				unji.rect.y = player1.rightbox.y;
				player1.holding = "";
				takeitem.play();
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player1.prevkey == "LEFT"){
				unji.rect.x = player1.leftbox.x;
				unji.rect.y = player1.leftbox.y;
				player1.holding = "";
				takeitem.play();
			}
		}
		
		if (player2.holding == "Unji") {
			if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_0) && player2.prevkey == "UP"){
				unji.rect.x = player2.topbox.x;
				unji.rect.y = player2.topbox.y;
				player2.holding = "";
				takeitem.play();
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_0) && player2.prevkey == "DOWN"){
				unji.rect.x = player2.downbox.x;
				unji.rect.y = player2.downbox.y;
				player2.holding = "";
				takeitem.play();
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_0) && player2.prevkey == "RIGHT"){
				unji.rect.x = player2.rightbox.x;
				unji.rect.y = player2.rightbox.y;
				player2.holding = "";
				takeitem.play();
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_0) && player2.prevkey == "LEFT"){
				unji.rect.x = player2.leftbox.x;
				unji.rect.y = player2.leftbox.y;
				player2.holding = "";
				takeitem.play();
			}
		}
	}
	
	public void check_breakbox(float delta) {
		//Check Collision
		
		//Cactus (Campfire)
		for (int i=0 ; i < breakbox.size ; ++i) {
			if (player1.rect.overlaps(breakbox.get(i))) {
				player1.checkcactus = true;
				player1.cactusdelay = TimeUtils.nanoTime();
				break;
			}
		}
				
		for (int i=0 ; i < breakbox.size ; ++i) {
			if (player2.rect.overlaps(breakbox.get(i))) {
				player2.checkcactus = true;
				player2.cactusdelay = TimeUtils.nanoTime();
				break;
			}
		}
	}
	
	public void check_behind(float delta) {
		//Check Collision
		for (int i=0 ; i < behind.size ; ++i) {
			if (player1.rect.overlaps(behind.get(i))) {
				player1.checkbehind = true;
				break;
			}
			player1.checkbehind = false;
		}
						
		for (int i=0 ; i < behind.size ; ++i) {
			if (player2.rect.overlaps(behind.get(i))) {
				player2.checkbehind = true;
				break;
			}
			player2.checkbehind = false;
		}
	}
	
	public void check_ice(float delta) {
		//Check Collision
		for (int i=0 ; i < ice.size ; ++i) {
			if (player1.rect.overlaps(ice.get(i))) {
				player1.checkice = true;
				break;
			}
			else {
				player1.checkice = false;
			}
		}
				
		for (int i=0 ; i < ice.size ; ++i) {
			if (player2.rect.overlaps(ice.get(i))) {
				player2.checkice = true;
				break;
			}
			else {
				player2.checkice = false;
			}
		}
	}
	
	public void check_exit(float delta) {
		
		for (int i=0; i < exit.size ; ++i) {
			if (player1.rect.overlaps(exit.get(i))) {
				player1.checkoverlaps = true;
				break;
			}
		}
		
		for (int i=0; i < exit.size ; ++i) {
			if (player2.rect.overlaps(exit.get(i))) {
				player2.checkoverlaps = true;
				break;
			}
		}
		
	}
	
	public void check_wall(float delta) {
		//Check Collision
		for (int i=0 ; i < boxes.size ; ++i) {
			if (player1.rect.overlaps(boxes.get(i))) {
				player1.checkoverlaps = true;
				break;
			}
		}
		
		for (int i=0 ; i < boxes.size ; ++i) {
			if (player2.rect.overlaps(boxes.get(i))) {
				player2.checkoverlaps = true;
				break;
			}
		}
	}
	
	public void check_shoes(float delta) {
		//shoes overlaps
		if (player1.rect.overlaps(shoes.rect)) {
			shoes.rect.x += 10000;
			player1.checkshoe = true;
			player1.shoedelay = TimeUtils.nanoTime();
			player1.speed = true;
			boost.play();
			
		}
		if (player2.rect.overlaps(shoes.rect)) {
			shoes.rect.x += 10000;
			player2.checkshoe = true;
			player2.shoedelay = TimeUtils.nanoTime();
			player2.speed = true;
			boost.play();
		}
		
		//Check Effect
		if (player1.checkshoe == true) {
			if (TimeUtils.nanoTime()- player1.shoedelay < 3000000000f) {
				player1.speedup = 2;
				player1.elapsedTime += 10*delta;
			}
			else {
				player1.speedup = 0;
				player1.shoedelay = 0;
				player1.checkshoe = false;
				player1.speed = false;
			}
		}
		
		if (player2.checkshoe == true) {
			if (TimeUtils.nanoTime()- player2.shoedelay < 3000000000f) {
				player2.speedup = 2;
				player2.elapsedTime += 10*delta;
			}
			else {
				player2.speedup = 0;
				player2.shoedelay = 0;
				player2.checkshoe = false;
				player2.speed = false;
			}
		}
	}
	
	public void check_banana(float delta) {
		//Banana
		if (player1.stuck == true) {
			if (TimeUtils.nanoTime()- player1.bananadelay < 1500000000f) {
				player1.currentFrame = (TextureRegion) player1.confused.getKeyFrame(player1.elapsedTime, true);
				player1.elapsedTime += 3*delta;
				player1.stuck = true;
			}
			else {
				player1.bananadelay = 0;
				player1.stuck = false;
			}
		}
		
		if (player2.stuck == true) {
			if (TimeUtils.nanoTime()- player2.bananadelay < 1500000000f) {
				player2.currentFrame = (TextureRegion) player2.confused.getKeyFrame(player2.elapsedTime, true);
				player2.elapsedTime += 3*delta;
				player2.stuck = true;
			}
			else {
				player2.bananadelay = 0;
				player2.stuck = false;
			}
		}
		//banana overlaps
		if (player1.rect.overlaps(banana.rect) && banana.peel == false) {
			banana.rect.x += 100000;
			player1.holding = "Banana";
			banana.peel = true;
			takeitem.play();
			
		}
		if (player2.rect.overlaps(banana.rect) && banana.peel == false) {
			banana.rect.x += 100000;
			player2.holding = "Banana";
			banana.peel = true;
			takeitem.play();
		}
		
		if (player1.rect.overlaps(banana.rect) && banana.peel == true) {
			banana.rect.x += 100000;
			player1.stuck = true;
			player1.bananadelay = TimeUtils.nanoTime();
			stun.play();
		}
		if (player2.rect.overlaps(banana.rect) && banana.peel == true) {
			banana.rect.x += 100000;
			player2.stuck = true;
			player2.bananadelay = TimeUtils.nanoTime();
			stun.play();
		}
		
		if (player1.holding == "Banana") {
			if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player1.prevkey == "UP"){
				banana.rect.x = player1.topbox.x;
				banana.rect.y = player1.topbox.y;
				player1.holding = "";
				takeitem.play();
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player1.prevkey == "DOWN"){
				banana.rect.x = player1.downbox.x;
				banana.rect.y = player1.downbox.y;
				player1.holding = "";
				takeitem.play();
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player1.prevkey == "RIGHT"){
				banana.rect.x = player1.rightbox.x;
				banana.rect.y = player1.rightbox.y;
				player1.holding = "";
				takeitem.play();
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player1.prevkey == "LEFT"){
				banana.rect.x = player1.leftbox.x;
				banana.rect.y = player1.leftbox.y;
				player1.holding = "";
				takeitem.play();
			}
		}
		
		if (player2.holding == "Banana") {
			if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_0) && player2.prevkey == "UP"){
				banana.rect.x = player2.topbox.x;
				banana.rect.y = player2.topbox.y;
				player2.holding = "";
				takeitem.play();
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_0) && player2.prevkey == "DOWN"){
				banana.rect.x = player2.downbox.x;
				banana.rect.y = player2.downbox.y;
				player2.holding = "";
				takeitem.play();
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_0) && player2.prevkey == "RIGHT"){
				banana.rect.x = player2.rightbox.x;
				banana.rect.y = player2.rightbox.y;
				player2.holding = "";
				takeitem.play();
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_0) && player2.prevkey == "LEFT"){
				banana.rect.x = player2.leftbox.x;
				banana.rect.y = player2.leftbox.y;
				player2.holding = "";
				takeitem.play();
			}
		}
	}
	
	public void handle(float delta) {
		//Player1 Standing//
		if (player1.stop == 0)
			player1.currentFrame = (TextureRegion) player1.stand.getKeyFrame(0, true);
		if (player1.stop == 1)
			player1.currentFrame = (TextureRegion) player1.stand.getKeyFrame(0.5f, true);
		if (player1.stop == 2)
			player1.currentFrame = (TextureRegion) player1.stand.getKeyFrame(1f, true);
		if (player1.stop == 3)
			player1.currentFrame = (TextureRegion) player1.stand.getKeyFrame(1.5f, true);
		//-------------------------------------------------------------------------------//
		
		//Player1 Movement//
		if (true) {
			if(Gdx.input.isKeyPressed(Input.Keys.W) && player1.checkoverlaps == false && player1.stuck == false 
					&& player1.checkice == false && player1.checkcactus == false && player1.canstuck == false){
		        player1.currentFrame = (TextureRegion) player1.go_up.getKeyFrame(player1.elapsedTime, true);
		        player1.pos_y += 3+player1.speedup;
		        if (player1.slow == false && player1.speed == false && player1.boneslow == false)
		        	player1.elapsedTime += 3*delta;
		        player1.stop = 1;
		        player1.rect.y += 3+player1.speedup;
		        player1.body.y += 3+player1.speedup;
		        player1.topbox.y += 3+player1.speedup;
				player1.downbox.y += 3+player1.speedup;
				player1.rightbox.y += 3+player1.speedup;
				player1.leftbox.y += 3+player1.speedup;
		        player1.prevkey = "UP";
		    }
			else if(Gdx.input.isKeyPressed(Input.Keys.S) && player1.checkoverlaps == false && player1.stuck == false 
					&& player1.checkice == false && player1.checkcactus == false && player1.canstuck == false) {
				player1.currentFrame = (TextureRegion) player1.go_down.getKeyFrame(player1.elapsedTime, true);
				player1.pos_y -= 3+player1.speedup;
				if (player1.slow == false && player1.speed == false && player1.boneslow == false)
					player1.elapsedTime += 3*delta;
				player1.stop = 0;
				player1.rect.y -= 3+player1.speedup;
				player1.body.y -= 3+player1.speedup;
				player1.topbox.y -= 3+player1.speedup;
				player1.downbox.y -= 3+player1.speedup;
				player1.rightbox.y -= 3+player1.speedup;
				player1.leftbox.y -= 3+player1.speedup;
				player1.prevkey = "DOWN";
			}
			else if(Gdx.input.isKeyPressed(Input.Keys.D) && player1.checkoverlaps == false && player1.stuck == false 
					&& player1.checkice == false && player1.checkcactus == false && player1.canstuck == false) {
				player1.currentFrame = (TextureRegion) player1.go_right.getKeyFrame(player1.elapsedTime, true);
				player1.pos_x += 3+player1.speedup;
				if (player1.slow == false && player1.speed == false && player1.boneslow == false)
					player1.elapsedTime += 3*delta;
				player1.stop = 2;
				player1.rect.x += 3+player1.speedup;
				player1.body.x += 3+player1.speedup;
				player1.topbox.x += 3+player1.speedup;
				player1.downbox.x += 3+player1.speedup;
				player1.rightbox.x += 3+player1.speedup;
				player1.leftbox.x += 3+player1.speedup;
				player1.prevkey = "RIGHT";
			}
			else if(Gdx.input.isKeyPressed(Input.Keys.A) && player1.checkoverlaps == false && player1.stuck == false 
					&& player1.checkice == false && player1.checkcactus == false && player1.canstuck == false) {
				player1.currentFrame = (TextureRegion) player1.go_left.getKeyFrame(player1.elapsedTime, true);
				player1.pos_x -= 3+player1.speedup;
				if (player1.slow == false && player1.speed == false && player1.boneslow == false)
					player1.elapsedTime += 3*delta;
				player1.stop = 3;
				player1.rect.x -= 3+player1.speedup;
				player1.body.x -= 3+player1.speedup;
				player1.topbox.x -= 3+player1.speedup;
				player1.downbox.x -= 3+player1.speedup;
				player1.rightbox.x -= 3+player1.speedup;
				player1.leftbox.x -= 3+player1.speedup;
				player1.prevkey = "LEFT";
			}
			else if (player1.checkoverlaps == true && player1.prevkey == "UP" && player1.checkice == false
					&& player1.checkcactus == false) {
				player1.currentFrame = (TextureRegion) player1.stand.getKeyFrame(0.5f, true);
				player1.pos_y -= 0.5;
				player1.rect.y -= 0.5;
				player1.body.y -= 0.5;
				player1.topbox.y -= 0.5;
				player1.downbox.y -= 0.5;
				player1.rightbox.y -= 0.5;
				player1.leftbox.y -= 0.5;
				player1.checkoverlaps = false;
				wallcrash.play(0.1f);
			}
			else if (player1.checkoverlaps == true && player1.prevkey == "DOWN" && player1.checkice == false
					&& player1.checkcactus == false) {
				player1.currentFrame = (TextureRegion) player1.stand.getKeyFrame(0f, true);
				player1.pos_y += 0.5;
				player1.rect.y += 0.5;
				player1.body.y += 0.5;
				player1.topbox.y += 0.5;
				player1.downbox.y += 0.5;
				player1.rightbox.y += 0.5;
				player1.leftbox.y += 0.5;
				player1.checkoverlaps = false;
				wallcrash.play(0.1f);
			}
			else if (player1.checkoverlaps == true && player1.prevkey == "RIGHT" && player1.checkice == false
					&& player1.checkcactus == false) {
				player1.currentFrame = (TextureRegion) player1.stand.getKeyFrame(1, true);
				player1.pos_x -= 0.5;
				player1.rect.x -= 0.5;
				player1.body.x -= 0.5;
				player1.topbox.x -= 0.5;
				player1.downbox.x -= 0.5;
				player1.rightbox.x -= 0.5;
				player1.leftbox.x -= 0.5;
				player1.checkoverlaps = false;
				wallcrash.play(0.1f);
			}
			else if (player1.checkoverlaps == true && player1.prevkey == "LEFT" && player1.checkice == false
					&& player1.checkcactus == false) {
				player1.currentFrame = (TextureRegion) player1.stand.getKeyFrame(1.5f, true);
				player1.pos_x += 0.5;
				player1.rect.x += 0.5;
				player1.body.x += 0.5;
				player1.topbox.x += 0.5;
				player1.downbox.x += 0.5;
				player1.rightbox.x += 0.5;
				player1.leftbox.x += 0.5;
				player1.checkoverlaps = false;
				wallcrash.play(0.1f);
			}
			//Ice
			else if (player1.checkice == true && player1.prevkey == "UP" && player1.checkoverlaps == false) {
				if (player1.checkoverlaps == false && player1.checkice == true) {
					player1.currentFrame = (TextureRegion) player1.stand.getKeyFrame(0.5f, true);
					System.out.println("Go");
					player1.pos_y += 3.5;
					player1.rect.y += 3.5;
					player1.body.y += 3.5;
					player1.topbox.y += 3.5;
					player1.downbox.y += 3.5;
					player1.rightbox.y += 3.5;
					player1.leftbox.y += 3.5;
					wallcrash.play(0.1f);
				}
				else {
					player1.checkoverlaps = false;
					player1.checkice = false;
				}
			}
			else if (player1.checkice == true && player1.prevkey == "DOWN" && player1.checkoverlaps == false) {
				if (player1.checkoverlaps == false && player1.checkice == true) {
					player1.currentFrame = (TextureRegion) player1.stand.getKeyFrame(0f, true);
					player1.pos_y -= 3.5;
					player1.rect.y -= 3.5;
					player1.body.y -= 3.5;
					player1.topbox.y -= 3.5;
					player1.downbox.y -= 3.5;
					player1.rightbox.y -= 3.5;
					player1.leftbox.y -= 3.5;
					wallcrash.play(0.1f);
				}
				else {
					player1.checkoverlaps = false;
					player1.checkice = false;
				}
			}
			else if (player1.checkice == true && player1.prevkey == "RIGHT" && player1.checkoverlaps == false) {
				if (player1.checkoverlaps == false && player1.checkice == true) {
					player1.currentFrame = (TextureRegion) player1.stand.getKeyFrame(1, true);
					player1.pos_x += 3.5;
					player1.rect.x += 3.5;
					player1.body.x += 3.5;
					player1.topbox.x += 3.5;
					player1.downbox.x += 3.5;
					player1.rightbox.x += 3.5;
					player1.leftbox.x += 3.5;
					wallcrash.play(0.1f);
				}
				else {
					player1.checkoverlaps = false;
					player1.checkice = false;
				}
			}
			else if (player1.checkice == true && player1.prevkey == "LEFT" && player1.checkoverlaps == false) {
				if (player1.checkoverlaps == false && player1.checkice == true) {
					player1.currentFrame = (TextureRegion) player1.stand.getKeyFrame(1.5f, true);
					player1.pos_x -= 3.5;
					player1.rect.x -= 3.5;
					player1.body.x -= 3.5;
					player1.topbox.x -= 3.5;
					player1.downbox.x -= 3.5;
					player1.rightbox.x -= 3.5;
					player1.leftbox.x -= 3.5;
					wallcrash.play(0.1f);
				}
				else {
					player1.checkoverlaps = false;
					player1.checkice = false;
				}
			}
			//Cactus(Campfire)
			else if (player1.checkcactus == true && player1.prevkey == "UP" && player1.checkoverlaps == false) {
				if (TimeUtils.nanoTime()- player1.cactusdelay < 1000000000f/2 && player1.checkoverlaps == false) {
					player1.currentFrame = (TextureRegion) player1.stand.getKeyFrame(0.5f, true);
					player1.pos_y -= 2;
					player1.rect.y -= 2;
					player1.body.y -= 2;
					player1.topbox.y -= 2;
					player1.downbox.y -= 2;
					player1.rightbox.y -= 2;
					player1.leftbox.y -= 2;
					wallcrash.play(0.1f);
				}
				else {
					player1.checkcactus = false;
					player1.cactusdelay = 0;
				}
			}
			else if (player1.checkcactus == true && player1.prevkey == "DOWN" && player1.checkoverlaps == false) {
				if (TimeUtils.nanoTime()- player1.cactusdelay < 1000000000f/2 && player1.checkoverlaps == false) {
					player1.currentFrame = (TextureRegion) player1.stand.getKeyFrame(0f, true);
					player1.pos_y += 2;
					player1.rect.y += 2;
					player1.body.y += 2;
					player1.topbox.y += 2;
					player1.downbox.y += 2;
					player1.rightbox.y += 2;
					player1.leftbox.y += 2;
					wallcrash.play(0.1f);
				}
				else {
					player1.checkcactus = false;
					player1.cactusdelay = 0;
				}
			}
			else if (player1.checkcactus == true && player1.prevkey == "RIGHT" && player1.checkoverlaps == false) {
				if (TimeUtils.nanoTime()- player1.cactusdelay < 1000000000f/2 && player1.checkoverlaps == false) {
					player1.currentFrame = (TextureRegion) player1.stand.getKeyFrame(1, true);
					player1.pos_x -= 2;
					player1.rect.x -= 2;
					player1.body.x -= 2;
					player1.topbox.x -= 2;
					player1.downbox.x -= 2;
					player1.rightbox.x -= 2;
					player1.leftbox.x -= 2;
					wallcrash.play(0.1f);
				}
				else {
					player1.checkcactus = false;
					player1.cactusdelay = 0;
				}
			}
			else if (player1.checkcactus == true && player1.prevkey == "LEFT" && player1.checkoverlaps == false) {
				if (TimeUtils.nanoTime()- player1.cactusdelay < 1000000000f/2 && player1.checkoverlaps == false) {
					player1.currentFrame = (TextureRegion) player1.stand.getKeyFrame(1.5f, true);
					player1.pos_x += 2;
					player1.rect.x += 2;
					player1.body.x += 2;
					player1.topbox.x += 2;
					player1.downbox.x += 2;
					player1.rightbox.x += 2;
					player1.leftbox.x += 2;
					wallcrash.play(0.1f);
				}
				else {
					player1.checkcactus = false;
					player1.cactusdelay = 0;
				}
			}
			//when touched box
			else if (player1.checkoverlaps == true && player1.prevkey == "UP" && player1.checkcactus == true) {
				player1.currentFrame = (TextureRegion) player1.stand.getKeyFrame(0.5f, true);
				player1.pos_y += 0.5;
				player1.rect.y += 0.5;
				player1.body.y += 0.5;
				player1.topbox.y += 0.5;
				player1.downbox.y += 0.5;
				player1.rightbox.y += 0.5;
				player1.leftbox.y += 0.5;
				player1.checkoverlaps = false;
				wallcrash.play(0.1f);
			}
			else if (player1.checkoverlaps == true && player1.prevkey == "DOWN" && player1.checkcactus == true) {
				player1.currentFrame = (TextureRegion) player1.stand.getKeyFrame(0f, true);
				player1.pos_y -= 0.5;
				player1.rect.y -= 0.5;
				player1.body.y -= 0.5;
				player1.topbox.y -= 0.5;
				player1.downbox.y -= 0.5;
				player1.rightbox.y -= 0.5;
				player1.leftbox.y -= 0.5;
				player1.checkoverlaps = false;
				wallcrash.play(0.1f);
			}
			else if (player1.checkoverlaps == true && player1.prevkey == "RIGHT" && player1.checkcactus == true) {
				player1.currentFrame = (TextureRegion) player1.stand.getKeyFrame(1, true);
				player1.pos_x += 0.5;
				player1.rect.x += 0.5;
				player1.body.x += 0.5;
				player1.topbox.x += 0.5;
				player1.downbox.x += 0.5;
				player1.rightbox.x += 0.5;
				player1.leftbox.x += 0.5;
				player1.checkoverlaps = false;
				wallcrash.play(0.1f);
			}
			else if (player1.checkoverlaps == true && player1.prevkey == "LEFT" && player1.checkcactus == true) {
				player1.currentFrame = (TextureRegion) player1.stand.getKeyFrame(1.5f, true);
				player1.pos_x -= 0.5;
				player1.rect.x -= 0.5;
				player1.body.x -= 0.5;
				player1.topbox.x -= 0.5;
				player1.downbox.x -= 0.5;
				player1.rightbox.x -= 0.5;
				player1.leftbox.x -= 0.5;
				player1.checkoverlaps = false;
				wallcrash.play(0.1f);
			}
		}
		//-----------------------------------------------------------------------------------------------------//
		
		//Player2 Standing//
		if (player2.stop == 0)
			player2.currentFrame = (TextureRegion) player2.stand.getKeyFrame(0, true);
		if (player2.stop == 1)
			player2.currentFrame = (TextureRegion) player2.stand.getKeyFrame(0.5f, true);
		if (player2.stop == 2)
			player2.currentFrame = (TextureRegion) player2.stand.getKeyFrame(1f, true);
		if (player2.stop == 3)
			player2.currentFrame = (TextureRegion) player2.stand.getKeyFrame(1.5f, true);
		//-------------------------------------------------------------------------------//
				
		//Player2 Movement//
		if (true) {
			if(Gdx.input.isKeyPressed(Input.Keys.UP) && player2.checkoverlaps == false && player2.stuck == false 
					&& player2.checkice== false && player2.checkcactus == false && player2.canstuck == false) {
				player2.currentFrame = (TextureRegion) player2.go_up.getKeyFrame(player2.elapsedTime, true);
			    player2.pos_y += 3+player2.speedup;
			    player2.body.y += 3+player2.speedup;
			    player2.rect.y += 3+player2.speedup;
			    player2.topbox.y += 3+player2.speedup;
				player2.downbox.y += 3+player2.speedup;
				player2.rightbox.y += 3+player2.speedup;
				player2.leftbox.y += 3+player2.speedup;
				if (player2.slow == false && player2.speed == false && player2.boneslow == false)
					player2.elapsedTime += 3*delta;
			    player2.stop = 1;
			    player2.prevkey = "UP";
			}
			else if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && player2.checkoverlaps == false && player2.stuck == false 
					&& player2.checkice == false && player2.checkcactus == false && player2.canstuck == false) {
				player2.currentFrame = (TextureRegion) player2.go_down.getKeyFrame(player2.elapsedTime, true);
				player2.pos_y -= 3+player2.speedup;
				player2.rect.y -= 3+player2.speedup;
				player2.body.y -= 3+player2.speedup;
				player2.topbox.y -= 3+player2.speedup;
				player2.downbox.y -= 3+player2.speedup;
				player2.rightbox.y -= 3+player2.speedup;
				player2.leftbox.y -= 3+player2.speedup;
				if (player2.slow == false && player2.speed == false && player2.boneslow == false)
					player2.elapsedTime += 3*delta;
				player2.stop = 0;
				player2.prevkey = "DOWN";
			}
			else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player2.checkoverlaps == false && player2.stuck == false 
					&& player2.checkice == false && player2.checkcactus == false && player2.canstuck == false) {
				player2.currentFrame = (TextureRegion) player2.go_right.getKeyFrame(player2.elapsedTime, true);
				player2.pos_x += 3+player2.speedup;
				player2.rect.x += 3+player2.speedup;
				player2.body.x += 3+player2.speedup;
				player2.topbox.x += 3+player2.speedup;
				player2.downbox.x += 3+player2.speedup;
				player2.rightbox.x += 3+player2.speedup;
				player2.leftbox.x += 3+player2.speedup;
				if (player2.slow == false && player2.speed == false && player2.boneslow == false)
					player2.elapsedTime += 3*delta;
				player2.stop = 2;
				player2.prevkey = "RIGHT";
			}
			else if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player2.checkoverlaps == false && player2.stuck == false 
					&& player2.checkice == false && player2.checkcactus == false && player2.canstuck == false) {
				player2.currentFrame = (TextureRegion) player2.go_left.getKeyFrame(player2.elapsedTime, true);
				player2.pos_x -= 3+player2.speedup;
				player2.rect.x -= 3+player2.speedup;
				player2.body.x -= 3+player2.speedup;
				player2.topbox.x -= 3+player2.speedup;
				player2.downbox.x -= 3+player2.speedup;
				player2.rightbox.x -= 3+player2.speedup;
				player2.leftbox.x -= 3+player2.speedup;
				if (player2.slow == false && player2.speed == false && player2.boneslow == false)
					player2.elapsedTime += 3*delta;
				player2.stop = 3;
				player2.prevkey = "LEFT";
			}
			else if (player2.checkoverlaps == true && player2.prevkey == "UP" && player2.checkice == false
					&& player2.checkcactus == false) {
				player2.currentFrame = (TextureRegion) player2.stand.getKeyFrame(0.5f, true);
				player2.pos_y -= 0.5;
				player2.rect.y -= 0.5;
				player2.body.y -= 0.5;
				player2.topbox.y -= 0.5;
				player2.downbox.y -= 0.5;
				player2.rightbox.y -= 0.5;
				player2.leftbox.y -= 0.5;
				player2.checkoverlaps = false;
				wallcrash.play(0.1f);
			}
			else if (player2.checkoverlaps == true && player2.prevkey == "DOWN" && player2.checkice == false
					&& player2.checkcactus == false) {
				player2.currentFrame = (TextureRegion) player2.stand.getKeyFrame(0f, true);
				player2.pos_y += 0.5;
				player2.rect.y += 0.5;
				player2.body.y += 0.5;
				player2.topbox.y += 0.5;
				player2.downbox.y += 0.5;
				player2.rightbox.y += 0.5;
				player2.leftbox.y += 0.5;
				player2.checkoverlaps = false;
				wallcrash.play(0.1f);
			}
			else if (player2.checkoverlaps == true && player2.prevkey == "RIGHT" && player2.checkice == false
					&& player2.checkcactus == false) {
				player2.currentFrame = (TextureRegion) player2.stand.getKeyFrame(1, true);
				player2.pos_x -= 0.5;
				player2.rect.x -= 0.5;
				player2.body.x -= 0.5;
				player2.topbox.x -= 0.5;
				player2.downbox.x -= 0.5;
				player2.rightbox.x -= 0.5;
				player2.leftbox.x -= 0.5;
				player2.checkoverlaps = false;
				wallcrash.play(0.1f);
			}
			else if (player2.checkoverlaps == true && player2.prevkey == "LEFT" && player2.checkice == false
					&& player2.checkcactus == false) {
				player2.currentFrame = (TextureRegion) player2.stand.getKeyFrame(1.5f, true);
				player2.pos_x += 0.5;
				player2.rect.x += 0.5;
				player2.body.x += 0.5;
				player2.topbox.x += 0.5;
				player2.downbox.x += 0.5;
				player2.rightbox.x += 0.5;
				player2.leftbox.x += 0.5;
				player2.checkoverlaps = false;
				wallcrash.play(0.1f);
			}
			//Ice
			else if (player2.checkice == true && player2.prevkey == "UP" && player2.checkoverlaps == false) {
				if (player2.checkoverlaps == false && player2.checkice == true) {
					player2.currentFrame = (TextureRegion) player2.stand.getKeyFrame(0.5f, true);
					player2.pos_y += 3.5;
					player2.rect.y += 3.5;
					player2.body.y += 3.5;
					player2.topbox.y += 3.5;
					player2.downbox.y += 3.5;
					player2.rightbox.y += 3.5;
					player2.leftbox.y += 3.5;
					wallcrash.play(0.1f);
				}
				else {
					player2.checkoverlaps = false;
					player2.checkice = false;
				}
			}
			else if (player2.checkice == true && player2.prevkey == "DOWN" && player2.checkoverlaps == false) {
				if (player2.checkoverlaps == false && player2.checkice == true) {
					player2.currentFrame = (TextureRegion) player2.stand.getKeyFrame(0f, true);
					player2.pos_y -= 3.5;
					player2.rect.y -= 3.5;
					player2.body.y -= 3.5;
					player2.topbox.y -= 3.5;
					player2.downbox.y -= 3.5;
					player2.rightbox.y -= 3.5;
					player2.leftbox.y -= 3.5;
					wallcrash.play(0.1f);
				}
				else {
					player2.checkoverlaps = false;
					player2.checkice = false;
				}
			}
			else if (player2.checkice == true && player2.prevkey == "RIGHT" && player2.checkoverlaps == false) {
				if (player2.checkoverlaps == false && player2.checkice == true) {
					player2.currentFrame = (TextureRegion) player2.stand.getKeyFrame(1, true);
					player2.pos_x += 3.5;
					player2.rect.x += 3.5;
					player2.body.x += 3.5;
					player2.topbox.x += 3.5;
					player2.downbox.x += 3.5;
					player2.rightbox.x += 3.5;
					player2.leftbox.x += 3.5;
					wallcrash.play(0.1f);
				}
				else {
					player2.checkoverlaps = false;
					player2.checkice = false;
				}
			}
			else if (player2.checkice == true && player2.prevkey == "LEFT" && player2.checkoverlaps == false) {
				if (player2.checkoverlaps == false && player2.checkice == true) {
					player2.currentFrame = (TextureRegion) player2.stand.getKeyFrame(1.5f, true);
					player2.pos_x -= 3.5;
					player2.rect.x -= 3.5;
					player2.body.x -= 3.5;
					player2.topbox.x -= 3.5;
					player2.downbox.x -= 3.5;
					player2.rightbox.x -= 3.5;
					player2.leftbox.x -= 3.5;
					wallcrash.play(0.1f);
				}
				else {
					player2.checkoverlaps = false;
					player2.checkice = false;
				}
			}
			//Cactus(Campfire)
			else if (player2.checkcactus == true && player2.prevkey == "UP" && player2.checkoverlaps == false) {
				if (TimeUtils.nanoTime()- player2.cactusdelay < 1000000000f/2 && player2.checkoverlaps == false) {
					player2.currentFrame = (TextureRegion) player2.stand.getKeyFrame(0.5f, true);
					player2.pos_y -= 2;
					player2.rect.y -= 2;
					player2.body.y -= 2;
					player2.topbox.y -= 2;
					player2.downbox.y -= 2;
					player2.rightbox.y -= 2;
					player2.leftbox.y -= 2;
					wallcrash.play(0.1f);
				}
				else {
					player2.checkcactus = false;
					player2.cactusdelay = 0;
				}
			}
			else if (player2.checkcactus == true && player2.prevkey == "DOWN" && player2.checkoverlaps == false) {
				if (TimeUtils.nanoTime()- player2.cactusdelay < 1000000000f/2 && player2.checkoverlaps == false) {
					player2.currentFrame = (TextureRegion) player2.stand.getKeyFrame(0f, true);
					player2.pos_y += 2;
					player2.rect.y += 2;
					player2.body.y += 2;
					player2.topbox.y += 2;
					player2.downbox.y += 2;
					player2.rightbox.y += 2;
					player2.leftbox.y += 2;
					wallcrash.play(0.1f);
				}
				else {
					player2.checkcactus = false;
					player2.cactusdelay = 0;
				}
			}
			else if (player2.checkcactus == true && player2.prevkey == "RIGHT" && player2.checkoverlaps == false) {
				if (TimeUtils.nanoTime()- player2.cactusdelay < 1000000000f/2 && player2.checkoverlaps == false) {
					player2.currentFrame = (TextureRegion) player2.stand.getKeyFrame(1, true);
					player2.pos_x -= 2;
					player2.rect.x -= 2;
					player2.body.x -= 2;
					player2.topbox.x -= 2;
					player2.downbox.x -= 2;
					player2.rightbox.x -= 2;
					player2.leftbox.x -= 2;
					wallcrash.play(0.1f);
				}
				else {
					player2.checkcactus = false;
					player2.cactusdelay = 0;
				}
			}
			else if (player2.checkcactus == true && player2.prevkey == "LEFT" && player2.checkoverlaps == false) {
				if (TimeUtils.nanoTime()- player2.cactusdelay < 1000000000f/2 && player2.checkoverlaps == false) {
					player2.currentFrame = (TextureRegion) player2.stand.getKeyFrame(1.5f, true);
					player2.pos_x += 2;
					player2.rect.x += 2;
					player2.body.x += 2;
					player2.topbox.x += 2;
					player2.downbox.x += 2;
					player2.rightbox.x += 2;
					player2.leftbox.x += 2;
					wallcrash.play(0.1f);
				}
				else {
					player2.checkcactus = false;
					player2.cactusdelay = 0;
				}
			}
			//when touched box
			else if (player2.checkoverlaps == true && player2.prevkey == "UP" && player2.checkcactus == true) {
				player2.currentFrame = (TextureRegion) player2.stand.getKeyFrame(0.5f, true);
				player2.pos_y += 0.5;
				player2.rect.y += 0.5;
				player2.body.y += 0.5;
				player2.topbox.y += 0.5;
				player2.downbox.y += 0.5;
				player2.rightbox.y += 0.5;
				player2.leftbox.y += 0.5;
				player2.checkoverlaps = false;
				wallcrash.play(0.1f);
			}
			else if (player2.checkoverlaps == true && player2.prevkey == "DOWN" && player2.checkcactus == true) {
				player2.currentFrame = (TextureRegion) player2.stand.getKeyFrame(0f, true);
				player2.pos_y -= 0.5;
				player2.rect.y -= 0.5;
				player2.body.y -= 0.5;
				player2.topbox.y -= 0.5;
				player2.downbox.y -= 0.5;
				player2.rightbox.y -= 0.5;
				player2.leftbox.y -= 0.5;
				player2.checkoverlaps = false;
				wallcrash.play(0.1f);
			}
			else if (player2.checkoverlaps == true && player2.prevkey == "RIGHT" && player2.checkcactus == true) {
				player2.currentFrame = (TextureRegion) player2.stand.getKeyFrame(1, true);
				player2.pos_x += 0.5;
				player2.rect.x += 0.5;
				player2.body.x += 0.5;
				player2.topbox.x += 0.5;
				player2.downbox.x += 0.5;
				player2.rightbox.x += 0.5;
				player2.leftbox.x += 0.5;
				player2.checkoverlaps = false;
				wallcrash.play(0.1f);
			}
			else if (player2.checkoverlaps == true && player2.prevkey == "LEFT" && player2.checkcactus == true) {
				player2.currentFrame = (TextureRegion) player2.stand.getKeyFrame(1.5f, true);
				player2.pos_x -= 0.5;
				player2.rect.x -= 0.5;
				player2.body.x -= 0.5;
				player2.topbox.x -= 0.5;
				player2.downbox.x -= 0.5;
				player2.rightbox.x -= 0.5;
				player2.leftbox.x -= 0.5;
				player2.checkoverlaps = false;
				wallcrash.play(0.1f);
			}
			
		}
		//-----------------------------------------------------------------------------------------------------//
	}
		
	public void check_win(float delta) {
		wintime += delta;
		//Check Win
		if (player1.rect.overlaps(player2.rect) && player1.win == false && player1.stuck == false) {
			end=true;
			player1.score ++;
			hud.score1 = player1.score;
			hud.score1Label.setText(String.format("%02d", hud.score1));
			player1.win = true;
			wintime = 0;
			winning.play();
		}
		else if (hud.sec == 0 && hud.min == 0 && player2.win == false) {
			if (escapecount == 0)
				escapesound.play();
			escapecount++;
			if (escape == exit.get(0) && player2.win == false) {
				System.out.println(0);
				tiledMap.getLayers().get("exit1").setVisible(false);
				if (player2.rect.overlaps(exit.get(0))) {
					end=true;
					player2.score++;
					player2.win = true;
					wintime = 0;
					winning.play();
				}
			}
			else if (escape == exit.get(1) && player2.win == false) {
				System.out.println(1);
				tiledMap.getLayers().get("exit2").setVisible(false);
				if (player2.rect.overlaps(exit.get(1))) {
					end=true;
					player2.score++;
					player2.win = true;
					wintime = 0;
					winning.play();
				}
			}
			else if (escape == exit.get(2) && player2.win == false) {
				System.out.println(2);
				tiledMap.getLayers().get("exit3").setVisible(false);
				if (player2.rect.overlaps(exit.get(2))) {
					end=true;
					player2.score++;
					player2.win = true;
					wintime = 0;
					winning.play();
				}
			}
			
		}
	}
	
	public void countdown(float delta) {
		hud.timeCount += delta;
		if (hud.timeCount >= 1) {
			if (hud.min == 0 && hud.sec == 0) {
				hud.stop = true;
			}
			if (player1.win == true || player2.win == true) {
				hud.stop = true;
			}
			if (hud.stop == false) {
				hud.sec --;
				if (hud.sec < 0) {
					hud.min = 0;
					hud.sec = 59;
				}
				hud.countdownLabel.setText(String.format("%02d : %02d", hud.min, hud.sec));
				hud.timeCount = 0;
			}
		}
	}
	
	@Override
	public void render(float delta) {
		
		//Clear
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//----------------------------------------//
		
		//Checking
		if (player1.win == false && player2.win == false) {
			handle(delta);
			check_banana(delta);
		}
		check_pickaxe(delta);
		check_bone(delta);
		check_can(delta);
		check_unji(delta);
		check_breakbox(delta);
		check_behind(delta);
		check_ice(delta);
		check_exit(delta);
		check_wall(delta);
		check_win(delta);
		check_shoes(delta);
		countdown(delta);
				
		
		//Change map
		if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
			main.setScreen(new Chapter2(main, player1.score, player2.score));
			music.stop();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
			main.setScreen(new Chapter1(main));
			music.stop();
		}
		//---------------------------------------------------//
		
		//Check Clipping//
//		player1.rect.x = Clipping.check_x(player1.rect.x, player1.size_x);
//		player1.rect.y = Clipping.check_y(player1.rect.y, player1.size_y);
//		
//		player2.rect.x = Clipping.check_x(player2.rect.x, player2.size_x);
//		player2.rect.y = Clipping.check_y(player2.rect.y, player2.size_y);
		//-----------------------------------//

		
		//Map//
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
       
        
        //HUD
        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        
		//Graphics Rendering//
		batch.begin();
		
		//item
		//shoes
		batch.draw(img_shoe, shoes.rect.x, shoes.rect.y);
		
		//banana
		if (banana.peel == false) {
			batch.draw(img_banana, banana.rect.x, banana.rect.y);
		}
		else {
			batch.draw(img_bananapeel, banana.rect.x, banana.rect.y);
		}
		
		//unji
		if (unji.unsack == false) {
			batch.draw(img_unjisack, unji.rect.x, unji.rect.y);
		}
		else {
			batch.draw(img_unji, unji.rect.x, unji.rect.y);
		}
				
		//Pickaxe
		batch.draw(img_pickaxe, pickaxe.rect.x, pickaxe.rect.y);
		
		//Can
		if (can.pickup == false) {
			can.currentFrame = (TextureRegion) can.anime.getKeyFrame(0, true);
			batch.draw(can.currentFrame, can.rect.x, can.rect.y);
		}
				
		//Can is throwed
		if (can.throwup == true && !(can.rect.overlaps(player1.rect)) && !(can.rect.overlaps(player2.rect))) {
			can.currentFrame = (TextureRegion) can.anime.getKeyFrame(can.elapsedTime, true);
			can.elapsedTime += 3*delta;
			can.rect.y += 4;
			batch.draw(can.currentFrame, can.rect.x, can.rect.y);
		}
		if (can.throwdown == true && !(can.rect.overlaps(player1.rect)) && !(can.rect.overlaps(player2.rect))) {
			can.currentFrame = (TextureRegion) can.anime.getKeyFrame(can.elapsedTime, true);
			can.elapsedTime += 3*delta;
			can.rect.y -= 4;
			batch.draw(can.currentFrame, can.rect.x, can.rect.y);
		}
		if (can.throwright == true && !(can.rect.overlaps(player1.rect)) && !(can.rect.overlaps(player2.rect))) {
			can.currentFrame = (TextureRegion) can.anime.getKeyFrame(can.elapsedTime, true);
			can.elapsedTime += 3*delta;
			can.rect.x += 4;
			batch.draw(can.currentFrame, can.rect.x, can.rect.y);
		}
		if (can.throwleft == true && !(can.rect.overlaps(player1.rect)) && !(can.rect.overlaps(player2.rect))) {
			can.currentFrame = (TextureRegion) can.anime.getKeyFrame(can.elapsedTime, true);
			can.elapsedTime += 3*delta;
			can.rect.x -= 4;
			batch.draw(can.currentFrame, can.rect.x, can.rect.y);
		}
				
		//Bone
		if (bone.pickup == false) {
			bone.currentFrame = (TextureRegion) bone.anime.getKeyFrame(0, true);
			batch.draw(bone.currentFrame, bone.rect.x, bone.rect.y);
		}
		
		//Bone is throwed
		if (bone.throwup == true && !(bone.rect.overlaps(player1.rect)) && !(bone.rect.overlaps(player2.rect))) {
			bone.currentFrame = (TextureRegion) bone.anime.getKeyFrame(bone.elapsedTime, true);
			bone.elapsedTime += 3*delta;
			bone.rect.y += 4;
			batch.draw(bone.currentFrame, bone.rect.x, bone.rect.y);
		}
		if (bone.throwdown == true && !(bone.rect.overlaps(player1.rect)) && !(bone.rect.overlaps(player2.rect))) {
			bone.currentFrame = (TextureRegion) bone.anime.getKeyFrame(bone.elapsedTime, true);
			bone.elapsedTime += 3*delta;
			bone.rect.y -= 4;
			batch.draw(bone.currentFrame, bone.rect.x, bone.rect.y);
		}
		if (bone.throwright == true && !(bone.rect.overlaps(player1.rect)) && !(bone.rect.overlaps(player2.rect))) {
			bone.currentFrame = (TextureRegion) bone.anime.getKeyFrame(bone.elapsedTime, true);
			bone.elapsedTime += 3*delta;
			bone.rect.x += 4;
			batch.draw(bone.currentFrame, bone.rect.x, bone.rect.y);
		}
		if (bone.throwleft == true && !(bone.rect.overlaps(player1.rect)) && !(bone.rect.overlaps(player2.rect))) {
			bone.currentFrame = (TextureRegion) bone.anime.getKeyFrame(bone.elapsedTime, true);
			bone.elapsedTime += 3*delta;
			bone.rect.x -= 4;
			batch.draw(bone.currentFrame, bone.rect.x, bone.rect.y);
		}
		
		
		
		//Player
		batch.draw(player1.currentFrame, player1.body.x, player1.body.y, player1.size_x, player1.size_y);
		batch.draw(player2.currentFrame, player2.body.x, player2.body.y, player2.size_x, player2.size_y);
		//batch.draw(grid, player1.rect.x, player1.rect.y, player1.rect.width, player1.rect.height);
//		batch.draw(grid, player1.topbox.x, player1.topbox.y, player1.topbox.width, player1.topbox.height);
//		batch.draw(grid, player1.downbox.x, player1.downbox.y, player1.downbox.width, player1.downbox.height);
//		batch.draw(grid, player1.rightbox.x, player1.rightbox.y, player1.rightbox.width, player1.rightbox.height);
//		batch.draw(grid, player1.leftbox.x, player1.leftbox.y, player1.leftbox.width, player1.leftbox.height);
		//batch.draw(grid, player2.rect.x, player2.rect.y, player2.rect.width, player2.rect.height);
//		batch.draw(grid, player2.topbox.x, player2.topbox.y, player2.topbox.width, player2.topbox.height);
//		batch.draw(grid, player2.downbox.x, player2.downbox.y, player2.downbox.width, player2.downbox.height);
//		batch.draw(grid, player2.rightbox.x, player2.rightbox.y, player2.rightbox.width, player2.rightbox.height);
//		batch.draw(grid, player2.leftbox.x, player2.leftbox.y, player2.leftbox.width, player2.leftbox.height);
		
		batch.end();
		
		//Behind Things
		int[] layer = {12, 13, 14};
		if (player1.checkbehind == true || player2.checkbehind == true) {
			tiledMapRenderer.render(layer);
		}
		
		//Statusbar
		batch.begin();
		batch.draw(img_stat1, 320, 630);
		batch.draw(img_stat2, 360, 630);
		batch.draw(img_stat1, 1150, 630);
		batch.draw(img_stat2, 1190, 630);
		//item
		if (player1.holding == "Banana")
			batch.draw(img_banana, 327, 637, 25, 25);
		if (player1.holding == "Can")
			batch.draw(can.atlas.findRegion("1"), 327, 637, 25, 25);
		if (player1.holding == "Bone")
			batch.draw(bone.atlas.findRegion("1"), 327, 637, 25, 25);
		if (player1.holding == "Unji")
			batch.draw(img_unjisack, 327, 637, 25, 25);
		if (player1.holding == "Pickaxe")
			batch.draw(img_pickaxe, 327, 637, 25, 25);
		if (player2.holding == "Banana")
			batch.draw(img_banana, 1157, 637, 25, 25);
		if (player2.holding == "Can")
			batch.draw(can.atlas.findRegion("1"), 1157, 637, 25, 25);
		if (player2.holding == "Bone")
			batch.draw(bone.atlas.findRegion("1"), 1157, 637, 25, 25);
		if (player2.holding == "Unji")
			batch.draw(img_unjisack, 1157, 637, 25, 25);
		if (player2.holding == "Pickaxe")
			batch.draw(img_pickaxe, 1157, 637, 25, 25);
		//effect
		if (player1.slow == true)
			batch.draw(img_unji, 367, 637, 25, 25);
		if (player1.speedup > 0)
			batch.draw(img_shoe, 367, 637, 25, 25);
		if (player1.stuck == true)
			batch.draw(img_bananapeel, 367, 637, 25, 25);
		if (player2.slow == true)
			batch.draw(img_unji, 1197, 637, 25, 25);
		if (player2.speedup > 0)
			batch.draw(img_shoe, 1197, 637, 25, 25);
		if (player2.stuck == true)
			batch.draw(img_bananapeel, 1197, 637, 25, 25);
		batch.end();
		
		
		//victory
		batch.begin();
		if (player1.win == true) {
			batch.draw(hunterwin, (w/2)-600, (h/2)-100, 1200, 400);
		}
						
		if (player2.win == true) {
			batch.draw(runnerwin, (w/2)-600, (h/2)-100, 1200, 400);
		}
		batch.end();
		
		//lastvictory
		batch.begin();
		if (player1.score > player2.score && end == true && wintime >= 3)
			batch.draw(lasthunterwin, 0, 0);
		if (player2.score > player1.score && end == true && wintime >= 3)
			batch.draw(lastrunnerwin, 0, 0);
		
		batch.end();
		
		//back to menu
		if (end == true && wintime >= 3) {
			if(Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
				music.stop();
				main.setScreen(new MainMenuScreen(main));
			}
		}
		
		//---------------------------------------------------------------------------------//
		
		
	}

	@Override
	public void dispose() {
		batch.dispose();
		grid.dispose();
		player1.atlas.dispose();
		player2.atlas.dispose();
		tiledMap.dispose();
		wallcrash.dispose();
		music.dispose();
		img_shoe.dispose();
		img_banana.dispose();
		img_bananapeel.dispose();
		hunterwin.dispose();
		runnerwin.dispose();
		hud.dispose();
		img_unjisack.dispose();
		img_unji.dispose();
		img_pickaxe.dispose();
		can.atlas.dispose();
		bone.atlas.dispose();
		boost.dispose();
		throwcan.dispose();
		throwbone.dispose();
		hitcan.dispose();
		hitbone.dispose();
		takeitem.dispose();
		broken.dispose();
		slow.dispose();
		stun.dispose();
		img_stat1.dispose();
		img_stat2.dispose();
		winning.dispose();
		lasthunterwin.dispose();
		lastrunnerwin.dispose();
		escapesound.dispose();
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
}
