package unsw.loopmania;

import unsw.loopmania.EndScreens.EndScreenController;

import java.util.ArrayList;
import java.util.List;

import org.codefx.libfx.listener.handle.ListenerHandle;
import org.codefx.libfx.listener.handle.ListenerHandles;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import unsw.loopmania.Shopping.ShoppingController;

import java.util.EnumMap;

import java.io.File;
import java.io.IOException;

import unsw.loopmania.building.*;
import unsw.loopmania.card.*;
import unsw.loopmania.Boss.Boss;
import unsw.loopmania.Boss.Doggie;
import unsw.loopmania.Boss.ElanMuske;
import unsw.loopmania.Enemy.*;
import unsw.loopmania.Exceptions.CharacterIsDead;
import unsw.loopmania.Gamemodes.GamemodeState;
import unsw.loopmania.Items.*;

/**
 * the draggable types.
 * If you add more draggable types, add an enum value here.
 * This is so we can see what type is being dragged.
 */
enum DRAGGABLE_TYPE {
    VampireCastleCARD,
    ZombiePitCARD,
    TowerCARD,
    VillageCARD,
    TrapCARD,
    CampfireCARD,
    BarracksCARD,
    AlchemistTentCARD,
    SurvivorsTentsCARD,
    FieldKitchenCARD,
    HolySpringCARD,
    Sword,
    Stake,
    Staff,
    Armour,
    Shield,
    Helmet,
    Potion,
    TheOneRing,
    Anduril,
    Ration,
    TreeStump,
}

/**
 * A JavaFX controller for the world.
 * 
 * All event handlers and the timeline in JavaFX run on the JavaFX application
 * thread:
 * https://examples.javacodegeeks.com/desktop-java/javafx/javafx-concurrency-example/
 * Note in
 * https://openjfx.io/javadoc/11/javafx.graphics/javafx/application/Application.html
 * under heading "Threading", it specifies animation timelines are run in the
 * application thread.
 * This means that the starter code does not need locks (mutexes) for resources
 * shared between the timeline KeyFrame, and all of the event handlers
 * (including between different event handlers).
 * This will make the game easier for you to implement. However, if you add
 * time-consuming processes to this, the game may lag or become choppy.
 * 
 * If you need to implement time-consuming processes, we recommend:
 * using Task
 * https://openjfx.io/javadoc/11/javafx.graphics/javafx/concurrent/Task.html by
 * itself or within a Service
 * https://openjfx.io/javadoc/11/javafx.graphics/javafx/concurrent/Service.html
 * 
 * Tasks ensure that any changes to public properties, change notifications for
 * errors or cancellation, event handlers, and states occur on the JavaFX
 * Application thread,
 * so is a better alternative to using a basic Java Thread:
 * https://docs.oracle.com/javafx/2/threads/jfxpub-threads.htm
 * The Service class is used for executing/reusing tasks. You can run tasks
 * without Service, however, if you don't need to reuse it.
 *
 * If you implement time-consuming processes in a Task or thread, you may need
 * to implement locks on resources shared with the application thread (i.e.
 * Timeline KeyFrame and drag Event handlers).
 * You can check whether code is running on the JavaFX application thread by
 * running the helper method printThreadingNotes in this class.
 * 
 * NOTE: http://tutorials.jenkov.com/javafx/concurrency.html and
 * https://www.developer.com/design/multithreading-in-javafx/#:~:text=JavaFX%20has%20a%20unique%20set,in%20the%20JavaFX%20Application%20Thread.
 * 
 * If you need to delay some code but it is not long-running, consider using
 * Platform.runLater
 * https://openjfx.io/javadoc/11/javafx.graphics/javafx/application/Platform.html#runLater(java.lang.Runnable)
 * This is run on the JavaFX application thread when it has enough time.
 */
public class LoopManiaWorldController {

    /**
     * squares gridpane includes path images, enemies, character, empty grass,
     * buildings
     */
    @FXML
    private GridPane squares;

    /**
     * cards gridpane includes cards and the ground underneath the cards
     */
    @FXML
    private GridPane cards;

    /**
     * anchorPaneRoot is the "background". It is useful since anchorPaneRoot
     * stretches over the entire game world,
     * so we can detect dragging of cards/items over this and accordingly update
     * DragIcon coordinates
     */
    @FXML
    private AnchorPane anchorPaneRoot;

    /**
     * equippedItems gridpane is for equipped items (e.g. swords, shield, axe)
     */
    @FXML
    private GridPane equippedItems;

    @FXML
    private GridPane unequippedInventory;

    /**
     * ADDED CODE
     */
    @FXML
    private VBox characterstats;
    @FXML
    private Text output;

    // all image views including tiles, character, enemies, cards... even though
    // cards in separate gridpane...
    private List<ImageView> entityImages;

    /**
     * when we drag a card/item, the picture for whatever we're dragging is set here
     * and we actually drag this node
     */
    private DragIcon draggedEntity;

    private boolean isPaused;
    private LoopManiaWorld world;

    /**
     * runs the periodic game logic - second-by-second moving of character through
     * maze, as well as enemies, and running of battles
     */
    private Timeline timeline;

    private Image goldPileImage;
    private Image potionPileImage;
    private Image vampireCastleBuildingImage;
    private Image vampireCastleCardImage;
    private Image zombiePitBuildingImage;
    private Image zombiePitCardImage;
    private Image towerBuildingImage;
    private Image towerCardImage;
    private Image villageBuildingImage;
    private Image villageCardImage;
    private Image trapBuildingImage;
    private Image trapCardImage;
    private Image campfireBuildingImage;
    private Image campfireCardImage;
    private Image barracksBuildingImage;
    private Image barracksCardImage;
    private Image basicEnemyImage;
    private Image zombieImage;
    private Image thiefImage;
    private Image vampireImage;
    private Image swordImage;
    private Image stakeImage;
    private Image staffImage;
    private Image armourImage;
    private Image shieldImage;
    private Image helmetImage;
    private Image potionImage;
    private Image ringImage;
    private Image andurilImage;
    private Image alchemistTentBuildingImage;
    private Image alchemistTentCardImage;
    private Image survivorsTentsBuildingImage;
    private Image survivorsTentsCardImage;
    private Image fieldKitchenBuildingImage;
    private Image fieldKitchenCardImage;
    private Image holySpringBuildingImage;
    private Image holySpringCardImage;
    private Image rationImage;
    private Image treeStumpImage;

    private Image chartImage;
    private Image doggieCoinImage;
    private Image doggieImage;
    private Image elanImage;

    /**
     * the image currently being dragged, if there is one, otherwise null.
     * Holding the ImageView being dragged allows us to spawn it again in the drop
     * location if appropriate.
     */
    private ImageView currentlyDraggedImage;

    /**
     * null if nothing being dragged, or the type of item being dragged
     */
    private DRAGGABLE_TYPE currentlyDraggedType;

    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered
     * when the draggable type is dropped over its appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneSetOnDragDropped;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered
     * when the draggable type is dragged over the background
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> anchorPaneRootSetOnDragOver;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered
     * when the draggable type is dropped in the background
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> anchorPaneRootSetOnDragDropped;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered
     * when the draggable type is dragged into the boundaries of its appropriate
     * gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneNodeSetOnDragEntered;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered
     * when the draggable type is dragged outside of the boundaries of its
     * appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneNodeSetOnDragExited;

    /**
     * object handling switching to the main menu
     */
    private MenuSwitcher mainMenuSwitcher;

    /**
     * object handling switching to the shopWindow menu
     */
    private MenuSwitcher shopMenuSwitcher;

    /**
     * object handling switching to the bestiary menu
     */
    private MenuSwitcher bestiaryMenuSwitcher;

    /**
     * object handling switching to end screen
     */
    private MenuSwitcher endScreenSwitcher;

    /**
     * Instance of the end screen controller so this controller
     * can communicate with the end screen
     */
    private EndScreenController endScreenController;

    // Keeps track of when the shop needs to be bought up
    private int nextCycleShop = 1;
    private int increaseCyleShop = 2;
    private int currentCycle = 0;

