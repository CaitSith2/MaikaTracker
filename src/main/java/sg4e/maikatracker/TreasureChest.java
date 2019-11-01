/*
 * Copyright (C) 2019 sg4e
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package sg4e.maikatracker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sg4e
 */
public class TreasureChest {
    public static final int PIXELS_PER_SQUARE = 16;
    
    private final int offsetX, offsetY;
    private final String id;
    
    private static final Map<String, String> SPOILER_ID = new HashMap<>();
    
    public TreasureChest(String id, int x, int y, String ... spoiler) {
        this.id = id;
        offsetX = x;
        offsetY = y;
        Arrays.asList(spoiler).forEach(s -> SPOILER_ID.put(s, id));
    }
    
    public int getX() {
        return offsetX;
    }
    
    public int getY() {
        return offsetY;
    }
    
    public String getId() {
        return id;
    }
    
    public static String valueOf(String spoiler) {
        return SPOILER_ID.get(spoiler);
    }
    
}
