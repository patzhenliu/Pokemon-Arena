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
        System.out.println("Choose a Pokemon: ");
        Scanner kb = new Scanner(System.in);
        int pokemonChosen = kb.nextInt();
        return pokemonChosen;
    }

    public void run(){
        ArrayList<Pokemon> partyPokemon = new ArrayList<Pokemon>();
        ArrayList<Pokemon> pokemonProfiles = readFile();
        printMenu(pokemonProfiles);
        for(int i = 0; i < partySize; i++){
            partyPokemon.add(pokemonProfiles.get(choosePokemon()-1)); /////check if chosen or non-existent ;)
        }
        printMenu(partyPokemon);

    }






    public static void main(String[] args) {

        PokemonArena game = new PokemonArena();
        game.run();
    }
}