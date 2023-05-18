/**
 * Written by z5309206 Gordon Wang
 */
package unsw.loopmania.Bestiary;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import unsw.loopmania.MenuSwitcher;

public class BestiaryController {

    private MenuSwitcher gameSwitcher;

    @FXML
    private Text textBox;

    @FXML
    private Button slimeButton;

    @FXML
    private Button zombieButton;

    @FXML
    private Button thiefButton;

    @FXML
    private Button vampireButton;

    @FXML
    private Button elanMuskeButton;

    @FXML
    private Button doggieButton;

    @FXML
    private Button exitButton;

    /**
     * Switching back to the main game
     * @param event
     */
    @FXML
    private void switchMenu(ActionEvent event) {
        try {
            switchToGame();
        } catch (Exception e) {
            System.out.println("ERROR SWITCHING MENU");
            System.exit(-1);
        }
        
    }

    /**
     * Display Doggie description
     * @param event
     */
    @FXML
    private void viewDoggie(ActionEvent event) {
        String text = "";
        text += "This is the Much Wow Doggie! It will pounce all across the map very quickly. ";
        text += "When a Doggie is around, it will bless the Doggie Coin Market, boosting sales.\n\n";
        text += "If you were to defeat one, which I don't know why you would, you'll be blessed with a Doggie Coin. ";
        text += "You can sell these at the shop for varying prices! Make sure to hold onto them until the prices are high!\n\n";
        text += "Watch out however, these Doggies attacks are so cute, it might stun you from making a turn. ";
        text += "They also seem to only want to play after you've made a few cycles around the map.";
        this.textBox.setText(text);
    }

    /**
     * Display Elan description
     * @param event
     */
    @FXML
    private void viewElan(ActionEvent event) {
        String text = "";
        text += "Our master, lord and saviour, Elan Muske! How do you not know him?\n\n";
        text += "He is a Doggie-trepreneur who founded SpoceD. I heard he visits the town whenever a worthy opponent is around... ";
        text += "When he is in town, his mere presence will sky rocket the Doggie Coin Market! However, his defeat will plummet the Market! ";
        text += "His attack damage is also based on how well the Market is doing. If it's doing well, he might OHKO you! ";
        text += "If it's doing poorly, he might even heal you... ";
        text += "He also can heal all other enemies that is within his sight... be careful!\n\n";
        text += "Defeating him will grant you up to FIVE Doggie Coins! That's like, worth more than me! Papa Elan, sign my paw!!";
        this.textBox.setText(text);
    }

    /**
     * Display Slug description
     * @param event
     */
    @FXML
    private void viewSlug(ActionEvent event) {
        String text = "";
        text += "Is it a Slug? Is it a Slime? Who knows! Me? Well, you're right. It is a Slug! I think...\n\n";
        text += "Slugs spawn randomly in the overworld. Legend has it that only two can be seen at once! Crazy right? ";
        text += "They barely do any damage, however if you seek for more challenge, perhaps a certain building card ";
        text += "that slimes can drop may revive the dead...";
        this.textBox.setText(text);
    }

    /**
     * Display Thief description
     * @param event
     */
    @FXML
    private void viewThief(ActionEvent event) {
        String text = "";
        text += "With every good boi, there's a bad boi. This filthy thief is no exception.\n\n";
        text += "Once it smells the gold in your pocket, it will ambush you and steal half your gold! ";
        text += "It will then attempt to run away from you quickly. Perhaps a certain building card can stop it in its tracks... ";
        text += "If you catch up to it, it won't damage you, but it will steal some of your ITEMS! ";
        text += "Defeat it quick so it has less time to steal more items!! I've lost my pup's savings cause of these thieves... \n\n";
        text += "Once it is defeated, you will get all the gold it has stolen back. However, a thief hides its items well, ";
        text += "so you won't be getting those back. Much unfortunate!";
        this.textBox.setText(text);
    }

    /**
     * Display Vampire description
     * @param event
     */
    @FXML
    private void viewVampire(ActionEvent event) {
        String text = "";
        text += "You thought it was a friendly, but it something else... a Vampire!\n\n";
        text += "Vampires emerge from their castles every five cycles and are immune to sunlight!";
        text += "However, they are not immune to weapons, specifically the stake! ";
        text += "Be careful, Vampire's have a lot of health and has a chance to deal a lethal bite, ";
        text += "but if you're well equipped, you'll be fine!\n\n";
        text += "Oh, I heard that Vampire's hate fire. Perhaps you can use this to your advantage?";
        this.textBox.setText(text);
    }

    /**
     * Display Zombie description
     * @param event
     */
    @FXML
    private void viewZombie(ActionEvent event) {
        String text = "";
        text += "There's a Zombie on our path! Where did it come from?\n\n";
        text += "Well, Zombie's emerge from a Zombie Pit and move incredibly slow.";
        text += "If you have any allies in your party, a Zombie bite might turn them against you. ";
        text += "For the undead, Zombie drop pretty useful loot that you can sell off or equip. ";
        text += "If you think Zombies are too easy, maybe a certain blood-thirsty card from them will give you a better challenge...";
        this.textBox.setText(text);
    }

    /**
     * Sets the MenuSwitcher
     * @param gameSwitcher
     */
    public void setGameSwitcher(MenuSwitcher gameSwitcher){
        this.gameSwitcher = gameSwitcher;
    }

    /**
     * Goes back to the main game
     * @throws IOException
     */
    @FXML
    private void switchToGame() throws IOException {
        gameSwitcher.switchMenu();
        this.textBox.setText("Welcome back! What do you need help with?");
    }

}
