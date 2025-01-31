package com.fixakathefix.towerpixeldungeon.actors.mobs.towers;

import com.fixakathefix.towerpixeldungeon.sprites.TowerLightning2Sprite;

public class TowerLightning2 extends TowerLightning1 {
    {
        HP = HT = 100;
        spriteClass = TowerLightning2Sprite.class;

        attackRange = 3;
        baseAttackDelay = 4f;

        cost = 600;
        upgrade1Cost = 700;
        damageMin = 25;
        damageMax = 40;
        upgradeLevel = 8;
        maxChainLength = 3;
    }
}
