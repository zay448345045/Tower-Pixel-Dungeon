package com.fixakathefix.towerpixeldungeon.items.herospells;

import com.fixakathefix.towerpixeldungeon.Assets;
import com.fixakathefix.towerpixeldungeon.Dungeon;
import com.fixakathefix.towerpixeldungeon.actors.Char;
import com.fixakathefix.towerpixeldungeon.actors.buffs.Barrier;
import com.fixakathefix.towerpixeldungeon.actors.buffs.Buff;
import com.fixakathefix.towerpixeldungeon.actors.mobs.Mob;
import com.fixakathefix.towerpixeldungeon.actors.mobs.towers.TowerCWall;
import com.fixakathefix.towerpixeldungeon.actors.mobs.towers.TowerCannon1;
import com.fixakathefix.towerpixeldungeon.actors.mobs.towers.TowerCannonMissileLauncher;
import com.fixakathefix.towerpixeldungeon.actors.mobs.towers.TowerCannonNuke;
import com.fixakathefix.towerpixeldungeon.actors.mobs.towers.TowerGrave1;
import com.fixakathefix.towerpixeldungeon.actors.mobs.towers.TowerGraveCrypt;
import com.fixakathefix.towerpixeldungeon.effects.particles.custom.CPShield;
import com.fixakathefix.towerpixeldungeon.levels.Level;
import com.fixakathefix.towerpixeldungeon.messages.Messages;
import com.fixakathefix.towerpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

import java.util.HashSet;

public class AbTrGreatWall extends HeroSpell{
    private static final int TURNS_ADDED_PER_WALL = 10;

    {
        image = ItemSpriteSheet.HEROSPELL_TR_WALL;
    }

    @Override
    public String info() {
        return desc() + "\n\n" + Messages.get(this, "cost", castCooldown(), TURNS_ADDED_PER_WALL);
    }
    @Override
    public void cast() {
        super.cast();
        Sample.INSTANCE.play(Assets.Sounds.MASTERY);
        for (Mob mob : Dungeon.level.mobs){
            if (mob instanceof TowerCWall){
                int wallnear = 0;
                for (int i : PathFinder.NEIGHBOURS8) if (Char.findChar(mob.pos + i) instanceof TowerCWall) wallnear+=Char.findChar(mob.pos + i).HT/10;
                mob.sprite.emitter().start(CPShield.TOCENTER, 0.01f, 30);
                Buff.affect(mob, Barrier.class).setShield(wallnear);
            }
        }
    }

    @Override
    protected int castCooldown() {
        int addturns = 0;

        HashSet<Mob> mobs = new HashSet<>(Level.mobs);
        for (Mob mob : mobs) if (mob!= null && mob.isAlive() && mob.alignment != null) {
            if (mob.alignment == Char.Alignment.ALLY) {
                if (mob instanceof TowerCWall)
                    addturns += TURNS_ADDED_PER_WALL;
            }
        }
        return 100 + addturns;
    }
}
