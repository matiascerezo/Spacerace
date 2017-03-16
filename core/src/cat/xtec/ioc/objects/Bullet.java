package cat.xtec.ioc.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;

import cat.xtec.ioc.helpers.AssetManager;

/**
 * Created by Matias on 16/03/2017.
 */

public class Bullet extends Actor {

    private Rectangle collisionRect;
    private Vector2 position;
    private int width, height;

    public Bullet(float x, float y, int width, int height) {

        this.width = width;
        this.height = height;
        position = new Vector2(x, y);

        // Creem el rectangle de col·lisions
        collisionRect = new Rectangle();
        RepeatAction repeat = new RepeatAction();
        repeat.setCount(RepeatAction.FOREVER);

        // Per a la gestio de hit
        setBounds(position.x, position.y, width, height);
        setTouchable(Touchable.enabled);
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

}
