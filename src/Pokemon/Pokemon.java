package Pokemon;
//Pokemon.java

import java.util.*;
enum pokemonType {Earth, Fire, Grass, Water, Fighting, Electric, Not_A_Type};

public class Pokemon {
    private String name;
    private int hp, energy;
    private pokemonType type, resistance, weakness;
    private ArrayList<Attack> attacks;

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
        energy = 50;
        this.type = type;
        this.resistance = resistance;
        this.weakness = weakness;
        this.attacks = attacks;
    }

    public void print(){
        System.out.printf("%-14s %-10d %-10d %-12s %-12s %-10s%n", name, hp, energy, type,
                (resistance==pokemonType.Not_A_Type)?"-":resistance,
                (weakness==pokemonType.Not_A_Type)?"-":weakness);
        //System.out.println();
        for(int i = 0; i< attacks.size(); i++){
            attacks.get(i).print(i + 1);

        }
        System.out.println("----------------------------------------------------------------------------");

    }

    public String getPokemonName(){
        return name;
    }

    public boolean isFainted(){
        return hp <= 0;
    }

    public void attack(int attackInt, Pokemon enemyPokemon){
        enemyPokemon.setHP(enemyPokemon.getHP() - attacks.get(attackInt).getDamage());
        energy -= attacks.get(attackInt).getEnergyCost();
    }


    public int getHP(){
        return hp;
    }

    private void setHP(int newHP){

        hp = newHP;
    }

    public int getAttackNum(){
        return attacks.size();
    }
}
