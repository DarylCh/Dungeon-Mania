package dungeonmania.Entity.NonPlayerEntity.LiveEntities;
import java.util.List;
import dungeonmania.Entity.NonPlayerEntity.NonPlayerEntity;

public interface LiveEntityState {
    public void moveNPC(List<NonPlayerEntity> entities);
}