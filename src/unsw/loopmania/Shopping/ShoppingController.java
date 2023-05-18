/**
 * Written by z5309206 Gordon Wang
 */
package unsw.loopmania.Shopping;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.LoopManiaWorldController;
import unsw.loopmania.MenuSwitcher;
import unsw.loopmania.Gamemodes.*;
import unsw.loopmania.Items.*;

/**
 * controller for the shopping window menu.
 */
public class ShoppingController {

    // Background images of the shop
    @FXML
    private ImageView berserkerBG;
    @FXML
    private ImageView survivalBG;
    @FXML
    private ImageView confusingBG;
    @FXML
    private ImageView standardBG;

    @FXML
    private Button swordButton;
    @FXML
    private Button stakeButton;
    @FXML
    private Button staffButton;
    @FXML
    private Button ArmorButton;
    @FXML
    private Button HelmetButton;
    @FXML
    private Button ShieldButton;
    @FXML
    private Button potionButton;
    @FXML
    private Button sellOneRingButton;

    private MenuSwitcher gameSwitcher;

    private LoopManiaWorld world;

    private LoopManiaWorldController worldController;

    @FXML
    private Button startGameButton;

    @FXML
    private GridPane unequippedInventory;

    @FXML
    private Text goldCounter;

    @FXML
    private Text outputText;

    @FXML
    private Text gamemodeText;

    @FXML
    private Text doggieCoinCounter;

    @FXML
    private Button sellDoggieCoin;

    @FXML
    private Text sellPrice;

    private final String SWORD_STRING = "sword";
    private final String STAKE_STRING = "stake";
    private final String STAFF_STRING = "staff";
    private final String ARMOUR_STRING = "armour";
    private final String SHIELD_STRING = "shield";
    private final String HELMET_STRING = "helmet";
    private final String POTION_STRING = "potion";
    private final String ATTACK_STRING = "attack";
    private final String HEALTH_STRING = "health";

    private boolean doggieCoinConfirmation = false;

    public void setGameSwitcher(MenuSwitcher gameSwitcher) {
        this.gameSwitcher = gameSwitcher;
    }

    public void setGamemode(GamemodeState gamemode) {
        disableAllBackground();
        if (gamemode instanceof GamemodeSurvival) {
            survivalBG.setOpacity(1);
            this.gamemodeText.setText("SURVIVAL");
        } else if (gamemode instanceof GamemodeBerserker) {
            berserkerBG.setOpacity(1);
            this.gamemodeText.setText("BERSERKER");
        } else if (gamemode instanceof GamemodeConfusing) {
            confusingBG.setOpacity(1);
            this.gamemodeText.setText("cOnFUsINg");
        } else {
            standardBG.setOpacity(1);
            this.gamemodeText.setText("STANDARD");
        }
    }

    /**
     * Disables all background images of the shopping interface
     */
    private void disableAllBackground() {
        berserkerBG.setOpacity(0);
        standardBG.setOpacity(0);
        confusingBG.setOpacity(0);
        survivalBG.setOpacity(0);
    }

    /**
     * Goes back to the main game
     * 
     * @throws IOException
     */
    @FXML
    private void switchToGame() throws IOException {
        gameSwitcher.switchMenu();
        outputReset();
        this.doggieCoinConfirmation = false;
        this.world.resetPurchases();
    }

    /**
     * Lets the shopping controller interact with the controller and the world
     * 
     * @param world
     * @param worldCon
     */
    public void setWorld(LoopManiaWorld world, LoopManiaWorldController worldCon) {
        this.world = world;
        this.worldController = worldCon;
    }

    /**
     * Binds the world's chara
     * cter gold, doggie coin and doggie sell price to the shopping controller
     * Also changes text color
     */
    public void bindGold() {
        this.goldCounter.textProperty().bind(world.getCharacterGoldProp().asString());
        this.sellPrice.textProperty().bind(world.getDoggieSellPriceProp().asString());
        this.doggieCoinCounter.textProperty().bind(world.getCharacterDoggieCoinProp().asString());
        this.goldCounter.setFill(Color.WHITE);
        this.sellPrice.setFill(Color.WHITE);
        this.doggieCoinCounter.setFill(Color.WHITE);
    }

