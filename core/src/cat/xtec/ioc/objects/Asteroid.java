package cat.xtec.ioc.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;

import java.util.Random;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.utils.Methods;
import cat.xtec.ioc.utils.Settings;

public class Asteroid extends Scrollable {

    private Circle collisionCircle;

    Random r;

    int assetAsteroid;
    boolean destruido;

    private float explosionTime = 0;

    public Asteroid(float x, float y, float width, float height, float velocity) {
        super(x, y, width, height, velocity);
        // Creem el cercle
        collisionCircle = new Circle();

        /* Accions */
        r = new Random();
        assetAsteroid = r.nextInt(15);

        setOrigin();

        explosionTime += 5;
        // Rotacio
        RotateByAction rotateAction = new RotateByAction();
        rotateAction.setAmount(-90f);
        rotateAction.setDuration(0.2f);

        // Accio de repetició
        RepeatAction repeat = new RepeatAction();
        repeat.setAction(rotateAction);
        repeat.setCount(RepeatAction.FOREVER);

        this.addAction(repeat);
    }

    public void setOrigin() {
        this.setOrigin(width / 2 + 1, height / 2);
    }

    /**
     * Método que se ejecuta todoel rato y actualiza el circulo de colisiones.
     *
     * @param delta
     */
    @Override
    public void act(float delta) {
        super.act(delta);
        // Actualitzem el cercle de col·lisions (punt central de l'asteroid i el radi.
        collisionCircle.set(position.x + width / 2.0f, position.y + width / 2.0f, width / 2.0f);
    }

    /**
     * Método que vuelve todoa su estado inicial.
     *
     * @param newX
     */
    @Override
    public void reset(float newX) {
        super.reset(newX);

        this.setVisible(true);
        destruido = false;
        // Obtenim un número al·leatori entre MIN i MAX
        float newSize = Methods.randomFloat(Settings.MIN_ASTEROID, Settings.MAX_ASTEROID);
        // Modificarem l'alçada i l'amplada segons l'al·leatori anterior
        width = height = 34 * newSize;

        // La posició serà un valor aleatòri entre 0 i l'alçada de l'aplicació menys l'alçada
        position.y = new Random().nextInt(Settings.GAME_HEIGHT - (int) height);

        assetAsteroid = r.nextInt(15);
        setOrigin();
        explosionTime = 0.0f;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(AssetManager.asteroid[assetAsteroid], position.x, position.y, this.getOriginX(), this.getOriginY(), width, height, this.getScaleX(), this.getScaleY(), this.getRotation());
    }

    // Retorna true si hi ha col·lisió
    public boolean collides(Spacecraft nau) {
        if (!destruido) {
            // Comprovem si han col·lisionat sempre i quan l'asteroid estigui a la mateixa alçada que la spacecraft
            return (Intersector.overlaps(collisionCircle, nau.getCollisionRect()));
        }
        return false;
    }

    /**
     * Método que comprueba si ha habido colision con la bala y devuelve un booleano con la respuesta.
     * Se desactiva para que la siguiente bala no colisione otra vez.
     *
     * @param bullet
     * @return
     */
    public boolean collidesWithBullet(Bullet bullet) {
        if (!destruido) {
            return (Intersector.overlaps(collisionCircle, bullet.getCollisionRect()));
        }
        return false;
    }

    /**
     * Método que se ejecuta cuando el asteroide choca con una bala, reproduce el sonido de explosión,
     * cambia el booleano a true para desactivar la colision en dicho metodo y oculta el asteroide para
     * causar sensación de que se ha destruido.
     */
    public void delete() {
        // Si hi ha hagut col·lisió: Reproduïm l'explosió
        AssetManager.explosion1Sound.play();
        destruido = true;
        this.setVisible(false);
    }
}