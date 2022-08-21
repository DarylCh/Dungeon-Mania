package dungeonmania;

import java.util.HashMap;

public class BuildableInstructions {

    // bow
    public HashMap<String, Integer> getBowInstructions() {
        return new HashMap<String, Integer>() {{
            put("wood", 1);
            put("arrow", 3);
        }};
    }

    // shieldType1
    public HashMap<String, Integer> getShieldType1Instructions() {
        return new HashMap<String, Integer>() {{
            put("wood", 2);
            put("treasure", 1);
        }};
    }

    // shieldType2
    public HashMap<String, Integer> getShieldType2Instructions() {
        return new HashMap<String, Integer>() {{
            put("wood", 2);
            put("key", 1);
        }};
    }
    public HashMap<String, Integer> getShieldType3Instructions() {
        return new HashMap<String, Integer>() {{
            put("wood", 2);
            put("sun_stone", 1);
        }};
    }
    public HashMap<String, Integer> getMidnightArmourInstructions() {
        return new HashMap<String, Integer>() {{
            put("sword", 1);
            put("sun_stone", 1);
        }};
    }
    public HashMap<String, Integer> getSceptreType1Instructions() {
        return new HashMap<String, Integer>() {{
            put("wood", 1);
            put("key", 1);
            put("sun_stone", 1);
        }};
    }
    public HashMap<String, Integer> getSceptreType2Instructions() {
        return new HashMap<String, Integer>() {{
            put("arrow", 2);
            put("key", 1);
            put("sun_stone", 1);
        }};
    }
    public HashMap<String, Integer> getSceptreType3Instructions() {
        return new HashMap<String, Integer>() {{
            put("wood", 1);
            put("treasure", 1);
            put("sun_stone", 1);
        }};
    }
    public HashMap<String, Integer> getSceptreType4Instructions() {
        return new HashMap<String, Integer>() {{
            put("arrow", 2);
            put("treasure", 1);
            put("sun_stone", 1);
        }};
    }
    public HashMap<String, Integer> getSceptreType5Instructions() {
        return new HashMap<String, Integer>() {{
            put("wood", 1);
            put("sun_stone", 2);
            
        }};
    }
    public HashMap<String, Integer> getSceptreType6Instructions() {
        return new HashMap<String, Integer>() {{
            put("arrow", 2);
            put("sun_stone", 2);
        }};
    }
}
