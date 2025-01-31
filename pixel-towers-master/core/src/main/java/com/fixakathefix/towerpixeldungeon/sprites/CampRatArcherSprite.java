/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Pixel Towers / Towers Pixel Dungeon
 * Copyright (C) 2024-2025 FixAkaTheFix (initials R. A. A.)
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

package com.fixakathefix.towerpixeldungeon.sprites;

import com.fixakathefix.towerpixeldungeon.Assets;
import com.fixakathefix.towerpixeldungeon.Dungeon;
import com.fixakathefix.towerpixeldungeon.items.weapon.missiles.darts.Dart;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class CampRatArcherSprite extends MobSprite {

	public CampRatArcherSprite() {
		super();
		
		texture( Assets.Sprites.TOWERCAMPRATARCHER );
		
		TextureFilm frames = new TextureFilm( texture, 16, 17 );
		
		idle = new Animation( 2, true );
		idle.frames( frames, 0, 0, 0, 1 );
		
		run = new Animation( 10, true );
		run.frames( frames, 6, 7, 8, 9, 10 );
		
		attack = new Animation( 15, false );
		attack.frames( frames, 2, 3, 4, 5, 0 );
		
		die = new Animation( 10, false );
		die.frames( frames, 11, 12, 13, 14 );

		zap = attack.clone();
		
		play( idle );
	}
	private int cellToAttack;

	@Override
	public void attack( int cell ) {
		if (!Dungeon.level.adjacent( cell, ch.pos )) {

			cellToAttack = cell;
			zap(cell);

		} else {

			super.attack( cell );

		}
	}

	@Override
	public void onComplete( Animation anim ) {
		if (anim == zap) {
			idle();

			((MissileSprite)parent.recycle( MissileSprite.class )).
					reset( this, cellToAttack, new Dart(), new Callback() {
						@Override
						public void call() {
							ch.onAttackComplete();
						}
					} );
		} else {
			super.onComplete( anim );
		}
	}

}
