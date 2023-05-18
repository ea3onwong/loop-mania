package unsw.loopmania.Items.DefendStrategies;

import unsw.loopmania.Enemy.BasicEnemy;

/**
 * Added by Eason
 * This is a class that represents the armour's behaviour. 
 */
public class ArmourDefendStrategy implements DefendStrategy{

    public int defend(BasicEnemy enemy, int damage) {
        return damage/2;
    }

    @Override
    public String toString() {
        return "armour";
    }
}
