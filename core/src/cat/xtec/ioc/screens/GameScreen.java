package cat.xtec.ioc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Set;

import cat.xtec.ioc.SpaceRace;
import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.helpers.InputHandler;
import cat.xtec.ioc.objects.Asteroid;
import cat.xtec.ioc.objects.Bullet;
import cat.xtec.ioc.objects.ScrollHandler;
import cat.xtec.ioc.objects.Spacecraft;
import cat.xtec.ioc.utils.Settings;

public class GameScreen implements Screen {

    // Els estats del joc
    public enum GameState {
        READY, RUNNING, GAMEOVER
    }

    private GameState currentState;

    // Objectes necessaris
    private Stage stage;
    private Spacecraft spacecraft;
    private Bullet bullet;
    private ScrollHandler scrollHandler;
    private SpaceRace game;
    // Encarregats de dibuixar elements per pantalla
    private ShapeRenderer shapeRenderer;
    private Batch batch;

    // Per controlar l'animació de l'explosió
    private float explosionTime = 0;

    // Preparem el textLayout per escriure text
    private GlyphLayout textLayout;
    private GlyphLayout textPuntuacio;
    int recordActual = 0;
    boolean playMusic;

    boolean vegada;

    public GameScreen() {
    }

    /**
     * Clase que gestiona el juego en si.
     * @param prevBatch
     * @param prevViewport
     * @param dificultad
     * @param music
     */
    public GameScreen(Batch prevBatch, Viewport prevViewport, String dificultad, boolean music) {

        /**
         * Para la configuracion del juego, tanto para quitar/poner la musica, como para establecer
         * la dificultad. Dependiendo de la dificultad escogida, modifica la velocidad de la nave
         * y el Gap de los asteroides, para que aparezcan mas, modificando el espacio entre ellos.
         */
        if (music) {
            //Iniciem la música
            AssetManager.music.play();
        } else {
            AssetManager.music.stop();
        }

        if (dificultad.equals("facil")) {
            Settings.ASTEROID_GAP += 30;
            Settings.SPACECRAFT_VELOCITY += 20;
        } else if (dificultad.equals("mig")) {
            Settings.ASTEROID_GAP += 25;
            Settings.SPACECRAFT_VELOCITY += 10;
        }
        // Creem el ShapeRenderer
        shapeRenderer = new ShapeRenderer();

        // Creem l'stage i assginem el viewport
        stage = new Stage(prevViewport, prevBatch);

        batch = stage.getBatch();
        scrollHandler = new ScrollHandler();
        // Creem la nau i la resta d'objectes
        spacecraft = new Spacecraft(Settings.SPACECRAFT_STARTX, Settings.SPACECRAFT_STARTY, Settings.SPACECRAFT_WIDTH, Settings.SPACECRAFT_HEIGHT, stage, scrollHandler);

        // Afegim els actors a l'stage
        stage.addActor(scrollHandler);
        stage.addActor(spacecraft);

        // Donem nom a l'Actor
        spacecraft.setName("spacecraft");

        // Iniciem el GlyphLayout
        textLayout = new GlyphLayout();
        textPuntuacio = new GlyphLayout();
        currentState = GameState.READY;
        //Assignem com a gestor d'entrada la classe InputHandler
        Gdx.input.setInputProcessor(new InputHandler(this));
    }

    /**
     * Metodo que devuelve un booleano si coincide el parametro con "no". Lo usaremos para quitar
     * la musica del juego o dejarla sonando.
     * @param volum
     * @return
     */
    public boolean isPlayMusic(String volum) {
        return volum.equals("no");
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        // Dibuixem tots els actors de l'stage
        stage.draw();

        int contador = 0;
        // Depenent de l'estat del joc farem unes accions o unes altres
        switch (currentState) {
            case GAMEOVER:
                updateGameOver(delta);
                break;
            case RUNNING:
                updateRunning(delta);
                break;
            case READY:
                if (contador == 0) {
                    updateReady(true);
                    contador = 1;
                } else {
                    updateReady(false);
                }
                break;
        }
    }

    /**
     * Pantalla intermedia entre la dificultad y el juego para que no inicie directamente cuando
     * pulsamos la dificultad que queremos.
     * @param primeraVegada
     */
    private void updateReady(boolean primeraVegada) {
        // Dibuixem el text al centre de la pantalla
        batch.begin();
        if (!primeraVegada) {
            AssetManager.font.draw(batch, textLayout, (Settings.GAME_WIDTH / 2) - textLayout.width / 2, (Settings.GAME_HEIGHT / 2) - textLayout.height / 2);
        } else {
            AssetManager.font.draw(batch, textLayout, Settings.GAME_WIDTH / 2 - textLayout.width / 2, Settings.GAME_HEIGHT / 2 - textLayout.height / 2);
            textLayout.setText(AssetManager.font, "Som-hi!");
        }
        batch.end();
    }

