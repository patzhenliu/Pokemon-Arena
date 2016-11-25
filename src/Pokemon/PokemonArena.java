package Pokemon;
//PokemonArena.java

import java.io.*;
import java.util.*;

public class PokemonArena {
    private int partySize = 4;
    /*   public void choosePokemon() {
        readFile();
        System.out.println(Arrays.toString(pokemon));
        selectPartyPokemon();
    }



    private void printPokemon() {
        //for (int i = 0; )
        return;
    }

    private void selectPartyPokemon() {
        for (int i = 0; i < partyNum; i++) {
            System.out.println("Choose a Pokemon: ");
            Scanner pokeChosen = new Scanner(System.in);
            int pokeIndex = pokeChosen.nextInt();
            createPokemon(pokeIndex);

        }
    }

    private void createPokemon(int pokeIndex) {
        System.out.println(pokemon[pokeIndex].split(","));
        return;
    }*/

    public ArrayList<Pokemon> readFile() {
        ArrayList<Pokemon> pokemonProfiles = new ArrayList<Pokemon>();
        try {
            Scanner file = new Scanner( new BufferedReader(
                    new FileReader("pokemon.txt")));
            int totalPokemon = Integer.parseInt(file.nextLine());
            for(int i = 0; i < totalPokemon; i++){
                pokemonProfiles.add(new Pokemon(file.nextLine()));
            }
            file.close();
        }
        catch(IOException ex){
            System.out.println("YOU WILL NEVER CATCH THEM ALL");
        }
        return pokemonProfiles;
    }

    public void printMenu(ArrayList<Pokemon> pokemonProfiles){

        int i = 1;
        System.out.printf("%43s%n","POKEMON ARENA MENU");
        System.out.println("============================================================================");
        System.out.printf("%7s%13s%13s%12s%17s%11s%n", "NAME", "HP", "ENERGY", "TYPE", "RESISTANCE", "WEAKNESS");
        System.out.println("============================================================================");
        for(Pokemon item : pokemonProfiles) {
            System.out.printf("%-3d",i);
            item.print();
            i++;
        }
        System.out.println();
    }

    public int choosePokemon(){
        System.out.print("Choose a Pokemon: ");
        Scanner kb = new Scanner(System.in);
        int pokemonChosen;
        try{
            pokemonChosen = kb.nextInt();
        }catch(Exception ex){
            return -1;
        }
        return pokemonChosen;
    }

    public void selectPokemon(ArrayList<Pokemon> pokemonProfiles,
                              ArrayList<Pokemon> partyPokemon,
                              ArrayList<Pokemon> enemyPokemon){
        System.out.println("Select (4) Pokemon to begin by typing in the number");
        while (partyPokemon.size() < partySize){
            int pokemonIndex = choosePokemon();
            if ((pokemonIndex > pokemonProfiles.size()) || (pokemonIndex <= 0)){
                System.out.println("There is no such Pokemon\n");
            }
            else if (partyPokemon.contains(pokemonProfiles.get(pokemonIndex-1))){
                System.out.printf("You've already chosen %s%n%n",pokemonProfiles.get(pokemonIndex-1).getPokemonName());
            }
            else{
                partyPokemon.add(pokemonProfiles.get(pokemonIndex-1));
                System.out.printf("You've chosen %s!%n%n",pokemonProfiles.get(pokemonIndex-1).getPokemonName());
            }
        }

        enemyPokemon = new ArrayList<Pokemon>(pokemonProfiles);
        for (int i = 0; i < partySize; i++){
            enemyPokemon.remove(partyPokemon.get(i));
        }

    }

	/*
    public void actionMenu(Pokemon pokemon){
    	int ATTACK = 1;
    	int RETREAT = 2;
    	int PASS = 3;

    	count = 0;
    	Scanner kb = new Scanner(System.in);
    	while (count < 1){
        	int action = kb.nextInt();
        	if (action == ATTACK){
        		pokemon.chooseAttack();
        		count++;
        	}
        	else if (action == RETREAT){
        		pokemon.retreat();
        		count++;
        	}
        	//else if (action == PASS){
        	//	pokemon.pass();
        	//}
        	else{
        		System.out.println("That is not a valid action");
        	}
    	}
    }*/

    public void run(){
        ArrayList<Pokemon> partyPokemon = new ArrayList<Pokemon>();
        ArrayList<Pokemon> enemyPokemon = new ArrayList<Pokemon>();
        ArrayList<Pokemon> pokemonProfiles = readFile();

        System.out.println("WELCOME TO POKEMON ARENA!\nHit Enter To Play");
        Scanner kb = new Scanner(System.in);
        kb.nextLine();

        printMenu(pokemonProfiles);
        selectPokemon(pokemonProfiles, partyPokemon, enemyPokemon); //Ask user to select partySize number of Pokemon

        printMenu(partyPokemon);
        Collections.shuffle(enemyPokemon);

        //randomize enemy pokemon list
        //choose who starts
        //choose attack
        //show stats
    }






    public static void main(String[] args) {

        PokemonArena game = new PokemonArena();
        game.run();
    }
}