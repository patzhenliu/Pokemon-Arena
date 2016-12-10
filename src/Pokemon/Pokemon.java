package Pokemon;
//Pokemon.java

import java.util.*;
enum pokemonType {Earth, Fire, Grass, Water, Fighting, Electric, Not_A_Type};

public class Pokemon {
    private String name;
    private int hp, maxhp, energy;
    private pokemonType type, resistance, weakness;
    private ArrayList<Attack> attacks;
    private boolean isDisabled;

    public Pokemon(String inputInfo){
        //Gyarados,100,water,leaf,earth,2,Dragon Rage,30,50, ,Bubblebeam,40,40,disable
        String[] pokemonInfo = inputInfo.split(",");

        setPokemon(pokemonInfo[0], Integer.parseInt(pokemonInfo[1]), stringToPokemonType(pokemonInfo[2]), stringToPokemonType(pokemonInfo[3]),
                stringToPokemonType(pokemonInfo[4]), stringToAttackArray(Integer.parseInt(pokemonInfo[5]), pokemonInfo));
    }

    private pokemonType stringToPokemonType(String type){
        pokemonType returnType;
        switch (type) {
            case "earth":
                returnType = pokemonType.Earth;
                break;
            case "fire":
                returnType = pokemonType.Fire;
                break;
            case "grass":
                returnType = pokemonType.Grass;
                break;
            case "water":
                returnType = pokemonType.Water;
                break;
            case "fighting":
                returnType = pokemonType.Fighting;
                break;
            case "electric":
                returnType = pokemonType.Electric;
                break;
            default:
                returnType = pokemonType.Not_A_Type;
        }

        return returnType;

    }

    private ArrayList<Attack> stringToAttackArray(int numberOfAttacks, String[] pokemonInfo){
        ArrayList<Attack> attacks = new ArrayList<Attack>();
        for (int i = 0; i < numberOfAttacks; i++) {
            attacks.add(new Attack(pokemonInfo[6 + (i * 4)], pokemonInfo[7 + (i * 4)], pokemonInfo[8 + (i * 4)], pokemonInfo[9 + (i * 4)]));
        }
        return attacks;
    }

    private void setPokemon(String name, int hp, pokemonType type, pokemonType resistance, pokemonType weakness, ArrayList<Attack> attacks){
        this.name = name;
        this.hp = hp;
        maxhp = hp;
        energy = 50;
        this.type = type;
        this.resistance = resistance;
        this.weakness = weakness;
        this.attacks = attacks;

    }

    public void printPokemonStatus(boolean yourPokemon){
        if (!yourPokemon){
            System.out.println("------------------------------");
            System.out.printf("%s HP: %d ENERGY: %d%n", name, hp, energy);
        }
        else if (yourPokemon){
            System.out.printf("%n%s HP: %d ENERGY: %d%n", name, hp, energy);
            System.out.println("------------------------------");
        }

    }

    public void print(){
        if (isFainted()){
            System.out.printf("%-14s %-10s%n", name, "FAINTED");
        }
        else {
            System.out.printf("%-14s %-10d %-10d %-12s %-12s %-10s%n", name, hp, energy, type,
                    (resistance == pokemonType.Not_A_Type) ? "-" : resistance,
                    (weakness == pokemonType.Not_A_Type) ? "-" : weakness);
            //System.out.println();
            for (int i = 0; i < attacks.size(); i++) {
                attacks.get(i).print(i + 1);

            }
        }
        System.out.println("----------------------------------------------------------------------------");

    }

    public String getName(){
        return name;
    }

    public boolean isFainted(){
        return hp <= 0;
    }

    public pokemonType getType(){
        return type;
    }

    public boolean attack(int attackInt, Pokemon enemyPokemon){
        boolean doAttack = true;
        int count = 0;
        int damage = attacks.get(attackInt).getDamage();

        if (enemyPokemon.getType() == weakness){
            damage = damage/2;
            //System.out.println("Not very Effective.");
        }

        else if (enemyPokemon.getType() == resistance){
            damage = damage * 2;
            //System.out.println("Super Effective!");
        }
        if (isDisabled){
            damage -= 10;
        }

        while (doAttack == true && enemyPokemon.getHP() > 0){
            doAttack = attacks.get(attackInt).doesPokemonAttack();
            //check resistance and weakness
            if (doAttack == true) {
                System.out.printf("%s used %s%n", getName(), getAttackList().get(attackInt).getName()); // delete later


                enemyPokemon.setHP(enemyPokemon.getHP() - damage);

                if (attacks.get(attackInt).isEnergyRecharged()){
                    setEnergy(energy + 20);
                }
            }

            if (attacks.get(attackInt).getSpecial() == attackType.Wild_Storm){
                if (doAttack) {
                    count++;
                    System.out.printf("(%d)%n", count);
                }
                else{
                    System.out.println();
                }
            }

            if (attacks.get(attackInt).getSpecial() != attackType.Wild_Storm){
                if (!doAttack){
                    if (attacks.get(attackInt).getSpecial() == attackType.Wild_Card){
                        System.out.printf("%s tried to use %s, but failed%n", name, attacks.get(attackInt).getName());
                    }
                }
                if (attacks.get(attackInt).getSpecial() == attackType.Recharge){
                    specialAttackRecharge();
                }
                if (attacks.get(attackInt).getSpecial() == attackType.Disable && !isDisabled){
                    enemyPokemon.setIsDisabled(enemyPokemon, true);
                }

                doAttack = false;
            }
        }

        setEnergy(energy - attacks.get(attackInt).getEnergyCost());
        if  (attacks.get(attackInt).getSpecial() == attackType.Wild_Storm){
            if (count > 0) {
                System.out.printf("%s used %s (%d) time(s)%n", name, attacks.get(attackInt).getName(), count);
            }
            else{
                System.out.printf("%s tried to use %s, but failed%n", name, attacks.get(attackInt).getName(), count);
            }
        }
        if (enemyPokemon.getType() == weakness){
            System.out.println("Not very Effective.");
        }

        else if (enemyPokemon.getType() == resistance){
            System.out.println("Super Effective!");
        }

        return attacks.get(attackInt).isStunned();
    }

    private void setEnergy(int newEnergy){
        energy = newEnergy;
    	/*if (energy < 0){
    		energy = 0;
    	}*/
        if (energy > 50){
            energy = 50;
        }
    }


    public int getHP(){
        return hp;
    }

    private void setHP(int newHP){
        if (newHP > maxhp){
            hp = maxhp;
        }
        else {
            hp = newHP;
        }
    }

    public ArrayList<Attack> getAttackList(){
        return attacks;
    }

    public int getAttackLength(){
        return attacks.size();
    }

    public int getEnergy(){
        return energy;
    }

    public void recoverEnergy(){
        setEnergy(energy + 10);
    }

    public void recoverHP(){
        setHP(hp + 20);
    }

    private void specialAttackRecharge(){
        setEnergy(energy + 20);
    }

    public void setDisabled(){
        if (isDisabled && !isFainted()) {
            isDisabled = false;
            System.out.printf("%s is no longer disabled%n", name);
        }
    }

    private void setIsDisabled(Pokemon enemyPokemon, boolean bool){
        System.out.printf("%s is disabled%n", enemyPokemon.getName());
        isDisabled = bool;
    }

}
