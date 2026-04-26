package knight.nameless.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import knight.nameless.Space;
import knight.nameless.objects.Player;

public class Hud {

    public final Stage stage;
    private static Label scoreLabel;
    private static Label livesLabel;
    private final BitmapFont font;
    private final Texture fontTexture;

    public Hud() {

        Space game = Space.INSTANCE;

        Viewport viewport = new ExtendViewport(game.SCREEN_WIDTH, game.SCREEN_HEIGHT);

        stage = new Stage(viewport);

        Table table = new Table();

        table.top();

        table.setFillParent(true);

        fontTexture = new Texture("fonts/test.png");
        fontTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font = new BitmapFont(Gdx.files.internal("fonts/test.fnt"), new TextureRegion(fontTexture));

        livesLabel = new Label("2", new Label.LabelStyle(font, Color.WHITE));
        scoreLabel = new Label("0", new Label.LabelStyle(font, Color.WHITE));
        Label worldLabel = new Label("LEVEL 01", new Label.LabelStyle(font, Color.WHITE));

        table.add(livesLabel).expandX().padTop(10);
        table.add(scoreLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);

        stage.addActor(table);
    }

    public static void addScore(int brickPoints) {

        Player.score += brickPoints;
        scoreLabel.setText(Player.score);
    }

    public static void takeAvailableHealth() {

        Player.livesQuantity -= 1;
        livesLabel.setText(Player.livesQuantity);
    }

    public void dispose(){
        font.dispose();
        fontTexture.dispose();
        stage.dispose();
    }
}
