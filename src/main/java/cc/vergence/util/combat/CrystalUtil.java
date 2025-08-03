package cc.vergence.util.combat;

import cc.vergence.util.interfaces.Wrapper;

import java.util.HashMap;
import java.util.Map;

public class CrystalUtil implements Wrapper {
    private static final Map<String, Integer> PROTECTION_MAP = new HashMap<>() {{
        put("protection", 1);
        put("blast_protection", 2);
        put("projectile_protection", 1);
        put("feather_falling", 1);
        put("fire_protection", 1);
    }};
}
