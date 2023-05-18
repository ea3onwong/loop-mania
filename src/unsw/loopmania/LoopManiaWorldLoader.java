package unsw.loopmania;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import org.javatuples.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Goals.*;
import unsw.loopmania.Items.Anduril;
import unsw.loopmania.Items.TheOneRing;
import unsw.loopmania.Items.TreeStump;
import unsw.loopmania.building.BarracksBuilding;
import unsw.loopmania.building.CampfireBuilding;
import unsw.loopmania.building.HeroCastleBuilding;
import unsw.loopmania.building.TowerBuilding;
import unsw.loopmania.building.VampireCastleBuilding;
import unsw.loopmania.building.VillageBuilding;
import unsw.loopmania.building.ZombiePitBuilding;

import java.util.List;

/**
 * Loads a world from a .json file.
 * 
 * By extending this class, a subclass can hook into entity creation.
 * This is useful for creating UI elements with corresponding entities.
 * 
 * this class is used to load the world.
 * it loads non-spawning entities from the configuration files.
 * spawning of enemies/cards must be handled by the controller.
 */
public abstract class LoopManiaWorldLoader {
    private JSONObject json;

    public LoopManiaWorldLoader(String filename) throws FileNotFoundException {
        json = new JSONObject(new JSONTokener(new FileReader("worlds/" + filename)));
    }

    /**
     * Parses the JSON to create a world.
     */
    public LoopManiaWorld load() {
        int width = json.getInt("width");
        int height = json.getInt("height");

        // path variable is collection of coordinates with directions of path taken...
        List<Pair<Integer, Integer>> orderedPath = loadPathTiles(json.getJSONObject("path"), width, height);

        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath);

        /**
         * ADDED CODE
         * Adds the different goals into the world and stores it in the world's set goal
         */
        JSONObject jsonGoals = json.getJSONObject("goal-condition");
        world.setGoal(addingGoals(jsonGoals));

        /**
         * ADDED CODE
         * Allows for rare items to be specified and enabled
         */
        JSONArray jsonRareItems = json.getJSONArray("rare_items");
        String rareItemName;
        for (int i = 0; i < jsonRareItems.length(); i++) {
            rareItemName = jsonRareItems.getString(i);
            switch (rareItemName) {
                case "the_one_ring":
                    world.enableRareItem(new TheOneRing(null, null));
                    break;
                case "anduril_flame_of_the_west":
                    world.enableRareItem(new Anduril(null, null));
                    break;
                case "tree_stump":
                    world.enableRareItem(new TreeStump(null, null));
                    break;
            }
        }

        JSONArray jsonEntities = json.getJSONArray("entities");

        // load non-path entities later so that they're shown on-top
        for (int i = 0; i < jsonEntities.length(); i++) {
            loadEntity(world, jsonEntities.getJSONObject(i), orderedPath);
        }

