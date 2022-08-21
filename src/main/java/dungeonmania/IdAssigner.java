package dungeonmania;

public class IdAssigner {
    static private int entity_id = 0;

    public static String assignId() {
        entity_id += 1;
        return String.valueOf(entity_id);
    }
}
