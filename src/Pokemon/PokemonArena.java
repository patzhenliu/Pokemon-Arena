package Pokemon;
//PokemonArena.java

import java.io.*;
import java.util.*;

public class PokemonArena {

    private class actionMenuReturn{
        //class to return more than one item
        public boolean changePokemon; // if you are switching pokemon (voluntary or mandatory)
        public boolean attacked;
        public boolean switchPokemon; // if YOU want to switch Pokemon voluntarily (retreat)
        public boolean stunned;

        public actionMenuReturn(){
            changePokemon = false;
            attacked = true;
            switchPokemon = false;
            stunned = false;
        }

    }

    private int partySize = 4;
    private enum actionType {ATTACK, RETREAT, PASS};


    private ArrayList<Pokemon> readFile() {
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

    private void printMenu(ArrayList<Pokemon> pokemonProfiles, String caption, boolean zeroToExit){
        //prints pokemon menu
        int i = 1;
        System.out.printf("%43s%n",caption);
        System.out.println("============================================================================");
        System.out.printf("%7s%13s%13s%12s%17s%11s%n", "NAME", "HP", "ENERGY", "TYPE", "RESISTANCE", "WEAKNESS");
        System.out.println("============================================================================");
        for(Pokemon item : pokemonProfiles) {
            System.out.printf("%-3d",i);
            item.print();
            i++;
        }
        if (zeroToExit){
            System.out.println("Press 0 to go back");
        }
        System.out.println();
    }

    private int chooseNumber(String caption, int listSize, ArrayList<Pokemon> partyPokemon, boolean choosingPokemon){
        //overloaded chooseNumber to accept a boolean choosingPokemon(user is choosing a pokemon and
        //not an action or attack) and not acceptZero (0 to return to action menu)
        return chooseNumber(caption, listSize, false, partyPokemon, choosingPokemon);
    }

    private int chooseNumber(String caption, int listSize, ArrayList<Pokemon> partyPokemon){
        //overloaded chooseNumber to accept a boolean acceptZero (0 to return to action menu) and not
        //choosingPokemon(user is choosing a pokemon and not an action or attack)
        return chooseNumber(caption, listSize, false, partyPokemon, false);
    }

    private int chooseNumber(String caption, int listSize, boolean acceptZero, ArrayList<Pokemon> partyPokemon, boolean choosingPokemon){
        //user inputs a number and checks if the number is valid based on: the size of the list they're choosing from, if pokemon is
        //fainted (if used to choose Pokemon)
        int numberChosen = -1;
        while((numberChosen > listSize) || (numberChosen <= 0)){
            numberChosen = -1;
            try{
                System.out.print(caption);
                Scanner kb = new Scanner(System.in);
                numberChosen = kb.nextInt();
                //System.out.println(numberChosen);

                if (choosingPokemon && numberChosen > 0){
                    //only checks if the pokemon of index numberChosen is fainted is user is selecting Pokemon and not an attack or action
                    if(partyPokemon.get(numberChosen-1).isFainted()){
                        System.out.println("This Pokemon has fainted\nPlease select a different Pokemon\n");
                        numberChosen = -2;
                    }
                }
                if(acceptZero && numberChosen == 0){
                    return -1;
                }


            }catch(Exception ex){

            }
            if((numberChosen > listSize) || (numberChosen <= 0 && !(numberChosen == -2 && choosingPokemon))){
                System.out.println("Please enter a number on the list\n");
            }
        }
        return numberChosen - 1;
    }

    private void selectPokemon(ArrayList<Pokemon> pokemonProfiles,
                               ArrayList<Pokemon> partyPokemon){
        //user selects pokemon they want to use for the game
        System.out.printf("Select (%d) Pokemon to begin by typing in a number:%n", partySize);
        while (partyPokemon.size() < partySize){
            int pokemonIndex = chooseNumber("Choose a Pokemon: ", pokemonProfiles.size(), partyPokemon);
            if (partyPokemon.contains(pokemonProfiles.get(pokemonIndex))){
                System.out.printf("You've already chosen %s%n%n",pokemonProfiles.get(pokemonIndex).getName());
            }
            else{
                partyPokemon.add(pokemonProfiles.get(pokemonIndex));
                System.out.printf("You've chosen %s!%n%n",pokemonProfiles.get(pokemonIndex).getName());
            }
        }

    }

    private Pokemon choosePokemonToFight(ArrayList<Pokemon> partyPokemon, Pokemon inBattle){
        //user chooses a pokemon out of their party pokemon to battle
        int pokemonIndex = 0;
        while (pokemonIndex != -1){
            pokemonIndex = chooseNumber("Choose a Pokemon to fight: ", partyPokemon.size(), true, partyPokemon, true);
            if (pokemonIndex != -1) {
                Pokemon chosenPokemon = partyPokemon.get(pokemonIndex);
                if (!chosenPokemon.equals(inBattle)) {
                    printIChooseYou(chosenPokemon);
                    return chosenPokemon;
                }
                else{
                    System.out.printf("%s is already in battle%n%n", chosenPokemon.getName());
                }
            }
        }
        return inBattle;
    }

    private Pokemon choosePokemonToFight(ArrayList<Pokemon> partyPokemon){
        //user chooses a pokemon out of their party pokemon to battle
        Pokemon p = partyPokemon.get(chooseNumber("Choose a Pokemon to fight: ", partyPokemon.size(), partyPokemon, true));
        printIChooseYou(p);
        return p;
    }

    private void printIChooseYou(Pokemon p){
        System.out.printf("%s, I CHOOSE YOU!%n", p.getName());
    }

    private void printActionMenu(Pokemon yourPokemon, Pokemon enemyPokemon, boolean printActions){
        //prints all actions user can perform in battle
        enemyPokemon.printPokemonStatus(false);
        yourPokemon.printPokemonStatus(true);
        System.out.println("What will you do?");
        if (printActions) {
            for (int i = 0; i < actionType.values().length; i++) {
                System.out.println(i + 1 + " - " + actionType.values()[i].toString());
            }
            System.out.println();
        }
    }

    private actionMenuReturn actionMenu(Pokemon yourPokemon, Pokemon enemyPokemon, ArrayList<Pokemon> partyPokemon){
        //asks user what action out of three (or just the option to pass if stunned)
        actionMenuReturn returnItem = new actionMenuReturn();

        printActionMenu(yourPokemon, enemyPokemon, true);
        int actionInt = chooseNumber("Choose an action: ", actionType.values().length, partyPokemon);

        switch (actionInt) {
            case 0: //attack
                System.out.println("=================================ATTACKS=================================");
                for (int i = 0; i < yourPokemon.getAttackLength(); i++){
                    yourPokemon.getAttackList().get(i).print(i+1);
                }
                System.out.print("Press 0 to go back\n");
                System.out.println();
                int attackInt = chooseNumber("Choose an attack: ", yourPokemon.getAttackLength(), true, partyPokemon, false);
                System.out.println();
                if(attackInt == -1){
                    returnItem.attacked = false;
                    break;
                }
                else if (yourPokemon.getEnergy() >= yourPokemon.getAttackList().get(attackInt).getEnergyCost()){ // check energy
                    returnItem.stunned = yourPokemon.attack(attackInt, enemyPokemon);

                }
                else{
                    System.out.println("You don't have enough energy to use this attack\n");
                    returnItem.attacked = false;
                }
                break;

            case 1: //retreat
                System.out.printf("%s, RETURN!%n", yourPokemon.getName());
                returnItem.changePokemon = true;
                returnItem.switchPokemon = true;
                break;

            case 2: //pass
                System.out.printf("%s passed%n",yourPokemon.getName());
                break;

        }

        return returnItem;
    }

    private boolean battle(Pokemon yourPokemon, Pokemon enemyPokemon, ArrayList<Pokemon> partyPokemon, ArrayList<Pokemon> enemyParty,
                           boolean playerSwitchedPokemon, boolean yourTurn){
        boolean youreStunned = false;
        actionMenuReturn menuReturn = new actionMenuReturn();

        while(!yourPokemon.isFainted() && !enemyPokemon.isFainted() && !menuReturn.changePokemon){
            boolean enemyAttacked = false;

            if (menuReturn.changePokemon){
                return menuReturn.switchPokemon;
            }
            else{

                //your turn
                if (yourTurn && !youreStunned && !playerSwitchedPokemon){
                    menuReturn = actionMenu(yourPokemon, enemyPokemon, partyPokemon);
                    if (menuReturn.attacked){
                        yourTurn = false;
                    }
                }
                else if (youreStunned){ //if your pokemon is stunned - skip turn
                    System.out.printf("%s has been stunned\n%s can't move%n",yourPokemon.getName(), yourPokemon.getName());
                    printActionMenu(yourPokemon, enemyPokemon, false);
                    System.out.println("ENTER - PASS");
                    Scanner kb = new Scanner(System.in);
                    kb.nextLine();
                    System.out.printf("%s passed%n",yourPokemon.getName());

                    youreStunned = false;
                    yourTurn = false;
                }

                if(enemyPokemon.isFainted()){ //check if you defeated current opponent
                    postBattle(partyPokemon, enemyPokemon, yourPokemon);
                }

                //opponent's turn
                if ((!yourTurn || playerSwitchedPokemon) && !menuReturn.stunned && !menuReturn.changePokemon && !enemyPokemon.isFainted()){
                    playerSwitchedPokemon = false;
                    for (int i = enemyPokemon.getAttackLength()-1; i > -1; i--){ //finds next attack it can afford to use
                        if (enemyPokemon.getEnergy() >= enemyPokemon.getAttackList().get(i).getEnergyCost()){
                            youreStunned = enemyPokemon.attack(i, yourPokemon);
                            enemyAttacked = true;
                            break;
                        }
                    }
                    if (!enemyAttacked){ //if enemy can't afford to attack
                        System.out.printf("%s passed%n",enemyPokemon.getName());
                    }
                    yourTurn = true;
                }
                else{
                    if (menuReturn.stunned){ //if enemy pokemon is stunned
                        System.out.printf("%s has been stunned\n%s can't move%n",enemyPokemon.getName(), enemyPokemon.getName());
                    }
                    menuReturn.stunned = false;
                    yourTurn = true;
                }

            }
            if(menuReturn.attacked){
                //all pokemon recover energy after each round
                recoverEnergy(partyPokemon, enemyParty);
            }
        }
        if (yourPokemon.isFainted()){ //checks if your current pokemon was defeated
            postBattle(partyPokemon, yourPokemon, enemyPokemon);
        }
        return menuReturn.switchPokemon;
    }

    private void postBattle(ArrayList<Pokemon> partyPokemon, Pokemon faintedPokemon, Pokemon pokemonInBattle){
        //resets some pokemon stats after battle - not disabled anymore and user's pokemon heal hp
        System.out.println(faintedPokemon.getName()+" has fainted");
        healHP(partyPokemon);
        pokemonInBattle.setDisabled();
        for (Pokemon p:partyPokemon){
            p.setDisabled();
        }
    }

    private void healHP(ArrayList<Pokemon> partyPokemon){
        //any of user's pokemon not fainted recover some hp
        for (Pokemon p:partyPokemon){
            if (!p.isFainted()){
                p.recoverHP();
            }
        }
    }

    private void recoverEnergy(ArrayList<Pokemon> partyPokemon, ArrayList<Pokemon> enemyParty){
        //all pokemon recover energy
        for (Pokemon p: partyPokemon){
            p.recoverEnergy();
        }
        for (Pokemon p: enemyParty){
            p.recoverEnergy();
        }
    }

    private boolean checkAllPokemonFainted(ArrayList<Pokemon> pokemon){
        //checks if all pokemon in an array list are fainted
        for (Pokemon p:pokemon){
            if (!p.isFainted()){
                return false;
            }
        }
        return true;
    }

    private boolean isGameOver(ArrayList<Pokemon> partyPokemon, ArrayList<Pokemon> enemyPokemon){
        //checks if all pokemon on one team is fainted - if so then game is over
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

    private int findNextAvailablePokemon(ArrayList<Pokemon> pokemon){
        //finds next enemy pokemon in list that is not fainted
        for (int i = 0; i < pokemon.size(); i++){
            if (!pokemon.get(i).isFainted()){
                return i;
            }
        }
        return 0;
    }

    public boolean askYesOrNo(String caption){
        //when opponent faints asks if user wants to change Pokemon
        String change = "";

        while(!change.toUpperCase().equals("N") && !change.toUpperCase().equals("Y")){
            try{
                Scanner kb = new Scanner(System.in);
                System.out.printf("%n%s [Y/N]%n", caption);
                change = kb.nextLine();

                if (change.toUpperCase().equals("Y")){
                    return true;
                }
                else if (change.toUpperCase().equals("N")){
                    return false;
                }
            }
            catch(Exception ex){
                System.out.println("Please enter Y or N\n");
            }
            System.out.println("Please enter Y or N\n");
        }
        return false;
    }

    private boolean randomizeTurn(){
        //randomly determines who starts
        Random rand = new Random();
        int chance = rand.nextInt(2);
        if (chance == 0){
            return true;
        }
        return false;
    }

    private void printEnemiesLeft(ArrayList<Pokemon> enemyParty){
        int count = 0;
        for (Pokemon p: enemyParty){
            if (!p.isFainted()){
                count++;
            }
        }
        System.out.printf("Enemies left: %d%n", count);
    }

    public void run(){
        //intro
        ArrayList<Pokemon> partyPokemon = new ArrayList<Pokemon>();
        ArrayList<Pokemon> enemyParty = new ArrayList<Pokemon>();
        ArrayList<Pokemon> pokemonProfiles = readFile();

        System.out.println("WELCOME TO POKEMON ARENA!\nHit Enter To Play");
        Scanner kb = new Scanner(System.in);
        kb.nextLine();

        //prints all pokemon and attacks
        printMenu(pokemonProfiles, "POKEMON ARENA MENU", false);
        selectPokemon(pokemonProfiles, partyPokemon); //Ask user to select partySize number of Pokemon

        //enemy party is created with the remaining pokemon
        enemyParty = new ArrayList<Pokemon>(pokemonProfiles);
        for (int i = 0; i < partySize; i++){
            enemyParty.remove(partyPokemon.get(i));
        }

        Collections.shuffle(enemyParty);

        boolean gameOver = false;
        printMenu(partyPokemon, "PARTY POKEMON", false);
        Pokemon enemyInBattle = enemyParty.get(findNextAvailablePokemon(enemyParty));
        Pokemon inBattle = choosePokemonToFight(partyPokemon);
        boolean playerSwitchedPokemon = false;
        boolean yourTurn = randomizeTurn();

        while (!gameOver) {
            //loops until the game is over (all pokemon on one team are fainted)
            boolean switchPokemon = battle(inBattle, enemyInBattle, partyPokemon, enemyParty, playerSwitchedPokemon, yourTurn);   ///change enemy pokemon when fainted
            gameOver = isGameOver(partyPokemon, enemyParty);

            if (enemyInBattle.isFainted() || inBattle.isFainted()){
                //if battle is over randomize who starts the next battle
                yourTurn = randomizeTurn();
            }

            if(!gameOver) {
                //changing pokemon either voluntarily or mandatory
                playerSwitchedPokemon = false;

                if (switchPokemon) {
                    playerSwitchedPokemon = true;
                }

                if (!switchPokemon && !inBattle.isFainted()) {
                    switchPokemon = askYesOrNo("Would you like to switch Pokemon?");
                    printEnemiesLeft(enemyParty);
                }
                if (switchPokemon || inBattle.isFainted()) {
                    if (playerSwitchedPokemon){
                        //if user's pokemon didn't faint they aren't required to change pokemon
                        printMenu(partyPokemon, "PARTY POKEMON", true);
                    }
                    else {
                        printMenu(partyPokemon, "PARTY POKEMON", false);
                    }
                    Pokemon currentPokemon = choosePokemonToFight(partyPokemon, inBattle);
                    if(currentPokemon.equals(inBattle)){
                        //if player didn't switch pokemon it's still their turn
                        yourTurn = true;
                    }
                    inBattle = currentPokemon;

                }
                enemyInBattle = enemyParty.get(findNextAvailablePokemon(enemyParty));
            }
        }
    }

    public static void main(String[] args) {
        boolean playGame = true;
        while(playGame) {
            PokemonArena game = new PokemonArena();
            game.run();
            playGame = game.askYesOrNo("Would you like to play again?");
        }
    }
}