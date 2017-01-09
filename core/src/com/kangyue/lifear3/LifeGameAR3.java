package com.kangyue.lifear3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

import com.badlogic.gdx.math.Rectangle;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class LifeGameAR3 extends ApplicationAdapter {

	private Texture dropImage;
	private Texture bucketImage;
	private Sound dropSound;
	private Music rainMusic;
	private SpriteBatch batch;

	private OrthographicCamera camera;

	private Rectangle bucket;
	private ArrayList<Rectangle> raindrops;
	private long lastDropTime;
	
	@Override
	public void create () {
		// load the image for the dropImageand the bucket. 64x64 pixel each
		dropImage = new Texture(Gdx.files.internal("droplet.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));

		// load the drop sound effect and the rain background music
		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		// start the playback of the background "music"
		rainMusic.setLooping(true);
		rainMusic.play();

		// create the camera and the Sprite Batch
		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,480);
		batch = new SpriteBatch();

		bucket = new Rectangle();
		bucket.x = 800/2 -64/2;
		bucket.y = 20;
		bucket.width = 64;
		bucket.height = 64;

		// create the raindrops array and spawn the first raindrop
		raindrops = new ArrayList<Rectangle>();
		spawnRaindDrop();
	}

	private void spawnRaindDrop(){
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0,800-64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();


	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		batch.draw(bucketImage, bucket.x, bucket.y);
		for(Rectangle raindrop: raindrops) {
			batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		batch.end();

		if(Gdx.input.isTouched()){

			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(),Gdx.input.getY(),0);
			camera.unproject(touchPos);
			bucket.x = (int) (touchPos.x -64/2);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
			bucket.x -=200*Gdx.graphics.getDeltaTime();

		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			bucket.x +=200*Gdx.graphics.getDeltaTime();

		//MAKE SURE THE BUCKET STAYS WITHIN THE SCREEN BOUNDS
		if(bucket.x < 0) bucket.x = 0;
		if(bucket.x > 800 - 64) bucket.x = 800-64;

		// check if we need to create a new raindrop
		if(TimeUtils.nanoTime() - lastDropTime > 1000000000){
			spawnRaindDrop();
		}

		// move the raindrops , remove any that are beneath the button edge of
		// the screen or thar hit the bucket. In the later case we play back
		// a sound effect as well.

		Iterator<Rectangle> iter = raindrops.iterator();
		while (iter.hasNext()){
			Rectangle raindrop = iter.next() ;
			raindrop.y -=200*Gdx.graphics.getDeltaTime();
			if(raindrop.y + 64 < 0) iter.remove();
			if(raindrop.overlaps(bucket)){
				dropSound.play();
				iter.remove();
			}
		}


	}
	
	@Override
	public void dispose () {

		// dispose of all the native resources
		batch.dispose();
		bucketImage.dispose();
		dropImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();

	}
}
