/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.towerpixel.towerpixeldungeon.sprites;

import com.towerpixel.towerpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.PointF;

public class TowerGuard3Sprite extends MobSprite {

    public TowerGuard3Sprite() {
        super();

        texture( Assets.Sprites.TOWERGUARD );

        TextureFilm frames = new TextureFilm( texture, 12, 16 );

        idle = new Animation( 2, true );
        idle.frames( frames, 40, 40, 40, 41, 40, 40, 41, 41 );

        run = new Animation( 15, true );
        run.frames( frames, 42, 43, 44, 45, 46, 47 );

        attack = new Animation( 12, false );
        attack.frames( frames, 48, 49, 50 );

        die = new Animation( 8, false );
        die.frames( frames, 51, 52, 53, 54 );

        play( idle );
    }
    public void bloodBurstA(PointF from, int damage) {
        /*
         * I guess this is considered a human. No blood for you.
         */
    }
}