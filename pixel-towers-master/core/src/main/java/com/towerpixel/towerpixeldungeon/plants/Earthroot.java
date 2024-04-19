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

package com.towerpixel.towerpixeldungeon.plants;

import com.towerpixel.towerpixeldungeon.Dungeon;
import com.towerpixel.towerpixeldungeon.actors.Char;
import com.towerpixel.towerpixeldungeon.actors.buffs.Barkskin;
import com.towerpixel.towerpixeldungeon.actors.buffs.Buff;
import com.towerpixel.towerpixeldungeon.actors.hero.Hero;
import com.towerpixel.towerpixeldungeon.actors.hero.HeroSubClass;
import com.towerpixel.towerpixeldungeon.effects.CellEmitter;
import com.towerpixel.towerpixeldungeon.effects.particles.EarthParticle;
import com.towerpixel.towerpixeldungeon.messages.Messages;
import com.towerpixel.towerpixeldungeon.sprites.ItemSpriteSheet;
import com.towerpixel.towerpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Camera;
import com.watabou.utils.Bundle;

public class Earthroot extends Plant {
	
	{
		image = 8;
		seedClass = Seed.class;
	}
	
	@Override
	public void activate( Char ch ) {

		if (ch != null){
			if (ch instanceof Hero && ((Hero) ch).subClass == HeroSubClass.WARDEN) {
				Buff.affect(ch, Barkskin.class).set(Dungeon.hero.lvl + 5, 5);
			} else {
				Buff.affect(ch, Armor.class).level(ch.HT);
			}
		}
		
		if (Dungeon.level.heroFOV[pos]) {
			CellEmitter.bottom( pos ).start( EarthParticle.FACTORY, 0.05f, 8 );
			Camera.main.shake( 1, 0.4f );
		}
	}
	
	public static class Seed extends Plant.Seed {
		{
			image = ItemSpriteSheet.SEED_EARTHROOT;

			plantClass = Earthroot.class;

			bones = true;
		}
	}
	
	public static class Armor extends Buff {
		
		private static final float STEP = 1f;
		
		private int pos;
		private int level;

		{
			type = buffType.POSITIVE;
			announced = true;
		}
		
		@Override
		public boolean act() {
			if (target.pos != pos) {
				detach();
			}
			spend( STEP );
			return true;
		}
		
		private static int blocking(){
			return (Dungeon.scalingDepth() + 5)/2;
		}
		
		public int absorb( int damage ) {
			if (pos != target.pos){
				detach();
				return damage;
			}
			int block = Math.min( damage, blocking());
			if (level <= block) {
				detach();
				return damage - block;
			} else {
				level -= block;
				return damage - block;
			}
		}
		
		public void level( int value ) {
			if (target != null) {
				if (level < value) {
					level = value;
				}
				pos = target.pos;
			}
		}
		
		@Override
		public int icon() {
			return BuffIndicator.ARMOR;
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (target.HT - level) / (float) target.HT);
		}

		@Override
		public String iconTextDisplay() {
			return Integer.toString(level);
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", blocking(), level);
		}

		private static final String POS		= "pos";
		private static final String LEVEL	= "level";
		
		@Override
		public void storeInBundle( Bundle bundle ) {
			super.storeInBundle( bundle );
			bundle.put( POS, pos );
			bundle.put( LEVEL, level );
		}
		
		@Override
		public void restoreFromBundle( Bundle bundle ) {
			super.restoreFromBundle( bundle );
			pos = bundle.getInt( POS );
			level = bundle.getInt( LEVEL );
		}
	}
}