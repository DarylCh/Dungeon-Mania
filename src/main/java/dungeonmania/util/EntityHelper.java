package dungeonmania.util;

import java.util.ArrayList;
import java.util.List;
import dungeonmania.Entity.*;
import dungeonmania.Entity.NonPlayerEntity.*;
import dungeonmania.response.models.EntityResponse;
import java.util.stream.Collectors;

public class EntityHelper {
        public static boolean isCardinallyAdjacent(Entity entity, Entity potentialneighbour) {
                int positionBombX = entity.getPosition().getX();
                int positionBombY = entity.getPosition().getY();
                int positionPotentialNeighbourX = potentialneighbour.getPosition().getX();
                int positionPotentialNeighbourY = potentialneighbour.getPosition().getY();
                boolean neighbourRightCondition = ((positionBombX + 1) == positionPotentialNeighbourX)
                                && positionPotentialNeighbourY == positionBombY;
                boolean neighbourLeftCondition = ((positionBombX - 1) == positionPotentialNeighbourX)
                                && positionPotentialNeighbourY == positionBombY;
                boolean neighbourUpCondition = ((positionBombY + 1) == positionPotentialNeighbourY)
                                && positionPotentialNeighbourX == positionBombX;
                boolean neighbourDownCondition = ((positionBombY - 1) == positionPotentialNeighbourY)
                                && positionPotentialNeighbourX == positionBombX;

                if (neighbourRightCondition || neighbourLeftCondition || neighbourUpCondition
                                || neighbourDownCondition) {
                        return true;
                }
                return false;
        }

        public static boolean isDiagonallyAdjacent(Entity entity, Entity potentialneighbour) {
                int positionBombX = entity.getPosition().getX();
                int positionBombY = entity.getPosition().getY();
                int positionPotentialNeighbourX = potentialneighbour.getPosition().getX();
                int positionPotentialNeighbourY = potentialneighbour.getPosition().getY();
                boolean neighbourTopRightCondition = ((positionBombX + 1) == positionPotentialNeighbourX)
                                && positionPotentialNeighbourY == (positionBombY + 1);
                boolean neighbourTopLeftCondition = ((positionBombX - 1) == positionPotentialNeighbourX)
                                && positionPotentialNeighbourY == (positionBombY + 1);
                boolean neighbourBottomRightCondition = ((positionBombY - 1) == positionPotentialNeighbourY)
                                && positionPotentialNeighbourX == (positionBombX + 1);
                boolean neighbourBottomLeftCondition = ((positionBombY - 1) == positionPotentialNeighbourY)
                                && positionPotentialNeighbourX == (positionBombX - 1);

                if (neighbourTopRightCondition || neighbourTopLeftCondition || neighbourBottomRightCondition
                                || neighbourBottomLeftCondition) {
                        return true;
                }
                return false;
        }

        public static boolean withinRadius(NonPlayerEntity npe, NonPlayerEntity npeTarget, int radius) {
                Position leftTopCorner = npe.getPosition().translateBy(-radius, radius);

                for (int i = leftTopCorner.getX(); i <= leftTopCorner.getX() + 2 * radius; i++) {
                        for (int j = leftTopCorner.getY(); j >= leftTopCorner.getY() - 2 * radius; j--) {
                                // check within radius but not same position
                                if (npeTarget.getPosition().equals(new Position(i, j))
                                                && !npeTarget.getPosition().equals(npe.getPosition())) {
                                        return true;
                                }
                        }
                }
                return false;
        }

        public static List<NonPlayerEntity> findAffectedEntities(Entity entity, List<NonPlayerEntity> entities,
                        Direction movement) {
                // cases where affected Entities can have multiple on same spot
                // boulder on floor switch
                // spider on wall

                List<NonPlayerEntity> affectedEntities = new ArrayList<>();
                Position newPosition = entity.getPosition().translateBy(movement);
                for (NonPlayerEntity tmpentity : entities) {
                        if (tmpentity.getPosition() == newPosition) {
                                affectedEntities.add(tmpentity);
                        }
                }
                return affectedEntities;
        }

        public static List<NonPlayerEntity> findPublishers(Entity entity, List<NonPlayerEntity> entities) {
                // square radius potential subscribers

                List<NonPlayerEntity> Publishers = new ArrayList<>();
                for (NonPlayerEntity tmpentity : entities) {
                        if (EntityHelper.isCardinallyAdjacent(entity, tmpentity)) {
                                Publishers.add(tmpentity);
                        }
                }
                return Publishers;
        }

        public static List<EntityResponse> getResponses(List<Entity> entities) {
                List<EntityResponse> responses = new ArrayList<>();
                for (Entity entity : entities) {
                        responses.add(new EntityResponse(entity.getId(), entity.getType(), entity.getPosition(),
                                        entity.getIsInteractable()));
                }
                return responses;
        }

        public static List<NonPlayerEntity> convertToNonplayerEntity(List<Entity> entities) {
                return entities.stream()
                                .map(e -> (NonPlayerEntity) e)
                                .collect(Collectors.toList());
        }

        public static List<Entity> convertToEntity(List<NonPlayerEntity> entities) {
                return entities.stream()
                                .map(e -> (Entity) e)
                                .collect(Collectors.toList());
        }

}
