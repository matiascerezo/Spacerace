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

import cat.xtec.ioc.SpaceRace;
import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.utils.Settings;

/**
 * Clase que gestiona la pantalla inicial
 */
public class SplashScreen implements Screen {

    private Stage stage;
    private SpaceRace game;

    private Label.LabelStyle textStyle;
    private Label textLbl;
    private TextButton botonJugar, botonOpciones;
    private TextButton.TextButtonStyle textButtonStyle;
    private Batch batch;

    public SplashScreen(SpaceRace game) {

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
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = AssetManager.font;
        textLbl = new Label("SpaceRace", textStyle);

        //Cremos los botones con su correspondiente texto.
        botonJugar = new TextButton("Jugar", textButtonStyle);
        botonOpciones = new TextButton("Opciones", textButtonStyle);

        // Creem el contenidor necessari per aplicar-li les accions
        Container container = new Container(textLbl);
        container.setTransform(true);
        container.center();
        container.setPosition(Settings.GAME_WIDTH / 2, Settings.GAME_HEIGHT / 2);

        Container containerJugar = new Container(botonJugar);
        containerJugar.setTransform(true);
        containerJugar.center();
        containerJugar.setPosition(Settings.GAME_WIDTH / 2, Settings.GAME_HEIGHT / 2 + 30);

        Container containerOpcions = new Container(botonOpciones);
        containerOpcions.setTransform(true);
        containerOpcions.center();
        containerOpcions.setPosition(Settings.GAME_WIDTH / 2, Settings.GAME_HEIGHT / 2 + 50);

        // Afegim les accions de escalar: primer es fa gran i després torna a l'estat original ininterrompudament
        container.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.scaleTo(1.5f, 1.5f, 1), Actions.scaleTo(1, 1, 1))));
        //Añadimos los contenedores al Stage.
        stage.addActor(container);
        stage.addActor(containerJugar);
        stage.addActor(containerOpcions);

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

        //Si se hace click en este TextButton, pasará a la dificultad del juego.
        botonJugar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(stage.getBatch(), stage.getViewport(), game, true));
                dispose();
            }
        });
        //Si se hace click en este TextButton, pasará a otra pantalla donde podremos quitar el volumen o no.
        botonOpciones.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new OptionsScreen(stage.getBatch(), stage.getViewport(), game));
            }
        });
        Gdx.input.setInputProcessor(stage);
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