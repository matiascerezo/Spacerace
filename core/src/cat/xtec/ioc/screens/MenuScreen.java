package cat.xtec.ioc.screens;

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
 * Created by Matias on 16/03/2017.
 */

public class MenuScreen implements Screen {
    private Stage stage;
    private SpaceRace game;

    private Label.LabelStyle textStyle;
    private Label textLbl;
    private int dificil, facil, mig;
    private TextButton botonFacil, botonDificil, botonMedio;
    private Batch batch;
    boolean music;

    private TextButton.TextButtonStyle textButtonStyle;

    /**
     * Clase que gestiona la pantalla de la dificultad del juego.
     * @param prevBatch
     * @param prevViewport
     * @param game
     * @param music
     */
    public MenuScreen(Batch prevBatch, Viewport prevViewport, SpaceRace game, final boolean music) {

        stage = new Stage(prevViewport, prevBatch);
        batch = stage.getBatch();
        this.music = music;

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = AssetManager.font;

        this.game = game;

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
        textLbl = new Label("Dificultat", textStyle);

        botonFacil = new TextButton("Facil", textButtonStyle);
        botonDificil = new TextButton("Dificil", textButtonStyle);
        botonMedio = new TextButton("Mig", textButtonStyle);

        // Creem el contenidor necessari per aplicar-li les accions
        Container containerFacil = new Container(botonFacil);
        containerFacil.setTransform(true);
        containerFacil.center();
        containerFacil.setPosition(Settings.GAME_WIDTH / 2, Settings.GAME_HEIGHT / 2 - 15);

        Container containerDif = new Container(textLbl);
        containerDif.setTransform(true);
        containerDif.center();
        containerDif.setPosition(Settings.GAME_WIDTH / 2, 15);

        Container containerDificil = new Container(botonDificil);
        containerDificil.setTransform(true);
        containerDificil.center();
        containerDificil.setPosition(Settings.GAME_WIDTH / 2, Settings.GAME_HEIGHT / 2 + 30);

        Container containerMig = new Container(botonMedio);
        containerMig.setTransform(true);
        containerMig.center();
        containerMig.setPosition(Settings.GAME_WIDTH / 2, Settings.GAME_HEIGHT / 2 + 7);

        //Añadimos al stage los contenedores.
        stage.addActor(containerFacil);
        stage.addActor(containerDificil);
        stage.addActor(containerDif);
        stage.addActor(containerMig);


        //Añadimos los listeners de los botones.
        botonFacil.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MenuScreen.this.game.setScreen(new GameScreen(MenuScreen.this.stage.getBatch(), MenuScreen.this.stage.getViewport(), "facil", music));
                dispose();
            }
        });
        botonMedio.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MenuScreen.this.game.setScreen(new GameScreen(MenuScreen.this.stage.getBatch(), MenuScreen.this.stage.getViewport(), "mig", music));
                dispose();
            }
        });

        botonDificil.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MenuScreen.this.game.setScreen(new GameScreen(MenuScreen.this.stage.getBatch(), MenuScreen.this.stage.getViewport(), "dificil", music));
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