        return world;
    }
    
    /**
     * ADDED CODE
     * Creates the goals for the player to achieve and loads it into the world
     * @param world
     * @param goals
     * @return the goal strategy
     */
    private GoalsComponent addingGoals(JSONObject goals) {

        GoalsComponent goalStrat;
        JSONArray subgoals;
        int quantity;
        String condition =  goals.getString("goal");

        if (condition.equals("AND") || condition.equals("OR")) {
            
            // Edit this part if we're adding more booleans
            // -GW
            if (condition.equals("AND")) {
                goalStrat = new GoalsBranchAND();
            } else {
                goalStrat = new GoalsBranchOR();
            }

            subgoals = goals.getJSONArray("subgoals");
            for (int i = 0; i < subgoals.length(); i++) {
                goalStrat.addGoal(addingGoals(subgoals.getJSONObject(i)));
            }
            return goalStrat;
        } else {
            
            // Bosses does not have a quantity
            if (condition.equals("bosses")){
                goalStrat = new GoalsLeafBoss(0);
                return goalStrat;
            }

            quantity = goals.getInt("quantity");
            // Edit this part if we're adding more win conditions
            // -GW
            switch(condition) {
                case "experience":
                    goalStrat = new GoalsLeafExperience(quantity);
                    return goalStrat;
                case "cycles":
                    goalStrat = new GoalsLeafCycle(quantity);
                    return goalStrat;
                case "gold":
                    goalStrat = new GoalsLeafGold(quantity);
                    return goalStrat;
                default:
                    System.err.println("Something went wrong");
                    System.exit(1);
            }
        }
        return null;
    } 

    /**
     * load an entity into the world
     * @param world backend world object
     * @param json a JSON object to parse (different from the )
     * @param orderedPath list of pairs of x, y cell coordinates representing game path
     */
    private void loadEntity(LoopManiaWorld world, JSONObject currentJson, List<Pair<Integer, Integer>> orderedPath) {
        String type = currentJson.getString("type");
        int x = currentJson.getInt("x");
        int y = currentJson.getInt("y");
        int indexInPath = orderedPath.indexOf(new Pair<Integer, Integer>(x, y));
        // Hero castle must be on the path, others can not be on the path
        if (type.equals("hero_castle")) {  
            assert indexInPath != -1;
        } else {
            assert indexInPath == -1;
        }

        Entity entity = null;

        switch (type) {
        case "hero_castle":
            // Loads hero castle AND character
            Character character = new Character(new PathPosition(indexInPath, orderedPath));
            HeroCastleBuilding heroCastle = new HeroCastleBuilding(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
            world.addBuildingEntity(heroCastle);
            world.setCharacter(character);
            onLoad(heroCastle);
            onLoad(character);
            entity = character;
            break;
        case "zombie_pit":
            ZombiePitBuilding pit = new ZombiePitBuilding(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
            world.addBuildingEntity(pit);
            world.registerBuildingToCharacter(pit);
            onLoad(pit);
            entity = pit;
            break;
        case "vampire_castle":
            VampireCastleBuilding castle = new VampireCastleBuilding(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
            world.addBuildingEntity(castle);
            world.registerBuildingToCharacter(castle);
            onLoad(castle);
            entity = castle;
            break;
        case "tower":
            TowerBuilding tower = new TowerBuilding(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
            world.addBuildingEntity(tower);
            onLoad(tower);
            entity = tower;
            break;
        case "village":
            VillageBuilding village = new VillageBuilding(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
            world.addBuildingEntity(village);
            onLoad(village);
            entity = village;
            break;
        case "barracks":
            BarracksBuilding barracks = new BarracksBuilding(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
            world.addBuildingEntity(barracks);
            onLoad(barracks);
            entity = barracks;
            break;
        case "campfire":
            CampfireBuilding campfire = new CampfireBuilding(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
            world.addBuildingEntity(campfire);
            onLoad(campfire);
            entity = campfire;
            break;
        case "path_tile":
            throw new RuntimeException("path_tile's aren't valid entities, define the path externally.");

        }
        world.addEntity(entity);
    }

    /**
     * load path tiles
     * @param path json data loaded from file containing path information
     * @param width width in number of cells
     * @param height height in number of cells
     * @return list of x, y cell coordinate pairs representing game path
     */
    private List<Pair<Integer, Integer>> loadPathTiles(JSONObject path, int width, int height) {
        if (!path.getString("type").equals("path_tile")) {
            // ... possible extension
            throw new RuntimeException(
                    "Path object requires path_tile type.  No other path types supported at this moment.");
        }
        PathTile starting = new PathTile(new SimpleIntegerProperty(path.getInt("x")), new SimpleIntegerProperty(path.getInt("y")));
        if (starting.getY() >= height || starting.getY() < 0 || starting.getX() >= width || starting.getX() < 0) {
            throw new IllegalArgumentException("Starting point of path is out of bounds");
        }
        // load connected path tiles
        List<PathTile.Direction> connections = new ArrayList<>();
        for (Object dir: path.getJSONArray("path").toList()){
            connections.add(Enum.valueOf(PathTile.Direction.class, dir.toString()));
        }

        if (connections.size() == 0) {
            throw new IllegalArgumentException(
                "This path just consists of a single tile, it needs to consist of multiple to form a loop.");
        }

        // load the first position into the orderedPath
        PathTile.Direction first = connections.get(0);
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(Pair.with(starting.getX(), starting.getY()));

        int x = starting.getX() + first.getXOffset();
        int y = starting.getY() + first.getYOffset();

        // add all coordinates of the path into the orderedPath
        for (int i = 1; i < connections.size(); i++) {
            orderedPath.add(Pair.with(x, y));
            
            if (y >= height || y < 0 || x >= width || x < 0) {
                throw new IllegalArgumentException("Path goes out of bounds at direction index " + (i - 1) + " (" + connections.get(i - 1) + ")");
            }
            
            PathTile.Direction dir = connections.get(i);
            PathTile tile = new PathTile(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
            x += dir.getXOffset();
            y += dir.getYOffset();
            if (orderedPath.contains(Pair.with(x, y)) && !(x == starting.getX() && y == starting.getY())) {
                throw new IllegalArgumentException("Path crosses itself at direction index " + i + " (" + dir + ")");
            }
            onLoad(tile, connections.get(i - 1), dir);
        }
        // we should connect back to the starting point
        if (x != starting.getX() || y != starting.getY()) {
            throw new IllegalArgumentException(String.format(
                    "Path must loop back around on itself, this path doesn't finish where it began, it finishes at %d, %d.",
                    x, y));
        }
        onLoad(starting, connections.get(connections.size() - 1), connections.get(0));
        return orderedPath;
    }

    public abstract void onLoad(Character character);
    public abstract void onLoad(HeroCastleBuilding herocastle);
    public abstract void onLoad(ZombiePitBuilding pit);
    public abstract void onLoad(VampireCastleBuilding herocastle);
    public abstract void onLoad(BarracksBuilding barracks);
    public abstract void onLoad(VillageBuilding village);
    public abstract void onLoad(CampfireBuilding campfire);
    public abstract void onLoad(TowerBuilding tower);
    public abstract void onLoad(PathTile pathTile, PathTile.Direction into, PathTile.Direction out);


}
