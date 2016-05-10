package com.sillybubbles.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

public class SillyBubblesGame extends Game {

    // Load music and sound effects
    Music mp3Music;
    Sound bubblePopSound;
    Sound applauseSound;

    public static float VOLUME = 1.0f;
    public static boolean soundOn = true;

    // adding a sound button
    TextureRegion soundButtonTextureOn;
    // adding a sound button
    TextureRegion soundButtonTextureOff;

    public enum ScreenType {
        LDPI, MDPI, HDPI, XHDPI, XXHDPI, XXXHDPI
    }

    // Stages for drawing
    private Stage stage;
    private Stage menuStage;
    private Stage waitingStage;

	// create the items and count
    PrizeItem bubbleItem = new PrizeItem("Bubble", 0);
    PrizeItem diamondItem = new PrizeItem("Diamond", 0);
	PrizeItem firstAidItem = new PrizeItem("First Aid", 0);
	PrizeItem starItem = new PrizeItem("Star", 0);
    PrizeItem bookItem = new PrizeItem("Book", 0);
    PrizeItem crystalItem = new PrizeItem("Crystal", 0);
    PrizeItem ringItem = new PrizeItem("Ring", 0);
    PrizeItem jewel1Item = new PrizeItem("Jewel1", 0);
    PrizeItem jewel2Item = new PrizeItem("Jewel2", 0);
    PrizeItem jewel3Item = new PrizeItem("Jewel3", 0);

    PrizeItem dragonItem = new PrizeItem("dragon", 0);
    PrizeItem redDiamondItem = new PrizeItem("Red Diamond", 0);
    PrizeItem cameraItem = new PrizeItem("Camera", 0);
    PrizeItem purpleBookItem = new PrizeItem("Purple Book", 0);
    PrizeItem appleItem = new PrizeItem("Apple", 0);
    PrizeItem brownBookItem = new PrizeItem("Brown Book", 0);
    PrizeItem breadItem = new PrizeItem("Bread", 0);
    PrizeItem burgerItem = new PrizeItem("Burger", 0);
    PrizeItem pizzaItem = new PrizeItem("Pizza", 0);
    PrizeItem rocketPenguinItem = new PrizeItem("Rocket Penguin", 0);

    // item labels to display item count on collection/menu screen
    Label bubbleLabel;
    Label diamondLabel;
    Label firstAidLabel;
    Label starLabel;
    Label bookLabel;
    Label crystalLabel;
    Label ringLabel;
    Label jewel1Label;
    Label jewel2Label;
    Label jewel3Label;
    Label dragonLabel;
    Label redDiamondLabel;
    Label cameraLabel;
    Label purpleBookLabel;
    Label appleLabel;
    Label brownBookLabel;
    Label breadLabel;
    Label burgerLabel;
    Label pizzaLabel;
    Label rocketPenguinLabel;


    Label collectionLabel;
    Label niceLabel;

    Label waitingLabel;
    Label screenshotSavedLabel;
    Label waitingCounterLabel;

    long startTime;
    long elapsedTime;

	TextureRegion background;
	ParallaxBackground rbg;

    BitmapFont textFont;
    BitmapFont textFontLarge;

    boolean playing = true; // flag to switch between playing and checking items
    boolean waiting = false;
    boolean needScreenshot = false;
    boolean firstRun = true; // this is the first run of the game

    class BubbleButton extends Actor {
        private TextureRegion _texture;

