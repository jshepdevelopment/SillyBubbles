package com.sillybubbles.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Random;

public class SillyBubblesGame extends Game {

	// create the items and count
	PrizeItem diamondItem = new PrizeItem("Diamond", 1);
	PrizeItem firstAidItem = new PrizeItem("First Aid", 1);
	PrizeItem starItem = new PrizeItem("Star", 1);
	TextureRegion background;
	ParallaxBackground rbg;

    private Game game;
    public 
    // constructor

	// create Bubble as an Actor and show the texture region
	class Bubble extends Actor {

		private TextureRegion _texture;
		private TextureRegion prizeTexture;
		int prizeID = 0;
		int speed = 0;

		public Bubble(TextureRegion texture){

			// set bounds
			_texture = texture;
			setBounds(getX(), getY(), _texture.getRegionWidth(), _texture.getRegionHeight());

			// get input
			this.addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int buttons) {
					//Gdx.app.log("JSLOG", "Touched" + getName());
					//setVisible(false);
					return true;
				}
			});
		}

		// implements draw() completely to handle rotation and scaling
		public void draw(Batch batch, float alpha) {
			batch.draw(prizeTexture,
					getX() + getScaleX() * 110,
					getY() + getScaleY() * 120, getOriginX(), getOriginY(),
					getWidth() / 2, getHeight() / 2, getScaleX(), getScaleY(), getRotation());

			batch.draw(_texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
					getScaleX(), getScaleY(), getRotation());
		}

        // This hit() instead of checking against a bounding box, checks a bounding circle.
        public Actor hit(float x, float y, boolean touchable){
            // If this Actor is hidden or untouchable, it cant be hit
            if(!this.isVisible() || this.getTouchable() == Touchable.disabled)
                return null;

            // get centerpoint of bounding circle, also known as the center of the rect
            float centerX = getWidth()/2;
            float centerY = getHeight()/2;

            // Square roots are bad m'kay. In "real" code, simply square both sides for much speedy fastness
            // This however is the proper, unoptimized and easiest to grok equation for a hit within a circle
            // You could of course use LibGDX's Circle class instead.

            // Calculate radius of circle
            float radius = (float) Math.sqrt(centerX * centerX +
                    centerY * centerY);

            // And distance of point from the center of the circle
            float distance = (float) Math.sqrt(((centerX - x) * (centerX - x))
                    + ((centerY - y) * (centerY - y)));

            // If the distance is less than the circle radius, it's a hit
            if(distance <= radius) {
				// check for justTouched will prevent holding hits
				if(Gdx.input.justTouched()) {
					Gdx.app.log("JSLOG", "bubble " + this + " hit!");
					if(this.prizeID==1) {
						diamondItem.itemCount++;
						Gdx.app.log("JSLOG", diamondItem.getItemCount() + " Diamonds collected.");
					}
					if(this.prizeID==2) {
						firstAidItem.itemCount++;
						Gdx.app.log("JSLOG", firstAidItem.getItemCount() + " First-aid collected.");
					}
					if(this.prizeID==3) {
						starItem.itemCount++;
						Gdx.app.log("JSLOG", starItem.getItemCount() + " Stars collected.");
					}
					this.reset();
				}
				return this;
			}

            // Otherwise, it isnt
            return null;
        }

		@Override
		public void act(float delta){

			this.setPosition(this.getX(), this.getY() + this.speed);
			//Gdx.app.log("JSLOG", "this.getY() " + this.getY() + " this.getX() " + this.getX());

			if(this.getY() > Gdx.graphics.getHeight() + _texture.getRegionHeight()) {


				this.reset();
			}

		}

		public void reset() {

			Random random = new Random();
			int randomPrize = random.nextInt(10) + 1;

			// set prize
			if (randomPrize == 1) {
				this.prizeTexture = new TextureRegion(new Texture("diamond.png"));
				this.prizeID = 1;
			}
			if (randomPrize == 2) {
				this.prizeTexture = new TextureRegion(new Texture("firstaid.png"));
				this.prizeID = 2;
			}
			if (randomPrize == 3) {
				this.prizeTexture = new TextureRegion(new Texture("star.png"));
				prizeID = 3;
			}
			if (randomPrize > 3) {
				this.prizeTexture = new TextureRegion(new Texture("empty.png"));
				prizeID = 0;// no prize ID is no prize
			}

			//Assign the position of the bubble to a random value within the screen boundaries
			int randomX = random.nextInt(Gdx.graphics.getWidth());
			this.setVisible(true);
			//this.setScale(random.nextFloat());
			this.speed =  random.nextInt(20) + 10;
			this.setPosition(randomX, -3000);

			//Gdx.app.log("JSLOG", "diamondItem.itemCount " + diamondItem.itemCount);
			//Gdx.app.log("JSLOG", "firstAidItem.itemCount " + firstAidItem.itemCount);
			//Gdx.app.log("JSLOG", "starItem.itemCount " + starItem.itemCount);

		}
	}


	private Stage stage;

	@Override
	public void create() {

        Preferences prefs = Gdx.app.getPreferences("BubblePrefs"); // load the prefs file
        diamondItem.itemCount = prefs.getInteger(diamondItem.itemName);
        firstAidItem.itemCount = prefs.getInteger(firstAidItem.itemName);
        starItem.itemCount = prefs.getInteger(starItem.itemName);

        background = new TextureRegion(new Texture("hills.png"));

		Bubble[] bubbles;
		int bubbleCount = 15;

		// stage = new Stage(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
		stage = new Stage(new ScreenViewport());
		final TextureRegion bubbleTexture = new TextureRegion(new Texture("bubble.png"));
		rbg = new ParallaxBackground(new ParallaxLayer[]{
				//	new ParallaxLayer(background, new Vector2(),new Vector2(0, 0)),
				new ParallaxLayer(background,new Vector2(1.0f,1.0f),new Vector2(0, 500)),
				//	new ParallaxLayer(background,new Vector2(0.1f,0),new Vector2(0, stage.getViewport().getScreenHeight()-200),new Vector2(0, 0)),
		}, stage.getViewport().getScreenWidth(), stage.getViewport().getScreenHeight(), new Vector2(150,0));

		// random number seed
		Random random = new Random();

		// initialize arrays
		bubbles = new Bubble[bubbleCount];
		// moveActions = new MoveToAction[bubbleCount];

		// make 10 bubble objects at random on screen locations
		for(int i = 0; i < bubbleCount; i++){
			bubbles[i] = new Bubble(bubbleTexture);
			//moveActions[i] = new MoveToAction();

			// random.nextInt(Gdx.graphics.getWidth());
			bubbles[i].setScale(random.nextFloat() * 2);
			bubbles[i].speed =  random.nextInt(20) + 10;
			bubbles[i].setPosition(bubbles[i].getX(), -3000);

			// set the name of the bubble to it's index within the loop
			bubbles[i].setName(Integer.toString(i));
			bubbles[i].reset();

			// add to the stage
			stage.addActor(bubbles[i]);

		}

		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		rbg.render(Gdx.graphics.getDeltaTime());
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		rbg = new ParallaxBackground(new ParallaxLayer[]{
				//	new ParallaxLayer(background, new Vector2(),new Vector2(0, 0)),
				new ParallaxLayer(background,new Vector2(1.0f,1.0f),new Vector2(0, 500)),
				//	new ParallaxLayer(background,new Vector2(0.1f,0),new Vector2(0, stage.getViewport().getScreenHeight()-200),new Vector2(0, 0)),
		}, stage.getViewport().getScreenWidth(), stage.getViewport().getScreenHeight()/2, new Vector2(150,0));
	}

	@Override
	public void pause() {

        Preferences prefs = Gdx.app.getPreferences("BubblePrefs");// We store the value 10 with the key of "highScore"
        prefs.putInteger(diamondItem.itemName, diamondItem.itemCount);
        prefs.putInteger(firstAidItem.itemName, firstAidItem.itemCount);
        prefs.putInteger(starItem.itemName, starItem.itemCount);
        prefs.flush(); // saves the preferences file

        Gdx.app.log("JSLOG", "Game Paused.");
		Gdx.app.log("JSLOG", "You have " + diamondItem.getItemCount() + " diamonds.");
		Gdx.app.log("JSLOG", "You have " + firstAidItem.getItemCount() + " first-aids.");
		Gdx.app.log("JSLOG", "You have " + starItem.getItemCount() + " stars.");

	}

	@Override
	public void resume() {
		Gdx.app.log("JSLOG", "Game On.");
	}
}