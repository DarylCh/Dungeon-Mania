package dungeonmania;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Entity.NonPlayerEntity.Item.Item;
import dungeonmania.Entity.NonPlayerEntity.Item.Sceptre;
import dungeonmania.Entity.NonPlayerEntity.Item.BattleItem.BattleItem;
import dungeonmania.Entity.NonPlayerEntity.Item.BattleItem.Bow;
import dungeonmania.Entity.NonPlayerEntity.Item.BattleItem.MidnightArmour;
import dungeonmania.Entity.NonPlayerEntity.Item.BattleItem.Shield;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;

public class Inventory {
    List<Item> items;

    public List<ItemResponse> getItemResponses() {
        List<ItemResponse> items = new ArrayList<>();
        for (Item item : this.items) {
            items.add(new ItemResponse(item.getId(), item.getType()));
        }
        return items;
    }

    public Inventory() {
        this.items = new ArrayList<Item>();
    }

    public void addToInventory(Item item) {
        items.add(item);
    }

    public List<Item> searchInventory(String type) {
        return this.items.stream()
                .filter(it -> it.getType().startsWith(type))
                .collect(Collectors.toList());
    }

    public Item searchInventoryById(String id) {
        for (Item item : items) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    // Making the assumption that items can be removed in any order if multiple
    // of the same type exist
    // returns true if removed, false if not found
    public boolean removeItemFromInventoryByType(String type) {
        for (Item item : items) {
            if (item.getType().equals(type)) {
                items.remove(item);
                return true;
            }
        }
        return false;
    }

    public boolean removeItemFromInventoryById(String id) {
        for (Item item : items) {
            if (item.getId().equals(id)) {
                items.remove(item);
                return true;
            }
        }
        return false;
    }

    // Takes in a quantity and an itemType
    // and checks whether the inventory contains that quantity of items (of type
    // itemType)
    public boolean checkInventoryContainsNOfType(Integer quantity, String itemType) {
        Integer numItemType = 0;

        // count the number of items in the inventory of type itemType
        for (Item item : items) {
            if (item.getType().equals(itemType)) {
                numItemType++;
            }
        }

        // return true if the inventory contains AT LEAST <quantity> items of type
        // itemType
        return numItemType >= quantity;
    }

    // Takes in a map of resources and their required quantities
    // and returns true if the resources and their required quantities exist in the
    // inventory
    public boolean checkResourcesExistInInventory(HashMap<String, Integer> buildableInstructions) {

        // Loop through the hashmap of resources and their quantities
        for (HashMap.Entry<String, Integer> entry : buildableInstructions.entrySet()) {
            String resource = entry.getKey();
            Integer quantity = entry.getValue();

            // if the inventory does NOT have enough of the resource
            if (!checkInventoryContainsNOfType(quantity, resource)) {
                return false;
            }
        }
        // otherwise, the inventory conatins enough of the required resources
        return true;
    }

    // WARNING: This function simply removes N items of type itemType from the
    // inventory
    // without considering the particular item that's being removed.
    // This function should NOT be used if it matters which particular item you're
    // removing.
    //
    // E.g. Suppose you have two swords in the inventory, one with full durability,
    // another with zero durability.
    // You want to remove the sword whose durability is down to zero.
    // This function would remove either sword (no bueno).
    public boolean removeNItemsOfTypeFromInventory(Integer numToRemove, String itemType) {
        if (checkInventoryContainsNOfType(numToRemove, itemType)) {
            for (int numRemoved = 0; numRemoved < numToRemove; ++numRemoved)
                removeItemFromInventoryByType(itemType);
            return true;
        }
        return false;
    }

    // Given a HashMap of resource items and their quantities
    // removes <quantity> of each item from the inventory
    public boolean removeResourcesFromInventory(HashMap<String, Integer> buildableInstructions, boolean retainSunStone) {

        if (checkResourcesExistInInventory(buildableInstructions)) {
            for (HashMap.Entry<String, Integer> entry : buildableInstructions.entrySet()) {
                String resource = entry.getKey();
                Integer quantity = entry.getValue();
                if (resource == "sun_stone" && retainSunStone && quantity > 0) {
                    removeNItemsOfTypeFromInventory(quantity - 1, resource);
                } else {
                    removeNItemsOfTypeFromInventory(quantity, resource);
                }
                
            }

            return true;
        }
        return false;
    }

    public void build(Dungeon dungeon, String buildable, Config config) throws InvalidActionException {
        switch (buildable) {
            case "bow":
                buildBow(config);
                break;
            case "shield":
                buildShield(config);
                break;
            case "midnight_armour":
                buildMidnightArmour(dungeon, config);
                break;
            case "sceptre":
                buildSceptre(config);
                break;
            default:
                throw new IllegalArgumentException("Build Error: \'" + buildable
                        + "\' is not a valid buildable item. Valid buidable items are \'bow\', \'shield\' and \'midnight_armour\'.");
        }
    }
    
    public void buildBow(Config config) throws InvalidActionException {
        BuildableInstructions buildableInstructions = new BuildableInstructions();
        HashMap<String, Integer> bowInstructions = buildableInstructions.getBowInstructions();

        if (checkResourcesExistInInventory(bowInstructions)) {
            removeResourcesFromInventory(bowInstructions, false);

            String id = IdAssigner.assignId();
            Bow newBow = new Bow(id, "bow", null, false, config.bow_durability, false, true, 0, 0);

            addToInventory(newBow);

        } else {
            throw new InvalidActionException(
                    "inventory.build() Error: player does not have sufficient resource items to craft a bow. Crafting a bow requires 1 wood + 3 arrows");
        }
    }

    private void buildShield(Config config) throws InvalidActionException {
        BuildableInstructions buildableInstructions = new BuildableInstructions();

        HashMap<String, Integer> sheildType1Instructions = buildableInstructions.getShieldType1Instructions();
        HashMap<String, Integer> sheildType2Instructions = buildableInstructions.getShieldType2Instructions();
        HashMap<String, Integer> sheildType3Instructions = buildableInstructions.getShieldType3Instructions();

        // if inventory has resources for shield (type1)
        if (checkResourcesExistInInventory(sheildType1Instructions)) {

            removeResourcesFromInventory(sheildType1Instructions, false);

            String id = IdAssigner.assignId();
            Shield newShield = new Shield(id, "shield", null, false, config.shield_durability, false, true, 0,  config.shield_defence);

            addToInventory(newShield);

        } else if (checkResourcesExistInInventory(sheildType2Instructions)) {
            removeResourcesFromInventory(sheildType2Instructions, false);

            String id = IdAssigner.assignId();
            Shield newShield = new Shield(id, "shield", null, false, config.shield_durability, false, true, 0, config.shield_defence);

            addToInventory(newShield);

        } else if (checkResourcesExistInInventory(sheildType3Instructions)) {
            removeResourcesFromInventory(sheildType3Instructions, true);

            String id = IdAssigner.assignId();
            Shield newShield = new Shield(id, "shield", null, false, config.shield_durability, false, true, 0, config.shield_defence);

            addToInventory(newShield);
        }
        else {
            throw new InvalidActionException(
                    "inventory.build() Error: player does not have sufficient resource items to craft a shield. Crafting a shield requires (2 wood + 1 treasure) OR (2 wood + 1 key) OR (2 wood + 1 sun stone).");
        }
    }
    public void buildMidnightArmour(Dungeon dungeon, Config config) throws InvalidActionException {
        BuildableInstructions buildableInstructions = new BuildableInstructions();
        HashMap<String, Integer> armourInstructions = buildableInstructions.getMidnightArmourInstructions();
        // check for zombies
        int numZombies = dungeon.getEntities().stream().filter(it -> it.getType().startsWith("zombie_toast")).filter(it -> !it.getType().startsWith("zombie_toast_spawner")).collect(Collectors.toList()).size();
        if (checkResourcesExistInInventory(armourInstructions) && numZombies == 0) {
            removeResourcesFromInventory(armourInstructions, false);

            String id = IdAssigner.assignId();
            MidnightArmour newArmour = new MidnightArmour(id, "midnight_armour", null, false, (int) Double.POSITIVE_INFINITY, false, true, config.midnight_armour_attack, config.midnight_armour_defence);

            addToInventory(newArmour);

        } else {
            throw new InvalidActionException(
                    "inventory.build() Error: player does not have sufficient resource items to craft midnight armour");
        }
    }
    private void buildSceptre(Config config) throws InvalidActionException {
        BuildableInstructions buildableInstructions = new BuildableInstructions();

        HashMap<String, Integer> sceptreType1Instructions = buildableInstructions.getSceptreType1Instructions();
        HashMap<String, Integer> sceptreType2Instructions = buildableInstructions.getSceptreType2Instructions();
        HashMap<String, Integer> sceptreType3Instructions = buildableInstructions.getSceptreType3Instructions();
        HashMap<String, Integer> sceptreType4Instructions = buildableInstructions.getSceptreType4Instructions();
        HashMap<String, Integer> sceptreType5Instructions = buildableInstructions.getSceptreType5Instructions();
        HashMap<String, Integer> sceptreType6Instructions = buildableInstructions.getSceptreType6Instructions();

        if (checkResourcesExistInInventory(sceptreType1Instructions)) {
            removeResourcesFromInventory(sceptreType1Instructions, false);;
        } else if (checkResourcesExistInInventory(sceptreType2Instructions)) {
            removeResourcesFromInventory(sceptreType2Instructions, false);
        } else if (checkResourcesExistInInventory(sceptreType3Instructions)) {
            removeResourcesFromInventory(sceptreType3Instructions, false);
        }
        else if (checkResourcesExistInInventory(sceptreType4Instructions)) {
            removeResourcesFromInventory(sceptreType4Instructions, false);
        } else if (checkResourcesExistInInventory(sceptreType5Instructions)) {
            removeResourcesFromInventory(sceptreType5Instructions, true);
        }
        else if (checkResourcesExistInInventory(sceptreType6Instructions)) {
            removeResourcesFromInventory(sceptreType6Instructions, true);
        } 
        else {
            throw new InvalidActionException(
                    "inventory.build() Error: player does not have sufficient resource items to craft a sceptre");
        }
        String id = IdAssigner.assignId();
        Sceptre newSceptre = new Sceptre(id, "sceptre", null, true, 1, false, true, config.mind_control_duration);
        addToInventory(newSceptre);
    }
    private boolean areAbleToBuild(String type) {
        BuildableInstructions buildableInstructions = new BuildableInstructions();
        switch (type) {

            case "bow":
                HashMap<String, Integer> bowInstructions = buildableInstructions.getBowInstructions();
                // return true if a bow can be built with current inventory
                return checkResourcesExistInInventory(bowInstructions);

            case "shield":
                HashMap<String, Integer> sheildType1Instructions = buildableInstructions.getShieldType1Instructions();
                HashMap<String, Integer> sheildType2Instructions = buildableInstructions.getShieldType2Instructions();
                // return true if a shield can be built with current inventory
                return (checkResourcesExistInInventory(sheildType1Instructions)
                        || checkResourcesExistInInventory(sheildType2Instructions));
            case "midnight_armour":
                HashMap<String, Integer> armourInstructions = buildableInstructions.getMidnightArmourInstructions();
                return (checkResourcesExistInInventory(armourInstructions));
            case "sceptre":
                HashMap<String, Integer> sceptreType1Instructions = buildableInstructions.getSceptreType1Instructions();
                HashMap<String, Integer> sceptreType2Instructions = buildableInstructions.getSceptreType2Instructions();
                HashMap<String, Integer> sceptreType3Instructions = buildableInstructions.getSceptreType3Instructions();
                HashMap<String, Integer> sceptreType4Instructions = buildableInstructions.getSceptreType4Instructions();
                HashMap<String, Integer> sceptreType5Instructions = buildableInstructions.getSceptreType5Instructions();
                HashMap<String, Integer> sceptreType6Instructions = buildableInstructions.getSceptreType6Instructions();
                return (checkResourcesExistInInventory(sceptreType1Instructions)) || (checkResourcesExistInInventory(sceptreType2Instructions)) ||
                (checkResourcesExistInInventory(sceptreType3Instructions)) || (checkResourcesExistInInventory(sceptreType4Instructions)) 
                || (checkResourcesExistInInventory(sceptreType5Instructions)) || (checkResourcesExistInInventory(sceptreType6Instructions));
            default:
                return false;
        }
    }

    public List<String> getListOfBuidableItemTypes() {
        List<String> listOfBuidableItemTypes = new ArrayList<String>();

        if (areAbleToBuild("bow")) {
            listOfBuidableItemTypes.add("bow");
        }

        if (areAbleToBuild("shield")) {
            listOfBuidableItemTypes.add("shield");
        } 
        if (areAbleToBuild("midnight_armour")) {
            listOfBuidableItemTypes.add("midnight_armour");
        }
        if (areAbleToBuild("sceptre")) {
            listOfBuidableItemTypes.add("sceptre");
        }

        return listOfBuidableItemTypes;
    }

    public List<BattleItem> getBattleItems() {
        System.out.println(items.stream().filter(item -> item instanceof BattleItem).map(item -> (BattleItem) item).collect(Collectors.toList()));
        return items.stream().filter(item -> item instanceof BattleItem).map(item -> (BattleItem) item).collect(Collectors.toList());
    }
}