    /**
     * @param world           world object loaded from file
     * @param initialEntities the initial JavaFX nodes (ImageViews) which should be
     *                        loaded into the GUI
     */
    public LoopManiaWorldController(LoopManiaWorld world, List<ImageView> initialEntities) {
        this.world = world;
        this.world.setController(this);
        entityImages = new ArrayList<>(initialEntities);
        goldPileImage = new Image((new File("src/images/gold_pile_world.png")).toURI().toString());
        potionPileImage = new Image((new File("src/images/spilt_wine.png")).toURI().toString());
        vampireCastleCardImage = new Image((new File("src/images/vampire_castle_card.png")).toURI().toString());
        basicEnemyImage = new Image((new File("src/images/slug.png")).toURI().toString());
        zombieImage = new Image((new File("src/images/zombie.png")).toURI().toString());
        vampireImage = new Image((new File("src/images/vampire.png")).toURI().toString());
        swordImage = new Image((new File("src/images/basic_sword.png")).toURI().toString());
        stakeImage = new Image((new File("src/images/stake.png")).toURI().toString());
        staffImage = new Image((new File("src/images/staff.png")).toURI().toString());
        armourImage = new Image((new File("src/images/armour.png")).toURI().toString());
        shieldImage = new Image((new File("src/images/shield.png")).toURI().toString());
        helmetImage = new Image((new File("src/images/helmet.png")).toURI().toString());
        potionImage = new Image((new File("src/images/brilliant_blue_new.png")).toURI().toString());
        ringImage = new Image((new File("src/images/the_one_ring.png")).toURI().toString());
        andurilImage = new Image((new File("src/images/anduril_flame_of_the_west.png")).toURI().toString());
        treeStumpImage = new Image((new File("src/images/tree_stump.png")).toURI().toString());
        vampireCastleBuildingImage = new Image(
                (new File("src/images/vampire_castle_building_purple_background.png")).toURI().toString());
        zombiePitBuildingImage = new Image((new File("src/images/zombie_pit.png")).toURI().toString());
        zombiePitCardImage = new Image((new File("src/images/zombie_pit_card.png")).toURI().toString());
        towerBuildingImage = new Image((new File("src/images/tower.png")).toURI().toString());
        towerCardImage = new Image((new File("src/images/tower_card.png")).toURI().toString());
        villageBuildingImage = new Image((new File("src/images/village.png")).toURI().toString());
        villageCardImage = new Image((new File("src/images/village_card.png")).toURI().toString());
        trapBuildingImage = new Image((new File("src/images/trap.png")).toURI().toString());
        trapCardImage = new Image((new File("src/images/trap_card.png")).toURI().toString());
        campfireBuildingImage = new Image((new File("src/images/campfire.png")).toURI().toString());
        campfireCardImage = new Image((new File("src/images/campfire_card.png")).toURI().toString());
        barracksBuildingImage = new Image((new File("src/images/barracks.png")).toURI().toString());
        barracksCardImage = new Image((new File("src/images/barracks_card.png")).toURI().toString());
        alchemistTentBuildingImage = new Image((new File("src/images/AlchemistTent.png")).toURI().toString());
        alchemistTentCardImage = new Image((new File("src/images/AlchemistTent.png")).toURI().toString());
        survivorsTentsBuildingImage = new Image((new File("src/images/SurvivorsTents.png")).toURI().toString());
        survivorsTentsCardImage = new Image((new File("src/images/SurvivorsTents.png")).toURI().toString());
        fieldKitchenBuildingImage = new Image((new File("src/images/FieldKitchen.png")).toURI().toString());
        fieldKitchenCardImage = new Image((new File("src/images/FieldKitchen.png")).toURI().toString());
        holySpringBuildingImage = new Image((new File("src/images/HolySpring.png")).toURI().toString());
        holySpringCardImage = new Image((new File("src/images/HolySpring.png")).toURI().toString());
        rationImage = new Image((new File("src/images/Ration.png")).toURI().toString());
        doggieCoinImage = new Image((new File("src/images/doggiecoin.png")).toURI().toString());
        chartImage = new Image((new File("src/images/chart.png")).toURI().toString());
        doggieImage = new Image((new File("src/images/doggie.png")).toURI().toString());
        elanImage = new Image((new File("src/images/ElanMuske.png")).toURI().toString());
        thiefImage = new Image((new File("src/images/thief.png")).toURI().toString());

        currentlyDraggedImage = null;
        currentlyDraggedType = null;

        // initialize them all...
        gridPaneSetOnDragDropped = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        anchorPaneRootSetOnDragOver = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        anchorPaneRootSetOnDragDropped = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        gridPaneNodeSetOnDragEntered = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        gridPaneNodeSetOnDragExited = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
    }

