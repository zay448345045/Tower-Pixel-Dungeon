package com.towerpixel.towerpixeldungeon.windows;

import static com.towerpixel.towerpixeldungeon.Badges.loadGlobal;

import com.badlogic.gdx.Gdx;
import com.towerpixel.towerpixeldungeon.Assets;
import com.towerpixel.towerpixeldungeon.Chrome;
import com.towerpixel.towerpixeldungeon.SPDSettings;
import com.towerpixel.towerpixeldungeon.ShatteredPixelDungeon;
import com.towerpixel.towerpixeldungeon.Styles;
import com.towerpixel.towerpixeldungeon.effects.particles.WindParticle;
import com.towerpixel.towerpixeldungeon.messages.Messages;
import com.towerpixel.towerpixeldungeon.scenes.GameScene;
import com.towerpixel.towerpixeldungeon.scenes.PixelScene;
import com.towerpixel.towerpixeldungeon.scenes.TitleScene;
import com.towerpixel.towerpixeldungeon.ui.*;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.Arrays;

public class WndStyles extends Window {

    private final int WIDTH = Math.min((int) (PixelScene.uiCamera.width * 0.85), 200);
    private final int HEIGHT = (int) (PixelScene.uiCamera.height * 0.85);
    private static final int TTL_HEIGHT = 18;
    private static final int BTN_HEIGHT = 18;
    private static final int GAP = 1;
    private ArrayList<IconButton> styleIconButtons = new ArrayList<>();
    private ArrayList<StyleButton> styleCheckBoxes = new ArrayList<>();

    public WndStyles() {

        super();
        loadGlobal();

        resize(WIDTH, HEIGHT);

        RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(this, "title"), 12);
        title.hardlight(TITLE_COLOR);
        title.setPos(
                (WIDTH - title.width()) / 2,
                (TTL_HEIGHT - title.height()) / 2
        );
        PixelScene.align(title);
        add(title);
        ArrayList<Styles.Style> allStyles = new ArrayList<>();

        for (Styles.Style style : Styles.Style.values()){
            if (style.exists(style)) {
                allStyles.add(style);
            }
        }
        ScrollPane pane = new ScrollPane(new Component()) {
            @Override
            public void onClick(float x, float y) {
                int size = styleCheckBoxes.size();
                for (int i = 0; i < size; i++) {
                    if (styleCheckBoxes.get(i).onClick(x, y)) break;
                }
                size = styleIconButtons.size();
                for (int i = 0; i < size; i++) {
                    if (styleIconButtons.get(i).inside(x, y)) {
                        int index = i;

                        String message = allStyles.get(index).desc();
                        String title = allStyles.get(index).btName();
                        ShatteredPixelDungeon.scene().add(
                                new WndTitledMessage(
                                        new Image(Assets.Interfaces.STYLEICONS, (allStyles.get(index).ordinal()) * 16, 0, 16, 16),
                                        title, message)
                        );

                        break;
                    }
                }
            }
        };
        add(pane);
        pane.setRect(0, title.bottom() + 2, WIDTH, HEIGHT - title.bottom() - 2);
        Component content = pane.content();

        float pos = 2;
        for (Styles.Style i : allStyles) {
            if (i.exists(i)) {

                final String style = i.btName();

                StyleButton styleCB = new StyleButton("       " + style, 9, i.condition(i) ? Chrome.Type.RED_BUTTON : Chrome.Type.GREY_BUTTON);
                //styleCB.checked(i.checked(i));
                styleCB.active = true;
                styleCB.style = i;

                pos += GAP;
                styleCB.setRect(0, pos, WIDTH - 16, BTN_HEIGHT);

                content.add(styleCB);
                styleCheckBoxes.add(styleCB);

                IconButton info = new IconButton(i.condition(i) ? new Image(Assets.Interfaces.UNLOCKICONS, 0, 0, 14, 14) : new Image(Assets.Interfaces.UNLOCKICONS, 14, 0, 14, 14)) {
                    @Override
                    protected void layout() {
                        super.layout();
                        hotArea.y = -5000;
                    }


                };
                info.setRect(styleCB.right(), pos, 16, BTN_HEIGHT);
                content.add(info);
                styleIconButtons.add(info);
                Image icon = new Image(Assets.Interfaces.STYLEICONS, (i.ordinal()) * 16, 0, 16, 16);
                icon.x = styleCB.left() + 1;
                icon.y = styleCB.top() + 1;
                content.add(icon);


                pos = styleCB.bottom();
            }
        }

        content.setSize(WIDTH, pos);
        this.visible = true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public class StyleButton extends RedButton {

        public Styles.Style style;

        public StyleButton(String label) {
            super(label);
        }
        public StyleButton(String label, int size, Chrome.Type chrometype) {
            super(label,  size, chrometype);
        }

        @Override
        protected void onClick() {
            super.onClick();
            if (active) {
                if (style.condition(style)){
                SPDSettings.style(style.index(style));
                ShatteredPixelDungeon.scene().add(new WndTitledMessage( Icons.INFO.get(),"The interface is set!界面设置好了！", "The game needs to _reload_ for the interface to change. Reopen it after it closes.游戏需要_重新加载_才能更改界面。关闭后重新打开。"){
                    @Override
                    public void onBackPressed() {
                        ShatteredPixelDungeon.instance.dispose();
                    }
                });
                }
            }
        }

        protected boolean onClick(float x, float y) {
            if (!inside(x, y)) return false;
            Sample.INSTANCE.play(Assets.Sounds.CLICK);
            onClick();
            return true;
        }

        @Override
        protected void layout() {
            super.layout();
            hotArea.width = hotArea.height = 0;
        }
    }
}
