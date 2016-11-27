package Pokemon;
//PokemonArena.java

import java.io.*;
import java.util.*;
import java.util.concurrent.SynchronousQueue;

public class PokemonArena {
    private int partySize = 4;
    private enum actionType {ATTACK, RETREAT, PASS};
    /*   public void chooseNumber() {
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

    public int chooseNumber(String caption, int listSize){

        int numberChosen = -1;

        while((numberChosen > listSize) || (numberChosen <= 0)){
            try{
                System.out.print(caption);
                Scanner kb = new Scanner(System.in);
                numberChosen = kb.nextInt();
            }catch(Exception ex){
                System.out.println("Please enter a number\n");
            }
            if((numberChosen > listSize) || (numberChosen <= 0)){
                System.out.println("Invalid input\n");
            }
        }
        return numberChosen - 1;
    }

    public void selectPokemon(ArrayList<Pokemon> pokemonProfiles,
                              ArrayList<Pokemon> partyPokemon,
                              ArrayList<Pokemon> enemyPokemon){
        System.out.println("Select (4) Pokemon to begin by typing in the number");
        while (partyPokemon.size() < partySize){
            int pokemonIndex = chooseNumber("Choose a Pokemon: ", pokemonProfiles.size());
            if (partyPokemon.contains(pokemonProfiles.get(pokemonIndex))){
                System.out.printf("You've already chosen %s%n%n",pokemonProfiles.get(pokemonIndex).getPokemonName());
            }
            else{
                partyPokemon.add(pokemonProfiles.get(pokemonIndex));
                System.out.printf("You've chosen %s!%n%n",pokemonProfiles.get(pokemonIndex).getPokemonName());
            }
        }

    }


    public Pokemon choosePokemonToFight(ArrayList<Pokemon> partyPokemon){
        Pokemon p = partyPokemon.get(chooseNumber("Choose a Pokemon to fight: ", partyPokemon.size()));
        System.out.printf("%s, I CHOOSE YOU!%n%n", p.getPokemonName());
        return p;
    }

    private void printActionMenu(){
        System.out.println("What will you do next?");
        for (int i = 0; i < actionType.values().length; i++){
            System.out.println(i+1 + " - " + actionType.values()[i].toString());
        }
    }

    private boolean actionMenu(Pokemon yourPokemon, Pokemon enemyPokemon){

        boolean changePokemon = false;

        printActionMenu();
        int actionInt = chooseNumber("Choose an action: ", actionType.values().length);

        switch (actionInt) {
            case 0:
                int attackInt = chooseNumber("Choose an attack: ", yourPokemon.getAttackNum());
                yourPokemon.attack(attackInt, enemyPokemon);
                break;
            case 1:
                changePokemon = true;
                break;
            case 2:

                break;
            //default:
        }
        return changePokemon;
    }

    public void battle(Pokemon yourPokemon, Pokemon enemyPokemon){
        //System.out.printf("%30s", "BATTLE");
        boolean yourTurn = true;
        boolean changePokemon = false;

        while(!yourPokemon.isFainted() && !enemyPokemon.isFainted() && !changePokemon){
            if (changePokemon){
                return;
            }
            if (yourTurn){
                changePokemon = actionMenu(yourPokemon, enemyPokemon);
                //yourTurn = false;
            }
            else{
                //cpu's turn
                return;
            }
        }
    }

    private boolean checkAllPokemonFainted(ArrayList<Pokemon> pokemon){
        for (Pokemon p:pokemon){
            if (!p.isFainted()){
                return false;
            }
        }
        return true;
    }

    private boolean isGameOver(ArrayList<Pokemon> partyPokemon, ArrayList<Pokemon> enemyPokemon){
        if (checkAllPokemonFainted(partyPokemon)){
            System.out.println("YOU LOSE!");
            return true;
        }
        else if (checkAllPokemonFainted(enemyPokemon)){
            System.out.println("YOU WIN!");
            return true;
        }
        return false;
    }

    public void run(){
        ArrayList<Pokemon> partyPokemon = new ArrayList<Pokemon>();
        ArrayList<Pokemon> enemyPokemon = new ArrayList<Pokemon>();
        ArrayList<Pokemon> pokemonProfiles = readFile();

        System.out.println("WELCOME TO POKEMON ARENA!\nHit Enter To Play");
        Scanner kb = new Scanner(System.in);
        kb.nextLine();

        printMenu(pokemonProfiles);
        selectPokemon(pokemonProfiles, partyPokemon, enemyPokemon); //Ask user to select partySize number of Pokemon

        enemyPokemon = new ArrayList<Pokemon>(pokemonProfiles);
        for (int i = 0; i < partySize; i++){
            enemyPokemon.remove(partyPokemon.get(i));
        }

        printMenu(partyPokemon);
        Collections.shuffle(enemyPokemon);

        boolean gameOver = false;
        while (!gameOver) {
            Pokemon inBattle = choosePokemonToFight(partyPokemon);
            //System.out.println(enemyPokemon.size());
            battle(inBattle, enemyPokemon.get(0));   ///change enemy pokemon when fainted
            gameOver = isGameOver(partyPokemon, enemyPokemon);
            System.out.println(enemyPokemon.get(0).getPokemonName() + " has " + enemyPokemon.get(0).getHP() + " hp");

        }
        //

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