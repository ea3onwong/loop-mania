package unsw.loopmania.Items.DefendStrategies;

import java.util.Random;

import unsw.loopmania.Enemy.BasicEnemy;

/**
 * Added by Eason and Adam.
 * This is a class that represents the shield's behaviour.
 */
public class ShieldDefendStrategy implements DefendStrategy {

    public int defend(BasicEnemy enemy, int damage) {
        
        Random rdm = new Random();

        // critical vampire attacks have a 60% lower chance of occurring (but still have normal attacks)
        if (enemy.getType().equals("Vampire") && damage == 20 && rdm.nextInt(10) < 6) {
            return 10;
        }
        return damage;
    }
    
    @Override
    public String toString() {
        return "shield";
    }
}
