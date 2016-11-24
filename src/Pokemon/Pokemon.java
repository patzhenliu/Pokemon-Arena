package Pokemon;
//Pokemon.java

import java.util.*;
enum pokemonType {EARTH, FIRE, GRASS, WATER, FIGHTING, ELECTRIC, NOT_A_TYPE};

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
                returnType = pokemonType.EARTH;
                break;
            case "fire":
                returnType = pokemonType.FIRE;
                break;
            case "grass":
                returnType = pokemonType.GRASS;
                break;
            case "water":
                returnType = pokemonType.WATER;
                break;
            case "fighting":
                returnType = pokemonType.FIGHTING;
                break;
            case "electric":
                returnType = pokemonType.ELECTRIC;
                break;
            default:
                returnType = pokemonType.NOT_A_TYPE;
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
                         (resistance==pokemonType.NOT_A_TYPE)?"-":resistance,
                         (weakness==pokemonType.NOT_A_TYPE)?"-":weakness);

    }
}
