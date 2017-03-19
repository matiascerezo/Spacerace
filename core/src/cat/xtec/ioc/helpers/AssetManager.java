package cat.xtec.ioc.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetManager {

    // Sprite Sheet
    public static Texture sheet;

    // Nau i fons
    public static TextureRegion spacecraft, spacecraftDown, spacecraftUp, background, bullet;

    // Asteroid
    public static TextureRegion[] asteroid;
    public static Animation asteroidAnim;

    // Explosió
    public static TextureRegion[] explosion;
    public static Animation explosionAnim;

    //Bala
    //public static Animation bulletAnim;

    // Sons
    public static Sound explosionSound, explosion1Sound, recordSound, shootSound, shootSound1;
    public static Music music;

    // Font
    public static BitmapFont font;

    public static void load() {
        // Carreguem les textures i li apliquem el mètode d'escalat 'nearest'
        sheet = new Texture(Gdx.files.internal("sheet.png"));
        sheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        // Sprites de la nau
        spacecraft = new TextureRegion(sheet, 0, 0, 36, 15);
        spacecraft.flip(false, true);

        spacecraftUp = new TextureRegion(sheet, 36, 0, 36, 15);
        spacecraftUp.flip(false, true);

        spacecraftDown = new TextureRegion(sheet, 72, 0, 36, 15);
        spacecraftDown.flip(false, true);

        // Carreguem els 16 estats de l'asteroid
        asteroid = new TextureRegion[16];
        for (int i = 0; i < asteroid.length; i++) {

            asteroid[i] = new TextureRegion(sheet, i * 34, 15, 34, 34);
            asteroid[i].flip(false, true);

        }

        // Creem l'animació de l'asteroid i fem que s'executi contínuament en sentit anti-horari
        asteroidAnim = new Animation(0.05f, asteroid);
        asteroidAnim.setPlayMode(Animation.PlayMode.LOOP_REVERSED);

        // Creem els 16 estats de l'explosió
        explosion = new TextureRegion[16];

        // Carreguem els 16 estats de l'explosió
        int index = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                explosion[index++] = new TextureRegion(sheet, j * 64, i * 64 + 49, 64, 64);
                explosion[index - 1].flip(false, true);
            }
        }

        // Finalment creem l'animació
        explosionAnim = new Animation(0.04f, explosion);

        // Fons de pantalla
        background = new TextureRegion(sheet, 0, 177, 480, 135);
        background.flip(false, true);

        //Bala
        bullet = new TextureRegion(sheet, 527, 304, 17, 8);
        bullet.flip(false, true);

        /******************************* Sounds *************************************/
        // Explosió
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        explosion1Sound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion1.wav"));

        //Musica record
        recordSound = Gdx.audio.newSound((Gdx.files.internal("sounds/record.wav")));

        //Sonido disparo
        shootSound = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.wav"));
        shootSound1 = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot1.wav"));

        // Música del joc
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/Afterburner.ogg"));
        music.setVolume(0.2f);
        music.setLooping(true);

        /******************************* Text *************************************/
        // Font space
        FileHandle fontFile = Gdx.files.internal("fonts/space.fnt");
        font = new BitmapFont(fontFile, true);
        font.getData().setScale(0.4f);
    }

    public static void dispose() {
        // Descrtem els recursos
        sheet.dispose();
        explosionSound.dispose();
        music.dispose();
        recordSound.dispose();
    }
}
