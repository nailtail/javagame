package com.mygdx.game.desktop;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Pic_Sel implements ApplicationListener {
    private SpriteBatch batch;
    private TextureAtlas textureAtlas;
    private Sprite sprite;
    private int currentFrame = 1;
    private String currentAtlasKey = new String("0001");
    
    @Override
    public void create() {        
        batch = new SpriteBatch();
        textureAtlas = new TextureAtlas(Gdx.files.internal("anime.atlas"));
        AtlasRegion region = textureAtlas.findRegion("0001");
        sprite = new Sprite(region);
        sprite.setPosition(120, 100);
        sprite.scale(2.5f);
        Timer.schedule(new Task(){
                @Override
                public void run() {
                    currentFrame++;
                    if(currentFrame > 20)
                        currentFrame = 1;
                    
                    // ATTENTION! String.format() doesnt work under GWT for god knows why...
 //                  currentAtlasKey = String.format("%04d", currentFrame);
                   sprite.setRegion(textureAtlas.findRegion(currentAtlasKey));
                }
            }
            ,0,1/30.0f);
    }

    @Override
    public void dispose() {
        batch.dispose();
        textureAtlas.dispose();
    }

    @Override
    public void render() {        
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}