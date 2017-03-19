package cat.xtec.ioc.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;

import cat.xtec.ioc.helpers.AssetManager;

/**
 * Created by Matias on 16/03/2017.
 */

/**
 * Clase que gestiona el objeto bala.
 */
public class Bullet extends Actor {

    private Rectangle collisionRect;
    private Vector2 position;
    private int width, height;
    private ScrollHandler scrollHandler;
    private Vector2 posicionPulsada;

    public Bullet(float x, float y, int width, int height, ScrollHandler scrollHandler, Vector2 pulsacion) {

        this.width = width;
        this.height = height;
        position = new Vector2(x, y);
        this.scrollHandler = scrollHandler;
        this.posicionPulsada = pulsacion;

        // Creem el rectangle de col·lisions
        collisionRect = new Rectangle();

        // Per a la gestio de hit
        setBounds(position.x, position.y, width, height);
    }

    /**
     * Creamos la textura de la bala
     *
     * @return
     */
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
        collisionRect.set(position.x, position.y, width, height + 2);
        if (collidesBullet(scrollHandler.getAsteroids())) {
            Gdx.app.log("Explosion", "Bullet colission");
            //gameScreen.iniciarExplosio(delta);
        }
    }


    public boolean collidesBullet(ArrayList<Asteroid> asteroids) {
        // Comprovem les col·lisions entre cada asteroid i la bala
        for (Asteroid asteroid : asteroids) {
            //Gdx.app.log("Asteroide", "" + asteroid.collidesWithBullet(this));
            if (asteroid.collidesWithBullet(this)) {
                this.remove();
                asteroid.delete();
                scrollHandler.setPuntuacioPerDestruccio(false, 10);
                Gdx.app.log("Colision", "Bullet-Asteroid-Collision-Desactivated");
                return true;
            }
        }
        return false;
    }
}
