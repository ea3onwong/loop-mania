package unsw.loopmania.Gamemodes;

import unsw.loopmania.Character;

public class GamemodeSurvival extends GamemodeStandard {

    private int totalPotionPurchase = 0;


    @Override
    public boolean buyPotion(Character character) {
        
        int currentTotalGold = character.getPropertyGold().getValue();

        if (super.buyPotion(character) && totalPotionPurchase == 0) {
            totalPotionPurchase++;
            return true;
        } else {
            if (currentTotalGold != character.getPropertyGold().getValue()) {
                // If the money has been taken, then return it
                character.setGold(currentTotalGold);
            }
            return false;
        }
    }

    @Override
    public void resetPurchases() {
        this.totalPotionPurchase = 0;
    }
    
}
