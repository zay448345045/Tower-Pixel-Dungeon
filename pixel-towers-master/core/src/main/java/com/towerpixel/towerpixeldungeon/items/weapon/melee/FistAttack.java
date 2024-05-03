package com.towerpixel.towerpixeldungeon.items.weapon.melee;

import static com.towerpixel.towerpixeldungeon.Dungeon.hero;

import com.towerpixel.towerpixeldungeon.Assets;
import com.towerpixel.towerpixeldungeon.Dungeon;
import com.towerpixel.towerpixeldungeon.actors.hero.Hero;
import com.towerpixel.towerpixeldungeon.actors.hero.HeroClass;
import com.towerpixel.towerpixeldungeon.items.Item;
import com.towerpixel.towerpixeldungeon.items.rings.RingOfForce;
import com.towerpixel.towerpixeldungeon.items.weapon.SpiritBow;
import com.towerpixel.towerpixeldungeon.items.weapon.Weapon;
import com.towerpixel.towerpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.towerpixel.towerpixeldungeon.messages.Messages;
import com.towerpixel.towerpixeldungeon.sprites.ItemSpriteSheet;
import com.towerpixel.towerpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class FistAttack extends MeleeWeapon{
    {
        image = ItemSpriteSheet.FIST_ATTACK;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 0.7f;
        rarity = 1;
        tier = 1;
        DLY = 0.667f;
    }
    public static int fistusedtimes = 0;
    @Override
    public int min(int lvl) {
        int dmg = (int)Math.ceil(super.min(lvl)+(fistusedtimes/(60+lvl))/2);
        if (lvl == 10) {
            dmg = 10000000;
        }
        return dmg;
    }
    @Override
    public int max(int lvl) {
        int dmg = (int)Math.ceil((super.max(lvl)+2*(fistusedtimes/(60+lvl)))/2);
        if (lvl == 10) {
            dmg = 1000000000;
        }
        return dmg;
    }
    @Override
    public boolean isUpgradable(){
        return lvl() < 10;
    }
    private int lvl(){
        return level();
    }
    @Override
    public int buffedLvl(){
        return lvl();
    }
    @Override
    public int STRReq(int lvl) {
        return hero.STR;
    }

    @Override
    public float abilityChargeUse( Hero hero ) {
        return super.abilityChargeUse(hero)/20;
    }


    @Override
    public String info() {
        String info = "";
        if (levelKnown) {
            if (lvl() < 10){
                info += Messages.get(this, "desc");
                info += "\n\n" + Messages.get(FistAttack.class, "stats_known",
                        augment.damageFactor(min()),
                        augment.damageFactor(max()),
                        Math.round(100 * (this.critChance + hero.critChance())),
                        (Math.round(100 * (this.critMult * hero.critMult()))) - 100);
            }
            else{
                info += Messages.get(this, "desc_max");
                info += "\n\n" + Messages.get(FistAttack.class, "stats_known_max",
                        Math.round(100 * (this.critChance + hero.critChance())),
                        (Math.round(100 * (this.critMult * hero.critMult()))) - 100);
            }
        } else {
            info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_unknown",
                    tier,
                    min(0),
                    Math.round(100*(this.critChance+hero.critChance())),
                    (Math.round(100*(this.critMult*hero.critMult())))-100);
        }
        switch (augment) {
            case SPEED:
                info += "\n\n" + Messages.get(Weapon.class, "faster");
                break;
            case DAMAGE:
                info += "\n\n" + Messages.get(Weapon.class, "stronger");
                break;
            case NONE:
        }

        if (enchantment != null && (cursedKnown || !enchantment.curse())){
            info += "\n\n" + Messages.get(Weapon.class, "enchanted", enchantment.name());
            info += " " + Messages.get(enchantment, "desc");
        }

        if (cursed && isEquipped( hero )) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
        } else if (cursedKnown && cursed) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed");
        } else if (!isIdentified() && cursedKnown){
            info += "\n\n" + Messages.get(Weapon.class, "not_cursed");
        }
        if (levelKnown && lvl()==10){
            info +="\n\n" + Messages.get(FistAttack.class,"stats_desc_max");
        }else{
            info += "\n\n" + Messages.get(FistAttack.class,"stats_desc");}
        if (Dungeon.hero.heroClass == HeroClass.DUELIST){
            info += "\n\n" + Messages.get(this, "ability_desc");
        }
        return info;
    }
    @Override
    public boolean isFist(){
        return true;
    }
    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }
    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        Sword.cleaveAbility(hero, target, 1f, this);
        fistusedtimes+=2;
        if (fistusedtimes >= 60+lvl()
            && lvl() < 10){
            fistusedtimes = 0;
            upgrade();
        }
    }
    @Override
    public Item upgrade(){
        GLog.newLine();
        GLog.p( Messages.get(FistAttack.class, "new_level") );
        return upgrade(false);
    }
}
