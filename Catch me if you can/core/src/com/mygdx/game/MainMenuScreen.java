package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MainMenuScreen implements Screen{
	final Main main;
	
	public SpriteBatch batch;
	public TextureAtlas menu, start, exit, title;
	public TextureRegion menucurrentFrame, startcurrentFrame, exitcurrentFrame, titlecurrentFrame;
	public Animation menu_anime, start_anime, exit_anime, title_anime;
	public float menutime = 0, titletime = 0, delay = 0;
	
	public MainMenuScreen(Main main) {
		this.main = main;
		
		//Sprite, Batch, Texture, Atlas
		batch = main.batch;
		menu = new TextureAtlas(Gdx.files.internal("menu/menu.atlas"));
		start = new TextureAtlas(Gdx.files.internal("start/start.atlas"));
		exit = new TextureAtlas(Gdx.files.internal("exit/exit.atlas"));
		title = new TextureAtlas(Gdx.files.internal("title/title.atlas"));
		//Animation
		menu_anime = new Animation(1.5f, menu.getRegions());
		start_anime = new Animation(1/2f, start.getRegions());
		exit_anime = new Animation(1/2f, exit.getRegions());
		title_anime = new Animation(0.6f, title.findRegion("title1"),title.findRegion("title1"), title.findRegion("title1")
					,title.findRegion("title1"), title.findRegion("title2"),title.findRegion("title3"),title.findRegion("title4")
					,title.findRegion("title5"),title.findRegion("title6"),title.findRegion("title5"),title.findRegion("title4")
					,title.findRegion("title3"),title.findRegion("title2"));
	}

	@Override
	public void render(float delta) {
		//Clear
		Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //----------------------------------------//
       
			menucurrentFrame = (TextureRegion) menu_anime.getKeyFrame(menutime, true);
			startcurrentFrame = (TextureRegion) start_anime.getKeyFrame(0, true);
			exitcurrentFrame = (TextureRegion) exit_anime.getKeyFrame(0, true);
			titlecurrentFrame = (TextureRegion) title_anime.getKeyFrame(titletime, true);
			
			titletime += 6*delta;
			menutime += 10*Gdx.graphics.getDeltaTime();
			//Move Mouse//
			if(Gdx.input.getX() >= (1280/2)-100 && Gdx.input.getX() <= (1280/2)+100) {
				if (Gdx.input.getY() >= (680/2)+65 && Gdx.input.getY() <= (680/2)+65+65) {
					startcurrentFrame = (TextureRegion) start_anime.getKeyFrame(0.5f, true);
				}
				if (Gdx.input.getY() >= (680/2)+65+100 && Gdx.input.getY() <= (680/2)+65+100+65) {
					exitcurrentFrame = (TextureRegion) exit_anime.getKeyFrame(0.5f, true);
				}
			}
			
			
			//Control//
			if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
		        if(Gdx.input.getX() >= (1280/2)-100 && Gdx.input.getX() <= (1280/2)+100) {
		        	//Start
		        	if (Gdx.input.getY() >= (680/2)+65 && Gdx.input.getY() <= (680/2)+65+65) {
		        		startcurrentFrame = (TextureRegion) start_anime.getKeyFrame(0.5f, true);
		        		delay += 0.5;
		        		if (delay > 2) {
		        			main.setScreen(new Chapter1(main));
		        			dispose();
		        		}
		        	}
		        	//Exit
		        	if (Gdx.input.getY() >= (680/2)+65+100 && Gdx.input.getY() <= (680/2)+65+100+65) {
		        		exitcurrentFrame = (TextureRegion) exit_anime.getKeyFrame(0.5f, true);
		        		delay += 0.5;
		        		if (delay > 2) {
		        			Gdx.app.exit();
		        			dispose();
		        		}
		        	}
		        	
		        }
		    }
			//-------------------------------------------------------------------------------//
			
			//Graphics Rendering//
			batch.begin();
			batch.draw(menucurrentFrame, 0, 0, 1280, 680);
			batch.draw(titlecurrentFrame, 290, 300, 700, 250);
			batch.draw(startcurrentFrame, (1280/2)-100, (680/2)-200, 200, 200);
			batch.draw(exitcurrentFrame, (1280/2)-100, (680/2)-300, 200, 200);
			batch.end();
			//---------------------------------------------------------------------------------//
        
        
	}

	@Override
	public void dispose() {
		menu.dispose();
		start.dispose();
		exit.dispose();
		
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