    /**
     * SELLING AND BUYING BUTTON CODES ARE BELOW
     */

    /**
     * Selling Doggie coin with confirmation
     * 
     * @param event
     */
    @FXML
    private void sellDoggieCoin(ActionEvent event) {
        String itemName = "Doggie Coin";
        if (doggieCoinConfirmation) {
            if (world.sellDoggieCoin()) {
                outputApproveSell(itemName);
            } else {
                outputDisapproveSell(itemName);
            }
        } else {
            String outputString = "A Doggie Coin sells for ";
            outputString += this.sellPrice.getText();
            outputString += " coins. Press \"SELL DOGGIECOIN\" if you are sure to make the transaction.";
            this.outputText.setText(outputString);
            this.doggieCoinConfirmation = true;
        }

    }

    /**
     * Sells the one ring
     * 
     * @param event
     */
    @FXML
    private void sellOneRing(ActionEvent event) {
        String itemName = "Ring";
        if (!this.worldController.sellItem(new TheOneRing(null, null))) {
            outputDisapproveSell(itemName);
        } else {
            outputApproveSell(itemName);
        }
    }

    /**
     * Sells Andruril
     * 
     * @param event
     */
    @FXML
    private void sellAndruril(ActionEvent event) {
        String itemName = "Anduril";
        if (!this.worldController.sellItem(new Anduril(null, null))) {
            outputDisapproveSell(itemName);
        } else {
            outputApproveSell(itemName);
        }
    }

    /**
     * Sells Tree Stump
     * 
     * @param event
     */
    @FXML
    private void sellTreeStump(ActionEvent event) {
        String itemName = "Tree Stump";
        if (!this.worldController.sellItem(new TreeStump(null, null))) {
            outputDisapproveSell(itemName);
        } else {
            outputApproveSell(itemName);
        }
    }

    /**
     * Buying and selling sword
     * 
     * @param event
     */
    @FXML
    private void buySword(ActionEvent event) {
        if (this.world.isInventoryFull()) {
            outputDisapproveFull();
            return;
        }
        if (!this.worldController.buyItem(SWORD_STRING)) {
            outputDisapproveBuy();
        } else {
            outputApproveBuy();
        }
    }

    @FXML
    private void sellSword(ActionEvent event) {
        String itemName = "Sword";
        if (!this.worldController.sellItem(new Sword(null, null))) {
            outputDisapproveSell(itemName);
        } else {
            outputApproveSell(itemName);
        }
    }

    /**
     * Buying and selling stake
     * 
     * @param event
     */
    @FXML
    private void buyStake(ActionEvent event) {
        if (this.world.isInventoryFull()) {
            outputDisapproveFull();
            return;
        }
        if (!this.worldController.buyItem(STAKE_STRING)) {
            outputDisapproveBuy();
        } else {
            outputApproveBuy();
        }
    }

    @FXML
    private void sellStake(ActionEvent event) {
        String itemName = "Stake";
        if (!this.worldController.sellItem(new Stake(null, null))) {
            outputDisapproveSell(itemName);
        } else {
            outputApproveSell(itemName);
        }
    }

    /**
     * Buying and selling staff
     * 
     * @param event
     */
    @FXML
    private void buyStaff(ActionEvent event) {
        if (this.world.isInventoryFull()) {
            outputDisapproveFull();
            return;
        }
        if (!this.worldController.buyItem(STAFF_STRING)) {
            outputDisapproveBuy();
        } else {
            outputApproveBuy();
        }
    }

    @FXML
    private void sellStaff(ActionEvent event) {
        String itemName = "Staff";
        if (!this.worldController.sellItem(new Staff(null, null))) {
            outputDisapproveSell(itemName);
        } else {
            outputApproveSell(itemName);
        }
    }

