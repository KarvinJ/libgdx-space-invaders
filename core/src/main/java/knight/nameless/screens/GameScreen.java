package knight.nameless.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import knight.nameless.Space;
import knight.nameless.helpers.GameDataHelper;
import knight.nameless.objects.*;
import knight.nameless.scenes.Hud;
import knight.nameless.scenes.PauseMenu;

import java.util.Iterator;

public class GameScreen extends ScreenAdapter {

    private final Space game;
    public OrthographicCamera camera;
    public ExtendViewport viewport;
    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
    private final Hud hud;
    private final PauseMenu pauseMenu;
    private final SpaceShip spaceShip;
    private final Player player;
    private final Array<Structure> structures;
    private final Array<Alien> aliens;
    private final Array<Laser> lasers;
    private final Array<Laser> alienLasers;
    private long lastAlienBulletTime;
    private float lastPlayerBulletTime;
    public static boolean isGamePaused;

    public GameScreen() {

        game = Space.INSTANCE;

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        player = new Player(new Rectangle(1000, 350, 32, 32));

        Structure structure = new Structure(new Rectangle(650, 450, 48, 32));
        Structure structure2 = new Structure(new Rectangle(900, 450, 48, 32));
        Structure structure3 = new Structure(new Rectangle(1150, 450, 48, 32));
        Structure structure4 = new Structure(new Rectangle(1400, 450, 48, 32));

        structures = new Array<>();

        structures.add(structure, structure2, structure3, structure4);

        aliens = createAliens();

        lasers = new Array<>();
        alienLasers = new Array<>();

        spaceShip = new SpaceShip();

        hud = new Hud();
        pauseMenu = new PauseMenu();

        camera = new OrthographicCamera();
        camera.position.set(game.SCREEN_WIDTH, game.SCREEN_HEIGHT, 0);
        viewport = new ExtendViewport(game.SCREEN_WIDTH, game.SCREEN_HEIGHT, camera);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private Array<Alien> createAliens() {

        int positionX;
        int positionY = 0;
        int alienPoints = 8;
        String spritePath;

        Array<Alien> temporalAliens = new Array<>();

        for (int i = 0; i < 5; i++) {

            positionX = 0;

            if (i == 0)
                spritePath = "images/alien_1.png";
            else if (i >= 3)
                spritePath = "images/alien_2.png";
            else
                spritePath = "images/alien_3.png";

            for (int j = 0; j < 11; j++) {

                temporalAliens.add(new Alien(positionX, positionY, spritePath, alienPoints));
                positionX += 60;
            }

            alienPoints--;
            positionY += 40;
        }

        return temporalAliens;
    }

    private void update(float deltaTime) {

        ScreenUtils.clear(0.11f, 0.11f, 0.11f, 1);

        player.update(deltaTime);

        if(TimeUtils.nanoTime() - lastAlienBulletTime > 1000000000)
            spawnAlienBullet();

        for (Iterator<Laser> iterator = alienLasers.iterator(); iterator.hasNext();) {

            Laser alienBullet = iterator.next();

            alienBullet.bounds.y -= 400 * deltaTime;

            if(player.hasCollisionWithTheBullet(alienBullet) || alienBullet.bounds.y < 300)
                iterator.remove();
        }

        for (Structure structure : structures){

            for (Iterator<Laser> iterator = lasers.iterator(); iterator.hasNext();) {

                Laser bullet = iterator.next();

                if (structure.hasCollisionWithTheBullet(bullet, true))
                    iterator.remove();
            }

            for (Iterator<Laser> iterator = alienLasers.iterator(); iterator.hasNext();) {

                Laser alienBullet = iterator.next();

                if (structure.hasCollisionWithTheBullet(alienBullet, false))
                    iterator.remove();
            }
        }

        for (Iterator<Alien> aliensIterator = aliens.iterator(); aliensIterator.hasNext();) {

            Alien alien = aliensIterator.next();

            alien.update(deltaTime);

            for (Iterator<Laser> bulletsIterator = lasers.iterator(); bulletsIterator.hasNext();) {

                Laser bullet = bulletsIterator.next();

                if (alien.hasCollisionWithTheBullet(bullet)) {

                    bulletsIterator.remove();
                    aliensIterator.remove();
                }

                else if (bullet.bounds.y > 1000)
                    bulletsIterator.remove();
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
            shootBullet();

        spaceShip.update(deltaTime);

        for (Iterator<Laser> bulletsIterator = lasers.iterator(); bulletsIterator.hasNext();) {

            Laser bullet = bulletsIterator.next();

            bullet.bounds.y += 400 * deltaTime;

            if (spaceShip.hasCollisionWithTheBullet(bullet) || bullet.bounds.y > 1000)
                bulletsIterator.remove();
        }
    }

    private void shootBullet() {

        //shoot a bullet every 1 second
        if (TimeUtils.nanoTime() - lastPlayerBulletTime > 1000000000) {

            lasers.add(new Laser(new Vector2(player.getBounds().x, player.getBounds().y)));
            lastPlayerBulletTime = TimeUtils.nanoTime();
        }
    }

    private void spawnAlienBullet() {

        int randomAlienIndex = MathUtils.random(0, aliens.size - 1);

        Alien alien = aliens.get(randomAlienIndex);

        alienLasers.add(new Laser(new Vector2(alien.getBounds().x, alien.getBounds().y)));

        lastAlienBulletTime = TimeUtils.nanoTime();
    }

    private void finishTheGame() {

        GameDataHelper.saveHighScore();
        game.setScreen(new MainMenuScreen());
    }

    @Override
    public void render(float deltaTime) {

        if (aliens.size == 0 || Player.livesQuantity < 0)
            finishTheGame();

        if (Gdx.input.isKeyJustPressed(Input.Keys.F1))
            isGamePaused = !isGamePaused;

        else if (!isGamePaused) {

            update(deltaTime);
            draw();
        } else {

            pauseMenu.stage.act();
            pauseMenu.stage.draw();
        }
    }

    private void draw() {

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        spaceShip.draw(batch);

        for (Alien alien : aliens)
            alien.draw(batch);

        for (Structure structure : structures)
            structure.draw(batch);

        player.draw(batch);

        batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.YELLOW);

        for (Laser alienBullet : alienLasers)
            alienBullet.draw(shapeRenderer);

        for (Laser bullet : lasers)
            bullet.draw(shapeRenderer);

        shapeRenderer.end();

        hud.stage.draw();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {

        player.dispose();
        spaceShip.dispose();
        hud.dispose();
        pauseMenu.dispose();
        batch.dispose();

        for (Structure structure : structures)
            structure.dispose();
    }
}
