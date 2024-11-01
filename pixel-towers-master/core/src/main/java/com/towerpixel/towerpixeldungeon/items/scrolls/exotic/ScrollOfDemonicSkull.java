package com.towerpixel.towerpixeldungeon.items.scrolls.exotic;

import static com.towerpixel.towerpixeldungeon.Dungeon.hero;
import static com.towerpixel.towerpixeldungeon.Dungeon.level;
import static com.towerpixel.towerpixeldungeon.items.wands.WandOfBlastWave.throwChar;

import com.towerpixel.towerpixeldungeon.Assets;
import com.towerpixel.towerpixeldungeon.Dungeon;
import com.towerpixel.towerpixeldungeon.actors.Actor;
import com.towerpixel.towerpixeldungeon.actors.Char;
import com.towerpixel.towerpixeldungeon.actors.buffs.Blindness;
import com.towerpixel.towerpixeldungeon.actors.buffs.Buff;
import com.towerpixel.towerpixeldungeon.actors.buffs.Cripple;
import com.towerpixel.towerpixeldungeon.actors.buffs.Hex;
import com.towerpixel.towerpixeldungeon.actors.buffs.Poison;
import com.towerpixel.towerpixeldungeon.actors.buffs.Vertigo;
import com.towerpixel.towerpixeldungeon.actors.buffs.Weakness;
import com.towerpixel.towerpixeldungeon.actors.hero.Hero;
import com.towerpixel.towerpixeldungeon.actors.mobs.Mob;
import com.towerpixel.towerpixeldungeon.actors.mobs.Statue;
import com.towerpixel.towerpixeldungeon.effects.CellEmitter;
import com.towerpixel.towerpixeldungeon.effects.particles.BlastParticle;
import com.towerpixel.towerpixeldungeon.effects.particles.ElmoParticle;
import com.towerpixel.towerpixeldungeon.effects.particles.FlameParticle;
import com.towerpixel.towerpixeldungeon.effects.particles.PurpleParticle;
import com.towerpixel.towerpixeldungeon.effects.particles.SmokeParticle;
import com.towerpixel.towerpixeldungeon.items.scrolls.Scroll;
import com.towerpixel.towerpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.towerpixel.towerpixeldungeon.mechanics.Ballistica;
import com.towerpixel.towerpixeldungeon.messages.Messages;
import com.towerpixel.towerpixeldungeon.scenes.GameScene;
import com.towerpixel.towerpixeldungeon.sprites.GnollSprite;
import com.towerpixel.towerpixeldungeon.sprites.ItemSpriteSheet;
import com.towerpixel.towerpixeldungeon.sprites.SkullDemonicSprite;
import com.towerpixel.towerpixeldungeon.sprites.SkullSprite;
import com.towerpixel.towerpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ScrollOfDemonicSkull extends ExoticScroll {

    {
        icon = ItemSpriteSheet.Icons.SCROLL_SKULL;
    }

    @Override
    public void doRead() {


        ArrayList<Integer> respawnPoints = new ArrayList<>();
        for (int iz = 0; iz < PathFinder.NEIGHBOURS8.length; iz++) {
            int p = hero.pos + PathFinder.NEIGHBOURS8[iz];
            if (Actor.findChar(p) == null && Dungeon.level.passable[p]) {
                respawnPoints.add(p);
            }
        }
        if (!respawnPoints.isEmpty()) {
            ExplosiveDemonicSkull skully = new ExplosiveDemonicSkull();
            skully.pos = Random.element(respawnPoints);
            CellEmitter.get(skully.pos).burst(ElmoParticle.FACTORY, 20);
            skully.state = skully.HUNTING;
            GameScene.add(skully);
        }

        Sample.INSTANCE.play(Assets.Sounds.BONES);

        readAnimation();

    }

    @Override
    public int value() {
        return 40 * quantity;
    }

    public static class ExplosiveDemonicSkull extends Mob {
        {
            HP = HT = 100;
            alignment = Alignment.ALLY;
            spriteClass = SkullDemonicSprite.class;

            defenseSkill = 50;

            viewDistance = 8;

            flying = true;

        }

        @Override
        protected boolean act() {
            beckon(hero.pos);
            return super.act();
        }

        @Override
        public boolean attack(Char enemy, float dmgMulti, float dmgBonus, float accMulti) {
            die(Hero.class);
            return super.attack(enemy, dmgMulti, dmgBonus, accMulti);
        }

        @Override
        public void die(Object cause) {
            int cell;
            GameScene.flash(0x00ff00);
            Sample.INSTANCE.play(Assets.Sounds.BLAST, 1f,0.5f);
            Sample.INSTANCE.play(Assets.Sounds.ALERT, 0.5f,1f);
            super.die(cause);
            for (int x = 0; x < level.width(); x++)
                for (int y = 0; y < level.height(); y++) {
                    cell = x + level.width() * y;
                    if (((enemy.pos%level.width() - x)*(enemy.pos%level.width() - x)) + ((enemy.pos/level.width() - y)*(enemy.pos/level.width() - y))<=36) {
                        Char ch = Char.findChar(cell);
                        if (ch != null) {
                            ch.damage(Math.round(damageRoll()) - enemy.drRoll(), this);
                            Buff.affect(ch, Hex.class, 15);
                            Buff.affect(ch, Poison.class).set(12);
                        }
                        if (level.heroFOV[cell]) {
                            CellEmitter.center(cell).burst(PurpleParticle.BURST, 5);
                            CellEmitter.floor(cell).start(ElmoParticle.FACTORY, 0.2f, 10);
                        }
                    }
                }
        }

        @Override
        public int attackSkill(Char target) {
            return 123456;
        }

        @Override
        public int damageRoll() {
            return Random.Int(hero.lvl * 5 + 15, hero.lvl * 10 + 25);
        }
    }
}