    /**
     * Método que se ejecuta todoe el rato al estar en "render" que actualiza la puntuacion en todom omento
     * y que si la nave colisiona, actualiza el estado del juego y muestra en la pantalla un resumen de
     * como ha ido la partida.
     * @param delta
     */
    private void updateRunning(float delta) {
        stage.act(delta);
        batch.begin();
        boolean colisio = false;
        if (!colisio) {
            textPuntuacio.setText(AssetManager.font, "Puntuacio: " + ((scrollHandler.getPuntuacio()+scrollHandler.getPuntuacioPerDestruccio())-scrollHandler.getPuntuacioPerDestruccio()));
            AssetManager.font.getData().setScale(0.3f);
            AssetManager.font.draw(batch, textPuntuacio, (Settings.GAME_WIDTH - 110), 2);
        }

        if (scrollHandler.collides(spacecraft)) {
            // Si hi ha hagut col·lisió: Reproduïm l'explosió i posem l'estat a GameOver
            AssetManager.explosionSound.play();
            stage.getRoot().findActor("spacecraft").remove();
            //textLayout.setText(AssetManager.font, "Has perdut!");
            currentState = GameState.GAMEOVER;
            colisio = true;
        }

        if (recordActual < (scrollHandler.getPuntuacio() + scrollHandler.getPuntuacioPerDestruccio())) {
            recordActual = ((scrollHandler.getPuntuacio() + scrollHandler.getPuntuacioPerDestruccio()) - scrollHandler.getPuntuacioPerDestruccio());
            //AssetManager.recordSound.play();
        }
        if (colisio) {
            AssetManager.font.getData().setScale(0.3f);
            if (scrollHandler.getPuntuacio() > 0) {
                AssetManager.font.draw(batch, textPuntuacio, (Settings.GAME_WIDTH / 2), 50);
                textLayout.setText(AssetManager.font, "Record: " + recordActual + " punts" + "\n\nPuntuacio: " + (scrollHandler.getPuntuacio() - scrollHandler.getPuntuacioPerDestruccio()) + " punts\n" +
                        "Asteroides destruits: " + scrollHandler.getPuntuacioPerDestruccio() + " punts\n\nTotal: " +
                        ((scrollHandler.getPuntuacioPerDestruccio() + scrollHandler.getPuntuacio()) - scrollHandler.getPuntuacioPerDestruccio()) + " punts");
            } else {
                textLayout.setText(AssetManager.font, "Record: " + recordActual + " punts\n\nPuntuacio: \n" + 0 + " punts JAJA");
            }
        }
        batch.end();
    }

    /**
     * Método que se ejecuta cuando mueres.
     * @param delta
     */
    private void updateGameOver(float delta) {
        stage.act(delta);
        batch.begin();
        AssetManager.font.draw(batch, textLayout, (Settings.GAME_WIDTH - textLayout.width) / 2, (Settings.GAME_HEIGHT - textLayout.height) / 2);
        // Si hi ha hagut col·lisió: Reproduïm l'explosió
        batch.draw(AssetManager.explosionAnim.getKeyFrame(explosionTime, false), (spacecraft.getX() + spacecraft.getWidth() / 2) - 32, spacecraft.getY() + spacecraft.getHeight() / 2 - 32, 64, 64);
        batch.end();
        explosionTime += delta;
    }

    /**
     * Para resetear el juego.
     */
    public void reset() {
        // Cridem als restart dels elements.
        spacecraft.reset();
        scrollHandler.reset();
        // Posem l'estat a 'Ready'
        currentState = GameState.READY;
        //Tornem a possar la mida normal
        AssetManager.font.getData().setScale(0.4f);
        // Afegim la nau a l'stage
        stage.addActor(spacecraft);
        vegada = true;
        // Posem a 0 les variables per controlar el temps jugat i l'animació de l'explosió
        explosionTime = 0.0f;
        //Reestablecemos a 0 la puntuacion
        scrollHandler.setPuntuacio(true, 0);
        scrollHandler.setPuntuacioPerDestruccio(true, 0);
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

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public Spacecraft getSpacecraft() {
        return spacecraft;
    }

    public Stage getStage() {
        return stage;
    }

    public ScrollHandler getScrollHandler() {
        return scrollHandler;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
    }
}