    @FXML
    public void initialize() {

        Image pathTilesImage = new Image((new File("src/images/32x32GrassAndDirtPath.png")).toURI().toString());
        Image inventorySlotImage = new Image((new File("src/images/empty_slot.png")).toURI().toString());
        // Added images
        Image heartImage = new Image((new File("src/images/heart.png")).toURI().toString(), 24, 24, false, false);
        Image goldImage = new Image((new File("src/images/gold_pile.png")).toURI().toString(), 24, 24, false, false);
        Image expImage = new Image((new File("src/images/exp.png")).toURI().toString(), 24, 24, false, false);
        Image allyImage = new Image((new File("src/images/Ally.png")).toURI().toString(), 24, 24, false, false);

        Rectangle2D imagePart = new Rectangle2D(0, 0, 32, 32);

        // Add the ground first so it is below all other entities (inculding all the
        // twists and turns)
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                ImageView groundView = new ImageView(pathTilesImage);
                groundView.setViewport(imagePart);
                squares.add(groundView, x, y);
            }
        }

        // load entities loaded from the file in the loader into the squares gridpane
        for (ImageView entity : entityImages) {
            squares.getChildren().add(entity);
        }

        // add the ground underneath the cards

        for (int x = 0; x < world.getWidth(); x++) {
            // ImageView groundView = new ImageView(pathTilesImage);
            ImageView groundView = new ImageView(inventorySlotImage);
            groundView.setViewport(imagePart);
            cards.add(groundView, x, 0);
        }

        // add the empty slot images for the unequipped inventory
        for (int x = 0; x < LoopManiaWorld.unequippedInventoryWidth; x++) {
            for (int y = 0; y < LoopManiaWorld.unequippedInventoryHeight; y++) {
                ImageView emptySlotView = new ImageView(inventorySlotImage);
                unequippedInventory.add(emptySlotView, x, y);
            }
        }

        /**
         * ADDED CODE
         * Adding the healthbar to the world view
         */
        StackPane stackHealth = new StackPane();
        Rectangle healthBar = new Rectangle(200.0, 25.0, Color.RED);
        healthBar.widthProperty().bind(world.getCharacterHealthProp().multiply(2));

        Rectangle healthBarBackground = new Rectangle(200.0, 25.0, Color.BLACK);
        ImageView heartView = new ImageView(heartImage);
        heartView.setViewport(imagePart);
        stackHealth.getChildren().addAll(healthBarBackground, healthBar, heartView);
        stackHealth.setAlignment(Pos.CENTER_LEFT);
        characterstats.getChildren().add(stackHealth);

        /**
         * ADDED CODE
         * Adding the gold count to the world view
         */
        GridPane gridCoins = new GridPane();
        ImageView goldView = new ImageView(goldImage);
        goldView.setViewport(imagePart);

        DropShadow shadow = new DropShadow();
        Text goldText = new Text();
        goldText.setEffect(shadow);
        goldText.setFont(Font.font(null, FontWeight.BOLD, 16));
        goldText.setFill(Color.YELLOW);

        goldText.textProperty().bind(world.getCharacterGoldProp().asString());
        gridCoins.add(goldView, 0, 0);
        gridCoins.add(goldText, 1, 0);
        characterstats.getChildren().add(gridCoins);

        /**
         * ADDED CODE
         * Adding the experience count to the world view
         */
        GridPane gridExp = new GridPane();
        ImageView expView = new ImageView(expImage);
        expView.setViewport(imagePart);

        Text expText = new Text();
        expText.setEffect(shadow);
        expText.setFont(Font.font(null, FontWeight.BOLD, 16));
        expText.setFill(Color.PURPLE);

        expText.textProperty().bind(world.getCharacterExpProp().asString());
        gridExp.add(expView, 0, 0);
        gridExp.add(expText, 1, 0);
        characterstats.getChildren().add(gridExp);

        /**
         * ADDED CODE
         * Shows DoggieCoin count and the sell price
         */
        GridPane gridCrypto = new GridPane();
        ImageView doggieCoinView = new ImageView(doggieCoinImage);
        doggieCoinView.setViewport(imagePart);
        ImageView chartView = new ImageView(chartImage);
        chartView.setViewport(imagePart);

        Text doggieCoinText = new Text();
        doggieCoinText.setEffect(shadow);
        doggieCoinText.setFont(Font.font(null, FontWeight.BOLD, 16));
        doggieCoinText.setFill(Color.GOLD);
        doggieCoinText.textProperty().bind(world.getCharacterDoggieCoinProp().asString());
        Text chartText = new Text();
        chartText.setEffect(shadow);
        chartText.setFont(Font.font(null, FontWeight.BOLD, 16));
        chartText.setFill(Color.BLACK);
        chartText.textProperty().bind(world.getDoggieSellPriceProp().asString());

        gridCrypto.add(doggieCoinView, 0, 0);
        gridCrypto.add(doggieCoinText, 1, 0);
        gridCrypto.add(chartView, 2, 0);
        gridCrypto.add(chartText, 3, 0);
        characterstats.getChildren().add(gridCrypto);

        /**
         * ADDED CODE
         * Adding the allies display
         */
        GridPane gridAlly = new GridPane();
        ImageView allyView = new ImageView(allyImage);
        Text allyText = new Text();
        allyText.setEffect(shadow);
        allyText.setFont(Font.font(null, FontWeight.BOLD, 16));
        allyText.setFill(Color.GREEN);

        allyText.textProperty().bind(world.getCharacterAlliesProp().asString());
        gridAlly.add(allyView, 0, 0);
        gridAlly.add(allyText, 1, 0);
        characterstats.getChildren().add(gridAlly);

        /**
         * ADDED CODE
         * Add a text box here that displays goals
         */
        Text goalToDo = new Text("To Do List");
        goalToDo.setUnderline(true);
        characterstats.getChildren().add(goalToDo);
        Text goalDisplay = new Text(world.printGoal());
        goalDisplay.setFont(Font.font(null, FontWeight.BOLD, 8));
        characterstats.getChildren().add(goalDisplay);

        /**
         * ADDED CODE
         * Add a text box here that changes based on in game events
         */
        this.output = new Text();
        this.output.maxWidth(30);
        characterstats.getChildren().add(this.output);

        /**
         * ADDED CODE
         * Adding the exit button at the end
         */
        Button bestiary = new Button("Consult Bestiary");
        bestiary.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    switchToBestiaryMenu();
                } catch (Exception exception) {
                    printThreadingNotes(exception.toString());
                }

            }
        });
        characterstats.getChildren().add(bestiary);

        /**
         * ADDED CODE
         * Adding the exit button at the end
         */
        Button exit = new Button("Back to Main Menu");
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    switchToMainMenu();
                } catch (Exception exception) {
                    printThreadingNotes(exception.toString());
                }

            }
        });
        characterstats.getChildren().add(exit);

        // create the draggable icon
        draggedEntity = new DragIcon();
        draggedEntity.setVisible(false);
        draggedEntity.setOpacity(0.7);
        anchorPaneRoot.getChildren().add(draggedEntity);
    }

    /**
     * create and run the timer
     */
    public void startTimer() {

        System.out.println("starting timer");
        isPaused = false;
        // trigger adding code to process main game logic to queue. JavaFX will target
        // framerate of 0.3 seconds
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.3), event -> {
            /**
             * ADDED CODE
             * Checks that the goals are met to win the game
             */
            if (world.isGoalMet()) {
                // NOTE: IT IS ASSUMED THAT THE GAME SIMPLY GOES BACK TO THE
                // MAIN MENU WHEN THE GAME ENDS.
                // WE WILL ADD GAMEOVER AND VICTORY FOR MILESTONE 3
                endGame(true);
            }

            world.runTickMoves();
            world.randomiseDoggieSellPrice();

            List<BasicEnemy> defeatedEnemies = null;
            try {
                defeatedEnemies = world.runBattles(System.currentTimeMillis());
            } catch (CharacterIsDead exception) {
                endGame(false);
            }

            if (defeatedEnemies != null) {
                for (BasicEnemy e : defeatedEnemies) {
                    reactToEnemyDefeat(e);
                }
            }

            // if Card entity is full, the oldest card will be removed, the removed card
            // will give the character a random item
            Card removedCard = null;
            try {
                removedCard = world.getRemovedCard();
            } catch (Exception e) {
                System.out.println("Something went wrong.");
            }
            if (removedCard != null) {
                reactToRemovedCard(removedCard);
            }

            // Run the buildings and its impact on characters and enemies
            world.runBuildings();

            // Gets new enemies spawned and loads them into the ui
            List<BasicEnemy> newEnemiesFromBuilding = world.runSpawnBuildings();
            for (BasicEnemy spawned : newEnemiesFromBuilding) {
                onLoad(spawned);
            }

            // Spawning in gold piles and potion piles
            GoldPileBuilding pileOfGold = world.possiblySpawnGoldPiles();
            if (pileOfGold != null) {
                onLoad(pileOfGold);
            }
            PotionPileBuilding pileOfPotion = world.possiblySpawnPotionPile();
            if (pileOfPotion != null) {
                onLoad(pileOfPotion);
            }

            // Spawning slimes
            List<BasicEnemy> newEnemies = world.possiblySpawnEnemies();
            for (BasicEnemy newEnemy : newEnemies) {
                onLoad(newEnemy);
            }
            // Spawning thief
            Thief filthyThief = world.spawnThief();
            if (filthyThief != null) {
                this.output.setText("A Thief has stolen your gold!");
                onLoad(filthyThief);
            }

            // Get one ration if character pass through field kitchen, get two rations if
            // the field kitchen is next to village
            if (world.checkCharacterStepOnFieldKitchen() == 1) {
                System.out.println("The character cooked in Field Kitchen and got 1 ration");
                loadRation();
            }
            if (world.checkCharacterStepOnFieldKitchen() == 2) {
                System.out.println("The character cooked in Field Kitchen and got 2 rations");
                loadRation();
                loadRation();
            }

            printThreadingNotes("HANDLED TIMER");
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * ADDED CODE
     * Sets the shopping controller with the loop mania world
     */
    public void setShoppingController(ShoppingController sc) {
        sc.setWorld(this.world, this);
    }

    /**
     * ADDED CODE
     * Sets the controller for the end screen so this controller
     * can communicate with the end screen
     * 
     * @param gm
     */
    public void setEndScreenController(EndScreenController esc) {
        this.endScreenController = esc;
    }

    /**
     * ADDED CODE
     * Sets the gamemode of the world
     */
    public void setGamemode(GamemodeState gm) {
        this.world.setGamemodeState(gm);
    }

    /**
     * ADDED CODE
     * Used by Character to notify the controller whenever it makes a new cycle
     * Controller observes the character
     * 
     * @param cycle
     */
    public void notifyCycleChange(int cycle) {
        this.currentCycle = cycle;
        if (cycle == nextCycleShop) {
            nextCycleShop += increaseCyleShop;
            increaseCyleShop++;
            try {
                printThreadingNotes("BRINGING UP SHOP");
                switchToShopMenu();
            } catch (Exception e) {
                printThreadingNotes("Error occured when trying to load shop");
                System.exit(1);
            }
        }

        // Attempt to spawn Doggie
        if (cycle >= 10) {
            Doggie doggie = world.spawnDoggie();
            if (doggie != null) {
                onLoad(doggie);
            }
        }
        // Spawns ElanMuske
        if (cycle >= 20 && world.getCharacterExpProp().get() >= 5000) {
            ElanMuske elan = world.spawnElanMuske();
            if (elan != null) {
                onLoad(elan);
            }
        }
    }

    /**
     * ADDED CODE
     * Ends the game and switches to the main menu
     */
    private void endGame(boolean didGameWin) {
        if (didGameWin) {
            printThreadingNotes("YOU WIN! CONGRATULATIONS");
        } else {
            printThreadingNotes("YOU LOST!");
        }

        // Setting up the end screen
        this.endScreenController.setGold(this.world.getCharacterGoldProp().get());
        this.endScreenController.setCycle(this.currentCycle);
        this.endScreenController.setExp(this.world.getCharacterExpProp().get());
        this.endScreenController.setBackground(didGameWin);

        try {
            switchToEndScreen();
        } catch (Exception e) {
            printThreadingNotes(e.toString());
        }
    }

    /**
     * ADDED CODE
     * Resets the game
     */
    public void resetGame() {
        this.nextCycleShop = 1;
        this.currentCycle = 0;
        this.increaseCyleShop = 2;
        // Sets the opacity for all images to 100% when resetting
        for (Node image : this.squares.getChildren()) {
            image.setOpacity(1);
        }
        world.resetWorld(System.currentTimeMillis());
    }

    /**
     * pause the execution of the game loop
     * the human player can still drag and drop items during the game pause
     */
    public void pause() {
        isPaused = true;
        System.out.println("pausing");
        timeline.stop();
    }

    public void terminate() {
        pause();
    }

    /**
     * pair the entity an view so that the view copies the movements of the entity.
     * add view to list of entity images
     * 
     * @param entity backend entity to be paired with view
     * @param view   frontend imageview to be paired with backend entity
     */
    private void addEntity(Entity entity, ImageView view) {
        trackPosition(entity, view);
        entityImages.add(view);
    }

    /**
     * load a vampire card from the world, and pair it with an image in the GUI
     */
    private void loadVampireCastleCard() {
        VampireCastleCard vampireCastleCard = world.loadVampireCastleCard();
        onLoad(vampireCastleCard);
    }

    /**
     * load a ZombiePit card from the world, and pair it with an image in the GUI
     */
    private void loadZombiePitCard() {
        ZombiePitCard zombiePitCard = world.loadZombiePitCard();
        onLoad(zombiePitCard);
    }

    /**
     * load a Tower card from the world, and pair it with an image in the GUI
     */
    private void loadTowerCard() {
        TowerCard towerCard = world.loadTowerCard();
        onLoad(towerCard);
    }

    /**
     * load a Village Card from the world, and pair it with an image in the GUI
     */
    private void loadVillageCard() {
        VillageCard villageCard = world.loadVillageCard();
        onLoad(villageCard);
    }

    /**
     * load a Trap Card from the world, and pair it with an image in the GUI
     */
    private void loadTrapCard() {
        TrapCard trapCard = world.loadTrapCard();
        onLoad(trapCard);
    }

    /**
     * load a Campfire Card from the world, and pair it with an image in the GUI
     */
    private void loadCampfireCard() {
        CampfireCard campfireCard = world.loadCampfireCard();
        onLoad(campfireCard);
    }

    /**
     * load a Barracks Card from the world, and pair it with an image in the GUI
     */
    private void loadBarracksCard() {
        BarracksCard barracksCard = world.loadBarracksCard();
        onLoad(barracksCard);
    }

    /**
     * load a Alchemist's Tent Card from the world, and pair it with an image in the
     * GUI
     */
    private void loadAlchemistTentCard() {
        AlchemistTentCard alchemistTentCard = world.loadAlchemistTentCard();
        onLoad(alchemistTentCard);
    }

    /**
     * load a Survivors' Tents Card from the world, and pair it with an image in the
     * GUI
     */
    private void loadSurvivorsTentsCard() {
        SurvivorsTentsCard survivorsTentsCard = world.loadSurvivorsTentsCard();
        onLoad(survivorsTentsCard);
    }

    /**
     * load a Field Kitchen Card from the world, and pair it with an image in the
     * GUI
     */
    private void loadFieldKitchenCard() {
        FieldKitchenCard fieldKitchenCard = world.loadFieldKitchenCard();
        onLoad(fieldKitchenCard);
    }

    /**
     * load a Holy Spring Card from the world, and pair it with an image in the GUI
     */
    private void loadHolySpringCard() {
        HolySpringCard holySpringCard = world.loadHolySpringCard();
        onLoad(holySpringCard);
    }

    /**
     * load a sword from the world, and pair it with an image in the GUI
     */
    private void loadSword() {
        // start by getting first available coordinates
        Sword sword = world.addUnequippedSword();
        onLoad(sword);
    }

    /**
     * Added by Eason
     * load a stake from the world, and pair it with an image in the GUI
     */
    public void loadStake() {
        Stake stake = world.addUnequippedStake();
        onLoad(stake);
    }

    /**
     * Added by Eason
     * load a staff from the world, and pair it with an image in the GUI
     */
    public void loadStaff() {
        Staff staff = world.addUnequippedStaff();
        onLoad(staff);
    }

    /**
     * Added by Eason
     * load an armour from the world, and pair it with an image in the GUI
     */
    public void loadArmour() {
        Armour armour = world.addUnequippedArmour();
        onLoad(armour);
    }

    /**
     * Added by Eason
     * load a shield from the world, and pair it with an image in the GUI
     */
    public void loadShield() {
        Shield shield = world.addUnequippedShield();
        onLoad(shield);
    }

    /**
     * Added by Eason
     * load a helmet from the world, and pair it with an image in the GUI
     */
    public void loadHelmet() {
        Helmet helmet = world.addUnequippedHelmet();
        onLoad(helmet);
    }

    /**
     * Added by Adam
     * load a potion from the world, and pair it with an image in the GUI
     */
    public void loadPotion() {
        Potion potion = world.addUnequippedPotion();
        onLoad(potion);
    }

    /**
     * Added by Adam
     * load the one ring from the world, and pair it with an image in the GUI
     */
    public void loadTheOneRing() {
        TheOneRing ring = world.addUnequippedTheOneRing();
        onLoad(ring);
    }

    /**
     * Added by Adam
     * load Anduril from the world, and pair it with an image in the GUI
     */
    public void loadAnduril() {
        Anduril anduril = world.addUnequippedAnduril();
        onLoad(anduril);
    }

    /**
     * Added by Adam
     * load TreeStump from the world, and pair it with an image in the GUI
     */
    public void loadTreeStump() {
        TreeStump treeStump = world.addUnequippedTreeStump();
        onLoad(treeStump);
    }

    /**
     * Added by Dongfei
     * load a ration from the world, and pair it with an image in the GUI
     */
    public void loadRation() {
        Ration ration = world.addUnequippedRation();
        onLoad(ration);
    }

    /**
     * run GUI events after an enemy is defeated, such as spawning
     * items/experience/gold
     * 
     * @param enemy defeated enemy for which we should react to the death of
     */
    private void reactToEnemyDefeat(BasicEnemy enemy) {
        // Reacting to defeating any boss
        if (enemy instanceof Boss) {
            Boss boss = (Boss) enemy;
            world.changeCharacterDoggieCoin(boss.dropCrypto());
            if (boss instanceof Doggie) {
                world.setCharacterDoggieSlayState(true);
            } else if (boss instanceof ElanMuske) {
                world.setCharacterElanSlayState(true);
            }
        }
        // react to character defeating an enemy
        StaticEntity loot = enemy.dropItemLoot();
        if (loot instanceof Sword) {
            loadSword();
        } else if (loot instanceof Staff) {
            loadStaff();
        } else if (loot instanceof Stake) {
            loadStake();
        } else if (loot instanceof Armour) {
            loadArmour();
        } else if (loot instanceof Shield) {
            loadShield();
        } else if (loot instanceof Helmet) {
            loadHelmet();
        } else if (loot instanceof Potion) {
            loadPotion();
        } else if (loot instanceof TheOneRing && this.world.isThisRareItemEnabled(loot)) {
            loadTheOneRing();
        } else if (loot instanceof Anduril && this.world.isThisRareItemEnabled(loot)) {
            loadAnduril();
        } else if (loot instanceof TreeStump && this.world.isThisRareItemEnabled(loot)) {
            loadTreeStump();
        }

        loot = enemy.dropBuildingLoot();

        if (loot instanceof VampireCastleCard) {
            loadVampireCastleCard();
        } else if (loot instanceof CampfireCard) {
            loadCampfireCard();
        } else if (loot instanceof BarracksCard) {
            loadBarracksCard();
        } else if (loot instanceof VillageCard) {
            loadVillageCard();
        } else if (loot instanceof ZombiePitCard) {
            loadZombiePitCard();
        } else if (loot instanceof TowerCard) {
            loadTowerCard();
        } else if (loot instanceof TrapCard) {
            loadTrapCard();
        } else if (loot instanceof AlchemistTentCard) {
            loadAlchemistTentCard();
        } else if (loot instanceof SurvivorsTentsCard) {
            loadSurvivorsTentsCard();
        } else if (loot instanceof FieldKitchenCard) {
            loadFieldKitchenCard();
        } else if (loot instanceof HolySpringCard) {
            loadHolySpringCard();
        }

    }

    /**
     * get the type of removedCard, and then give a random item depends on the type
     * of removedCard
     * 
     * @param removedCard
     */
    private void reactToRemovedCard(Card removedCard) {

        StaticEntity loot = removedCard.dropItemLoot();
        if (loot instanceof Sword) {
            loadSword();
        } else if (loot instanceof Staff) {
            loadStaff();
        } else if (loot instanceof Stake) {
            loadStake();
        } else if (loot instanceof Armour) {
            loadArmour();
        } else if (loot instanceof Shield) {
            loadShield();
        } else if (loot instanceof Helmet) {
            loadHelmet();
        } else if (loot instanceof Potion) {
            loadPotion();
        } else if (loot instanceof TheOneRing && this.world.isThisRareItemEnabled(loot)) {
            loadTheOneRing();
        } else if (loot instanceof Anduril && this.world.isThisRareItemEnabled(loot)) {
            loadAnduril();
        } else if (loot instanceof TreeStump && this.world.isThisRareItemEnabled(loot)) {
            loadTreeStump();
        }

    }

    /**
     * load a vampire castle card into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the cards GridPane.
     * 
     * @param vampireCastleCard
     */
    private void onLoad(VampireCastleCard vampireCastleCard) {
        ImageView view = new ImageView(vampireCastleCardImage);

        // FROM
        // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, DRAGGABLE_TYPE.VampireCastleCARD, cards, squares);

        addEntity(vampireCastleCard, view);
        cards.getChildren().add(view);
    }

    /**
     * load a zombie Pit Card into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the cards GridPane.
     * 
     * @param zombiePitCard
     */
    private void onLoad(ZombiePitCard zombiePitCard) {
        ImageView view = new ImageView(zombiePitCardImage);

        // FROM
        // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, DRAGGABLE_TYPE.ZombiePitCARD, cards, squares);

        addEntity(zombiePitCard, view);
        cards.getChildren().add(view);
    }

    /**
     * load a Tower Card into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the cards GridPane.
     * 
     * @param towerCard
     */
    private void onLoad(TowerCard towerCard) {
        ImageView view = new ImageView(towerCardImage);

        // FROM
        // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, DRAGGABLE_TYPE.TowerCARD, cards, squares);

        addEntity(towerCard, view);
        cards.getChildren().add(view);
    }

    /**
     * load a Village Card into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the cards GridPane.
     * 
     * @param villageCard
     */
    private void onLoad(VillageCard villageCard) {
        ImageView view = new ImageView(villageCardImage);

        // FROM
        // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, DRAGGABLE_TYPE.VillageCARD, cards, squares);

        addEntity(villageCard, view);
        cards.getChildren().add(view);
    }

    /**
     * load a Trap Card into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the cards GridPane.
     * 
     * @param trapCard
     */
    private void onLoad(TrapCard trapCard) {
        ImageView view = new ImageView(trapCardImage);

        // FROM
        // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, DRAGGABLE_TYPE.TrapCARD, cards, squares);

        addEntity(trapCard, view);
        cards.getChildren().add(view);
    }

    /**
     * load a Campfire Card into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the cards GridPane.
     * 
     * @param campfireCard
     */
    private void onLoad(CampfireCard campfireCard) {
        ImageView view = new ImageView(campfireCardImage);

        // FROM
        // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, DRAGGABLE_TYPE.CampfireCARD, cards, squares);

        addEntity(campfireCard, view);
        cards.getChildren().add(view);
    }

    /**
     * load a Barracks Card into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the cards GridPane.
     * 
     * @param barracksCard
     */
    private void onLoad(BarracksCard barracksCard) {
        ImageView view = new ImageView(barracksCardImage);

        // FROM
        // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, DRAGGABLE_TYPE.BarracksCARD, cards, squares);

        addEntity(barracksCard, view);
        cards.getChildren().add(view);
    }

    /**
     * load a Alchemist's Tent Card into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the cards GridPane.
     * 
     * @param alchemistTentCard
     */
    private void onLoad(AlchemistTentCard alchemistTentCard) {
        ImageView view = new ImageView(alchemistTentCardImage);

        // FROM
        // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, DRAGGABLE_TYPE.AlchemistTentCARD, cards, squares);

        addEntity(alchemistTentCard, view);
        cards.getChildren().add(view);
    }

    /**
     * load a Survivors' Tents Card into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the cards GridPane.
     * 
     * @param survivorsTentsCard
     */
    private void onLoad(SurvivorsTentsCard survivorsTentsCard) {
        ImageView view = new ImageView(survivorsTentsCardImage);

        // FROM
        // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, DRAGGABLE_TYPE.SurvivorsTentsCARD, cards, squares);

        addEntity(survivorsTentsCard, view);
        cards.getChildren().add(view);
    }

    /**
     * load a Field Kitchen Card into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the cards GridPane.
     * 
     * @param fieldKitchenCard
     */
    private void onLoad(FieldKitchenCard fieldKitchenCard) {
        ImageView view = new ImageView(fieldKitchenCardImage);

        // FROM
        // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, DRAGGABLE_TYPE.FieldKitchenCARD, cards, squares);

        addEntity(fieldKitchenCard, view);
        cards.getChildren().add(view);
    }

    /**
     * load a Holy Spring Card into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the cards GridPane.
     * 
     * @param holySpringCard
     */
    private void onLoad(HolySpringCard holySpringCard) {
        ImageView view = new ImageView(holySpringCardImage);

        // FROM
        // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, DRAGGABLE_TYPE.HolySpringCARD, cards, squares);

        addEntity(holySpringCard, view);
        cards.getChildren().add(view);
    }

    /**
     * load a sword into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the unequippedInventory GridPane.
     * 
     * @param sword
     */
    private void onLoad(Sword sword) {
        ImageView view = new ImageView(swordImage);
        addDragEventHandlers(view, DRAGGABLE_TYPE.Sword, unequippedInventory, equippedItems);
        addEntity(sword, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * Added by Eason
     * load a stake into the GUI
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the unequippedInventory GridPane.
     * 
     * @param stake
     */
    private void onLoad(Stake stake) {
        ImageView view = new ImageView(stakeImage);
        addDragEventHandlers(view, DRAGGABLE_TYPE.Stake, unequippedInventory, equippedItems);
        addEntity(stake, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * Added by Eason
     * load a staff into the GUI
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the unequippedInventory GridPane.
     * 
     * @param staff
     */
    private void onLoad(Staff staff) {
        ImageView view = new ImageView(staffImage);
        addDragEventHandlers(view, DRAGGABLE_TYPE.Staff, unequippedInventory, equippedItems);
        addEntity(staff, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * Added by Eason
     * load a armour into the GUI
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the unequippedInventory GridPane.
     * 
     * @param armour
     */
    private void onLoad(Armour armour) {
        ImageView view = new ImageView(armourImage);
        addDragEventHandlers(view, DRAGGABLE_TYPE.Armour, unequippedInventory, equippedItems);
        addEntity(armour, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * Added by Eason
     * load a shield into the GUI
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the unequippedInventory GridPane.
     * 
     * @param shield
     */
    private void onLoad(Shield shield) {
        ImageView view = new ImageView(shieldImage);
        addDragEventHandlers(view, DRAGGABLE_TYPE.Shield, unequippedInventory, equippedItems);
        addEntity(shield, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * Added by Eason
     * load a helmet into the GUI
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the unequippedInventory GridPane.
     * 
     * @param helmet
     */
    private void onLoad(Helmet helmet) {
        ImageView view = new ImageView(helmetImage);
        addDragEventHandlers(view, DRAGGABLE_TYPE.Helmet, unequippedInventory, equippedItems);
        addEntity(helmet, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * Added by Adam
     * load a potion into the GUI
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the unequippedInventory GridPane.
     * 
     * @param potion
     */
    private void onLoad(Potion potion) {
        ImageView view = new ImageView(potionImage);
        addDragEventHandlers(view, DRAGGABLE_TYPE.Potion, unequippedInventory, equippedItems);
        addEntity(potion, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * Added by Adam
     * load a the one ring into the GUI
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the unequippedInventory GridPane.
     * 
     * @param ring
     */
    private void onLoad(TheOneRing ring) {
        ImageView view = new ImageView(ringImage);
        addDragEventHandlers(view, DRAGGABLE_TYPE.TheOneRing, unequippedInventory, equippedItems);
        addEntity(ring, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * Added by Adam
     * load a potion into the GUI
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the unequippedInventory GridPane.
     * 
     * @param anduril
     */
    private void onLoad(Anduril anduril) {
        ImageView view = new ImageView(andurilImage);
        addDragEventHandlers(view, DRAGGABLE_TYPE.Anduril, unequippedInventory, equippedItems);
        addEntity(anduril, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * Added by Dongfei
     * load a ration into the GUI
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the unequippedInventory GridPane.
     * 
     * @param ration
     */
    private void onLoad(Ration ration) {
        ImageView view = new ImageView(rationImage);
        addDragEventHandlers(view, DRAGGABLE_TYPE.Ration, unequippedInventory, equippedItems);
        addEntity(ration, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * Added by Adam
     * load a tree stump into the GUI
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the unequippedInventory GridPane.
     * 
     * @param treeStump
     */
    private void onLoad(TreeStump treeStump) {
        ImageView view = new ImageView(treeStumpImage);
        addDragEventHandlers(view, DRAGGABLE_TYPE.TreeStump, unequippedInventory, equippedItems);
        addEntity(treeStump, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * EDITED CODE
     * load an enemy into the GUI
     * 
     * @param enemy
     */
    private void onLoad(BasicEnemy enemy) {
        ImageView view;
        if (enemy instanceof Vampire) {
            view = new ImageView(vampireImage);
        } else if (enemy instanceof Zombie) {
            view = new ImageView(zombieImage);
        } else if (enemy instanceof Thief) {
            view = new ImageView(thiefImage);
        } else if (enemy instanceof Doggie) {
            view = new ImageView(doggieImage);
        } else if (enemy instanceof ElanMuske) {
            view = new ImageView(elanImage);
        } else {
            view = new ImageView(basicEnemyImage);
        }
        addEntity(enemy, view);
        squares.getChildren().add(view);
    }

    /**
     * Loads in the gold pile into the gui
     * 
     * @param goldPile
     */
    private void onLoad(GoldPileBuilding goldPile) {
        ImageView view = new ImageView(goldPileImage);
        addEntity(goldPile, view);
        squares.getChildren().add(view);
    }

    /**
     * Loads in the potion pile into the gui
     * 
     * @param goldPile
     */
    private void onLoad(PotionPileBuilding potionPile) {
        ImageView view = new ImageView(potionPileImage);
        addEntity(potionPile, view);
        squares.getChildren().add(view);
    }

    /**
     * load a building into the GUI
     * 
     * @param building
     */
    private void onLoad(VampireCastleBuilding building) {
        ImageView view = new ImageView(vampireCastleBuildingImage);
        addEntity(building, view);
        squares.getChildren().add(view);
    }

    /**
     * load a building into the GUI
     * 
     * @param building
     */
    private void onLoad(ZombiePitBuilding building) {
        ImageView view = new ImageView(zombiePitBuildingImage);
        addEntity(building, view);
        squares.getChildren().add(view);
    }

    /**
     * load a building into the GUI
     * 
     * @param building
     */
    private void onLoad(TowerBuilding building) {
        ImageView view = new ImageView(towerBuildingImage);
        addEntity(building, view);
        squares.getChildren().add(view);
    }

    /**
     * load a building into the GUI
     * 
     * @param building
     */
    private void onLoad(VillageBuilding building) {
        ImageView view = new ImageView(villageBuildingImage);
        addEntity(building, view);
        squares.getChildren().add(view);
    }

    /**
     * load a building into the GUI
     * 
     * @param building
     */
    private void onLoad(TrapBuilding building) {
        ImageView view = new ImageView(trapBuildingImage);
        addEntity(building, view);
        squares.getChildren().add(view);
    }

    /**
     * load a building into the GUI
     * 
     * @param building
     */
    private void onLoad(CampfireBuilding building) {
        ImageView view = new ImageView(campfireBuildingImage);
        addEntity(building, view);
        squares.getChildren().add(view);
    }

    /**
     * load a building into the GUI
     * 
     * @param building
     */
    private void onLoad(BarracksBuilding building) {
        ImageView view = new ImageView(barracksBuildingImage);
        addEntity(building, view);
        squares.getChildren().add(view);
    }

    /**
     * load a building into the GUI
     * 
     * @param building
     */
    private void onLoad(AlchemistTentBuilding building) {
        ImageView view = new ImageView(alchemistTentBuildingImage);
        addEntity(building, view);
        squares.getChildren().add(view);
    }

    /**
     * load a building into the GUI
     * 
     * @param building
     */
    private void onLoad(SurvivorsTentsBuilding building) {
        ImageView view = new ImageView(survivorsTentsBuildingImage);
        addEntity(building, view);
        squares.getChildren().add(view);
    }

    /**
     * load a building into the GUI
     * 
     * @param building
     */
    private void onLoad(FieldKitchenBuilding building) {
        ImageView view = new ImageView(fieldKitchenBuildingImage);
        addEntity(building, view);
        squares.getChildren().add(view);
    }

    /**
     * load a building into the GUI
     * 
     * @param building
     */
    private void onLoad(HolySpringBuilding building) {
        ImageView view = new ImageView(holySpringBuildingImage);
        addEntity(building, view);
        squares.getChildren().add(view);
    }

    /**
     * add drag event handlers for dropping into gridpanes, dragging over the
     * background, dropping over the background.
     * These are not attached to invidual items such as swords/cards.
     * 
     * @param draggableType  the type being dragged - card or item
     * @param sourceGridPane the gridpane being dragged from
     * @param targetGridPane the gridpane the human player should be dragging to
     *                       (but we of course cannot guarantee they will do so)
     */
    private void buildNonEntityDragHandlers(DRAGGABLE_TYPE draggableType, GridPane sourceGridPane,
            GridPane targetGridPane) {

        gridPaneSetOnDragDropped.put(draggableType, new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                boolean graphicWasPlaced = false;
                /*
                 * you might want to design the application so dropping at an invalid location
                 * drops at the most recent valid location hovered over,
                 * or simply allow the card/item to return to its slot (the latter is easier, as
                 * you won't have to store the last valid drop location!)
                 */
                if (currentlyDraggedType == draggableType) {
                    // problem = event is drop completed is false when should be true...
                    // https://bugs.openjdk.java.net/browse/JDK-8117019
                    // putting drop completed at start not making complete on VLAB...

                    // Data dropped
                    // If there is an image on the dragboard, read it and use it
                    Dragboard db = event.getDragboard();
                    Node node = event.getPickResult().getIntersectedNode();
                    if (node != targetGridPane && db.hasImage()) {

                        // ADDED CODE
                        graphicWasPlaced = true;

                        Integer cIndex = GridPane.getColumnIndex(node);
                        Integer rIndex = GridPane.getRowIndex(node);
                        int x = cIndex == null ? 0 : cIndex;
                        int y = rIndex == null ? 0 : rIndex;
                        // Places at 0,0 - will need to take coordinates once that is implemented
                        ImageView image = new ImageView(db.getImage());

                        int nodeX = GridPane.getColumnIndex(currentlyDraggedImage);
                        int nodeY = GridPane.getRowIndex(currentlyDraggedImage);
                        switch (draggableType) {
                            case VampireCastleCARD:
                                if (!world.checkPathTileOrNonPathTile(x, y)) {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    VampireCastleBuilding vampireCastleBuilding = convertVampireCastleCardToBuildingByCoordinates(
                                            nodeX, nodeY, x, y);

                                    onLoad(vampireCastleBuilding);
                                } else {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    convertVampireCastleCardToBuildingByCoordinates(nodeX, nodeY, x, y);

                                    loadVampireCastleCard();
                                }
                                break;
                            case ZombiePitCARD:
                                if (!world.checkPathTileOrNonPathTile(x, y)) {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    ZombiePitBuilding zombiePitBuilding = convertZombiePitCardToBuildingByCoordinates(
                                            nodeX, nodeY, x, y);

                                    onLoad(zombiePitBuilding);
                                } else {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    convertZombiePitCardToBuildingByCoordinates(nodeX, nodeY, x, y);

                                    loadZombiePitCard();
                                }
                                break;
                            case TowerCARD:
                                if (!world.checkPathTileOrNonPathTile(x, y)) {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    TowerBuilding towerBuilding = convertTowerCardToBuildingByCoordinates(nodeX, nodeY,
                                            x, y);

                                    onLoad(towerBuilding);
                                } else {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    convertTowerCardToBuildingByCoordinates(nodeX, nodeY, x, y);

                                    loadTowerCard();
                                }
                                break;
                            case VillageCARD:
                                if (world.checkPathTileOrNonPathTile(x, y)) {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    VillageBuilding villageBuilding = convertVillageCardToBuildingByCoordinates(nodeX,
                                            nodeY, x, y);

                                    onLoad(villageBuilding);
                                } else {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    convertVillageCardToBuildingByCoordinates(nodeX, nodeY, x, y);

                                    loadVillageCard();
                                }
                                break;
                            case TrapCARD:
                                if (world.checkPathTileOrNonPathTile(x, y)) {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    TrapBuilding trapBuilding = convertTrapCardToBuildingByCoordinates(nodeX, nodeY, x,
                                            y);

                                    onLoad(trapBuilding);
                                } else {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    convertTrapCardToBuildingByCoordinates(nodeX, nodeY, x, y);

                                    loadTrapCard();
                                }

                                break;
                            case CampfireCARD:
                                if (!world.checkPathTileOrNonPathTile(x, y)) {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    CampfireBuilding campfireBuilding = convertCampfireCardToBuildingByCoordinates(
                                            nodeX, nodeY, x, y);

                                    onLoad(campfireBuilding);
                                } else {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    convertCampfireCardToBuildingByCoordinates(nodeX, nodeY, x, y);

                                    loadCampfireCard();
                                }
                                break;
                            case BarracksCARD:
                                if (world.checkPathTileOrNonPathTile(x, y)) {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    BarracksBuilding barracksBuilding = convertBarracksCardToBuildingByCoordinates(
                                            nodeX, nodeY, x, y);

                                    onLoad(barracksBuilding);
                                } else {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    convertBarracksCardToBuildingByCoordinates(nodeX, nodeY, x, y);

                                    loadBarracksCard();
                                }
                                break;
                            case AlchemistTentCARD:
                                if (world.checkPathTileOrNonPathTile(x, y)) {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    AlchemistTentBuilding alchemistTentBuilding = convertAlchemistTentCardToBuildingByCoordinates(
                                            nodeX, nodeY, x, y);

                                    onLoad(alchemistTentBuilding);
                                } else {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    convertAlchemistTentCardToBuildingByCoordinates(nodeX, nodeY, x, y);

                                    loadAlchemistTentCard();
                                }
                                break;
                            case SurvivorsTentsCARD:
                                if (world.checkPathTileOrNonPathTile(x, y)) {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    SurvivorsTentsBuilding survivorsTentsBuilding = convertSurvivorsTentsCardToBuildingByCoordinates(
                                            nodeX, nodeY, x, y);

                                    onLoad(survivorsTentsBuilding);
                                } else {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    convertSurvivorsTentsCardToBuildingByCoordinates(nodeX, nodeY, x, y);

                                    loadSurvivorsTentsCard();
                                }
                                break;
                            case FieldKitchenCARD:
                                if (world.checkPathTileOrNonPathTile(x, y)) {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    FieldKitchenBuilding fieldKitchenBuilding = convertFieldKitchenCardToBuildingByCoordinates(
                                            nodeX, nodeY, x, y);

                                    onLoad(fieldKitchenBuilding);
                                } else {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    convertFieldKitchenCardToBuildingByCoordinates(nodeX, nodeY, x, y);

                                    loadFieldKitchenCard();
                                }
                                break;
                            case HolySpringCARD:
                                if (world.checkPathTileOrNonPathTile(x, y)) {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    HolySpringBuilding holySpringBuilding = convertHolySpringCardToBuildingByCoordinates(
                                            nodeX, nodeY, x, y);

                                    onLoad(holySpringBuilding);
                                } else {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    convertHolySpringCardToBuildingByCoordinates(nodeX, nodeY, x, y);

                                    loadHolySpringCard();
                                }
                                break;
                            case Sword:
                                if (x == 0 && y == 0) {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    removeItemByCoordinates(nodeX, nodeY);
                                    targetGridPane.add(image, x, y, 1, 1);
                                    world.addBackendSword(x, y);
                                } else {
                                    graphicWasPlaced = false;
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    removeItemByCoordinates(nodeX, nodeY);
                                    loadSword();
                                }
                                break;
                            case Stake:
                                if (x == 0 && y == 0) {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    removeItemByCoordinates(nodeX, nodeY);
                                    targetGridPane.add(image, x, y, 1, 1);
                                    world.addBackendStake(x, y);
                                } else {
                                    graphicWasPlaced = false;
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    removeItemByCoordinates(nodeX, nodeY);
                                    loadStake();
                                }
                                break;
                            case Staff:
                                if (x == 0 && y == 0) {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    removeItemByCoordinates(nodeX, nodeY);
                                    targetGridPane.add(image, x, y, 1, 1);
                                    world.addBackendStaff(x, y);
                                } else {
                                    graphicWasPlaced = false;
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    removeItemByCoordinates(nodeX, nodeY);
                                    loadStaff();
                                }
                                break;
                            case Armour:
                                if (x == 1 && y == 0) {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    removeItemByCoordinates(nodeX, nodeY);
                                    targetGridPane.add(image, x, y, 1, 1);
                                    world.addBackendArmour(x, y);
                                } else {
                                    graphicWasPlaced = false;
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    removeItemByCoordinates(nodeX, nodeY);
                                    loadArmour();
                                }
                                break;
                            case Shield:
                                if (x == 2 && y == 0) {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    removeItemByCoordinates(nodeX, nodeY);
                                    targetGridPane.add(image, x, y, 1, 1);
                                    world.addBackendShield(x, y);
                                } else {
                                    graphicWasPlaced = false;
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    removeItemByCoordinates(nodeX, nodeY);
                                    loadShield();
                                }
                                break;
                            case Helmet:
                                if (x == 1 && y == 0) {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    removeItemByCoordinates(nodeX, nodeY);
                                    targetGridPane.add(image, x, y, 1, 1);
                                    world.addBackendHelmet(x, y);
                                } else {
                                    graphicWasPlaced = false;
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    removeItemByCoordinates(nodeX, nodeY);
                                    loadHelmet();
                                }
                                break;
                            case Anduril:
                                if (x == 0 && y == 0) {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    removeItemByCoordinates(nodeX, nodeY);
                                    targetGridPane.add(image, x, y, 1, 1);
                                    world.addBackendAnduril(x, y);
                                } else {
                                    graphicWasPlaced = false;
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    removeItemByCoordinates(nodeX, nodeY);
                                    loadAnduril();
                                }
                                break;
                            case TreeStump:
                                if (x == 2 && y == 0) {
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    removeItemByCoordinates(nodeX, nodeY);
                                    targetGridPane.add(image, x, y, 1, 1);
                                    world.addBackendTreeStump(x, y);
                                } else {
                                    graphicWasPlaced = false;
                                    removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                    removeItemByCoordinates(nodeX, nodeY);
                                    loadTreeStump();
                                }
                                break;
                            case Potion:
                                graphicWasPlaced = false;
                                break;
                            case Ration:
                                graphicWasPlaced = false;
                                break;
                            case TheOneRing:
                                graphicWasPlaced = false;
                                break;
                            default:
                                break;
                        }

                        if (graphicWasPlaced) {
                            draggedEntity.setVisible(false);
                            draggedEntity.setMouseTransparent(false);
                            // remove drag event handlers before setting currently dragged image to null
                            currentlyDraggedImage = null;
                            currentlyDraggedType = null;
                        }

                        printThreadingNotes("DRAG DROPPED ON GRIDPANE HANDLED");
                    }
                }
                event.setDropCompleted(true);
                // consuming prevents the propagation of the event to the anchorPaneRoot (as a
                // sub-node of anchorPaneRoot, GridPane is prioritized)
                // https://openjfx.io/javadoc/11/javafx.base/javafx/event/Event.html#consume()
                // to understand this in full detail, ask your tutor or read
                // https://docs.oracle.com/javase/8/javafx/events-tutorial/processing.htm
                event.consume();
            }
        });

        // this doesn't fire when we drag over GridPane because in the event handler for
        // dragging over GridPanes, we consume the event
        anchorPaneRootSetOnDragOver.put(draggableType, new EventHandler<DragEvent>() {
            // https://github.com/joelgraff/java_fx_node_link_demo/blob/master/Draggable_Node/DraggableNodeDemo/src/application/RootLayout.java#L110
            @Override
            public void handle(DragEvent event) {
                if (currentlyDraggedType == draggableType) {
                    if (event.getGestureSource() != anchorPaneRoot && event.getDragboard().hasImage()) {
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                }
                if (currentlyDraggedType != null) {
                    draggedEntity.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                }
                event.consume();
            }
        });

        // this doesn't fire when we drop over GridPane because in the event handler for
        // dropping over GridPanes, we consume the event
        anchorPaneRootSetOnDragDropped.put(draggableType, new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                if (currentlyDraggedType == draggableType) {
                    // Data dropped
                    // If there is an image on the dragboard, read it and use it
                    Dragboard db = event.getDragboard();
                    Node node = event.getPickResult().getIntersectedNode();
                    if (node != anchorPaneRoot && db.hasImage()) {
                        // Places at 0,0 - will need to take coordinates once that is implemented
                        currentlyDraggedImage.setVisible(true);
                        draggedEntity.setVisible(false);
                        draggedEntity.setMouseTransparent(false);
                        // remove drag event handlers before setting currently dragged image to null
                        removeDraggableDragEventHandlers(draggableType, targetGridPane);

                        currentlyDraggedImage = null;
                        currentlyDraggedType = null;
                    }
                }
                // let the source know whether the image was successfully transferred and used
                event.setDropCompleted(true);
                event.consume();
            }
        });
    }

    /**
     * remove the card from the world, and spawn and return a building instead where
     * the card was dropped
     * 
     * @param cardNodeX     the x coordinate of the card which was dragged, from 0
     *                      to width-1
     * @param cardNodeY     the y coordinate of the card which was dragged (in
     *                      starter code this is 0 as only 1 row of cards)
     * @param buildingNodeX the x coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to width-1
     * @param buildingNodeY the y coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to height-1
     * @return building entity returned from the world
     */
    private VampireCastleBuilding convertVampireCastleCardToBuildingByCoordinates(int cardNodeX, int cardNodeY,
            int buildingNodeX, int buildingNodeY) {
        return world.convertVampireCastleCardToBuildingByCoordinates(cardNodeX, cardNodeY, buildingNodeX,
                buildingNodeY);
    }

    /**
     * remove the card from the world, and spawn and return a building instead where
     * the card was dropped
     * 
     * @param cardNodeX     the x coordinate of the card which was dragged, from 0
     *                      to width-1
     * @param cardNodeY     the y coordinate of the card which was dragged (in
     *                      starter code this is 0 as only 1 row of cards)
     * @param buildingNodeX the x coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to width-1
     * @param buildingNodeY the y coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to height-1
     * @return building entity returned from the world
     */
    private ZombiePitBuilding convertZombiePitCardToBuildingByCoordinates(int cardNodeX, int cardNodeY,
            int buildingNodeX, int buildingNodeY) {
        return world.convertZombiePitCardToBuildingByCoordinates(cardNodeX, cardNodeY, buildingNodeX, buildingNodeY);
    }

    /**
     * remove the card from the world, and spawn and return a building instead where
     * the card was dropped
     * 
     * @param cardNodeX     the x coordinate of the card which was dragged, from 0
     *                      to width-1
     * @param cardNodeY     the y coordinate of the card which was dragged (in
     *                      starter code this is 0 as only 1 row of cards)
     * @param buildingNodeX the x coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to width-1
     * @param buildingNodeY the y coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to height-1
     * @return building entity returned from the world
     */
    private TowerBuilding convertTowerCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX,
            int buildingNodeY) {
        return world.convertTowerCardToBuildingByCoordinates(cardNodeX, cardNodeY, buildingNodeX, buildingNodeY);
    }

    /**
     * remove the card from the world, and spawn and return a building instead where
     * the card was dropped
     * 
     * @param cardNodeX     the x coordinate of the card which was dragged, from 0
     *                      to width-1
     * @param cardNodeY     the y coordinate of the card which was dragged (in
     *                      starter code this is 0 as only 1 row of cards)
     * @param buildingNodeX the x coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to width-1
     * @param buildingNodeY the y coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to height-1
     * @return building entity returned from the world
     */
    private VillageBuilding convertVillageCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX,
            int buildingNodeY) {
        return world.convertVillageCardToBuildingByCoordinates(cardNodeX, cardNodeY, buildingNodeX, buildingNodeY);
    }

    /**
     * remove the card from the world, and spawn and return a building instead where
     * the card was dropped
     * 
     * @param cardNodeX     the x coordinate of the card which was dragged, from 0
     *                      to width-1
     * @param cardNodeY     the y coordinate of the card which was dragged (in
     *                      starter code this is 0 as only 1 row of cards)
     * @param buildingNodeX the x coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to width-1
     * @param buildingNodeY the y coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to height-1
     * @return building entity returned from the world
     */
    private TrapBuilding convertTrapCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX,
            int buildingNodeY) {
        return world.convertTrapCardToBuildingByCoordinates(cardNodeX, cardNodeY, buildingNodeX, buildingNodeY);
    }

    /**
     * remove the card from the world, and spawn and return a building instead where
     * the card was dropped
     * 
     * @param cardNodeX     the x coordinate of the card which was dragged, from 0
     *                      to width-1
     * @param cardNodeY     the y coordinate of the card which was dragged (in
     *                      starter code this is 0 as only 1 row of cards)
     * @param buildingNodeX the x coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to width-1
     * @param buildingNodeY the y coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to height-1
     * @return building entity returned from the world
     */
    private CampfireBuilding convertCampfireCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX,
            int buildingNodeY) {
        return world.convertCampfireCardToBuildingByCoordinates(cardNodeX, cardNodeY, buildingNodeX, buildingNodeY);
    }

    /**
     * remove the card from the world, and spawn and return a building instead where
     * the card was dropped
     * 
     * @param cardNodeX     the x coordinate of the card which was dragged, from 0
     *                      to width-1
     * @param cardNodeY     the y coordinate of the card which was dragged (in
     *                      starter code this is 0 as only 1 row of cards)
     * @param buildingNodeX the x coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to width-1
     * @param buildingNodeY the y coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to height-1
     * @return building entity returned from the world
     */
    private BarracksBuilding convertBarracksCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX,
            int buildingNodeY) {
        return world.convertBarracksCardToBuildingByCoordinates(cardNodeX, cardNodeY, buildingNodeX, buildingNodeY);
    }

    /**
     * remove the card from the world, and spawn and return a building instead where
     * the card was dropped
     * 
     * @param cardNodeX     the x coordinate of the card which was dragged, from 0
     *                      to width-1
     * @param cardNodeY     the y coordinate of the card which was dragged (in
     *                      starter code this is 0 as only 1 row of cards)
     * @param buildingNodeX the x coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to width-1
     * @param buildingNodeY the y coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to height-1
     * @return building entity returned from the world
     */
    private AlchemistTentBuilding convertAlchemistTentCardToBuildingByCoordinates(int cardNodeX, int cardNodeY,
            int buildingNodeX, int buildingNodeY) {
        return world.convertAlchemistTentCardToBuildingByCoordinates(cardNodeX, cardNodeY, buildingNodeX,
                buildingNodeY);
    }

    /**
     * remove the card from the world, and spawn and return a building instead where
     * the card was dropped
     * 
     * @param cardNodeX     the x coordinate of the card which was dragged, from 0
     *                      to width-1
     * @param cardNodeY     the y coordinate of the card which was dragged (in
     *                      starter code this is 0 as only 1 row of cards)
     * @param buildingNodeX the x coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to width-1
     * @param buildingNodeY the y coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to height-1
     * @return building entity returned from the world
     */
    private SurvivorsTentsBuilding convertSurvivorsTentsCardToBuildingByCoordinates(int cardNodeX, int cardNodeY,
            int buildingNodeX, int buildingNodeY) {
        return world.convertSurvivorsTentsCardToBuildingByCoordinates(cardNodeX, cardNodeY, buildingNodeX,
                buildingNodeY);
    }

    /**
     * remove the card from the world, and spawn and return a building instead where
     * the card was dropped
     * 
     * @param cardNodeX     the x coordinate of the card which was dragged, from 0
     *                      to width-1
     * @param cardNodeY     the y coordinate of the card which was dragged (in
     *                      starter code this is 0 as only 1 row of cards)
     * @param buildingNodeX the x coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to width-1
     * @param buildingNodeY the y coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to height-1
     * @return building entity returned from the world
     */
    private FieldKitchenBuilding convertFieldKitchenCardToBuildingByCoordinates(int cardNodeX, int cardNodeY,
            int buildingNodeX, int buildingNodeY) {
        return world.convertFieldKitchenCardToBuildingByCoordinates(cardNodeX, cardNodeY, buildingNodeX, buildingNodeY);
    }

    /**
     * remove the card from the world, and spawn and return a building instead where
     * the card was dropped
     * 
     * @param cardNodeX     the x coordinate of the card which was dragged, from 0
     *                      to width-1
     * @param cardNodeY     the y coordinate of the card which was dragged (in
     *                      starter code this is 0 as only 1 row of cards)
     * @param buildingNodeX the x coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to width-1
     * @param buildingNodeY the y coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to height-1
     * @return building entity returned from the world
     */
    private HolySpringBuilding convertHolySpringCardToBuildingByCoordinates(int cardNodeX, int cardNodeY,
            int buildingNodeX, int buildingNodeY) {
        return world.convertHolySpringCardToBuildingByCoordinates(cardNodeX, cardNodeY, buildingNodeX, buildingNodeY);
    }

    /**
     * remove an item from the unequipped inventory by its x and y coordinates in
     * the unequipped inventory gridpane
     * 
     * @param nodeX x coordinate from 0 to unequippedInventoryWidth-1
     * @param nodeY y coordinate from 0 to unequippedInventoryHeight-1
     */
    private void removeItemByCoordinates(int nodeX, int nodeY) {
        world.removeUnequippedInventoryItemByCoordinates(nodeX, nodeY);
    }

    /**
     * add drag event handlers to an ImageView
     * 
     * @param view           the view to attach drag event handlers to
     * @param draggableType  the type of item being dragged - card or item
     * @param sourceGridPane the relevant gridpane from which the entity would be
     *                       dragged
     * @param targetGridPane the relevant gridpane to which the entity would be
     *                       dragged to
     */
    private void addDragEventHandlers(ImageView view, DRAGGABLE_TYPE draggableType, GridPane sourceGridPane,
            GridPane targetGridPane) {
        view.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                currentlyDraggedImage = view; // set image currently being dragged, so squares setOnDragEntered can
                                              // detect it...
                currentlyDraggedType = draggableType;
                // Drag was detected, start drap-and-drop gesture
                // Allow any transfer node
                Dragboard db = view.startDragAndDrop(TransferMode.MOVE);

                // Put ImageView on dragboard
                ClipboardContent cbContent = new ClipboardContent();
                cbContent.putImage(view.getImage());
                db.setContent(cbContent);
                view.setVisible(false);

                buildNonEntityDragHandlers(draggableType, sourceGridPane, targetGridPane);

                draggedEntity.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                switch (draggableType) {
                    case VampireCastleCARD:
                        draggedEntity.setImage(vampireCastleCardImage);
                        break;
                    case ZombiePitCARD:
                        draggedEntity.setImage(zombiePitCardImage);
                        break;
                    case TowerCARD:
                        draggedEntity.setImage(towerCardImage);
                        break;
                    case VillageCARD:
                        draggedEntity.setImage(villageCardImage);
                        break;
                    case TrapCARD:
                        draggedEntity.setImage(trapCardImage);
                        break;
                    case CampfireCARD:
                        draggedEntity.setImage(campfireCardImage);
                        break;
                    case BarracksCARD:
                        draggedEntity.setImage(barracksCardImage);
                        break;
                    case Sword:
                        draggedEntity.setImage(swordImage);
                        break;
                    case Stake:
                        draggedEntity.setImage(stakeImage);
                        break;
                    case Staff:
                        draggedEntity.setImage(staffImage);
                        break;
                    case Armour:
                        draggedEntity.setImage(armourImage);
                        break;
                    case Shield:
                        draggedEntity.setImage(shieldImage);
                        break;
                    case Helmet:
                        draggedEntity.setImage(helmetImage);
                        break;
                    case Anduril:
                        draggedEntity.setImage(andurilImage);
                        break;
                    case TreeStump:
                        draggedEntity.setImage(treeStumpImage);
                        break;
                    default:
                        break;
                }

                draggedEntity.setVisible(true);
                draggedEntity.setMouseTransparent(true);
                draggedEntity.toFront();

                // IMPORTANT!!!
                // to be able to remove event handlers, need to use addEventHandler
                // https://stackoverflow.com/a/67283792
                targetGridPane.addEventHandler(DragEvent.DRAG_DROPPED, gridPaneSetOnDragDropped.get(draggableType));
                anchorPaneRoot.addEventHandler(DragEvent.DRAG_OVER, anchorPaneRootSetOnDragOver.get(draggableType));
                anchorPaneRoot.addEventHandler(DragEvent.DRAG_DROPPED,
                        anchorPaneRootSetOnDragDropped.get(draggableType));

                for (Node n : targetGridPane.getChildren()) {
                    // events for entering and exiting are attached to squares children because that
                    // impacts opacity change
                    // these do not affect visibility of original image...
                    // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
                    gridPaneNodeSetOnDragEntered.put(draggableType, new EventHandler<DragEvent>() {
                        public void handle(DragEvent event) {
                            if (currentlyDraggedType == draggableType) {
                                // The drag-and-drop gesture entered the target
                                // show the user that it is an actual gesture target
                                if (event.getGestureSource() != n && event.getDragboard().hasImage()) {
                                    n.setOpacity(0.7);
                                }
                            }
                            event.consume();
                        }
                    });
                    gridPaneNodeSetOnDragExited.put(draggableType, new EventHandler<DragEvent>() {
                        public void handle(DragEvent event) {
                            if (currentlyDraggedType == draggableType) {
                                n.setOpacity(1);
                            }

                            event.consume();
                        }
                    });
                    n.addEventHandler(DragEvent.DRAG_ENTERED, gridPaneNodeSetOnDragEntered.get(draggableType));
                    n.addEventHandler(DragEvent.DRAG_EXITED, gridPaneNodeSetOnDragExited.get(draggableType));
                }
                event.consume();
            }

        });
    }

    /**
     * remove drag event handlers so that we don't process redundant events
     * this is particularly important for slower machines such as over VLAB.
     * 
     * @param draggableType  either cards, or items in unequipped inventory
     * @param targetGridPane the gridpane to remove the drag event handlers from
     */
    private void removeDraggableDragEventHandlers(DRAGGABLE_TYPE draggableType, GridPane targetGridPane) {
        // remove event handlers from nodes in children squares, from anchorPaneRoot,
        // and squares
        targetGridPane.removeEventHandler(DragEvent.DRAG_DROPPED, gridPaneSetOnDragDropped.get(draggableType));

        anchorPaneRoot.removeEventHandler(DragEvent.DRAG_OVER, anchorPaneRootSetOnDragOver.get(draggableType));
        anchorPaneRoot.removeEventHandler(DragEvent.DRAG_DROPPED, anchorPaneRootSetOnDragDropped.get(draggableType));

        for (Node n : targetGridPane.getChildren()) {
            n.removeEventHandler(DragEvent.DRAG_ENTERED, gridPaneNodeSetOnDragEntered.get(draggableType));
            n.removeEventHandler(DragEvent.DRAG_EXITED, gridPaneNodeSetOnDragExited.get(draggableType));
        }
    }

    /**
     * handle the pressing of keyboard keys.
     * Specifically, we should pause when pressing SPACE
     * 
     * @param event some keyboard key press
     */
    @FXML
    public void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case SPACE:
                if (isPaused) {
                    this.output.setText("");
                    startTimer();
                } else {
                    this.output.setText("Paused");
                    pause();
                }
                break;
            case P:
                if (!isPaused) {
                    if (world.consumePotion()) {
                        this.output.setText("Potion consumed!");
                    } else {
                        this.output.setText("You don't have any potions!");
                    }
                }
                break;
            case R:
                world.consumeRation();
                break;
            case O:
                if (this.world.isThisRareItemEnabled(new Anduril(null, null))) {
                    loadAnduril();
                }
                break;
            case I:
                if (this.world.isThisRareItemEnabled(new TreeStump(null, null))) {
                    loadTreeStump();
                }
                break;
            case G:
                world.changeCharacterGold(100);
                break;
            default:
                break;
        }
    }

    /**
     * Sets the main menu
     * 
     * @param mainMenuSwitcher
     */
    public void setMainMenuSwitcher(MenuSwitcher mainMenuSwitcher) {
        this.mainMenuSwitcher = mainMenuSwitcher;
    }

    /**
     * this method is triggered when click button to go to main menu in FXML
     * 
     * @throws IOException
     */
    @FXML
    private void switchToMainMenu() throws IOException {
        pause();
        mainMenuSwitcher.switchMenu();
    }

    /**
     * ADDED CODE
     * Sets the shop menu
     * 
     * @param mainMenuSwitcher
     */
    public void setShopMenuSwitcher(MenuSwitcher shopMenuSwitcher) {
        this.shopMenuSwitcher = shopMenuSwitcher;
    }

    /**
     * ADDED CODE
     * Switches to the shop menu
     */
    private void switchToShopMenu() throws IOException {
        pause();
        this.shopMenuSwitcher.switchMenu();
    }

    /**
     * ADDED CODE
     * Sets the shop menu
     * 
     * @param mainMenuSwitcher
     */
    public void setBesiaryMenuSwitcher(MenuSwitcher bestiaryMenuSwitcher) {
        this.bestiaryMenuSwitcher = bestiaryMenuSwitcher;
    }

    /**
     * ADDED CODE
     * Switches to the shop menu
     */
    private void switchToBestiaryMenu() throws IOException {
        pause();
        this.bestiaryMenuSwitcher.switchMenu();
    }

    /**
     * ADDED CODE
     * Sets the end screen menu
     * 
     * @param mainMenuSwitcher
     */
    public void setEndScreenMenuSwitcher(MenuSwitcher endScreenSwitcher) {
        this.endScreenSwitcher = endScreenSwitcher;
    }

    /**
     * ADDED CODE
     * Switches to the end screen menu
     */
    private void switchToEndScreen() throws IOException {
        pause();
        this.endScreenSwitcher.switchMenu();
    }

    /**
     * Below are for buying and selling items. Returns false if there is not enough
     * gold
     * on the player, true if otherwise. All of these functions require the cost
     * of the item
     * 
     * @param profit
     * @param itemName
     * @return boolean
     */
    public boolean sellItem(StaticEntity item) {
        // Items MUST match the item's .toString() function for this to work
        // One ring should be "the_one_ring"
        int profit = world.getSellPrice(item);
        if (world.removeUnequippedInventoryItemByClassName(item.toString())) {
            printThreadingNotes("Sold " + item + " and gained profit " + profit);
            world.changeCharacterGold(profit);
            return true;
        }
        printThreadingNotes("Did not sell " + item);
        return false;
    }

    /**
     * Buys the item. Returns true if success, false if otherwise
     * 
     * @param itemName
     * @return boolean
     */
    public boolean buyItem(String itemName) {
        if (world.makePurchase(itemName.toLowerCase())) {
            printThreadingNotes("Purchasing " + itemName);
            switch (itemName.toLowerCase()) {
                case "sword":
                    loadSword();
                    break;
                case "stake":
                    loadStake();
                    break;
                case "staff":
                    loadStaff();
                    break;
                case "armour":
                    loadArmour();
                    break;
                case "shield":
                    loadShield();
                    break;
                case "helmet":
                    loadHelmet();
                    break;
                case "potion":
                    loadPotion();
                    break;
                case "attack":
                    world.buyAttackIncrease();
                    break;
                case "health":
                    world.buyMaxHPIncrease();
                    break;
                default:
                    return false;
            }
            return true;
        }
        printThreadingNotes("Failed purchase of " + itemName);
        return false;
    }

    /**
     * Set a node in a GridPane to have its position track the position of an
     * entity in the world.
     *
     * By connecting the model with the view in this way, the model requires no
     * knowledge of the view and changes to the position of entities in the
     * model will automatically be reflected in the view.
     * 
     * note that this is put in the controller rather than the loader because we
     * need to track positions of spawned entities such as enemy
     * or items which might need to be removed should be tracked here
     * 
     * NOTE teardown functions setup here also remove nodes from their GridPane. So
     * it is vital this is handled in this Controller class
     * 
     * @param entity
     * @param node
     */
    private void trackPosition(Entity entity, Node node) {
        GridPane.setColumnIndex(node, entity.getX());
        GridPane.setRowIndex(node, entity.getY());

        ChangeListener<Number> xListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setColumnIndex(node, newValue.intValue());
            }
        };
        ChangeListener<Number> yListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setRowIndex(node, newValue.intValue());
            }
        };

        // if need to remove items from the equipped inventory, add code to remove from
        // equipped inventory gridpane in the .onDetach part
        ListenerHandle handleX = ListenerHandles.createFor(entity.x(), node)
                .onAttach((o, l) -> o.addListener(xListener))
                .onDetach((o, l) -> {
                    o.removeListener(xListener);
                    entityImages.remove(node);
                    squares.getChildren().remove(node);
                    cards.getChildren().remove(node);
                    equippedItems.getChildren().remove(node);
                    unequippedInventory.getChildren().remove(node);
                })
                .buildAttached();
        ListenerHandle handleY = ListenerHandles.createFor(entity.y(), node)
                .onAttach((o, l) -> o.addListener(yListener))
                .onDetach((o, l) -> {
                    o.removeListener(yListener);
                    entityImages.remove(node);
                    squares.getChildren().remove(node);
                    cards.getChildren().remove(node);
                    equippedItems.getChildren().remove(node);
                    unequippedInventory.getChildren().remove(node);
                })
                .buildAttached();
        handleX.attach();
        handleY.attach();

        // this means that if we change boolean property in an entity tracked from here,
        // position will stop being tracked
        // this wont work on character/path entities loaded from loader classes
        entity.shouldExist().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obervable, Boolean oldValue, Boolean newValue) {
                handleX.detach();
                handleY.detach();
            }
        });
    }

    /**
     * we added this method to help with debugging so you could check your code is
     * running on the application thread.
     * By running everything on the application thread, you will not need to worry
     * about implementing locks, which is outside the scope of the course.
     * Always writing code running on the application thread will make the project
     * easier, as long as you are not running time-consuming tasks.
     * We recommend only running code on the application thread, by using Timelines
     * when you want to run multiple processes at once.
     * EventHandlers will run on the application thread.
     */
    private void printThreadingNotes(String currentMethodLabel) {
        System.out.println("\n###########################################");
        System.out.println("current method = " + currentMethodLabel);
        System.out.println("In application thread? = " + Platform.isFxApplicationThread());
        System.out.println("Current system time = " + java.time.LocalDateTime.now().toString().replace('T', ' '));
        // ADDED CODE: Prints useful debugging information
        System.out.println("Win Condition = " + world.printGoal());
        System.out.println("Character health = " + world.getCharacterHealthProp().getValue() + "/"
                + world.getCharacterMaxHealth());
        System.out.println("Character cycles = " + this.currentCycle);
        System.out.println("Next shop cycle = " + this.nextCycleShop);
    }
}
