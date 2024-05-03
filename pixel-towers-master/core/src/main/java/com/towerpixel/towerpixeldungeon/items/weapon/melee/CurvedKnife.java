package com.towerpixel.towerpixeldungeon.items.weapon.melee;


import com.towerpixel.towerpixeldungeon.Assets;
import com.towerpixel.towerpixeldungeon.actors.Char;
import com.towerpixel.towerpixeldungeon.actors.buffs.Buff;
import com.towerpixel.towerpixeldungeon.actors.buffs.Hex;
import com.towerpixel.towerpixeldungeon.actors.hero.Hero;
import com.towerpixel.towerpixeldungeon.actors.mobs.Mob;
import com.towerpixel.towerpixeldungeon.items.weapon.Weapon;
import com.towerpixel.towerpixeldungeon.items.weapon.curses.Annoying;
import com.towerpixel.towerpixeldungeon.items.weapon.curses.Dazzling;
import com.towerpixel.towerpixeldungeon.items.weapon.curses.Degrading;
import com.towerpixel.towerpixeldungeon.items.weapon.curses.Displacing;
import com.towerpixel.towerpixeldungeon.items.weapon.curses.Explosive;
import com.towerpixel.towerpixeldungeon.items.weapon.curses.Friendly;
import com.towerpixel.towerpixeldungeon.items.weapon.curses.Polarized;
import com.towerpixel.towerpixeldungeon.items.weapon.curses.Sacrificial;
import com.towerpixel.towerpixeldungeon.items.weapon.curses.Wayward;
import com.towerpixel.towerpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class CurvedKnife extends MeleeWeapon {
    public static int sac = 0;
    {
        image = ItemSpriteSheet.CURVED_KNIFE;
        hitSound = Assets.Sounds.HIT_STAB;
        hitSoundPitch = 1.1f;
        DLY = 0.7f;
        ACC = 1.2f;
        cursed = true;
        cursedKnown = true;
        enchantment = Enchantment.randomCurse(Annoying.class, Displacing.class, Dazzling.class, Explosive.class,Wayward.class, Polarized.class, Friendly.class, Degrading.class);
        tier = 2;
        rarity = 3;
    }
    @Override
    public int min(int lvl) {
        return (tier + 1) +    //3 base
                lvl * (tier + 1) + sac;   //scaling the same
    }
    @Override
    public int max(int lvl) {
        return 2 * (tier + 1) +    //6 base
                lvl * (tier + 1) + sac * sac;   //scaling unchanged
    }
    ///they increase with each sacrificed duelist

    public int proc(Char attacker, Char defender, int damage) {
        Buff.affect( defender, Hex.class,2 );
        return super.proc(attacker, defender, damage);
    }

    @Override
    public int damageRoll(Char owner) {
        if (owner instanceof Hero) {
            Hero hero = (Hero) owner;
            Char enemy = hero.enemy();
            if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)) {
                int diff = max() - min();
                int damage = augment.damageFactor(Random.NormalIntRange(
                        min() + Math.round(diff * 0.7f),
                        max()));
                int exStr = hero.STR() - STRReq();
                if (exStr > 0) {
                    damage += Random.IntRange(0, exStr);
                }
                return damage;
            }
        }
        return super.damageRoll(owner);
    }

    @Override
    public float abilityChargeUse(Hero hero) {
        return super.abilityChargeUse(hero);
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        CurvedKnife.sacrifice(hero,  this);
    }
    public static void sacrifice(Hero hero, MeleeWeapon wep){
        wep.beforeAbilityUsed(hero);
        hero.damage(20,hero);
        sac++;//check hero class for sac
        Sample.INSTANCE.play( Assets.Sounds.HIT_STAB );
        wep.afterAbilityUsed(hero);
    }
}
