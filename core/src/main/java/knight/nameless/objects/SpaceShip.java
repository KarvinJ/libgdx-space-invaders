package knight.nameless.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import knight.nameless.scenes.Hud;

public class SpaceShip extends GameObject {

    private boolean isDestroyed;
    private float stateTimer;
    private boolean shouldGoRight;
    private final float velocityX;

    public SpaceShip() {
        super(
            1600, 900,
            "okay.wav", "images/mystery.png"
        );

        velocityX = 150;
    }

    public void update(float deltaTime) {

        if (!shouldGoRight)
            moveToTheLeft(deltaTime);
        else
            moveToTheRight(deltaTime);
    }

    private void moveToTheRight(float deltaTime) {

        actualBounds.x += velocityX * deltaTime;

        if (actualBounds.x > 1000) {

            stateTimer += deltaTime;

            if (stateTimer > 10){
                shouldGoRight = false;

                stateTimer = 0;
            }
        }
    }

    private void moveToTheLeft(float deltaTime) {

        if (actualBounds.x < 450) {

            stateTimer += deltaTime;

            if (stateTimer > 10){
                shouldGoRight = true;

                stateTimer = 0;
            }
        }

        else
            actualBounds.x -= velocityX * deltaTime;
    }

    @Override
    public void draw(Batch batch) {

        if (!isDestroyed)
            super.draw(batch);
    }

    public boolean hasCollisionWithTheBullet(Laser bullet) {

        if (!isDestroyed && actualBounds.overlaps(bullet.bounds)) {

            isDestroyed = true;

            Hud.addScore(50);

            actionSound.play();

            bullet.collision();

            return true;
        }

        return false;
    }
}