        public BubbleButton(TextureRegion texture){            // set bounds
            _texture = texture;
            setBounds(getX(), getY(), _texture.getRegionWidth(), _texture.getRegionHeight());

            // get input
            this.addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int buttons) {
                    return true;
                }
            });
        }

        // implements draw() completely to handle rotation and scaling
        public void draw(Batch batch, float alpha) {
            batch.draw(_texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                    getScaleX(), getScaleY(), getRotation());
        }

        public Actor hit(float x, float y, boolean touchable) {

            // get centerpoint of bounding circle, also known as the center of the rect
            float centerX = getWidth() / 2;
            float centerY = getHeight() / 2;

            // Calculate radius of circle
            float radius = (float) Math.sqrt(centerX * centerX +
                    centerY * centerY);

            // And distance of point from the center of the circle
            float distance = (float) Math.sqrt(((centerX - x) * (centerX - x))
                    + ((centerY - y) * (centerY - y)));

            // If the distance is less than the circle radius, it's a hit
            if (distance <= radius) {
                if (Gdx.input.justTouched()) {
                        Gdx.app.log("JSLOG", "Bubble button pressed.");

                        bubbleLabel.setText(" " + bubbleItem.getItemCount());
                        diamondLabel.setText(" " + diamondItem.getItemCount());
                        firstAidLabel.setText(" " + firstAidItem.getItemCount());
                        starLabel.setText(" " + starItem.getItemCount());
                        bookLabel.setText(" " + bookItem.getItemCount());
                        crystalLabel.setText(" " + crystalItem.getItemCount());
                        ringLabel.setText(" " + ringItem.getItemCount());
                        jewel1Label.setText(" " + jewel1Item.getItemCount());
                        jewel2Label.setText(" " + jewel2Item.getItemCount());
                        jewel3Label.setText(" " + jewel3Item.getItemCount());
                        dragonLabel.setText(" " + dragonItem.getItemCount());
                        redDiamondLabel.setText(" " + redDiamondItem.getItemCount());
                        cameraLabel.setText(" " + cameraItem.getItemCount());
                        purpleBookLabel.setText(" " + purpleBookItem.getItemCount());
                        appleLabel.setText(" " + appleItem.getItemCount());
                        brownBookLabel.setText(" " + brownBookItem.getItemCount());
                        breadLabel.setText(" " + breadItem.getItemCount());
                        burgerLabel.setText(" " + burgerItem.getItemCount());
                        pizzaLabel.setText(" " + pizzaItem.getItemCount());
                        rocketPenguinLabel.setText(" " + rocketPenguinItem.getItemCount());

                        Gdx.input.setInputProcessor(menuStage);
                        playing = false;
                        applauseSound.play(VOLUME);
                        Gdx.app.log("JSLOG", "playing should be false,  playing is " + playing);
                }
            }
            // button not pressed, return null
            return null;
        }

        @Override
        public void act(float delta) {
            this.setPosition(getParent().getX(), getParent().getY());//Gdx.graphics.getHeight() - _texture.getRegionHeight());
        }
    }

    class BubbleBackButton extends Actor {
        private TextureRegion _texture;

        public BubbleBackButton(TextureRegion texture){
            // set bounds
            _texture = texture;
            setBounds(getX(), getY(), _texture.getRegionWidth(), _texture.getRegionHeight());

            // get input
            this.addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int buttons) {
                    return true;
                }
            });
        }

        // implements draw() completely to handle rotation and scaling
        public void draw(Batch batch, float alpha) {
            batch.draw(_texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                    getScaleX(), getScaleY(), getRotation());
        }

        public Actor hit(float x, float y, boolean touchable) {

            // get centerpoint of bounding circle, also known as the center of the rect
            float centerX = getWidth() / 2;
            float centerY = getHeight() / 2;

            // Calculate radius of circle
            float radius = (float) Math.sqrt(centerX * centerX +
                    centerY * centerY);

            // And distance of point from the center of the circle
            float distance = (float) Math.sqrt(((centerX - x) * (centerX - x))
                    + ((centerY - y) * (centerY - y)));

            // If the distance is less than the circle radius, it's a hit
            if (distance <= radius) {
                if (Gdx.input.justTouched()) {
                    Gdx.app.log("JSLOG", "Bubble back button pressed.");
                    Gdx.input.setInputProcessor(stage);
                    playing = true;
                    Gdx.app.log("JSLOG", "playing should be true, playing is " + playing);
                    screenshotSavedLabel.remove();
                }
            }

            // button not pressed, return null
            return null;
        }

        @Override
        public void act(float delta) {
            this.setPosition(0, 0);//Gdx.graphics.getHeight() - _texture.getRegionHeight());
        }
    }

    //screenshot button
    class ScreenShotButton extends Actor {
        private TextureRegion _texture;

        public ScreenShotButton(TextureRegion texture){
            // set bounds
            _texture = texture;
            setBounds(getX(), getY(), _texture.getRegionWidth(), _texture.getRegionHeight());

            // get input
            this.addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int buttons) {
                    return true;
                }
            });
        }

        // implements draw() completely to handle rotation and scaling
        public void draw(Batch batch, float alpha) {
            batch.draw(_texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                    getScaleX(), getScaleY(), getRotation());
        }

        public Actor hit(float x, float y, boolean touchable) {

            // get centerpoint of bounding circle, also known as the center of the rect
            float centerX = getWidth() / 2;
            float centerY = getHeight() / 2;

            // Calculate radius of circle
            float radius = (float) Math.sqrt(centerX * centerX +
                    centerY * centerY);

            // And distance of point from the center of the circle
            float distance = (float) Math.sqrt(((centerX - x) * (centerX - x))
                    + ((centerY - y) * (centerY - y)));

            // If the distance is less than the circle radius, it's a hit
            if (distance <= radius) {
                if (Gdx.input.justTouched()) {
                    Gdx.app.log("JSLOG", "Screenshot button pressed.");
                    needScreenshot = true;
                }
            }

            // button not pressed, return null
            return null;
        }

        @Override
        public void act(float delta) {
            //this.setPosition(getParent().getX(), getHeight());//Gdx.graphics.getHeight() - _texture.getRegionHeight());
            //this.setPosition(Gdx.graphics.getWidth()-this.getWidth(), 0);//Gdx.graphics.getHeight() - _texture.getRegionHeight());
        }
    }

    //sound button
    class SoundButton extends Actor {
        private TextureRegion _texture;

        public SoundButton(TextureRegion texture){
            // set bounds
            _texture = texture;
            setBounds(getX(), getY(), _texture.getRegionWidth(), _texture.getRegionHeight());

            // get input
            this.addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int buttons) {
                    return true;
                }
            });
        }

        // implements draw() completely to handle rotation and scaling
        public void draw(Batch batch, float alpha) {
            batch.draw(_texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                    getScaleX(), getScaleY(), getRotation());
        }

        public Actor hit(float x, float y, boolean touchable) {

            // get centerpoint of bounding circle, also known as the center of the rect
            float centerX = getWidth() / 2;
            float centerY = getHeight() / 2;

            // Calculate radius of circle
            float radius = (float) Math.sqrt(centerX * centerX +
                    centerY * centerY);

            // And distance of point from the center of the circle
            float distance = (float) Math.sqrt(((centerX - x) * (centerX - x))
                    + ((centerY - y) * (centerY - y)));

            // If the distance is less than the circle radius, it's a hit
            if (distance <= radius) {
                if (Gdx.input.justTouched()) {
                    Gdx.app.log("JSLOG", "Sound button pressed.");

                    if (soundOn) {
                        this._texture = soundButtonTextureOff;
                        mp3Music.stop();
                        muteFX();
                    }

                    else if (!soundOn) {
                        this._texture = soundButtonTextureOn;
                        mp3Music.play();
                        normalizeFX();
                    }
                }
            }

            // button not pressed, return null
            return null;
        }

        @Override
        public void act(float delta) {
        }
    }

	// create Bubble as an Actor and show the texture region
	class Bubble extends Actor {

		private TextureRegion texture;
		//private TextureRegion prizeTexture;

        //prize texture upon initialization
        TextureRegion prizeTexture = new TextureRegion(new Texture("empty.png"));

        // loading all textures
        TextureRegion diamondTexture = new TextureRegion(new Texture("diamond.png"));
        TextureRegion firstAidTexture = new TextureRegion(new Texture("firstaid.png"));
        TextureRegion starTexture = new TextureRegion(new Texture("star.png"));
        TextureRegion bookTexture = new TextureRegion(new Texture("book.png"));
        TextureRegion crystalTexture = new TextureRegion(new Texture("crystal.png"));
        TextureRegion ringTexture = new TextureRegion(new Texture("ring.png"));
        TextureRegion jewel1Texture = new TextureRegion(new Texture("jewel1.png"));
        TextureRegion jewel2Texture = new TextureRegion(new Texture("jewel2.png"));
        TextureRegion jewel3Texture = new TextureRegion(new Texture("jewel3.png"));
        TextureRegion dragonTexture = new TextureRegion(new Texture("dragon.png"));
        TextureRegion redDiamondTexture = new TextureRegion(new Texture("reddiamond.png"));
        TextureRegion cameraTexture = new TextureRegion(new Texture("camera.png"));
        TextureRegion purpleBookTexture = new TextureRegion(new Texture("purplebook.png"));
        TextureRegion appleTexture = new TextureRegion(new Texture("apple.png"));
        TextureRegion brownBookTexture = new TextureRegion(new Texture("brownbook.png"));
        TextureRegion breadTexture = new TextureRegion(new Texture("bread.png"));
        TextureRegion burgerTexture = new TextureRegion(new Texture("burger.png"));
        TextureRegion pizzaTexture = new TextureRegion(new Texture("pizza.png"));
        TextureRegion rocketPenguinTexture = new TextureRegion(new Texture("rocketpenguin.png"));

        TextureRegion bombTexture = new TextureRegion(new Texture("bomb.png"));
        TextureRegion emptyTexture = new TextureRegion(new Texture("empty.png"));

        TextureRegion bubbleWhiteTexture = new TextureRegion(new Texture("bubblewhite.png"));
        TextureRegion bubbleBlackTexture = new TextureRegion(new Texture("bubbleblack.png"));
        TextureRegion bubbleBlueTexture = new TextureRegion(new Texture("bubbleblue.png"));
        TextureRegion bubbleGreenTexture = new TextureRegion(new Texture("bubblegreen.png"));
        TextureRegion bubbleOrangeTexture = new TextureRegion(new Texture("bubbleorange.png"));
        TextureRegion bubblePurpleTexture = new TextureRegion(new Texture("bubblepurple.png"));
        TextureRegion bubbleRedTexture = new TextureRegion(new Texture("bubblered.png"));
        TextureRegion bubbleYellowTexture = new TextureRegion(new Texture("bubbleyellow.png"));

		int prizeID = 0;
		int speed = 0;
        ParticleEffect pe = new ParticleEffect();

        int speedModifier;
        float scaleModifier;

		public Bubble(int speedModifier, float scaleModifier){

            this.speedModifier = speedModifier;
            this.scaleModifier = scaleModifier;

            this.pe.load(Gdx.files.internal("bubblepop.p"), Gdx.files.internal(""));
            this.pe.getEmitters().first().setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
            //this.pe.scaleEffect(scaleModifier);

            Random random = new Random();
            int randomBubble = random.nextInt(8) + 1;

            // set a random texture
            if (randomBubble == 1) this.texture = bubbleWhiteTexture;
            if (randomBubble == 2) this.texture = bubbleBlackTexture;
            if (randomBubble == 3) this.texture = bubbleBlueTexture;
            if (randomBubble == 4) this.texture = bubbleGreenTexture;
            if (randomBubble == 5) this.texture = bubbleOrangeTexture;
            if (randomBubble == 6) this.texture = bubblePurpleTexture;
            if (randomBubble == 7) this.texture = bubbleRedTexture;
            if (randomBubble == 8) this.texture = bubbleYellowTexture;

            // set bounds
            this.setBounds(getX(), getY(), texture.getRegionWidth(), texture.getRegionHeight());

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
					getX() + getScaleX() * 80,
					getY() + getScaleY() * 80, getOriginX(), getOriginY(),
					getWidth() / 2, getHeight() / 2, getScaleX(), getScaleY(), getRotation());

			batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                    getScaleX(), getScaleY(), getRotation());
