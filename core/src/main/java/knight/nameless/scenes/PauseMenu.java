package knight.nameless.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import knight.nameless.Space;
import knight.nameless.helpers.AssetsHelper;
import knight.nameless.helpers.GameDataHelper;
import knight.nameless.screens.GameScreen;

public class PauseMenu {

    private final Space game;
    public final Stage stage;
    private final Skin skin;
    private final BitmapFont font;
    private final Texture fontTexture;

    public PauseMenu() {

        game = Space.INSTANCE;

        Viewport viewport = new ExtendViewport(game.SCREEN_WIDTH, game.SCREEN_HEIGHT);

        stage = new Stage(viewport);

        Table table = new Table();

        table.setFillParent(true);

        fontTexture = new Texture("fonts/test.png");
        fontTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font = new BitmapFont(Gdx.files.internal("fonts/test.fnt"), new TextureRegion(fontTexture));

        Label pauseLabel = new Label("Pause Menu", new Label.LabelStyle(font, Color.RED));

        Label scoreLabel = new Label(
            "High Score: " + GameDataHelper.loadHighScore(),
            new Label.LabelStyle(font, Color.WHITE)
        );

        table.add(pauseLabel).expandX().padBottom(15);
        table.row();

        table.add(scoreLabel).expandX().padBottom(15);
        table.row();

        stage.addActor(table);

        skin = AssetsHelper.loadUiSkin();

        addButton(table, "Resume").addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                GameScreen.isGamePaused = false;

                return super.touchDown(event, x, y, pointer, button);
            }
        });

        addButton(table, "Retry").addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen());

                return super.touchDown(event, x, y, pointer, button);
            }
        });

        addButton(table, "Quit").addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();

                return super.touchDown(event, x, y, pointer, button);
            }
        });

        Gdx.input.setInputProcessor(stage);
    }

    private TextButton addButton(Table table, String buttonName) {

        TextButton textButton = new TextButton(buttonName, skin);

        table.add(textButton).width(150).height(60).padBottom(15);
        table.row();

        return textButton;
    }

    public void dispose(){
        font.dispose();
        fontTexture.dispose();
        stage.dispose();
        skin.dispose();
    }
}
