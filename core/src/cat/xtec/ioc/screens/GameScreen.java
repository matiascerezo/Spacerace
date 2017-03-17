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
    private GlyphLayout textFacil;
    private GlyphLayout textMig;
    private GlyphLayout textDificil;
    private GlyphLayout textPuntuacio;
    //private GlyphLayout textPuntuacio;
    int recordActual = 0;

    boolean vegada;
    Thread thread;

    public GameScreen(Batch prevBatch, Viewport prevViewport, String dificultad) {

        // Iniciem la música
        AssetManager.music.play();

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

        // Creem la nau i la resta d'objectes
        spacecraft = new Spacecraft(Settings.SPACECRAFT_STARTX, Settings.SPACECRAFT_STARTY, Settings.SPACECRAFT_WIDTH, Settings.SPACECRAFT_HEIGHT, stage);
        scrollHandler = new ScrollHandler();

        // Afegim els actors a l'stage
        stage.addActor(scrollHandler);
        stage.addActor(spacecraft);

        //stage.addActor(bullet);
        // Donem nom a l'Actor
        spacecraft.setName("spacecraft");

        // Iniciem el GlyphLayout
        textLayout = new GlyphLayout();
        textFacil = new GlyphLayout();
        textMig = new GlyphLayout();
        textDificil = new GlyphLayout();
        textPuntuacio = new GlyphLayout();
        //textLayout.setText(AssetManager.font, "Estas\n preparat?");

        currentState = GameState.READY;

        // Assignem com a gestor d'entrada la classe InputHandler
        Gdx.input.setInputProcessor(new InputHandler(this));


    }

    private void drawElements() {

        // Recollim les propietats del Batch de l'Stage
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        // Pintem el fons de negre per evitar el "flickering"
        //Gdx.gl20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        //Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Inicialitzem el shaperenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Definim el color (verd)
        shapeRenderer.setColor(new Color(0, 1, 0, 1));

        // Pintem la nau
        shapeRenderer.rect(spacecraft.getX(), spacecraft.getY(), spacecraft.getWidth(), spacecraft.getHeight());

        // Recollim tots els Asteroid
        ArrayList<Asteroid> asteroids = scrollHandler.getAsteroids();
        Asteroid asteroid;

        for (int i = 0; i < asteroids.size(); i++) {

            asteroid = asteroids.get(i);
            switch (i) {
                case 0:
                    shapeRenderer.setColor(1, 0, 0, 1);
                    break;
                case 1:
                    shapeRenderer.setColor(0, 0, 1, 1);
                    break;
                case 2:
                    shapeRenderer.setColor(1, 1, 0, 1);
                    break;
                default:
                    shapeRenderer.setColor(1, 1, 1, 1);
                    break;
            }
            shapeRenderer.circle(asteroid.getX() + asteroid.getWidth() / 2, asteroid.getY() + asteroid.getWidth() / 2, asteroid.getWidth() / 2);
        }
        shapeRenderer.end();
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
                    updateReady(true, false);
                    contador = 1;
                } else {
                    updateReady(false,true);
                }
                break;
        }
        //drawElements();
    }

    private void updateReady(boolean primeraVegada, boolean mostrar) {
        // Dibuixem el text al centre de la pantalla
        batch.begin();
        if (!primeraVegada) {
            AssetManager.font.draw(batch, textLayout, (Settings.GAME_WIDTH / 2) - textLayout.width / 2, (Settings.GAME_HEIGHT / 2) - textLayout.height / 2);
            //AssetManager.font.draw(batch, textPuntuacio, (Settings.GAME_WIDTH / 2) - textDificil.width / 2, (Settings.GAME_HEIGHT / 2) - textMig.height / 2);
        } else {
            if (mostrar) {
                AssetManager.font.draw(batch, textLayout, Settings.GAME_WIDTH / 2 - textLayout.width / 2, Settings.GAME_HEIGHT / 2 - textLayout.height / 2);
                textLayout.setText(AssetManager.font, "Torna a intentar-ho");
            } else {
                AssetManager.font.draw(batch, textLayout, Settings.GAME_WIDTH / 2 - textLayout.width / 2, Settings.GAME_HEIGHT / 2 - textLayout.height / 2);
                textLayout.setText(AssetManager.font, "Som-hi!");
            }
        }
        //stage.addActor(textLbl);
        batch.end();
    }


    private void updateRunning(float delta) {
        stage.act(delta);
        batch.begin();
        boolean colisio = false;
        if (!colisio) {
            textPuntuacio.setText(AssetManager.font, "Puntuacio: " + scrollHandler.getPuntuacio());
            AssetManager.font.getData().setScale(0.3f);
            AssetManager.font.draw(batch, textPuntuacio, (Settings.GAME_WIDTH - 100), 2);
            /*if (scrollHandler.getPuntuacio()>100){
                AssetManager.font.draw(batch, textPuntuacio, (Settings.GAME_WIDTH - 110), 2);
            }*/
        }

        if (scrollHandler.collides(spacecraft)) {
            // Si hi ha hagut col·lisió: Reproduïm l'explosió i posem l'estat a GameOver
            AssetManager.explosionSound.play();
            stage.getRoot().findActor("spacecraft").remove();
            //textLayout.setText(AssetManager.font, "Has perdut!");
            currentState = GameState.GAMEOVER;
            colisio = true;
        }

        //if (scrollHandler.co)
        if (recordActual < scrollHandler.getPuntuacio()) {
            recordActual = scrollHandler.getPuntuacio();
            //AssetManager.recordSound.play();
        }
        if (colisio) {
            AssetManager.font.getData().setScale(0.4f);
            if (scrollHandler.getPuntuacio() > 0) {
                textLayout.setText(AssetManager.font, "Record: " + recordActual + " punts" + "\n\nPuntuacio: " + scrollHandler.getPuntuacio() + " punts\n");
            } else {
                textLayout.setText(AssetManager.font, "Record: " + recordActual + "\n\nPuntuacio: \n" + 0 + " punts JAJA");
            }
        }
        batch.end();
    }

    private void updateGameOver(float delta) {
        stage.act(delta);

        batch.begin();
        AssetManager.font.draw(batch, textLayout, (Settings.GAME_WIDTH - textLayout.width) / 2, (Settings.GAME_HEIGHT - textLayout.height) / 2);
        // Si hi ha hagut col·lisió: Reproduïm l'explosió i posem l'estat a GameOver
        batch.draw(AssetManager.explosionAnim.getKeyFrame(explosionTime, false), (spacecraft.getX() + spacecraft.getWidth() / 2) - 32, spacecraft.getY() + spacecraft.getHeight() / 2 - 32, 64, 64);
        batch.end();

        explosionTime += delta;
    }

    public void reset() {

        // Cridem als restart dels elements.
        spacecraft.reset();
        scrollHandler.reset();

        thread = new Thread();


        // Posem l'estat a 'Ready'
        currentState = GameState.READY;

        //Tornem a possar la mida normal
        AssetManager.font.getData().setScale(0.4f);

        // Afegim la nau a l'stage
        stage.addActor(spacecraft);
        vegada = true;

        // Posem a 0 les variables per controlar el temps jugat i l'animació de l'explosió
        explosionTime = 0.0f;

        scrollHandler.setPuntuacio(true, 0);
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
