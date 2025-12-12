package com.fixakathefix.towerpixeldungeon.actors.mobs.towers;

import static com.fixakathefix.towerpixeldungeon.Dungeon.level;

import com.fixakathefix.towerpixeldungeon.Assets;
import com.fixakathefix.towerpixeldungeon.Dungeon;
import com.fixakathefix.towerpixeldungeon.actors.Char;
import com.fixakathefix.towerpixeldungeon.actors.buffs.Buff;
import com.fixakathefix.towerpixeldungeon.actors.buffs.ChampionEnemy;
import com.fixakathefix.towerpixeldungeon.actors.buffs.Minion;
import com.fixakathefix.towerpixeldungeon.actors.buffs.MinionBoss;
import com.fixakathefix.towerpixeldungeon.actors.mobs.Mob;
import com.fixakathefix.towerpixeldungeon.effects.CellEmitter;
import com.fixakathefix.towerpixeldungeon.effects.particles.BloodParticle;
import com.fixakathefix.towerpixeldungeon.effects.particles.ShadowParticle;
import com.fixakathefix.towerpixeldungeon.levels.Arena;
import com.fixakathefix.towerpixeldungeon.scenes.GameScene;
import com.fixakathefix.towerpixeldungeon.sprites.CharSprite;
import com.fixakathefix.towerpixeldungeon.sprites.NightmareBossRiftSprite;
import com.fixakathefix.towerpixeldungeon.windows.WndModes;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class EnemyBossPortal extends EnemyPortal {

    {
        spriteClass = NightmareBossRiftSprite.class;
        HP = HT = Dungeon.depth*150;

    }

    @Override
    public CharSprite sprite() { // changes the icon in the mob info window
        NightmareBossRiftSprite sprite = (NightmareBossRiftSprite) Reflection.newInstance(spriteClass);;
        if (HP<HT*0.5f) {
            sprite.broken();
        }
        return sprite;
    }
    public void summonMobs(){
        ArrayList<Integer> candidatesNear = new ArrayList<>();
        Sample.INSTANCE.play(Assets.Sounds.CURSED);
        counting = false;
        countDownToSpawnLeft = countDownToSpawn;
        for (int count = 0; count<chooseMobCount()*3;count++){
            count++;
            for (int i : PathFinder.NEIGHBOURS25){
                int cell = pos + i;
                if (Char.findChar(cell) == null && level.passable[cell]){
                    candidatesNear.add(cell);
                }
            }
            if (!candidatesNear.isEmpty()){
                Mob mob = Reflection.newInstance(chooseMobClass());
                mob.pos = Random.element(candidatesNear);
                mob.state = mob.HUNTING;
                mob.alignment = alignment;
                Buff.affect(mob, MinionBoss.class);
                if (level.mode == WndModes.Modes.HARDMODE) Buff.affect(mob, ChampionEnemy.Blazing.class);
                GameScene.add(mob);
                CellEmitter.floor(mob.pos).burst(ShadowParticle.CURSE, 20);
                Sample.INSTANCE.play(Assets.Sounds.DEATH, 1f, 0.75f);
            }
        }
    }
}
