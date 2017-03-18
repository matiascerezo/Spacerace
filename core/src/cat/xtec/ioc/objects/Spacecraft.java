package cat.xtec.ioc.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.utils.Settings;

public class Spacecraft extends Actor {

    // Distintes posicions de la spacecraft, recta, pujant i baixant
    public static final int SPACECRAFT_STRAIGHT = 0;
    public static final int SPACECRAFT_UP = 1;
    public static final int SPACECRAFT_DOWN = 2;

    // Paràmetres de la spacecraft
    private Vector2 position;
    private int width, height;
    private int direction;
    private float velocityX, velocityY;
    private Stage stage;
    private ScrollHandler scrollHandler;


    private Rectangle collisionRect;

    public Spacecraft(float x, float y, int width, int height, Stage stage) {

        //Inicialitzem els arguments segons la crida del constructor
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);
        velocityX = 0;
        velocityY = 0;
        this.stage = stage;
        scrollHandler = new ScrollHandler();


        //Inicialitzem la spacecraft a l'estat normal
        direction = SPACECRAFT_STRAIGHT;

        //Creem el rectangle de col·lisions
        collisionRect = new Rectangle();

        //Rotacio
        RotateByAction rotateAction = new RotateByAction();
        rotateAction.setAmount(-90f);
        rotateAction.setDuration(0.2f);

        //Accio de repetició
        RepeatAction repeat = new RepeatAction();
        repeat.setAction(rotateAction);
        repeat.setCount(RepeatAction.FOREVER);

        //Per a la gestio de hit
        setBounds(position.x, position.y, width, height);
        setTouchable(Touchable.enabled);
    }

    public void act(float delta) {

        //Movem la spacecraft depenent de la direcció controlant que no surti de la pantalla
        switch (direction) {
            case SPACECRAFT_UP:
                if (this.position.y - Settings.SPACECRAFT_VELOCITY * delta >= 0) {
                    this.position.y -= Settings.SPACECRAFT_VELOCITY * delta;
                }
                break;
            case SPACECRAFT_DOWN:
                if (this.position.y + height + Settings.SPACECRAFT_VELOCITY * delta <= Settings.GAME_HEIGHT) {
                    this.position.y += Settings.SPACECRAFT_VELOCITY * delta;
                }
                break;
            case SPACECRAFT_STRAIGHT:
                break;
        }

        collisionRect.set(position.x, position.y + 3, width, 10);
        setBounds(position.x, position.y, width, height);

    }

    public void shoot(Vector2 pulsacion) {
        for (Actor actor : stage.getActors()) {
            if (actor.getName() != null && actor.getName().equalsIgnoreCase("spacecraft")) {
                stage.addActor(new Bullet(actor.getX() + actor.getWidth(), actor.getY() + actor.getHeight() / 2, 17, 8, scrollHandler, pulsacion));
                AssetManager.shootSound1.play();
                break;
            }
        }
    }
    public void shoot(){

        //stage.addActor(new Bullet(actor.getX() + actor.getWidth(), actor.getY() + actor.getHeight() / 2, 17, 8, scrollHandler));
    }

    // Getters dels atributs principals
    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void goUp() {
        direction = SPACECRAFT_UP;
        velocityY = -Settings.SPACECRAFT_VELOCITY;
    }

    // Canviem la direcció de la spacecraft: Baixa
    public void goDown() {
        direction = SPACECRAFT_DOWN;
        velocityY = Settings.SPACECRAFT_VELOCITY;
    }

    // Posem la spacecraft al seu estat original
    public void goStraight() {

        direction = SPACECRAFT_STRAIGHT;
        velocityX = Settings.SPACECRAFT_VELOCITY_X;
    }

    public void goBack() {
        direction = SPACECRAFT_STRAIGHT;
        velocityX = -Settings.SPACECRAFT_VELOCITY_X;

    }

    public void pause() {
        velocityX = 0;
        velocityY = 0;
        direction = SPACECRAFT_STRAIGHT;
    }

    // Obtenim el TextureRegion depenent de la posició de la spacecraft
    public TextureRegion getSpacecraftTexture() {

        switch (direction) {
            case SPACECRAFT_STRAIGHT:
                return AssetManager.spacecraft;
            case SPACECRAFT_UP:
                return AssetManager.spacecraftUp;
            case SPACECRAFT_DOWN:
                return AssetManager.spacecraftDown;
            default:
                return AssetManager.spacecraft;
        }
    }

    public void reset() {

        // La posem a la posició inicial i a l'estat normal
        position.x = Settings.SPACECRAFT_STARTX;
        position.y = Settings.SPACECRAFT_STARTY;
        direction = SPACECRAFT_STRAIGHT;
        collisionRect = new Rectangle();
    }

    @Override
    public float getRotation() {
        float angle;
        if (velocityX == 0) angle = 0;
        else
            angle = (float) -Math.atan(velocityY / velocityX);
        return angle;

    }

    @Override
    public float getOriginX() {
        return position.x + width / 2;
    }

    @Override
    public float getOriginY() {
        return position.y + height / 2;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(getSpacecraftTexture(), position.x, position.y, width, height);
    }

    public Rectangle getCollisionRect() {
        return collisionRect;
    }
}
