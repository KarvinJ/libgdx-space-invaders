package knight.nameless.objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Laser {

    public final Rectangle bounds;
    public boolean isDestroyed;

    public Laser(Vector2 position) {
        bounds = new Rectangle(position.x + 16, position.y + 15, 4, 16);
    }

    public void draw(ShapeRenderer shapeRenderer) {

        if (!isDestroyed)
            shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void collision() {
        isDestroyed = true;
    }
}
