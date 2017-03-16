package cat.xtec.ioc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.StretchViewport;

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
    private int velocitatEntreAsteroids;

    public MenuScreen(SpaceRace game) {

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

        // Creem el contenidor necessari per aplicar-li les accions
        Container container = new Container(textLbl);
        container.setTransform(true);
        container.center();
        container.setPosition(Settings.GAME_WIDTH / 2, Settings.GAME_HEIGHT / 2);

        // Afegim les accions de escalar: primer es fa gran i després torna a l'estat original ininterrompudament

        stage.addActor(container);

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

        // Si es fa clic en la pantalla, canviem la pantalla
        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(stage.getBatch(), stage.getViewport()));
            dispose();
        }
        //if (Gdx.input.)

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

    public int getDificil() {
        return dificil;
    }

    public void setDificil(int dificil) {
        this.dificil = dificil;
    }

    public int getFacil() {
        return facil;
    }

    public void setFacil(int facil) {
        this.facil = facil;
    }

    public int getMig() {
        return mig;
    }

    public void setMig(int mig) {
        this.mig = mig;
        setVelocitatEntreAsteroids(30);
    }

    public int getVelocitatEntreAsteroids() {
        return velocitatEntreAsteroids;
    }

    public void setVelocitatEntreAsteroids(int velocitatEntreAsteroids) {
        this.velocitatEntreAsteroids = velocitatEntreAsteroids;
    }
}
/*private Image playBtn, creditsBtn, optionsBtn;
private Stage stage;
private boolean playTouch, optionsTouch, creditsTouch;
private OrthographicCamera cam;
private Viewport viewport;
private MainClass game;
LoadingScreen loading;


public MenuScreen(MainClass game) {

    this.game = game;
    cam = new OrthographicCamera();
    viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cam);

}


@Override
public void show() {
    Table table = new Table();
    table.center();
    table.setFillParent(true);

    playBtn = new Image(new Texture("buttons/playBtn.png"));
    creditsBtn = new Image(new Texture("buttons/CreditsBtn.png"));
    optionsBtn = new Image(new Texture("buttons/optionsBtn.png"));

    stage = new Stage();

    playBtn.setSize(playBtn.getImageWidth()*2, playBtn.getImageHeight()*2);
    creditsBtn.setSize(creditsBtn.getImageWidth()*2, creditsBtn.getImageHeight()*2);
    optionsBtn.setSize(optionsBtn.getImageWidth()*2, optionsBtn.getImageHeight()*2);


    Gdx.input.setInputProcessor(stage);

    playBtn.addListener(new InputListener() {

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            ((Game) Gdx.app.getApplicationListener()).setScreen(new LoadingScreen(game));
            playTouch = true;
            return true;
        }
    });

    optionsBtn.addListener(new InputListener() {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            optionsTouch = true;
            return true;
        }
    });

    creditsBtn.addListener(new InputListener() {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

            ((Game)Gdx.app.getApplicationListener()).setScreen(new  CreditsScreen(game));
            creditsTouch = true;
            return true;
        }
    });


    table.add(playBtn).expandX().padTop(playBtn.getHeight()/4);
    table.row();
    table.add(optionsBtn).expandX().padTop(optionsBtn.getHeight()/4);
    table.row();
    table.add(creditsBtn).expandX().padTop(creditsBtn.getHeight()/4);
    table.pack();

    stage.addActor(table);*/