var GRASS_TILE = 0;
var STONE_TILE = 1;

var loaded_tiles = [];

fn loadTexture(path) {
	var t = gfx.loadTexture(path);
	if type(t) == type(null) {
		println("Failed to load texture: ", path);
	}
	return t;
}

fn load_tiles() {
	loaded_tiles[GRASS_TILE] = loadTexture("grass.png");
	loaded_tiles[STONE_TILE] = loadTexture("stone.png");
}

fn make_tilemap(w, h) {
	var tm = {
		w = w,
		h = h,
		tiles = [],
		set = tilemap_set,
		draw = tilemap_draw
	};
	
	for i in range(tm.w*tm.h) {
		tm.tiles[i] = GRASS_TILE;
	}
	
	return tm;
}

fn tilemap_set(self, x, y, tile) {
	self.tiles[x+y*self.w] = tile;
}

fn tilemap_draw(self, xoffs, yoffs) {
	for y in range(self.h) {
		for x in range(self.w) {
			var texture = loaded_tiles[self.tiles[x+y*self.w]];
			
			var xx = x*32 + xoffs;
			var yy = y*32 + yoffs;
			
			if !((xx < -32) || (xx >= 800) || (yy < -32) || (yy >= 600)) {
				gfx.drawTexture(texture, xx, yy);
			}
		}
	}
}

fn main() {
	gfx.init(800, 600);
	
	load_tiles();
	
	var tilemap = make_tilemap(128, 128);
	tilemap.set(tilemap, 2, 2, STONE_TILE);
	
	var x = 0;
	var y = 0;
	
	var i = 0;
	var minmax = 100;
	
	while !gfx.shouldClose() {
		gfx.clear(0, 0, 0);
		
//		if gfx.get_key_state(gfx.KEY_LEFT) {
//			x--;
//		} else if gfx.get_key_state(gfx.KEY_RIGHT) {
//			x++;
//		}
		
//		if gfx.get_key_state(gfx.KEY_UP) {
//			y--;
//		} else if gfx.get_key_state(gfx.KEY_DOWN) {
//			y++;
//		}
		
		tilemap.draw(tilemap, x - minmax + i, y + i);
		gfx.present();
		
		i += 0.01;
		if i >= minmax*2 {
			i = 0;
		}
	}
}