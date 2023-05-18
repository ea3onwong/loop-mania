package unsw.loopmania;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import unsw.loopmania.Bestiary.BestiaryController;
import unsw.loopmania.EndScreens.EndScreenController;
import unsw.loopmania.Shopping.ShoppingController;

/**
 * the main application
 * run main method from this class
 */
public class LoopManiaApplication extends Application {

    /**
     * the controller for the game. Stored as a field so can terminate it when click
     * exit button
     */
    private LoopManiaWorldController mainController;

    @Override
    public void start(Stage primaryStage) throws IOException {
        // set title on top of window bar
        primaryStage.setTitle("Loop Mania");

        // prevent human player resizing game window (since otherwise would see white
        // space)
        // alternatively, you could allow rescaling of the game (you'd have to program
        // resizing of the JavaFX nodes)
        primaryStage.setResizable(false);

        // load the main game
        LoopManiaWorldControllerLoader loopManiaLoader = new LoopManiaWorldControllerLoader(
                "world_with_twists_and_turns.json");
        // UNCOMMENT ME FOR PREBUILT SPAWNERS
        // LoopManiaWorldControllerLoader loopManiaLoader = new
        // LoopManiaWorldControllerLoader("world_with_twists_and_turns_and_spawners.json");
        mainController = loopManiaLoader.loadController();
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("LoopManiaView.fxml"));
        gameLoader.setController(mainController);
        Parent gameRoot = gameLoader.load();

        // load the main menu
        MainMenuController mainMenuController = new MainMenuController();
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("MainMenuView.fxml"));
        menuLoader.setController(mainMenuController);
        Parent mainMenuRoot = menuLoader.load();

        // load the shopping menu
        ShoppingController shopController = new ShoppingController();
        FXMLLoader shoppingLoader = new FXMLLoader(getClass().getResource("Shopping/ShoppingView.fxml"));
        shoppingLoader.setController(shopController);
        Parent shoppingWindow = shoppingLoader.load();

        mainController.setShoppingController(shopController);

        // load the gameover screens
        EndScreenController endScreenController = new EndScreenController();
        FXMLLoader endScreenLoader = new FXMLLoader(getClass().getResource("EndScreens/EndScreenView.fxml"));
        endScreenLoader.setController(endScreenController);
        Parent endScreenRoot = endScreenLoader.load();

        // load the bestiary screen
        BestiaryController bestiaryController = new BestiaryController();
        FXMLLoader bestiaryLoader = new FXMLLoader(getClass().getResource("Bestiary/BestiaryView.fxml"));
        bestiaryLoader.setController(bestiaryController);
        Parent bestiaryRoot = bestiaryLoader.load();

        // create new scene with the main menu (so we start with the main menu)
        Scene scene = new Scene(mainMenuRoot);

        // set functions which are activated when button click to switch menu is pressed
        // e.g. from main menu to start the game, or from the game to return to main
        // menu
        mainController.setMainMenuSwitcher(() -> {
            switchToRoot(scene, mainMenuRoot, primaryStage);
        });
        mainMenuController.setGameSwitcher(() -> {
            switchToRoot(scene, gameRoot, primaryStage);
            mainController.resetGame();
            mainController.startTimer();
        });
        // Adds the shop window to the main game window
        mainController.setShopMenuSwitcher(() -> {
            switchToRoot(scene, shoppingWindow, primaryStage);
            shopController.bindGold();
        });
        // Adds the bestiary window to the main game window
        mainController.setBesiaryMenuSwitcher(() -> {
            switchToRoot(scene, bestiaryRoot, primaryStage);
            shopController.bindGold();
        });
        // Adds the bestiary window to the main game window
        bestiaryController.setGameSwitcher(() -> {
            switchToRoot(scene, gameRoot, primaryStage);
            mainController.startTimer();
        });
        // Adds switcher to change from shopping window to main game
        shopController.setGameSwitcher(() -> {
            switchToRoot(scene, gameRoot, primaryStage);
            mainController.startTimer();
        });
        // Adds switcher to change from main game to end screen
        mainController.setEndScreenMenuSwitcher(() -> {
            switchToRoot(scene, endScreenRoot, primaryStage);
        });
        // Adds switcher to change from end screen to main emnu
        endScreenController.setGameSwitcher(() -> {
            switchToRoot(scene, mainMenuRoot, primaryStage);
        });
        mainController.setShoppingController(shopController);
        mainMenuController.setWorldController(mainController);
        mainMenuController.setShoppingController(shopController);
        mainController.setEndScreenController(endScreenController);
        // deploy the main onto the stage
        gameRoot.requestFocus();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        // wrap up activities when exit program
        mainController.terminate();
    }

    /**
     * switch to a different Root
     */
    private void switchToRoot(Scene scene, Parent root, Stage stage) {
        scene.setRoot(root);
        root.requestFocus();
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    // Play music automatically when the game starts.
    public static void playMusic(String musicName)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        // UNCOMMENT IF YOU AREN'T USING VLAB
        File file = new File("src/music/" + musicName);
        AudioInputStream audio = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audio);
        clip.start();

    }

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        launch(args);
    }
}
