package unsw.loopmania;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import unsw.loopmania.Gamemodes.GamemodeBerserker;
import unsw.loopmania.Gamemodes.GamemodeConfusing;
import unsw.loopmania.Gamemodes.GamemodeStandard;
import unsw.loopmania.Gamemodes.GamemodeSurvival;
import unsw.loopmania.Shopping.ShoppingController;

/**
 * controller for the main menu.
 */
public class MainMenuController {
    /**
     * facilitates switching to main game
     */
    private MenuSwitcher gameSwitcher;

    private LoopManiaWorldController worldController;

    private ShoppingController shopController;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private Button startGameButton;

    @FXML
    private Button startGameBerserker;

    @FXML
    private Button startGameSurvival;

    @FXML
    private Button startGameConfusing;

    @FXML
    private Button changeToDifficulty;

    @FXML
    public void changeBackgroundImage(ActionEvent event) {

        backgroundImage.setOpacity(0);
        changeToDifficulty.setDisable(true);
        startGameButton.setDisable(false);
        startGameBerserker.setDisable(false);
        startGameSurvival.setDisable(false);
        startGameConfusing.setDisable(false);
    }

    public void setWorldController(LoopManiaWorldController worldController) {
        this.worldController = worldController;
    }

    public void setShoppingController(ShoppingController shopController) {
        this.shopController = shopController;
    }

    public void setGameSwitcher(MenuSwitcher gameSwitcher) {
        this.gameSwitcher = gameSwitcher;
    }

    /**
     * facilitates switching to main game upon button click
     * 
     * @throws IOException
     */
    @FXML
    private void switchToGame() throws IOException {
        worldController.setGamemode(new GamemodeStandard());
        shopController.setGamemode(new GamemodeStandard());
        gameSwitcher.switchMenu();
    }

    /**
     * facilitates switching to main game upon button click
     * 
     * @throws IOException
     */
    @FXML
    private void switchToGameSurvival() throws IOException {
        worldController.setGamemode(new GamemodeSurvival());
        shopController.setGamemode(new GamemodeSurvival());
        gameSwitcher.switchMenu();
    }

    /**
     * facilitates switching to main game upon button click
     * 
     * @throws IOException
     */
    @FXML
    private void switchToGameBerserker() throws IOException {
        worldController.setGamemode(new GamemodeBerserker());
        shopController.setGamemode(new GamemodeBerserker());
        gameSwitcher.switchMenu();
    }

    /**
     * facilitates switching to main game upon button click
     * 
     * @throws IOException
     */
    @FXML
    private void switchToGameConfusing() throws IOException {
        worldController.setGamemode(new GamemodeConfusing());
        shopController.setGamemode(new GamemodeConfusing());
        gameSwitcher.switchMenu();
    }

    @FXML
    private void leaveMenu() throws IOException {
        System.exit(0);
    }
}
