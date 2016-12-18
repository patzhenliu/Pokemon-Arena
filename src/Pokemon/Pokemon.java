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
        String[] pokemonInfo = inputInfo.split(",");

        setPokemon(pokemonInfo[0], Integer.parseInt(pokemonInfo[1]), stringToPokemonType(pokemonInfo[2]), stringToPokemonType(pokemonInfo[3]),
                stringToPokemonType(pokemonInfo[4]), stringToAttackArray(Integer.parseInt(pokemonInfo[5]), pokemonInfo));
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

    private pokemonType stringToPokemonType(String type){
        //takes a string and converts it to a PokemonType
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
        //created a list of attacks for the pokemon
        ArrayList<Attack> attacks = new ArrayList<Attack>();
        for (int i = 0; i < numberOfAttacks; i++) {
            attacks.add(new Attack(pokemonInfo[6 + (i * 4)], pokemonInfo[7 + (i * 4)], pokemonInfo[8 + (i * 4)], pokemonInfo[9 + (i * 4)]));
        }
        return attacks;
    }

    public void printPokemonStatus(boolean yourPokemon){
        //prints your pokemon and opponent's status during the battle (hp and energy)

        if (!yourPokemon){
            System.out.printf("------------------------------");
        }

        System.out.printf("%n%s HP: %d ENERGY: %d%n", name, hp, energy);

        if (yourPokemon){

            System.out.println("------------------------------");
        }

    }

    public void print(){
        //prints all of the pokemon's information (unless the pokemon is fainted - prints fainted)
        if (isFainted()){
            System.out.printf("%-14s %-10s%n", name, "FAINTED");
        }
        else {
            System.out.printf("%-14s %-10d %-10d %-12s %-12s %-10s%n", name, hp, energy, type,
                    (resistance == pokemonType.Not_A_Type) ? "-" : resistance,
                    (weakness == pokemonType.Not_A_Type) ? "-" : weakness);

            for (int i = 0; i < attacks.size(); i++) {
                attacks.get(i).print(i + 1);

            }
        }
        System.out.println("----------------------------------------------------------------------------");

    }

    public boolean attack(int attackInt, Pokemon enemyPokemon){
        //attacks (or not) based on: if pokemon is disabled, weakness and resistance, attack special
        boolean doAttack = true;
        int count = 0;
        int damage = attacks.get(attackInt).getDamage();

        if (enemyPokemon.getType().equals(weakness)){
            damage = damage/2;
        }

        else if (enemyPokemon.getType().equals(resistance)){
            damage = damage * 2;
        }
        if (isDisabled){
            damage -= 10;
        }

        while (doAttack == true && enemyPokemon.getHP() > 0){
            doAttack = attacks.get(attackInt).doesPokemonAttack();
            if (doAttack == true) {
                System.out.printf("%s used %s", getName(), getAttackList().get(attackInt).getName());

                enemyPokemon.setHP(enemyPokemon.getHP() - damage);

                if (attacks.get(attackInt).isEnergyRecharged()){
                    setEnergy(energy + 20);
                }
            }

            if (attacks.get(attackInt).getSpecial() == attackType.Wild_Storm){
                if (doAttack) {
                    count++;
                    System.out.printf("(%d)", count);
                }
            }
            System.out.printf("%n");

            if (attacks.get(attackInt).getSpecial() != attackType.Wild_Storm){
                if (!doAttack){
                    if (attacks.get(attackInt).getSpecial() == attackType.Wild_Card){
                        System.out.printf("%s tried to use %s, but failed%n", name, attacks.get(attackInt).getName());
                    }
                }
                if (attacks.get(attackInt).getSpecial() == attackType.Recharge){
                    specialAttackRecharge();
                }
                if (attacks.get(attackInt).getSpecial() == attackType.Disable && !enemyPokemon.getIsDisabled()){
                    enemyPokemon.setIsDisabled(enemyPokemon, true);
                }

                doAttack = false;
                //only wild storm attacks will go through the while loop more than once
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
        if (enemyPokemon.getType().equals(weakness)){
            System.out.println("Not very Effective.");
        }

        else if (enemyPokemon.getType().equals(resistance)){
            System.out.println("Super Effective!");
        }

        return attacks.get(attackInt).isStunned();
    }

    private void setEnergy(int newEnergy){
        //sets new energy - maximum 50
        energy = newEnergy;

        if (energy > 50){
            energy = 50;
        }
    }

    private void setHP(int newHP){
        //sets new hp - maximum hp is the hp they started with
        if (newHP > maxhp){
            hp = maxhp;
        }
        else {
            hp = newHP;
        }
    }

    public void setDisabled(){
        //sets pokemon to not disabled anymore
        if (isDisabled && !isFainted()) {
            isDisabled = false;
            System.out.printf("%s is no longer disabled%n", name);
        }
    }

    public boolean getIsDisabled(){
        return isDisabled;
    }

    private void setIsDisabled(Pokemon enemyPokemon, boolean bool){
        //sets pokemon as disabled
        if (!isDisabled){
            System.out.printf("%s is disabled%n", enemyPokemon.getName());
            isDisabled = bool;
        }
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

    public int getHP(){
        return hp;
    }

}
