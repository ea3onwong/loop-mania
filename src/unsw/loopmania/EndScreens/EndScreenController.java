/**
 * Written by z5309206 Gordon Wang
 */
package unsw.loopmania.EndScreens;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import unsw.loopmania.MenuSwitcher;

public class EndScreenController {

    @FXML
    private ImageView victoryGraphic;

    @FXML
    private ImageView defeatGraphic;

    @FXML
    private Text expDisplay;

    @FXML
    private Text goldDisplay;

    @FXML
    private Text cycleDisplay;

    private MenuSwitcher backToMainMenu;

    public void setGameSwitcher(MenuSwitcher screenSwitcher){
        this.backToMainMenu = screenSwitcher;
    }

    /**
     * Sets what is displayed on exp display with the given finalExp
     * @param finalExp
     */
    public void setExp(int finalExp) {
        expDisplay.setText(String.valueOf(finalExp));
    }

    /**
     * Sets what is displayed on gold display with the given finalGold
     * @param finalGold
     */
    public void setGold(int finalGold) {
        goldDisplay.setText(String.valueOf(finalGold));
    }

    /**
     * Sets what is displayed on cycle display with the given finalCycle
     * @param finalCycle
     */
    public void setCycle(int finalCycle) {
        cycleDisplay.setText(String.valueOf(finalCycle));
    }

    /**
     * Changes what picture is shown depending on if the game
     * was victorious or not
     * @param wasGameVictorious
     */
    public void setBackground(boolean wasGameVictorious) {
        if (wasGameVictorious) {
            this.victoryGraphic.setOpacity(1);
            this.defeatGraphic.setOpacity(0);
        } else {
            this.victoryGraphic.setOpacity(0);
            this.defeatGraphic.setOpacity(1);
        }
    }

    @FXML
    public void backToMainMenu(ActionEvent event) throws IOException {
        backToMainMenu.switchMenu();
    }
    

}
