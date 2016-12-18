package Pokemon;
//Attack.java

import java.util.*;

enum attackType {Stun, Wild_Card, Wild_Storm, Disable, Recharge, None};

public class Attack {
    private class specialAttackReturn{
        //class to return more than one item

        public boolean stunned;
        public boolean doAttack;
        public boolean recharge;
        public boolean disabled;

        public specialAttackReturn(){
            stunned = false;
            doAttack = true;
            recharge = false;
            disabled = false;
        }

    }
    private String name;
    private int energyCost, damage;
    private attackType special; //special attack for the attack (if there are any)

    public Attack(String name, String energyCost, String damage, String special){
        setAttack(name, Integer.parseInt(energyCost), Integer.parseInt(damage), stringToAttackType(special));
    }

    private void setAttack(String name, int energyCost, int damage, attackType special){
        this.name = name;
        this.energyCost = energyCost;
        this.damage = damage;
        this.special = special;
    }

    private attackType stringToAttackType(String type){
        //turns the string from text file to an attackType
        attackType returnType;
        switch (type) {
            case "stun":
                returnType = attackType.Stun;
                break;
            case "wild card":
                returnType = attackType.Wild_Card;
                break;
            case "wild storm":
                returnType = attackType.Wild_Storm;
                break;
            case "disable":
                returnType = attackType.Disable;
                break;
            case "recharge":
                returnType = attackType.Recharge;
                break;
            default:
                returnType = attackType.None;
        }

        return returnType;

    }

    public void print(int attackNumber){
        //prints all attack information
        System.out.printf("%s %-1s %-2d %-13s ENERGY COST: %-5d DAMAGE: %-5d SP. ATK: %s%n", "-", "ATK", attackNumber,  name,
                energyCost, damage, (special==attackType.None)?"-":special.toString().replace("_", " "));

    }

    public specialAttackReturn useSpecial(){
        //cases for every special attack
        specialAttackReturn returnItem = new specialAttackReturn();

        Random rand = new Random();
        int chance = rand.nextInt(2);

        switch (special) {
            case Stun:
                if (chance == 1){
                    returnItem.stunned = true;
                }
                break;

            case Wild_Card:
                if (chance == 1){
                    returnItem.doAttack = false;
                }
                break;

            case Wild_Storm:
                if (chance == 1){
                    returnItem.doAttack = false;
                }
                break;

            case Disable:
                returnItem.disabled = true;
                break;

            case Recharge:
                returnItem.recharge = true;
                break;

        }
        return returnItem;
    }

    public boolean isEnergyRecharged(){
        specialAttackReturn rechargeReturn = new specialAttackReturn();
        rechargeReturn = useSpecial();
        return rechargeReturn.recharge;
    }

    public boolean doesPokemonAttack(){
        //checks if attack if performed
        specialAttackReturn attackReturn = new specialAttackReturn();
        attackReturn = useSpecial();
        return attackReturn.doAttack;
    }

    public boolean isPokemonDisabled(){
        //checks if attack will disable opponent
        specialAttackReturn disableReturn = new specialAttackReturn();
        disableReturn = useSpecial();
        return disableReturn.disabled;
    }

    public boolean isStunned(){
        //checks if attack will stun opponent
        specialAttackReturn stunReturn = new specialAttackReturn();
        stunReturn = useSpecial();
        return stunReturn.stunned;
    }

    public attackType getSpecial(){
        return special;
    }

    public String getName(){
        return name;
    }

    public int getDamage(){
        return damage;
    }

    public int getEnergyCost(){
        return energyCost;
    }
}
