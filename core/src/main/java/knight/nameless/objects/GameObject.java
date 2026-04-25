package knight.nameless.objects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import knight.nameless.helpers.AssetsHelper;

public abstract class GameObject {

    protected final Rectangle actualBounds;
    protected final Sound actionSound;
    private final Texture sprite;

    protected GameObject(Rectangle bounds, String soundPath, String spritePath) {

        sprite = new Texture(spritePath);
        actualBounds = bounds;
        actualBounds.setSize(sprite.getWidth(), sprite.getHeight());
        actionSound = AssetsHelper.loadSound(soundPath);

    }

    public void draw(Batch batch) {
        batch.draw(sprite, actualBounds.x, actualBounds.y, actualBounds.width, actualBounds.height);
    }

    public Rectangle getBounds() {
        return actualBounds;
    }

    public void dispose() {
        sprite.dispose();
        actionSound.dispose();
    }
}
