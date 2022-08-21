package dungeonmania.util;

public class Path {
    Position pos;
    Path prev;

    public Path(Position pos, Path prev) {
        this.pos = pos;
        this.prev = prev;
    }

    public Position getPosition() {
        return pos;
    }

    public Path getPrev() {
        return prev;
    }

    public Position findPreviousPosition() {
        return prev.getPosition();
    }
    
}
