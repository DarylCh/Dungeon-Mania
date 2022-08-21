package dungeonmania.Entity;

import dungeonmania.util.Position;
/*Superclass for all entities in this game*/

public class Entity {
    private String id;
    private String type;
    private Position position;
    private boolean isInteractable;

    /**
     * Instantiates the 'Entity' class
     * 
     * @param id             - unique id of entity
     * @param type           - type of entity
     * @param position       - the co-ordinate position of the entity
     * @param isInteractable - determines if the player may interact with this
     *                       entity
     */
    public Entity(String id, String type, Position position, boolean isInteractable) {
        this.id = id;
        this.type = type;
        this.position = position;
        //this.prevP
        this.isInteractable = isInteractable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean getIsInteractable() {
        return isInteractable;
    }

    public void setIsInteractable(boolean isInteractable) {
        this.isInteractable = isInteractable;
    }

}
