package com.towerpixel.towerpixeldungeon.levels;

import static com.towerpixel.towerpixeldungeon.Dungeon.hero;

import com.towerpixel.towerpixeldungeon.Assets;
import com.towerpixel.towerpixeldungeon.Dungeon;
import com.towerpixel.towerpixeldungeon.actors.Char;
import com.towerpixel.towerpixeldungeon.actors.blobs.Blob;
import com.towerpixel.towerpixeldungeon.actors.buffs.Buff;
import com.towerpixel.towerpixeldungeon.actors.mobs.DM300;
import com.towerpixel.towerpixeldungeon.actors.mobs.Goblin;
import com.towerpixel.towerpixeldungeon.actors.mobs.Mob;
import com.towerpixel.towerpixeldungeon.actors.mobs.towers.TowerMiner;
import com.towerpixel.towerpixeldungeon.items.Generator;
import com.towerpixel.towerpixeldungeon.items.quest.Pickaxe;
import com.towerpixel.towerpixeldungeon.levels.features.LevelTransition;
import com.towerpixel.towerpixeldungeon.levels.painters.Painter;
import com.towerpixel.towerpixeldungeon.messages.Messages;
import com.towerpixel.towerpixeldungeon.scenes.GameScene;
import com.towerpixel.towerpixeldungeon.tiles.DungeonTilemap;
import com.towerpixel.towerpixeldungeon.utils.GLog;
import com.towerpixel.towerpixeldungeon.windows.WndModes;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Arena11 extends Arena{

    {

        name = "Upper caves";
        WIDTH = 100;
        HEIGHT = 100;
        color1 = 0x534f3e;
        color2 = 0xb9d661;

        startGold = 1500;
        startLvl = 11;
        viewDistance = 15;

        maxWaves = 15;

        amuletCell = WIDTH/2 +WIDTH*HEIGHT/2;
        exitCell = amuletCell;
        towerShopKeeperCell = amuletCell - 10*WIDTH - 3;
        normalShopKeeperCell = amuletCell - 10*WIDTH + 2;

        waveCooldownNormal = 10;
        waveCooldownBoss = 100;
    }


    @Override
    public void playLevelMusic() {
        Music.INSTANCE.playTracks(
                new String[]{Assets.Music.CAVES_BOSS},
                new float[]{1},
                false);
    }

    @Override
    public int mobsToDeploy(int wave) {
        switch (wave){
            case 1: return 10;
            case 2: return 2;
            case 3: return 10;
            case 4: return 3;
            case 5: return 4;
            case 6: return 4;
            case 7: return 10;
            case 8: return 8;
            case 9: return 20;
            case 10: return 10;
            case 11: return 7;
            case 12: return 10;
            case 13: return 30;
            case 14: return 12;
            case 15: return 20;
        }
        return 1;
    }

    @Override
    public void deploymobs(int wave, Direction direction, int group) {
        if (wave == 15){
            super.deploymobs(wave, Direction.TOOUP, 1);
        } else if (wave % 5 == 0) {
            super.deploymobs(wave, Direction.TOODOWN, 1);
        } else if (wave % 2 == 1) {
            super.deploymobs(wave, Direction.TOORIGHT, 1);
        } else super.deploymobs(wave, Direction.TOOLEFT, 1);


    }

    @Override
    protected boolean build() {
        boolean checkUp = true;
        do try {
            setSize(WIDTH, HEIGHT);

            Painter.fill(this, 0, 0, WIDTH, HEIGHT, Terrain.WALL);
            Painter.fill(this, 4, HEIGHT / 2, WIDTH - 8, 4, Terrain.EMPTY);
            Painter.fill(this, WIDTH / 2, 4, 4, HEIGHT - 8, Terrain.EMPTY);
            Painter.fillEllipse(this, 9, 9, WIDTH - 18, HEIGHT - 18, Terrain.EMPTY);
            Painter.fillEllipse(this, 10, 10, WIDTH - 20, HEIGHT - 20, Terrain.WALL);

            ArrayList<Integer> empty = new ArrayList<>();
            for (int m = 3; m < WIDTH - 3; m++)
                for (int n = 3; n < HEIGHT - 3; n++) {
                    if (this.map[m + WIDTH * n] == Terrain.EMPTY) empty.add(m + WIDTH * n);
                }
            for (int tile : empty) {
                if (Math.random() > 0.3) this.map[tile + 1] = Terrain.EMPTY;
                if (Math.random() > 0.3) this.map[tile - 1] = Terrain.EMPTY;
                if (Math.random() > 0.3) this.map[tile + WIDTH] = Terrain.EMPTY;
                if (Math.random() > 0.3) this.map[tile - WIDTH] = Terrain.EMPTY;
            }


            int x = WIDTH / 2;
            int y = HEIGHT / 2;
            ArrayList<Integer> candidatesforspawn = new ArrayList<>();
            for (int m = 10; m < WIDTH - 10; m++)
                for (int n = 10; n < HEIGHT - 10; n++) {
                    if (((m < 11) || (m > WIDTH - 11)) || ((n < 11) || (n > HEIGHT - 11)))
                        candidatesforspawn.add(m + WIDTH * n);
                }


            for (int xcur = 5; xcur<WIDTH-5;xcur++){
                Painter.fill(this, xcur-2,HEIGHT/2-2, 5 , 5, Terrain.EMPTY);
            }
            for (int ycur = HEIGHT/2; ycur<HEIGHT-5;ycur++){
                Painter.fill(this, WIDTH/2-2 + Random.NormalIntRange(-1,1),ycur-2 , 5 , 5, Terrain.EMPTY);
            }
            for (int xcur = 5; xcur<WIDTH-5;xcur++){
                Painter.fill(this, xcur-2,HEIGHT/2-2, 5 , 5, Terrain.EMPTY);
            }
            for (int ycur = HEIGHT/2; ycur<HEIGHT-5;ycur++){
                Painter.fill(this, WIDTH/2-2 + Random.NormalIntRange(-1,1),ycur-2 , 5 , 5, Terrain.EMPTY);
            }


            ArrayList<Integer> emptyTiles = new ArrayList<>();
            for (int m = 2; m < WIDTH - 2; m++)
                for (int n = 2; n < HEIGHT - 2; n++) {
                    if (this.map[m + WIDTH * n] == Terrain.EMPTY) emptyTiles.add(m + WIDTH * n);
                }
            for (int tile : emptyTiles) {
                this.map[tile + 1] = Terrain.EMPTY;
                this.map[tile - 1] = Terrain.EMPTY;
                this.map[tile + WIDTH] = Terrain.EMPTY;
                this.map[tile - WIDTH] = Terrain.EMPTY;
            }
            Painter.fill(this, 0, 0, WIDTH, 1, Terrain.WALL);
            Painter.fill(this, 0, 0, 1, HEIGHT, Terrain.WALL);
            Painter.fill(this, WIDTH - 1, 0, 1, HEIGHT, Terrain.WALL);
            Painter.fill(this, 0, HEIGHT - 1, WIDTH, 1, Terrain.WALL);


            for (int m = 0; m < WIDTH; m++)
                for (int n = 0; n < HEIGHT; n++) {
                    if (this.map[m + WIDTH * n] == Terrain.WALL && Math.random() > 0.95)
                        this.map[m + WIDTH * n] = Terrain.WALL_DECO;
                }

            for (int x1 = 1; x1 < WIDTH - 1; x1++)
                for (int y1 = 1; y1 < HEIGHT - 1; y1++) {

                    int cell = x1 + WIDTH * y1;
                    //Random grass
                    if (Math.random() > 0.92) {
                        if (this.map[cell] == Terrain.EMPTY) this.map[cell] = Terrain.GRASS;

                    }
                    //tall grass
                    if (Math.random() > 0.92) {
                        if (this.map[cell] == Terrain.EMPTY) this.map[cell] = Terrain.HIGH_GRASS;
                    }
                    //water
                    if (Math.random() > 0.98) {
                        if (this.map[cell] == Terrain.EMPTY) this.map[cell] = Terrain.WATER;
                        if (this.map[cell + 1] == Terrain.EMPTY) this.map[cell + 1] = Terrain.WATER;
                        if (this.map[cell - 1] == Terrain.EMPTY) this.map[cell - 1] = Terrain.WATER;
                        if (this.map[cell + WIDTH] == Terrain.EMPTY)
                            this.map[cell + WIDTH] = Terrain.WATER;
                        if (this.map[cell - WIDTH] == Terrain.EMPTY)
                            this.map[cell - WIDTH] = Terrain.WATER;
                    }
                    //lakes
                    if (Math.random() > 0.996) {
                        if (this.map[cell] == Terrain.EMPTY) try {
                            int lakewidth = Random.Int(4, 10);
                            int lakeheight = Random.Int(4, 10);
                            Painter.fillEllipse(this, cell % WIDTH - lakewidth / 2, cell / WIDTH - lakeheight / 2, lakewidth, lakeheight, Terrain.WATER);
                        } catch (Exception ignored) {
                        }
                    }
                    //gardens
                    if (Math.random() > 0.999) {
                        if (this.map[cell] == Terrain.EMPTY) try {
                            int lakewidth = Random.Int(4, 10);
                            int lakeheight = Random.Int(4, 10);
                            if (Math.random() > 0.8)
                                Painter.fillEllipse(this, cell % WIDTH - lakewidth / 2, cell / WIDTH - lakeheight / 2, lakewidth, lakeheight, Terrain.HIGH_GRASS);
                            Painter.fillEllipse(this, cell % WIDTH - lakewidth / 2 + 1, cell / WIDTH - lakeheight / 2 + 1, lakewidth - 2, lakeheight - 2, Terrain.GRASS);
                        } catch (Exception ignored) {
                        }
                    }
                    //strange platforms
                    if (Math.random() > 0.996) {
                        if (this.map[cell] == Terrain.EMPTY) try {
                            int lakewidth = Random.Int(2, 5);
                            int lakeheight = Random.Int(2, 5);
                            Painter.fillDiamond(this, cell % WIDTH - lakewidth / 2, cell / WIDTH - lakeheight / 2, lakewidth, lakeheight, Terrain.EMPTY_SP);
                        } catch (Exception ignored) {
                        }
                    }
                }

            Painter.fillEllipse(this, WIDTH / 2 - 7, HEIGHT / 2 - 7, 14, 14, Terrain.EMPTY);

            for (int x1 = 1; x1 < WIDTH - 1; x1++)
                for (int y1 = 1; y1 < HEIGHT - 1; y1++) {
                    int cell = x1 + WIDTH * y1;
                    //some puddles
                    if (Math.random() > 0.92) {
                        if (this.map[cell] == Terrain.EMPTY) this.map[cell] = Terrain.EMPTY_DECO;
                    }
                }
            Painter.fill(this, WIDTH / 2 - 6, HEIGHT / 2 - 11, 12, 7, Terrain.EMPTY);
            Painter.fill(this, WIDTH / 2 - 6, HEIGHT / 2 - 11, 12, 3, Terrain.BARRICADE);
            Painter.fill(this, WIDTH / 2 - 5, HEIGHT / 2 - 10, 10, 2, Terrain.EMPTY_SP);


            LevelTransition exit = new LevelTransition(this, exitCell, LevelTransition.Type.REGULAR_EXIT);

            transitions.add(exit);

            this.map[exitCell] = Terrain.EXIT;
            this.map[amuletCell] = Terrain.PEDESTAL;

        } catch (Exception tryAgain){checkUp=false;} while (!checkUp);
        return true;
    }


    @Override
    public void addDestinations() {
        ArrayList<Integer> candidates = new ArrayList<>();
        for (int m = 0; m<WIDTH*HEIGHT;m++){
            if (this.passable[m]&&this.map[m]==Terrain.EMPTY) candidates.add(m);
        }
        this.drop(Generator.random(Generator.Category.ARTIFACT),Random.element(candidates));
        this.drop(Generator.random(Generator.Category.RING),Random.element(candidates));
        this.drop(Generator.random(Generator.Category.WAND),Random.element(candidates));
        this.drop(Generator.random(Generator.Category.SCROLL),Random.element(candidates));
        this.drop(Generator.random(Generator.Category.POTION),Random.element(candidates));
        this.drop(Generator.random(Generator.Category.SCROLL),Random.element(candidates));
        this.drop(Generator.random(Generator.Category.POTION),Random.element(candidates));
        this.drop(Generator.random(Generator.Category.SCROLL),Random.element(candidates));
        this.drop(Generator.random(Generator.Category.POTION),Random.element(candidates));
        this.drop(Generator.random(Generator.Category.MIS_T3),Random.element(candidates));
        this.drop(Generator.random(Generator.Category.MIS_T2),Random.element(candidates));
        this.drop(Generator.random(Generator.Category.MIS_T4),Random.element(candidates));
        this.drop(Generator.random(Generator.Category.MIS_T5),Random.element(candidates));
        this.drop(Generator.random(Generator.Category.ARMOR),Random.element(candidates));
        this.drop(Generator.random(Generator.Category.ARMOR),Random.element(candidates));
        this.drop(Generator.random(Generator.Category.WEP_T1),Random.element(candidates));
        this.drop(Generator.random(Generator.Category.WEP_T2),Random.element(candidates));
        this.drop(Generator.random(Generator.Category.WEP_T3),Random.element(candidates));
        this.drop(Generator.random(Generator.Category.WEP_T4),Random.element(candidates));
        this.drop(Generator.random(Generator.Category.WEP_T5),Random.element(candidates));
        this.drop(new Pickaxe() ,Random.element(candidates));
        this.drop(new Pickaxe() ,Random.element(candidates));
        this.drop(new Pickaxe() ,Random.element(candidates));
        this.drop(new Pickaxe() ,Random.element(candidates));
        this.drop(new Pickaxe() ,Random.element(candidates));
        this.drop(new Pickaxe() ,Random.element(candidates));
        this.drop(new Pickaxe() ,Random.element(candidates));
        this.drop(new Pickaxe() ,Random.element(candidates));
        candidates.clear();

        super.addDestinations();
    }

    @Override
    public void initNpcs() {
        super.initNpcs();
        if (mode == WndModes.Modes.CHALLENGE){
            TowerMiner miner1 = new TowerMiner();
            miner1.pos = amuletCell - 6;
            GameScene.add(miner1);
            TowerMiner miner2 = new TowerMiner();
            miner2.pos = amuletCell + 6;
            GameScene.add(miner2);
            TowerMiner miner3 = new TowerMiner();
            miner3.pos = amuletCell - 5*WIDTH;
            GameScene.add(miner3);
            TowerMiner miner4 = new TowerMiner();
            miner4.pos = amuletCell + 6*WIDTH;
            GameScene.add(miner4);

            TowerMiner miner5 = new TowerMiner();
            miner5.pos = amuletCell - 4 - 4*WIDTH;
            GameScene.add(miner5);
            TowerMiner miner6 = new TowerMiner();
            miner6.pos = amuletCell + 4 - 4*WIDTH;
            GameScene.add(miner6);
            TowerMiner miner7 = new TowerMiner();
            miner7.pos = amuletCell - 4 + 4*WIDTH;
            GameScene.add(miner7);
            TowerMiner miner8 = new TowerMiner();
            miner8.pos = amuletCell + 4 + 4*WIDTH;
            GameScene.add(miner8);

        } else {
            TowerMiner miner1 = new TowerMiner();
            miner1.pos = amuletCell - 6;
            GameScene.add(miner1);
            TowerMiner miner2 = new TowerMiner();
            miner2.pos = amuletCell + 6;
            GameScene.add(miner2);
        }

    }

    @Override
    public void doStuffEndwave(int wave) {
        for (Mob mob : mobs){
            if (mob instanceof TowerMiner){
                TowerMiner miner = (TowerMiner) mob;
                if (mode == WndModes.Modes.CHALLENGE){
                    miner.dropGold(55);
                } else {
                    miner.dropGold(230);
                }
            }
        }
        super.doStuffEndwave(wave);
    }

    @Override
    public String tileName( int tile ) {
        switch (tile) {
            case Terrain.GRASS:
                return Messages.get(CavesLevel.class, "grass_name");
            case Terrain.HIGH_GRASS:
                return Messages.get(CavesLevel.class, "high_grass_name");
            case Terrain.WATER:
                return Messages.get(CavesLevel.class, "water_name");
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc( int tile ) {
        switch (tile) {
            case Terrain.ENTRANCE:
                return Messages.get(CavesLevel.class, "entrance_desc");
            case Terrain.EXIT:
                return Messages.get(CavesLevel.class, "exit_desc");
            case Terrain.HIGH_GRASS:
                return Messages.get(CavesLevel.class, "high_grass_desc");
            case Terrain.WALL_DECO:
                return Messages.get(CavesLevel.class, "wall_deco_desc");
            case Terrain.BOOKSHELF:
                return Messages.get(CavesLevel.class, "bookshelf_desc");
            default:
                return super.tileDesc( tile );
        }
    }

    @Override
    public Group addVisuals() {
        super.addVisuals();
        addCavesVisuals( this, visuals );
        return visuals;
    }

    public static void addCavesVisuals( Level level, Group group ) {
        for (int i=0; i < level.length(); i++) {
            if (level.map[i] == Terrain.WALL_DECO) {
                group.add( new Arena11.Vein( i ) );
            }
        }
    }

    public static class Vein extends Group {

        private int pos;

        private float delay;

        public Vein( int pos ) {
            super();

            this.pos = pos;

            delay = Random.Float( 2 );
        }

        @Override
        public void update() {

            if (visible = (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {

                super.update();

                if ((delay -= Game.elapsed) <= 0) {

                    //pickaxe can remove the ore, should remove the sparkling too.
                    if (Dungeon.level.map[pos] != Terrain.WALL_DECO){
                        kill();
                        return;
                    }

                    delay = Random.Float();

                    PointF p = DungeonTilemap.tileToWorld( pos );
                    ((Arena11.Sparkle)recycle( Arena11.Sparkle.class )).reset(
                            p.x + Random.Float( DungeonTilemap.SIZE ),
                            p.y + Random.Float( DungeonTilemap.SIZE ) );
                }
            }
        }
    }

    public static void dropRock( Char target ) {

        hero.interrupt();
        final int rockCenter;

        rockCenter = target.pos;


        int safeCell;
        do {
            safeCell = rockCenter + PathFinder.NEIGHBOURS8[Random.Int(8)];
        } while ((Dungeon.level.solid[safeCell] && Random.Int(2) == 0)
                || (Blob.volumeAt(safeCell, CavesBossLevel.PylonEnergy.class) > 0 && Random.Int(2) == 0));

        ArrayList<Integer> rockCells = new ArrayList<>();

        int start = rockCenter - Dungeon.level.width() * 3 - 3;
        int pos;
        for (int y = 0; y < 7; y++) {
            pos = start + Dungeon.level.width() * y;
            for (int x = 0; x < 7; x++) {
                if (!Dungeon.level.insideMap(pos)) {
                    pos++;
                    continue;
                }
                //add rock cell to pos, if it is not solid, and isn't the safecell
                if (!Dungeon.level.solid[pos] && pos != safeCell && Random.Int(Dungeon.level.distance(rockCenter, pos)) == 0) {
                    //don't want to overly punish players with slow move or attack speed
                    rockCells.add(pos);
                }
                pos++;
            }
        }
        Buff.append(hero, DM300.FallingRockBuff.class, GameMath.gate(1, target.cooldown(), 3)).setRockPositions(rockCells);
    }

    public static final class Sparkle extends PixelParticle {

        public void reset( float x, float y ) {
            revive();

            this.x = x;
            this.y = y;

            left = lifespan = 0.5f;
        }

        @Override
        public void update() {
            super.update();

            float p = left / lifespan;
            size( (am = p < 0.5f ? p * 2 : (1 - p) * 2) * 2 );
        }
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_CAVES;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_CAVES;
    }

}
