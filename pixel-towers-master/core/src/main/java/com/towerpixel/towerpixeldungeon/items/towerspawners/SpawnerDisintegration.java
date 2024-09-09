package com.towerpixel.towerpixeldungeon.items.towerspawners;

import com.towerpixel.towerpixeldungeon.Dungeon;
import com.towerpixel.towerpixeldungeon.actors.Actor;
import com.towerpixel.towerpixeldungeon.actors.Char;
import com.towerpixel.towerpixeldungeon.actors.mobs.towers.TowerDisintegration1;
import com.towerpixel.towerpixeldungeon.actors.mobs.towers.TowerLightning1;
import com.towerpixel.towerpixeldungeon.scenes.GameScene;
import com.towerpixel.towerpixeldungeon.sprites.ItemSpriteSheet;

public class SpawnerDisintegration extends TowerSpawner {

    {
        image = ItemSpriteSheet.TOWERSPAWNER_DISINTEGRATION;
        stackable = true;
        defaultAction = AC_THROW;
        towerClass = TowerDisintegration1.class;
    }

    @Override
    public int value() {
        return 60;
    }

}