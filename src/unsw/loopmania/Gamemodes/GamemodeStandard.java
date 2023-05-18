package unsw.loopmania.Gamemodes;

import unsw.loopmania.Character;
import unsw.loopmania.StaticEntity;
import unsw.loopmania.Items.*;

public class GamemodeStandard implements GamemodeState {

    private final int SWORD_PRICE = 5;
    private final int STAKE_PRICE = 10;
    private final int STAFF_PRICE = 15;
    private final int ARMOUR_PRICE = 10;
    private final int SHIELD_PRICE = 10;
    private final int HELMET_PRICE = 20;
    private final int POTION_PRICE = 10;
    private final int RING_PRICE = 100;
    private final int ANDURIL_PRICE = 200;
    private final int TREESTUMP_PRICE = 200;
    private final int ATTACK_PRICE = 25;
    private final int HEALTH_PRICE = 25;
    private final int RESELL_DIVISOR = 5;

    @Override
    public boolean buySword(Character character) {
        int currentTotalGold = character.getPropertyGold().getValue();

        if (currentTotalGold >= SWORD_PRICE) {
            character.changeGold(-SWORD_PRICE);
            return true;
        } else {
            return false;
        }   
    }

    @Override
    public boolean buyStake(Character character) {
        int currentTotalGold = character.getPropertyGold().getValue();

        if (currentTotalGold >= STAKE_PRICE) {
            character.changeGold(-STAKE_PRICE);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean buyStaff(Character character) {
        int currentTotalGold = character.getPropertyGold().getValue();

        if (currentTotalGold >= STAFF_PRICE) {
            character.changeGold(-STAFF_PRICE);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean buyArmour(Character character) {
        int currentTotalGold = character.getPropertyGold().getValue();

        if (currentTotalGold >= ARMOUR_PRICE) {
            character.changeGold(-ARMOUR_PRICE);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean buyShield(Character character) {
        int currentTotalGold = character.getPropertyGold().getValue();

        if (currentTotalGold >= SHIELD_PRICE) {
            character.changeGold(-SHIELD_PRICE);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean buyHelmet(Character character) {
        int currentTotalGold = character.getPropertyGold().getValue();

        if (currentTotalGold >= HELMET_PRICE) {
            character.changeGold(-HELMET_PRICE);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean buyPotion(Character character) {
        int currentTotalGold = character.getPropertyGold().getValue();

        if (currentTotalGold >= POTION_PRICE) {
            character.changeGold(-POTION_PRICE);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean buyAttack(Character character) {
        int currentTotalGold = character.getPropertyGold().getValue();

        if (currentTotalGold >= ATTACK_PRICE) {
            character.changeGold(-ATTACK_PRICE);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean buyHealth(Character character) {
        int currentTotalGold = character.getPropertyGold().getValue();

        if (currentTotalGold >= HEALTH_PRICE) {
            character.changeGold(-HEALTH_PRICE);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getPriceOfItem(StaticEntity item, boolean applySell) {
        int divisor = RESELL_DIVISOR;
        if (!applySell) {
            divisor = 1;
        }

        if (item instanceof Sword) {
            return SWORD_PRICE / divisor;
        } else if (item instanceof Stake) {
            return STAKE_PRICE / divisor;
        } else if (item instanceof Staff) {
            return STAFF_PRICE / divisor;
        } else if (item instanceof Armour) {
            return ARMOUR_PRICE / divisor;
        } else if (item instanceof Shield) {
            return ARMOUR_PRICE / divisor;
        } else if (item instanceof Helmet) {
            return ARMOUR_PRICE / divisor;
        } else if (item instanceof Potion) {
            return POTION_PRICE / divisor;
        } else if (item instanceof TheOneRing) {
            return RING_PRICE;
        } else if (item instanceof Anduril) {
            return ANDURIL_PRICE;
        } else if (item instanceof TreeStump) {
            return TREESTUMP_PRICE;
        }
        return 0;
    }

    @Override
    public void resetPurchases() {
        // Does nothing as standard does not store any dynamic data
        return;
    }

    @Override
    public boolean randomiseProperties() {
        return false;
    }
    
}