//            batch.begin();
            this.pe.draw(batch);
           // batch.end();
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
                    // play bubble pop sound
                    bubblePopSound.play(VOLUME);
					//Gdx.app.log("JSLOG", "bubble " + this + " hit!");
					if(this.prizeID==1) {
						diamondItem.itemCount++;
						Gdx.app.log("JSLOG", diamondItem.getItemCount() + " Diamonds collected.");
					}
					if(this.prizeID==2) {
						firstAidItem.itemCount++;
                        if(adsController.isWifiConnected()) {
                            adsController.hideBannerAd();
                        }
						Gdx.app.log("JSLOG", firstAidItem.getItemCount() + " First-aid collected.");
                        Gdx.app.log("JSLOG", firstAidItem.getItemCount() + " Banner ad removed.");
					}
					if(this.prizeID==3) {
						starItem.itemCount++;
						Gdx.app.log("JSLOG", starItem.getItemCount() + " Stars collected.");
					}
                    if(this.prizeID==4) {
                        bookItem.itemCount++;
                        Gdx.app.log("JSLOG", bookItem.getItemCount() + " Books collected.");
                    }
                    if(this.prizeID==5) {
                        crystalItem.itemCount++;
                        Gdx.app.log("JSLOG", crystalItem.getItemCount() + " Crystals collected.");
                    }
                    if(this.prizeID==6) {
                        ringItem.itemCount++;
                        Gdx.app.log("JSLOG", ringItem.getItemCount() + " Rings collected.");
                    }
                    if(this.prizeID==7) {
                        jewel1Item.itemCount++;
                        Gdx.app.log("JSLOG", jewel1Item.getItemCount() + " Jewel1s collected.");
                    }
                    if(this.prizeID==8) {
                        jewel2Item.itemCount++;
                        Gdx.app.log("JSLOG", jewel2Item.getItemCount() + " Jewel2s collected.");
                    }
                    if(this.prizeID==9) {
                        jewel3Item.itemCount++;
                        Gdx.app.log("JSLOG", jewel3Item.getItemCount() + " Jewel3s collected.");
                    }
                    if(this.prizeID==10) {
                        dragonItem.itemCount++;
                        Gdx.app.log("JSLOG", dragonItem.getItemCount() + " Dragons collected.");
                    }
                    if(this.prizeID==11) {
                        redDiamondItem.itemCount++;
                        Gdx.app.log("JSLOG", redDiamondItem.getItemCount() + " Red Diamonds collected.");
                    }
                    if(this.prizeID==12) {
                        cameraItem.itemCount++;
                        Gdx.app.log("JSLOG", cameraItem.getItemCount() + " Cameras collected.");
                    }
                    if(this.prizeID==13) {
                        purpleBookItem.itemCount++;
                        Gdx.app.log("JSLOG", purpleBookItem.getItemCount() + " Purple Books collected.");
                    }
                    if(this.prizeID==14) {
                        appleItem.itemCount++;
                        Gdx.app.log("JSLOG", appleItem.getItemCount() + " Apples collected.");
                    }
                    if(this.prizeID==15) {
                        brownBookItem.itemCount++;
                        Gdx.app.log("JSLOG", brownBookItem.getItemCount() + " Brown Books collected.");
                    }
                    if(this.prizeID==16) {
                        breadItem.itemCount++;
                        Gdx.app.log("JSLOG", breadItem.getItemCount() + " Breads collected.");
                    }
                    if(this.prizeID==17) {
                        burgerItem.itemCount++;
                        Gdx.app.log("JSLOG", burgerItem.getItemCount() + " Burgers collected.");
                    }
                    if(this.prizeID==18) {
                        pizzaItem.itemCount++;
                        Gdx.app.log("JSLOG", pizzaItem.getItemCount() + " Zas collected.");
                    }
                    if(this.prizeID==19) {
                        // this is not actually a prize, collecting a bomb halts game for 5 seconds
                        Gdx.app.log("JSLOG", "Boom!");
                        waiting = true;
                        if(adsController.isWifiConnected()) {
                            adsController.showBannerAd();
                        }
                        startTime = TimeUtils.millis();
                    }
                    if(this.prizeID==20) {
                        rocketPenguinItem.itemCount++;
                        Gdx.app.log("JSLOG", rocketPenguinItem.getItemCount() + "Rocket penguins collected.");
                    }
                    // pop the bubble
                    Gdx.input.vibrate(25);
                    bubbleItem.itemCount++;
                    Gdx.app.log("JSLOG", bubbleItem.getItemCount() + " bubbles popped.");
                    this.pe.setPosition(this.getX() + getScaleX() * 160, this.getY() + getScaleY() * 160);
                    this.pe.start();
                    this.reset();
				}
				return this;
			}
            // Otherwise, it isn't
            return null;
        }

		@Override
        public void act(float delta) {

            if(waiting) {
                elapsedTime = TimeUtils.timeSinceMillis(startTime);
                //Gdx.app.log("JSLOG", "elapsed time is " + elapsedTime);

                waitingCounterLabel.setText("" + (6 - (1 + elapsedTime / 1000)));

                //stop waiting after 5 seconds
                if(elapsedTime > 5000)waiting = false;

            }

            this.setPosition(this.getX(), this.getY() + this.speed);

            // if the bubble wanders to far up the y axis, reset it
            if(this.getY() > Gdx.graphics.getHeight() + texture.getRegionHeight()) {
                this.reset();
			}

            // update particle effects
            pe.update(Gdx.graphics.getDeltaTime());

        }

		public void reset() {

			Random random = new Random();
			int randomPrize = random.nextInt(50000) + 1;

            // special debug flag
            boolean debug = false;

            // bool for prize is set
            boolean hasPrize = false;

			// set prize
			if (randomPrize > 6000 && randomPrize <= 9000) {
				this.prizeTexture = diamondTexture;
				this.prizeID = 1;
                hasPrize = true;
                if (debug) diamondItem.itemCount++;
			}
			if (randomPrize > 0 && randomPrize <=  3000) {
				this.prizeTexture = firstAidTexture;
				this.prizeID = 2;
                hasPrize = true;
                if (debug) firstAidItem.itemCount++;
            }
			if (randomPrize > 3000 && randomPrize <= 6000) {
				this.prizeTexture = starTexture;
				prizeID = 3;
                hasPrize = true;
                if (debug) starItem.itemCount++;
            }
            if (randomPrize > 29000 && randomPrize <= 30000) {
                this.prizeTexture = bookTexture;
                this.prizeID = 4;
                hasPrize = true;
                if (debug) bookItem.itemCount++;
            }
            if (randomPrize > 23000 && randomPrize <= 25000) {
                this.prizeTexture = crystalTexture;
                this.prizeID = 5;
                hasPrize = true;
                if (debug) crystalItem.itemCount++;
            }
            if (randomPrize >= 49995) {
                this.prizeTexture = ringTexture;
                prizeID = 6;
                hasPrize = true;
                if (debug) ringItem.itemCount++;
            }
            if (randomPrize > 31500 && randomPrize <= 32000) {
                this.prizeTexture = jewel1Texture;
                prizeID = 7;
                hasPrize = true;
                if (debug) jewel1Item.itemCount++;
            }
            if (randomPrize > 31000 && randomPrize <= 31500) {
                this.prizeTexture = jewel2Texture;
                prizeID = 8;
                hasPrize = true;
                if (debug) jewel2Item.itemCount++;
            }
            if (randomPrize > 21000 && randomPrize <= 23000) {
                this.prizeTexture = jewel3Texture;
                prizeID = 9;
                hasPrize = true;
                if (debug) jewel3Item.itemCount++;
            }
            if (randomPrize > 34900 && randomPrize <= 35000) {
                this.prizeTexture = dragonTexture;
                this.prizeID = 10;
                hasPrize = true;
                if (debug) dragonItem.itemCount++;
            }
            if (randomPrize > 32000 && randomPrize <= 32250) {
                this.prizeTexture = redDiamondTexture;
                this.prizeID = 11;
                hasPrize = true;
                if (debug) redDiamondItem.itemCount++;
            }
            if (randomPrize > 30000 && randomPrize <= 31000) {
                this.prizeTexture = cameraTexture;
                prizeID = 12;
                hasPrize = true;
                if (debug) cameraItem.itemCount++;
            }
            if (randomPrize > 25000 && randomPrize <= 27000 ) {
                this.prizeTexture = purpleBookTexture;
                this.prizeID = 13;
                hasPrize = true;
                if (debug) purpleBookItem.itemCount++;
            }
            if (randomPrize > 27000 && randomPrize <= 29000) {
                this.prizeTexture = appleTexture;
                this.prizeID = 14;
                hasPrize = true;
                if (debug) appleItem.itemCount++;
            }
            if (randomPrize > 9000 && randomPrize <= 12000) {
                this.prizeTexture = brownBookTexture;
                prizeID = 15;
                hasPrize = true;
                if (debug) brownBookItem.itemCount++;
            }
            if (randomPrize > 12000 && randomPrize <=  15000) {
                this.prizeTexture = breadTexture;
                prizeID = 16;
                hasPrize = true;
                if (debug) breadItem.itemCount++;
            }
            if (randomPrize > 15000 && randomPrize <= 18000) {
                this.prizeTexture = burgerTexture;
                prizeID = 17;
                hasPrize = true;
                if (debug) burgerItem.itemCount++;
            }
            if (randomPrize > 18000 && randomPrize <= 21000) {
                this.prizeTexture = pizzaTexture;
                prizeID = 18;
                hasPrize = true;
                if (debug) pizzaItem.itemCount++;
            }
            if (randomPrize > 32500 && randomPrize <= 33500) {
                this.prizeTexture = bombTexture;
                hasPrize = true;
                prizeID = 19;
              }
            if (randomPrize > 33500 && randomPrize <= 33600) {
                this.prizeTexture = rocketPenguinTexture;
                this.prizeID = 20;
                hasPrize = true;
                if (debug) rocketPenguinItem.itemCount++;
            }

            if (!hasPrize) {
                this.prizeTexture = emptyTexture;
                prizeID = 0;// no prize ID is no prize
            }

            Gdx.app.log("JSLOG", "prizeID " + prizeID  + " set.");


            //Assign the position of the bubble to a random value within the screen boundaries
			int randomX = random.nextInt(Gdx.graphics.getWidth());
			//this.setVisible(true);
			//this.setScale(random.nextFloat());
			this.speed =  (random.nextInt(10) + 5) * speedModifier;
			this.setPosition(randomX, 0 - (Gdx.graphics.getHeight() + randomX));
            this.setScale(random.nextFloat() * this.scaleModifier);

            int randomBubble = random.nextInt(8) + 1;
            // Gdx.app.log("JSLOG", "Random bubble color " + randomBubble + " set.");

            // set a random texture
            if (randomBubble == 1) this.texture = bubbleWhiteTexture;
            if (randomBubble == 2) this.texture = bubbleBlackTexture;
            if (randomBubble == 3) this.texture = bubbleBlueTexture;
            if (randomBubble == 4) this.texture = bubbleGreenTexture;
            if (randomBubble == 5) this.texture = bubbleOrangeTexture;
            if (randomBubble == 6) this.texture = bubblePurpleTexture;
            if (randomBubble == 7) this.texture = bubbleRedTexture;
            if (randomBubble == 8) this.texture = bubbleYellowTexture;

		}
	}

	@Override
	public void create() {

        // debug flag
        boolean debug = false;

        // Music
        mp3Music = Gdx.audio.newMusic(Gdx.files.internal("anewday.mp3"));
        bubblePopSound = Gdx.audio.newSound(Gdx.files.internal("pop.ogg"));
        applauseSound = Gdx.audio.newSound(Gdx.files.internal("applause.mp3"));

        //Define the screen as MDPI as baseline
        ScreenType screenType = ScreenType.MDPI;

        // define various screen sizes and resolutions
        int screenWidth = Gdx.graphics.getWidth();
        //int screenHeight = Gdx.graphics.getHeight();

        //more for higher dp screens
        int bubbleCount = 0;

        int baseWidth = 320;
        float scaleModifier = 0.5f;
        int speedModifier = 1;
        //int baseHeight  = 480; // not sure if this will be necessary

        if ( screenWidth <  baseWidth ) screenType = ScreenType.LDPI;
        if ( screenWidth >= baseWidth * 1 ) screenType = ScreenType.MDPI;
        if ( screenWidth >= baseWidth * 1.5 ) screenType = ScreenType.HDPI;
        if ( screenWidth >= baseWidth * 2 ) screenType = ScreenType.XHDPI;
        if ( screenWidth >= baseWidth * 3 ) screenType = ScreenType.XXHDPI;
        if ( screenWidth >= baseWidth * 4 ) screenType = ScreenType.XXXHDPI;

        Gdx.app.log("JSLOG", "screenType is " + screenType.toString());

        // setting up font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("cartoon.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator.FreeTypeFontParameter parameterLarge = new FreeTypeFontGenerator.FreeTypeFontParameter();

        // set font size based on screen type
        if (screenType == ScreenType.XXXHDPI){
            parameter.size = 72;
            parameter.borderWidth = 3;
            parameterLarge.size = 288;
            parameterLarge.borderWidth = 3;
        }
        if (screenType == ScreenType.XXHDPI) {
            parameter.size = 54;
            parameter.borderWidth = 3;
            parameterLarge.size = 108;
            parameterLarge.borderWidth = 3;
        }
        if (screenType == ScreenType.XHDPI){
            parameter.size = 48;
            parameter.borderWidth = 2;
            parameterLarge.size = 72;
            parameterLarge.borderWidth = 2;
        }
        if (screenType == ScreenType.HDPI) {
            parameter.size = 36;
            parameter.borderWidth = 2;
            parameterLarge.size = 54;
            parameterLarge.borderWidth = 2;
        }
        if (screenType == ScreenType.MDPI){
            parameter.size = 24;
            parameter.borderWidth = 1;
            parameterLarge.size = 36;
            parameterLarge.borderWidth = 1;
        }
        if (screenType == ScreenType.LDPI){
            parameter.size = 18;
            parameter.borderWidth = 1;
            parameterLarge.size = 18;
            parameterLarge.borderWidth = 1;
        }

        parameter.borderColor = Color.BLACK;
        //parameter.characters = "0123456789.!?-集宝箱砰你得候着啦哈的忠诚毋庸置疑太漂亮了就是泡大师王真吗终极超级忠诚都在这儿小伙伴们棒截屏已保存好";
        parameter.characters = "0123456789.!?-ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        textFont = generator.generateFont(parameter);
        //textFont = generator.generateFont(40, "0123456789。!？-集宝箱砰你得候着啦哈的忠诚毋庸置疑太漂亮了就是泡大师王真吗终极超级忠诚都在这儿小伙伴们棒截屏已保存好", false);
        textFontLarge = generator.generateFont(parameterLarge);

        // setting up animated penguin walk
        TextureAtlas textureAtlas;
        Animation animation;
        textureAtlas = new TextureAtlas(Gdx.files.internal("penguinwalk.atlas"));
        animation = new Animation(1/15f, textureAtlas.getRegions());

        AnimatedImage penguinWalking = new AnimatedImage(animation);

        Preferences prefs = Gdx.app.getPreferences("BubblePrefs"); // load the prefs file

        bubbleItem.itemCount = prefs.getInteger(bubbleItem.itemName);
        diamondItem.itemCount = prefs.getInteger(diamondItem.itemName);
        firstAidItem.itemCount = prefs.getInteger(firstAidItem.itemName);
        starItem.itemCount = prefs.getInteger(starItem.itemName);
        bookItem.itemCount = prefs.getInteger(bookItem.itemName);
        crystalItem.itemCount = prefs.getInteger(crystalItem.itemName);
        ringItem.itemCount = prefs.getInteger(ringItem.itemName);
        jewel1Item.itemCount = prefs.getInteger(jewel1Item.itemName);
        jewel2Item.itemCount = prefs.getInteger(jewel2Item.itemName);
        jewel3Item.itemCount = prefs.getInteger(jewel3Item.itemName);
        dragonItem.itemCount = prefs.getInteger(dragonItem.itemName);
        redDiamondItem.itemCount = prefs.getInteger(redDiamondItem.itemName);
        cameraItem.itemCount = prefs.getInteger(cameraItem.itemName);
        purpleBookItem.itemCount = prefs.getInteger(purpleBookItem.itemName);
        appleItem.itemCount = prefs.getInteger(appleItem.itemName);
        brownBookItem.itemCount = prefs.getInteger(brownBookItem.itemName);
        breadItem.itemCount = prefs.getInteger(breadItem.itemName);
        burgerItem.itemCount = prefs.getInteger(burgerItem.itemName);
        pizzaItem.itemCount = prefs.getInteger(pizzaItem.itemName);
        rocketPenguinItem.itemCount = prefs.getInteger(rocketPenguinItem.itemName);


        // Item images used in menu screen
        Image bubbleImage = new Image(new TextureRegion((new Texture("bubblesmall.png"))));
        Image diamondImage = new Image(new TextureRegion((new Texture("diamond.png"))));
        Image firstAidImage = new Image(new TextureRegion((new Texture("firstaid.png"))));
        Image starImage = new Image(new TextureRegion((new Texture("star.png"))));
        Image bookImage = new Image(new TextureRegion((new Texture("book.png"))));
        Image crystalImage = new Image(new TextureRegion((new Texture("crystal.png"))));
        Image ringImage = new Image(new TextureRegion((new Texture("ring.png"))));
        Image jewel1Image = new Image(new TextureRegion((new Texture("jewel1.png"))));
        Image jewel2Image = new Image(new TextureRegion((new Texture("jewel2.png"))));
        Image jewel3Image = new Image(new TextureRegion((new Texture("jewel3.png"))));
        Image dragonImage = new Image(new TextureRegion((new Texture("dragon.png"))));
        Image redDiamondImage = new Image(new TextureRegion((new Texture("reddiamond.png"))));
        Image cameraImage = new Image(new TextureRegion((new Texture("camera.png"))));
        Image purpleBookImage = new Image(new TextureRegion((new Texture("purplebook.png"))));
        Image appleImage = new Image(new TextureRegion((new Texture("apple.png"))));
        Image brownBookImage = new Image(new TextureRegion((new Texture("brownbook.png"))));
        Image breadImage = new Image(new TextureRegion((new Texture("bread.png"))));
        Image burgerImage = new Image(new TextureRegion((new Texture("burger.png"))));
        Image pizzaImage = new Image(new TextureRegion((new Texture("pizza.png"))));
        Image rocketPenguinImage = new Image(new TextureRegion((new Texture("rocketpenguin.png"))));
        Image emptyImage = new Image(new TextureRegion((new Texture("empty.png"))));

        Label.LabelStyle labelStyle = new Label.LabelStyle(textFont, Color.WHITE);
        Label.LabelStyle labelStyleLarge = new Label.LabelStyle(textFontLarge, Color.YELLOW);

        bubbleLabel = new Label("  " + bubbleItem.itemCount, labelStyle);
        diamondLabel = new Label("  " + diamondItem.itemCount, labelStyle);
        firstAidLabel = new Label("  " + firstAidItem.itemCount, labelStyle);
        starLabel = new Label("  " + starItem.itemCount, labelStyle);
        bookLabel = new Label("  " + bookItem.itemCount, labelStyle);
        crystalLabel = new Label("  " + crystalItem.itemCount, labelStyle);
        ringLabel = new Label("  " + ringItem.itemCount, labelStyle);
        jewel1Label = new Label("  " + jewel1Item.itemCount, labelStyle);
        jewel2Label = new Label("  " + jewel2Item.itemCount, labelStyle);
        jewel3Label = new Label("  " + jewel3Item.itemCount, labelStyle);
        dragonLabel = new Label("  " + dragonItem.itemCount, labelStyle);
        redDiamondLabel = new Label("  " + redDiamondItem.itemCount, labelStyle);
        cameraLabel = new Label("  " + cameraItem.itemCount, labelStyle);
        purpleBookLabel = new Label("  " + purpleBookItem.itemCount, labelStyle);
        appleLabel = new Label("  " + appleItem.itemCount, labelStyle);
        brownBookLabel = new Label("  " + brownBookItem.itemCount, labelStyle);
        breadLabel = new Label("  " + breadItem.itemCount, labelStyle);
        burgerLabel = new Label("  " + burgerItem.itemCount, labelStyle);
        pizzaLabel = new Label("  " + pizzaItem.itemCount, labelStyle);
        rocketPenguinLabel = new Label("  " + rocketPenguinItem.itemCount, labelStyle);
        collectionLabel = new Label("-Your Collection-", labelStyle);
        niceLabel = new Label("Nice!", labelStyle);

        // adding the menu back button
        final TextureRegion bubbleBackButtonTexture = new TextureRegion(new Texture("backbutton.png"));
        BubbleBackButton bubbleBackButton = new BubbleBackButton(bubbleBackButtonTexture);

        // adding a screenshot button
        final TextureRegion screenShotButtonTexture = new TextureRegion(new Texture("screenshotbutton.png"));
        ScreenShotButton screenShotButton = new ScreenShotButton(screenShotButtonTexture);

        // table to store item count labels
        final Table scrollTable = new Table();

        scrollTable.top().center();
        // impossibly rare items
        scrollTable.add(collectionLabel).colspan(2);
        scrollTable.row();
        scrollTable.add(ringImage).left().pad(10);
        scrollTable.add(ringLabel);
        scrollTable.row();
        // insanely rare items
        scrollTable.add(dragonImage).left().pad(10);
        scrollTable.add(dragonLabel);
        scrollTable.row();
        scrollTable.add(rocketPenguinImage).left().pad(10);
        scrollTable.add(rocketPenguinLabel);
        scrollTable.row();
        scrollTable.add(redDiamondImage).left().pad(10);
        scrollTable.add(redDiamondLabel);
        scrollTable.row();
        // ultra rare items
        scrollTable.add(jewel1Image).left().pad(10);
        scrollTable.add(jewel1Label);
        scrollTable.row();
        scrollTable.add(jewel2Image).left().pad(10);
        scrollTable.add(jewel2Label);
        scrollTable.row();
        // very rare items
        scrollTable.add(bookImage).left().pad(10);
        scrollTable.add(bookLabel);
        scrollTable.row();
        scrollTable.add(cameraImage).left().pad(10);
        scrollTable.add(cameraLabel);
        scrollTable.row();
        // fairly rare items
        scrollTable.add(crystalImage).left().pad(10);
        scrollTable.add(crystalLabel);
        scrollTable.row();
        scrollTable.add(purpleBookImage).left().pad(10);
        scrollTable.add(purpleBookLabel);
        scrollTable.row();
        scrollTable.add(appleImage).left().pad(10);
        scrollTable.add(appleLabel);
        scrollTable.row();
        scrollTable.add(jewel3Image).left().pad(10);
        scrollTable.add(jewel3Label);
        scrollTable.row();
        // common items
        scrollTable.add(brownBookImage).left().pad(10);
        scrollTable.add(brownBookLabel);
        scrollTable.row();
        scrollTable.add(burgerImage).left().pad(10);
        scrollTable.add(burgerLabel);
        scrollTable.row();
        scrollTable.add(pizzaImage).left().pad(10);
        scrollTable.add(pizzaLabel);
        scrollTable.row();
        scrollTable.add(firstAidImage).left().pad(10);
        scrollTable.add(firstAidLabel);
        scrollTable.row();
        scrollTable.add(diamondImage).left().pad(10);
        scrollTable.add(diamondLabel);
        scrollTable.row();
        scrollTable.add(breadImage).left().pad(10);
        scrollTable.add(breadLabel);
        scrollTable.row();
        scrollTable.add(starImage).left().pad(10);
        scrollTable.add(starLabel);
        scrollTable.row();
        scrollTable.add(bubbleImage).left().pad(10);
        scrollTable.add(bubbleLabel);
        scrollTable.row();
        scrollTable.add(niceLabel).colspan(2);
        scrollTable.row();
        scrollTable.add(emptyImage);

        // a ScrollPane to place table for scrolling
        final ScrollPane scroller = new ScrollPane(scrollTable);
        scroller.setFadeScrollBars(false);

        final Table table = new Table();
        table.setFillParent(true);
        //table.top().center();
        table.add(scroller).fill().expand();

        // button textures
        final TextureRegion bubbleButtonTexture = new TextureRegion(new Texture("treasurechest.png"));

        // adding sound button
        soundButtonTextureOn = new TextureRegion(new Texture("soundon.png"));
        soundButtonTextureOff = new TextureRegion(new Texture("soundoff.png"));
        SoundButton soundButton = new SoundButton(soundButtonTextureOn);

        // add a bubbleButton
        BubbleButton bubbleButton = new BubbleButton(bubbleButtonTexture);

        // change size based on screen type
        if (screenType == ScreenType.XXXHDPI) {
            bubbleImage.setScale(2.5f);
            diamondImage.setScale(2.5f);
            firstAidImage.setScale(2.5f);
            starImage.setScale(2.5f);
            bookImage.setScale(2.5f);
            crystalImage.setScale(2.5f);
            ringImage.setScale(2.5f);
            jewel1Image.setScale(2.5f);
            jewel2Image.setScale(2.5f);
            jewel3Image.setScale(2.5f);
            dragonImage.setScale(2.5f);
            redDiamondImage.setScale(2.5f);
            cameraImage.setScale(2.5f);
            purpleBookImage.setScale(2.5f);
            appleImage.setScale(2.5f);
            brownBookImage.setScale(2.5f);
            breadImage.setScale(2.5f);
            burgerImage.setScale(2.5f);
            pizzaImage.setScale(2.5f);
            rocketPenguinImage.setScale(2.5f);
            bubbleCount = 12;
            scaleModifier = 2f;
            speedModifier = 4;
            if(debug) speedModifier = 1000;
            penguinWalking.scaleBy(5f);
            penguinWalking.setPosition(0, 320);
            screenShotButton.setPosition(Gdx.graphics.getWidth() - screenShotButton.getWidth(), 0);
            soundButton.setPosition(Gdx.graphics.getWidth() - soundButton.getWidth(), 0);
            background = new TextureRegion(new Texture("hillsxxxhdpi.png"));

        }
        if (screenType == ScreenType.XXHDPI) {
            bubbleImage.setScale(2.5f);
            diamondImage.setScale(2.5f);
            firstAidImage.setScale(2.5f);
            starImage.setScale(2.5f);
            bookImage.setScale(2.5f);
            crystalImage.setScale(2.5f);
            ringImage.setScale(2.5f);
            jewel1Image.setScale(2.5f);
            jewel2Image.setScale(2.5f);
            jewel3Image.setScale(2.5f);
            dragonImage.setScale(2.5f);
            redDiamondImage.setScale(2.5f);
            cameraImage.setScale(2.5f);
            purpleBookImage.setScale(2.5f);
            appleImage.setScale(2.5f);
            brownBookImage.setScale(2.5f);
            breadImage.setScale(2.5f);
            burgerImage.setScale(2.5f);
            pizzaImage.setScale(2.5f);
            rocketPenguinImage.setScale(2.5f);
            bubbleCount = 12;
            scaleModifier = 1.8f;
            speedModifier = 3;
            penguinWalking.scaleBy(5f);
            penguinWalking.setPosition(0, 300);
            screenShotButton.setPosition(Gdx.graphics.getWidth() - screenShotButton.getWidth(), 0);
            soundButton.setPosition(Gdx.graphics.getWidth() - soundButton.getWidth(), 0);
            background = new TextureRegion(new Texture("hillsxxhdpi.png"));

        }
        if (screenType == ScreenType.XHDPI) {
            bubbleImage.setScale(1.5f);
            diamondImage.setScale(1.5f);
            firstAidImage.setScale(1.5f);
            starImage.setScale(1.5f);
            bookImage.setScale(1.5f);
            crystalImage.setScale(1.5f);
            ringImage.setScale(1.5f);
            jewel1Image.setScale(1.5f);
            jewel2Image.setScale(1.5f);
            jewel3Image.setScale(1.5f);
            dragonImage.setScale(1.5f);
            redDiamondImage.setScale(1.5f);
            cameraImage.setScale(1.5f);
            purpleBookImage.setScale(1.5f);
            appleImage.setScale(1.5f);
            brownBookImage.setScale(1.5f);
            breadImage.setScale(1.5f);
            burgerImage.setScale(1.5f);
            pizzaImage.setScale(1.5f);
            rocketPenguinImage.setScale(1.5f);
            bubbleCount = 12;
            scaleModifier = 1.5f;
            speedModifier = 3;
            penguinWalking.scaleBy(2.5f);
            penguinWalking.setPosition(0, 200);
            bubbleButton.setScale(.5f);
            bubbleBackButton.setScale(.5f);
            screenShotButton.setScale(.5f);
            soundButton.setScale(.5f);
            screenShotButton.setPosition(Gdx.graphics.getWidth() - 132, 0);
            soundButton.setPosition(Gdx.graphics.getWidth() - 132, 0);
            background = new TextureRegion(new Texture("hillsxhdpi.png"));

        }
        if (screenType == ScreenType.HDPI) {
            bubbleImage.setScale(1.25f);
            diamondImage.setScale(1.25f);
            firstAidImage.setScale(1.25f);
            starImage.setScale(1.25f);
            bookImage.setScale(1.25f);
            crystalImage.setScale(1.25f);
            ringImage.setScale(1.25f);
            jewel1Image.setScale(1.25f);
            jewel2Image.setScale(1.25f);
            jewel3Image.setScale(1.25f);
            dragonImage.setScale(1.25f);
            redDiamondImage.setScale(1.25f);
            cameraImage.setScale(1.25f);
            purpleBookImage.setScale(1.25f);
            appleImage.setScale(1.25f);
            brownBookImage.setScale(1.25f);
            breadImage.setScale(1.25f);
            burgerImage.setScale(1.25f);
            pizzaImage.setScale(1.25f);
            rocketPenguinImage.setScale(1.25f);
            bubbleCount = 10;
            scaleModifier = 1.0f;
            speedModifier = 2;
            penguinWalking.scaleBy(2f);
            penguinWalking.setPosition(0, 150);
            bubbleButton.setScale(.35f);
            bubbleBackButton.setScale(.35f);
            screenShotButton.setScale(.35f);
            soundButton.setScale(.35f);
            screenShotButton.setPosition(Gdx.graphics.getWidth() - 98, 0);
            soundButton.setPosition(Gdx.graphics.getWidth() - 98, 0);
            background = new TextureRegion(new Texture("hillshdpi.png"));
        }

        if (screenType == ScreenType.MDPI) {
            bubbleImage.setScale(1f);
            diamondImage.setScale(1f);
            firstAidImage.setScale(1f);
            starImage.setScale(1f);
            bookImage.setScale(1f);
            crystalImage.setScale(1f);
            ringImage.setScale(1f);
            jewel1Image.setScale(1f);
            jewel2Image.setScale(1f);
            jewel3Image.setScale(1f);
            dragonImage.setScale(1f);
            redDiamondImage.setScale(1f);
            cameraImage.setScale(1f);
            purpleBookImage.setScale(1f);
            appleImage.setScale(1f);
            brownBookImage.setScale(1f);
            breadImage.setScale(1f);
            burgerImage.setScale(1f);
            pizzaImage.setScale(1f);
            rocketPenguinImage.setScale(1f);
            bubbleCount = 8;
            scaleModifier = .8f;
            speedModifier = 1;
            penguinWalking.scaleBy(1f);
            penguinWalking.setPosition(0, 96);
            bubbleButton.setScale(.25f);
            bubbleBackButton.setScale(.25f);
            screenShotButton.setScale(.25f);
            screenShotButton.setPosition(Gdx.graphics.getWidth() - 68, 0);
            soundButton.setScale(.25f);
            soundButton.setPosition(Gdx.graphics.getWidth() - 68, 0);
            background = new TextureRegion(new Texture("hillsmdpi.png"));
        }
        if (screenType == ScreenType.LDPI)  {
            bubbleImage.setScale(1f);
            diamondImage.setScale(1f);
            firstAidImage.setScale(1f);
            starImage.setScale(1f);
            bookImage.setScale(1f);
            crystalImage.setScale(1f);
            ringImage.setScale(1f);
            jewel1Image.setScale(1f);
            jewel2Image.setScale(1f);
            jewel3Image.setScale(1f);
            dragonImage.setScale(1f);
            redDiamondImage.setScale(1f);
            cameraImage.setScale(1f);
            purpleBookImage.setScale(1f);
            appleImage.setScale(1f);
            brownBookImage.setScale(1f);
            breadImage.setScale(1f);
            burgerImage.setScale(1f);
            pizzaImage.setScale(1f);
            rocketPenguinImage.setScale(1f);
            bubbleCount = 8;
            scaleModifier = .8f;
            speedModifier = 1;
            penguinWalking.scaleBy(1f);
            penguinWalking.setPosition(0, 96);
            bubbleButton.setScale(.25f);
            bubbleBackButton.setScale(.25f);
            screenShotButton.setScale(.25f);
            screenShotButton.setPosition(Gdx.graphics.getWidth() - 68, 0);
            soundButton.setPosition(Gdx.graphics.getWidth() - 68, 0);
            background = new TextureRegion(new Texture("hillsmdpi.png"));
        }

		Bubble[] bubbles;

		// set up the stage
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        // this is a simple game, so we just gonna have a menu type list of viewable items
        // on a separate stage in the same class
        menuStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        // finally a waiting stage to display if player pops bubble with bomb
        waitingStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        rbg = new ParallaxBackground(new ParallaxLayer[]{
				//	new ParallaxLayer(background, new Vector2(),new Vector2(0, 0)),
				new ParallaxLayer(background,new Vector2(1.0f,1.0f),new Vector2(0, 500)),
				//	new ParallaxLayer(background,new Vector2(0.1f,0),new Vector2(0, stage.getViewport().getScreenHeight()-200),new Vector2(0, 0)),
		}, stage.getViewport().getScreenWidth(), stage.getViewport().getScreenHeight(), new Vector2(150,0));

		// initialize arrays for bubbles
		bubbles = new Bubble[bubbleCount];

		// make 10 bubble objects at random on screen locations
		for(int i = 0; i < bubbleCount; i++){
			bubbles[i] = new Bubble(speedModifier, scaleModifier);
            bubbles[i].setName(Integer.toString(i));
            bubbles[i].reset();
			// add to the stage
			stage.addActor(bubbles[i]);
		}

        // main stage actors, other than bubbles
        stage.addActor(bubbleButton);
        stage.addActor(soundButton);
        stage.addActor(penguinWalking);

        // menu stage actors
        menuStage.addActor(table);
        menuStage.addActor(bubbleBackButton);
        menuStage.addActor(screenShotButton);

        // get input
        Gdx.input.setInputProcessor(stage);

        // for waiting
        waitingLabel = new Label("Bomb! You must wait. Ha ha ha.", labelStyle);
        waitingCounterLabel = new Label("0", labelStyleLarge);
        if(bubbleItem.itemCount > 1000) {
            //print
            waitingLabel = new Label("Your loyalty is unquestionable. Props.", labelStyle);
        }
        if(bubbleItem.itemCount > 10000) {
            waitingLabel = new Label("You are bubble master.", labelStyle);
        }
        if(bubbleItem.itemCount > 25000) {
            waitingLabel = new Label("Bubble king!!", labelStyle);
        }
        if(bubbleItem.itemCount > 50000) {
            waitingLabel = new Label("Really? All Hail Bubble King!", labelStyle);
        }
        if(bubbleItem.itemCount > 100000) {
            waitingLabel = new Label("Extreme Loyalty.", labelStyle);
        }
        if(bubbleItem.itemCount > 500000) {
            waitingLabel = new Label("That's all folks!", labelStyle);
        }
        if(bubbleItem.itemCount > 1000000) {
            waitingLabel = new Label("You Rule.", labelStyle);
        }
        waitingCounterLabel.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        waitingLabel.setPosition(0, 0);
        waitingStage.addActor(waitingCounterLabel);
        waitingStage.addActor(waitingLabel);

        // screenshot saved label
        screenshotSavedLabel = new Label("Screenshot Saved.", labelStyle);
        screenshotSavedLabel.setPosition((Gdx.graphics.getWidth() / 2)/2, 0);

        // show banner ad if connected
        if(adsController.isWifiConnected()) {
            adsController.showBannerAd();
        }

        //play music
        mp3Music.setLooping(true);
        mp3Music.play();
        mp3Music.setVolume(VOLUME);

    }

    private AdsController adsController;

    public SillyBubblesGame(AdsController adsController){
        this.adsController = adsController;
    }

	@Override
	public void dispose() {
        mp3Music.dispose();
        bubblePopSound.dispose();
        applauseSound.dispose();

        stage.dispose();
        menuStage.dispose();
        waitingStage.dispose();
    }

    @Override
    public void render() {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (playing) {
            rbg.render(Gdx.graphics.getDeltaTime());
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();

            if (waiting) {
                rbg.render(Gdx.graphics.getDeltaTime());
                waitingStage.act(Gdx.graphics.getDeltaTime());
                waitingStage.draw();
            }
        } else {
            rbg.render(Gdx.graphics.getDeltaTime());
            menuStage.act(Gdx.graphics.getDeltaTime());
            menuStage.draw();
        }

        if (needScreenshot) {
            takeScreenshot();
            menuStage.addActor(screenshotSavedLabel);
            needScreenshot = false;
        }

	}

	@Override
    public void resize(int width, int height) {

        stage.getViewport().update(width, height, true);
        //menuStage.getViewport().update(width, height, true);

		rbg = new ParallaxBackground(new ParallaxLayer[]{
				//	new ParallaxLayer(background, new Vector2(),new Vector2(0, 0)),
				new ParallaxLayer(background,new Vector2(1.0f,1.0f),new Vector2(0, 500)),
				//	new ParallaxLayer(background,new Vector2(0.1f,0),new Vector2(0, stage.getViewport().getScreenHeight()-200),new Vector2(0, 0)),
		}, stage.getViewport().getScreenWidth(), stage.getViewport().getScreenHeight()/2, new Vector2(150,0));

	}

	@Override
	public void pause() {

        // save the amount of items collected if game is paused
        Preferences prefs = Gdx.app.getPreferences("BubblePrefs");// We store the value 10 with the key of "highScore"
        prefs.putInteger(bubbleItem.itemName, bubbleItem.itemCount);
        prefs.putInteger(diamondItem.itemName, diamondItem.itemCount);
        prefs.putInteger(firstAidItem.itemName, firstAidItem.itemCount);
        prefs.putInteger(starItem.itemName, starItem.itemCount);
        prefs.putInteger(bookItem.itemName, bookItem.itemCount);
        prefs.putInteger(crystalItem.itemName, crystalItem.itemCount);
        prefs.putInteger(ringItem.itemName, ringItem.itemCount);
        prefs.putInteger(jewel1Item.itemName, jewel1Item.itemCount);
        prefs.putInteger(jewel2Item.itemName, jewel2Item.itemCount);
        prefs.putInteger(jewel3Item.itemName, jewel3Item.itemCount);
        prefs.putInteger(dragonItem.itemName, dragonItem.itemCount);
        prefs.putInteger(redDiamondItem.itemName, redDiamondItem.itemCount);
        prefs.putInteger(cameraItem.itemName, cameraItem.itemCount);
        prefs.putInteger(purpleBookItem.itemName, purpleBookItem.itemCount);
        prefs.putInteger(appleItem.itemName, appleItem.itemCount);
        prefs.putInteger(brownBookItem.itemName, brownBookItem.itemCount);
        prefs.putInteger(breadItem.itemName, breadItem.itemCount);
        prefs.putInteger(burgerItem.itemName, burgerItem.itemCount);
        prefs.putInteger(pizzaItem.itemName, pizzaItem.itemCount);
        prefs.putInteger(rocketPenguinItem.itemName, rocketPenguinItem.itemCount);
        prefs.flush(); // saves the preferences file
        Gdx.app.log("JSLOG", "Game Paused.");

    }


	@Override
	public void resume() {
        Gdx.app.log("JSLOG", "Game On.");

        // interstitial ad on pause
        if (adsController.isWifiConnected() && !firstRun) {
            adsController.showInterstitialAd(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Interstitial app closed");
                    //Gdx.app.exit();
                }
            });
        } else {
            System.out.println("Interstitial ad not (yet) loaded");
        }

        firstRun = false;
	}

    public void takeScreenshot() {

        Date date = new Date(TimeUtils.millis());

        Gdx.app.log("JSLOG", "isExternalStorageAvailable: " + Gdx.files.isExternalStorageAvailable());

        FileHandle file;
        file = new FileHandle(Gdx.files.getExternalStoragePath() + "sillybubbles" + date.toString() + ".png");
        Gdx.app.log("JSLOG", "saved to: " + file.toString());
           System.out.println(file.file().getAbsolutePath());
            Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            try {
                PixmapIO.PNG writer = new PixmapIO.PNG((int) (pixmap.getWidth() * pixmap.getHeight() * 1.5f));
                writer.write(file, pixmap);
                writer.write(file, pixmap); // Write twice to make sure the object is reusable.
                writer.dispose();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
    }

    public static void muteFX(){
        VOLUME = 0.0f;
        soundOn = false;
    }
    public static void normalizeFX(){
        VOLUME = 1.0f;
        soundOn = true;
    }
}

