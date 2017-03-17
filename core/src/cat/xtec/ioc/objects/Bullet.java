package cat.xtec.ioc.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;

import java.util.ArrayList;

import cat.xtec.ioc.helpers.AssetManager;

/**
 * Created by Matias on 16/03/2017.
 */

public class Bullet extends Actor {

    private Rectangle collisionRect;
    private Vector2 position;
    private int width, height;
    private int direction;
    private ScrollHandler scrollHandler;

    public Bullet(float x, float y, int width, int height, ScrollHandler scrollHandler) {

        this.width = width;
        this.height = height;
        position = new Vector2(x, y);
        this.scrollHandler = scrollHandler;

        // Creem el rectangle de col·lisions
        collisionRect = new Rectangle();
        //RepeatAction repeat = new RepeatAction();
        //repeat.setCount(RepeatAction.FOREVER);

        // Per a la gestio de hit
        setBounds(position.x, position.y, width, height);
        //setTouchable(Touchable.enabled);
    }

    public TextureRegion getBulletTexture() {
        return AssetManager.bullet;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(getBulletTexture(), position.x, position.y, width, height);
    }

    public Rectangle getCollisionRect() {
        return collisionRect;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        //Meter en Settings la velocidad
        this.position.x += 60 * delta;

        //collisionRect.set(position.x, position.y + 3, width, 10);
        //collisionRect.set(position.x, position.y, width, height + 2);
        collisionRect.set(position.x,position.y, width, height);
        collidesBullet(scrollHandler.getAsteroids());
    }

    public boolean collidesBullet(ArrayList<Asteroid> asteroids) {
        // Comprovem les col·lisions entre cada asteroid i la nau
        for (Asteroid asteroid : asteroids) {
            //Gdx.app.log("Asteroide", "" + asteroid.collidesWithBullet(this));
            if (asteroid.collidesWithBullet(this)) {
                Gdx.app.log("Proyectil", "desaparece");
                this.remove();
                return true;
            }
        }
        return false;
    }
}
