package knight.nameless;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import knight.nameless.screens.MainMenuScreen;

public class Space extends Game {

    public static Space INSTANCE;
    public final int SCREEN_WIDTH = 1024;
    public final int SCREEN_HEIGHT = 640;
    public AssetDescriptor<Skin> uiSkin;

    public Space() {

        INSTANCE = this;
    }

    @Override
    public void create() {

        uiSkin = new AssetDescriptor<>(
            "ui/uiskin.json",
            Skin.class,
            new SkinLoader.SkinParameter("ui/uiskin.atlas")
        );

        setScreen(new MainMenuScreen());
    }
}