    /**
     * Buying and selling armor
     * 
     * @param event
     */
    @FXML
    private void buyArmor(ActionEvent event) {
        if (this.world.isInventoryFull()) {
            outputDisapproveFull();
            return;
        }
        if (!this.worldController.buyItem(ARMOUR_STRING)) {
            outputDisapproveBuy();
        } else {
            outputApproveBuy();
        }
    }

    @FXML
    private void sellArmor(ActionEvent event) {
        String itemName = "Armour";
        if (!this.worldController.sellItem(new Armour(null, null))) {
            outputDisapproveSell(itemName);
        } else {
            outputApproveSell(itemName);
        }
    }

    /**
     * Buying and selling shield
     * 
     * @param event
     */
    @FXML
    private void buyShield(ActionEvent event) {
        if (this.world.isInventoryFull()) {
            outputDisapproveFull();
            return;
        }
        if (!this.worldController.buyItem(SHIELD_STRING)) {
            outputDisapproveBuy();
        } else {
            outputApproveBuy();
        }
    }

    @FXML
    private void sellShield(ActionEvent event) {
        String itemName = "Shield";
        if (!this.worldController.sellItem(new Shield(null, null))) {
            outputDisapproveSell(itemName);
        } else {
            outputApproveSell(itemName);
        }
    }

    /**
     * BUYING AND SELLING HELMET
     * 
     * @param event
     */
    @FXML
    private void buyHelmet(ActionEvent event) {
        if (this.world.isInventoryFull()) {
            outputDisapproveFull();
            return;
        }
        if (!this.worldController.buyItem(HELMET_STRING)) {
            outputDisapproveBuy();
        } else {
            outputApproveBuy();
        }
    }

    @FXML
    private void sellHelmet(ActionEvent event) {
        String itemName = "Helmet";
        if (!this.worldController.sellItem(new Helmet(null, null))) {
            outputDisapproveSell(itemName);
        } else {
            outputApproveSell(itemName);
        }
    }

    /**
     * Buying and selling potion
     * 
     * @param event
     */
    @FXML
    private void buyPotion(ActionEvent event) {
        if (this.world.isInventoryFull()) {
            outputDisapproveFull();
            return;
        }
        if (!this.worldController.buyItem(POTION_STRING)) {
            outputDisapproveBuy();
        } else {
            outputApproveBuy();
        }
    }

    @FXML
    private void sellPotion(ActionEvent event) {
        String itemName = "Potion";
        if (!this.worldController.sellItem(new Potion(null, null))) {
            outputDisapproveSell(itemName);
        } else {
            outputApproveSell(itemName);
        }
    }

    /**
     * Buying attack damage increase
     * 
     * @param event
     */
    @FXML
    private void buyAttack(ActionEvent event) {
        if (!this.worldController.buyItem(ATTACK_STRING)) {
            outputDisapproveBuy();
        } else {
            outputApproveBuy();
        }
    }

    /**
     * Buying max health increase
     * 
     * @param event
     */
    @FXML
    private void buyHealth(ActionEvent event) {
        if (!this.worldController.buyItem(HEALTH_STRING)) {
            outputDisapproveBuy();
        } else {
            outputApproveBuy();
        }
    }

    /**
     * Feedback to the player when purchasing or selling items
     */

    private void outputApproveBuy() {
        outputText.setText("Successful purchase!");
    }

    private void outputDisapproveFull() {
        outputText.setText("You can't carry anymore items!");
    }

    private void outputDisapproveBuy() {
        outputText.setText("No can do mate!");
    }

    private void outputDisapproveSell(String itemName) {
        String indefArticle = "a";
        if (itemName.matches("(?i)[aeiou].*")) {
            indefArticle += "n";
        }
        outputText.setText("You don't have " + indefArticle + " " + itemName + " to sell!");
    }

    private void outputApproveSell(String itemName) {
        outputText.setText("Your " + itemName + " will find a nice new home with me!");
    }

    private void outputReset() {
        outputText.setText("Welcome back, Hero!");
    }

}
