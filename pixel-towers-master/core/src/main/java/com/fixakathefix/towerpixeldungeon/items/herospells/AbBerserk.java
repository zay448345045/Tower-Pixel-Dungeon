package com.fixakathefix.towerpixeldungeon.items.herospells;

import com.fixakathefix.towerpixeldungeon.Assets;
import com.fixakathefix.towerpixeldungeon.Dungeon;
import com.fixakathefix.towerpixeldungeon.actors.buffs.Adrenaline;
import com.fixakathefix.towerpixeldungeon.actors.buffs.Buff;
import com.fixakathefix.towerpixeldungeon.actors.buffs.Strength;
import com.fixakathefix.towerpixeldungeon.effects.Speck;
import com.fixakathefix.towerpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class AbBerserk extends HeroSpell {
    {
        image = ItemSpriteSheet.HEROSPELL_RAGE;
    }

    @Override
    public void cast() {
        super.cast();
        int time = 15;
        if (Dungeon.hero.HP < Dungeon.hero.HT/2) time = 20;
        if (Dungeon.hero.HP < Dungeon.hero.HT/4) time = 30;
        Buff.affect(Dungeon.hero, Adrenaline.class, time);

        curUser.sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
        Sample.INSTANCE.play(Assets.Sounds.CHALLENGE, 1f, 0.9f);
    }

    @Override
    protected int castCooldown() {
        int cooldown = 100 - Dungeon.hero.lvl*2;
        if (Dungeon.hero == null) return 100;
        return cooldown;
    }
}
