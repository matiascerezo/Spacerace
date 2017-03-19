package cat.xtec.ioc.objects;

import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.Random;

import cat.xtec.ioc.utils.Methods;
import cat.xtec.ioc.utils.Settings;

public class ScrollHandler extends Group {

    // Fons de pantalla
    Background bg, bg_back;

    // Asteroides
    int numAsteroids, puntuacio, puntuacioPerDestruccio;
    private ArrayList<Asteroid> asteroids;

    // Objecte Random
    Random r;

    public ScrollHandler() {
        // Creem els dos fons
        bg = new Background(0, 0, Settings.GAME_WIDTH * 2, Settings.GAME_HEIGHT, Settings.BG_SPEED);
        bg_back = new Background(bg.getTailX(), 0, Settings.GAME_WIDTH * 2, Settings.GAME_HEIGHT, Settings.BG_SPEED);

        // Afegim els fons al grup
        addActor(bg);
        addActor(bg_back);

        // Creem l'objecte random
        r = new Random();

        // Comencem amb 3 asteroids
        numAsteroids = 5;

        // Creem l'ArrayList
        asteroids = new ArrayList<Asteroid>();

        // Definim una mida aleatòria entre el mínim i el màxim
        float newSize = Methods.randomFloat(Settings.MIN_ASTEROID, Settings.MAX_ASTEROID) * 34;

        // Afegim el primer Asteroid a l'Array i al grup
        Asteroid asteroid = new Asteroid(Settings.GAME_WIDTH, r.nextInt(Settings.GAME_HEIGHT - (int) newSize), newSize, newSize, Settings.ASTEROID_SPEED);
        asteroids.add(asteroid);
        addActor(asteroid);

        // Des del segon fins l'últim asteroide
        for (int i = 1; i < numAsteroids; i++) {
            // Creem la mida al·leatòria
            newSize = Methods.randomFloat(Settings.MIN_ASTEROID, Settings.MAX_ASTEROID) * 34;
            // Afegim l'asteroid.
            asteroid = new Asteroid(asteroids.get(asteroids.size() - 1).getTailX() + Settings.ASTEROID_GAP, r.nextInt(Settings.GAME_HEIGHT - (int) newSize), newSize, newSize, Settings.ASTEROID_SPEED);
            // Afegim l'asteroide a l'ArrayList
            asteroids.add(asteroid);
            // Afegim l'asteroide al grup d'actors
            addActor(asteroid);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        // Si algun element està fora de la pantalla, fem un reset de l'element.
        if (bg.isLeftOfScreen()) {
            bg.reset(bg_back.getTailX());
        } else if (bg_back.isLeftOfScreen()) {
            bg_back.reset(bg.getTailX());
        }

        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid asteroid = asteroids.get(i);
            if (asteroid.isLeftOfScreen()) {
                if (i == 0) {
                    asteroid.reset(asteroids.get(asteroids.size() - 1).getTailX() + Settings.ASTEROID_GAP);
                } else {
                    asteroid.reset(asteroids.get(i - 1).getTailX() + Settings.ASTEROID_GAP);
                }
                //Cuando un asteroide sobrepasa la pantalla se incrementa en +10 los puntos.
                //Puntuacio
                setPuntuacio(false, 10);
            }
        }
    }

    /**
     * Para obtener la puntuacion
     * @return
     */
    public int getPuntuacio() {
        return puntuacio;
    }

    /**
     * Para hacer el set de puntuacion, si el boolean esta en true, se pone a 0. Esto se usara para
     * que cada vez que la nave explota se reinicie a 0 la puntuacion.
     * @param reiniciar
     * @param puntuacio
     */
    public void setPuntuacio(boolean reiniciar, int puntuacio) {
        if (reiniciar) {
            this.puntuacio = puntuacio;
        } else {
            this.puntuacio += puntuacio;
        }
    }

    /**
     * Igual que el metodo de "setPuntuacio()" pero con otra puntuacion distinta, que se incrementara
     * en +10 cuando la bala impacta con un asteroide. Se reiniciara a 0 cuando el boolean sea true.
     * @param reiniciar
     * @param puntuacioPerDestruccio
     */
    public void setPuntuacioPerDestruccio(boolean reiniciar, int puntuacioPerDestruccio) {
        if (reiniciar) {
            this.puntuacioPerDestruccio = puntuacioPerDestruccio;
        } else {
            this.puntuacioPerDestruccio += puntuacioPerDestruccio;
        }
    }

    /**
     * Para obtener la puntuacion que hagamos al destruir los asteroides.
     * @return
     */
    public int getPuntuacioPerDestruccio() {
        return puntuacioPerDestruccio;
    }

    /**
     * Método para comprobar que la nave choca con el asteroide.
     * @param nau
     * @return
     */
    public boolean collides(Spacecraft nau) {
        // Comprovem les col·lisions entre cada asteroid i la nau
        for (Asteroid asteroid : asteroids) {
            if (asteroid.collides(nau)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Para resetear to-do desde el principio
     */
    public void reset() {
        // Posem el primer asteroid fora de la pantalla per la dreta
        asteroids.get(0).reset(Settings.GAME_WIDTH);
        // Calculem les noves posicions de la resta d'asteroids.
        for (int i = 1; i < asteroids.size(); i++) {
            asteroids.get(i).reset(asteroids.get(i - 1).getTailX() + Settings.ASTEROID_GAP);
        }
    }

    /**
     * Para obtener los asteroides.
     * @return
     */
    public ArrayList<Asteroid> getAsteroids() {
        return asteroids;
    }
}