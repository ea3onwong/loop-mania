package unsw.loopmania.Gamemodes;

import unsw.loopmania.Character;

public class GamemodeBerserker extends GamemodeStandard {

    private int totalArmourPurchase = 0;


    @Override
    public boolean buyArmour(Character character) {
        int currentTotalGold = character.getPropertyGold().getValue();

        if (super.buyArmour(character) && totalArmourPurchase == 0) {
            totalArmourPurchase++;
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
    public boolean buyShield(Character character) {
        int currentTotalGold = character.getPropertyGold().getValue();

        if (super.buyShield(character) && totalArmourPurchase == 0) {
            totalArmourPurchase++;
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
    public boolean buyHelmet(Character character) {
        int currentTotalGold = character.getPropertyGold().getValue();

        if (super.buyHelmet(character) && totalArmourPurchase == 0) {
            totalArmourPurchase++;
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
        this.totalArmourPurchase = 0;
    }
    
}
