package cat.xtec.ioc.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import cat.xtec.ioc.SpaceRace;
import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.utils.Settings;

/**
 * Created by Matias on 17/03/2017.
 */


/**
 * Clase que gestiona las opciones del juego, poner/quitar volumen.
 */
public class OptionsScreen implements Screen {

    private Stage stage;
    private TextButton botonQuitarVolumen, botonPonerVolumen;
    private SpaceRace game;
    private GameScreen gameScreen;
    private Label textLbl;
    private Label.LabelStyle textStyle;
    boolean music;

    private TextButton.TextButtonStyle textButtonStyle;

    public OptionsScreen(Batch prevBatch, Viewport prevViewport, final SpaceRace game) {

        stage = new Stage(prevViewport, prevBatch);

        //Creamos la textura del boton
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = AssetManager.font;

        this.game = game;
        gameScreen = new GameScreen();

        // Creem la càmera de les dimensions del joc
        OrthographicCamera camera = new OrthographicCamera(Settings.GAME_WIDTH, Settings.GAME_HEIGHT);
        // Posant el paràmetre a true configurem la càmera per a
        // que faci servir el sistema de coordenades Y-Down
        camera.setToOrtho(true);

        // Creem el viewport amb les mateixes dimensions que la càmera
        StretchViewport viewport = new StretchViewport(Settings.GAME_WIDTH, Settings.GAME_HEIGHT, camera);

        // Creem l'stage i assginem el viewport
        stage = new Stage(viewport);

        // Afegim el fons
        stage.addActor(new Image(AssetManager.background));

        // Creem l'estil de l'etiqueta i l'etiqueta
        textStyle = new Label.LabelStyle(AssetManager.font, null);
        textLbl = new Label("Opcions", textStyle);

        //Establim el text que anirá en els botons.
        botonQuitarVolumen = new TextButton("Treure música", textButtonStyle);
        botonPonerVolumen = new TextButton("Possar música", textButtonStyle);

        // Creem el contenidor necessari per aplicar-li les accions
        Container containerPonerVolum = new Container(botonPonerVolumen);
        containerPonerVolum.setTransform(true);
        containerPonerVolum.center();
        containerPonerVolum.setPosition(Settings.GAME_WIDTH / 2, Settings.GAME_HEIGHT / 2 - 15);

        Container containerDif = new Container(textLbl);
        containerDif.setTransform(true);
        containerDif.center();
        containerDif.setPosition(Settings.GAME_WIDTH / 2, 15);

        Container containerQuitarVolum = new Container(botonQuitarVolumen);
        containerQuitarVolum.setTransform(true);
        containerQuitarVolum.center();
        containerQuitarVolum.setPosition(Settings.GAME_WIDTH / 2, Settings.GAME_HEIGHT / 2 + 30);

        stage.addActor(containerPonerVolum);
        stage.addActor(containerQuitarVolum);
        stage.addActor(containerDif);

        /**
         * Apliquem els listeners als botons.
         */
        botonPonerVolumen.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameScreen.isPlayMusic("no");
                music = true;
                game.setScreen(new MenuScreen(stage.getBatch(), stage.getViewport(), game, music));
                dispose();
            }
        });
        botonQuitarVolumen.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameScreen.isPlayMusic("si");
                music = false;
                game.setScreen(new MenuScreen(stage.getBatch(), stage.getViewport(), game, music));
                dispose();
            }
        });

        Gdx.input.setInputProcessor(stage);

        // Creem la imatge de la nau i li assignem el moviment en horitzontal
        Image spacecraft = new Image(AssetManager.spacecraft);
        float y = Settings.GAME_HEIGHT / 2 + textLbl.getHeight();
        spacecraft.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.moveTo(0 - spacecraft.getWidth(), y), Actions.moveTo(Settings.GAME_WIDTH, y, 5))));

        stage.addActor(spacecraft);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        stage.draw();
        stage.act(delta);
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
}
