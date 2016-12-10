package Pokemon;
//PokemonArena.java

import java.io.*;
import java.util.*;

public class PokemonArena {

    private class actionMenuReturn{

        public boolean changePokemon; // go back to main while loop
        public boolean attacked;
        public boolean switchPokemon; // if YOU want to switch Pokemon
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

    private void printMenu(ArrayList<Pokemon> pokemonProfiles, String caption){

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
        System.out.println();
    }

    private int chooseNumber(String caption, int listSize, ArrayList<Pokemon> partyPokemon, boolean choosingPokemon){
        return chooseNumber(caption, listSize, false, partyPokemon, choosingPokemon);
    }

    private int chooseNumber(String caption, int listSize, ArrayList<Pokemon> partyPokemon){
        return chooseNumber(caption, listSize, false, partyPokemon, false);
    }

    private int chooseNumber(String caption, int listSize, boolean acceptZero, ArrayList<Pokemon> partyPokemon, boolean choosingPokemon){
        int numberChosen = -1;
        while((numberChosen > listSize) || (numberChosen <= 0)){
            numberChosen = -1;
            try{
                System.out.print(caption);
                Scanner kb = new Scanner(System.in);
                numberChosen = kb.nextInt();
                //System.out.println(partyPokemon.get(numberChosen).isFainted());
                if (choosingPokemon){
                    if(partyPokemon.get(numberChosen-1).isFainted()){
                        System.out.println("This Pokemon has fainted\nPlease select a different Pokemon\n");
                        numberChosen = -2;
                    }
                }

                if(acceptZero && numberChosen == 0){
                    return -1;
                }
                //////fix cant choose pokemon if fainted

            }catch(Exception ex){ // dont say all the time: 'please enter a number'
                //System.out.println("Please enter a number on the list\n");
            }
            if((numberChosen > listSize) || (numberChosen <= 0) && numberChosen != -2){
                System.out.println("Please enter a number on the list\n");
            }
        }
        return numberChosen - 1;
    }

    private void selectPokemon(ArrayList<Pokemon> pokemonProfiles,
                               ArrayList<Pokemon> partyPokemon){
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


    private Pokemon choosePokemonToFight(ArrayList<Pokemon> partyPokemon){
        Pokemon p = partyPokemon.get(chooseNumber("Choose a Pokemon to fight: ", partyPokemon.size(), partyPokemon, true));
        System.out.printf("%s, I CHOOSE YOU!%n", p.getName());
        return p;
    }

    private void printActionMenu(Pokemon yourPokemon, Pokemon enemyPokemon, boolean printActions){
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

        actionMenuReturn returnItem = new actionMenuReturn();

        printActionMenu(yourPokemon, enemyPokemon, true);
        int actionInt = chooseNumber("Choose an action: ", actionType.values().length, partyPokemon);
        switch (actionInt) {
            case 0:
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
                    //System.out.println(returnItem.stunned);
                    //System.out.printf("%s used %s%n", yourPokemon.getName(), yourPokemon.getAttackList().get(attackInt).getName());
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
            //default:
        }

        return returnItem;
    }

    private boolean battle(Pokemon yourPokemon, Pokemon enemyPokemon, ArrayList<Pokemon> partyPokemon, ArrayList<Pokemon> enemyParty,
                           boolean playerSwitchedPokemon, boolean yourTurn){
        //System.out.printf("%30s", "BATTLE");
        boolean youreStunned = false; //what to do?
        actionMenuReturn menuReturn = new actionMenuReturn();

        while(!yourPokemon.isFainted() && !enemyPokemon.isFainted() && !menuReturn.changePokemon){
            boolean enemyAttacked = false;

            if (menuReturn.changePokemon){
                return menuReturn.switchPokemon;
            }
            else{
                //System.out.println(yourTurn);
                //System.out.println(!youreStunned);
                //System.out.println(!playerSwitchedPokemon);
                if (yourTurn && !youreStunned && !playerSwitchedPokemon){
                    menuReturn = actionMenu(yourPokemon, enemyPokemon, partyPokemon);
                    if (menuReturn.attacked){
                        yourTurn = false;
                    }
                }
                else if (youreStunned){
                    System.out.printf("%s has been stunned\n%s can't move%n",yourPokemon.getName(), yourPokemon.getName());
                    printActionMenu(yourPokemon, enemyPokemon, false);
                    System.out.println("(Click ENTER to pass)");
                    Scanner kb = new Scanner(System.in);
                    kb.nextLine();

                    youreStunned = false;
                    yourTurn = false;
                }
                if(enemyPokemon.isFainted()){
                    postBattle(partyPokemon, enemyPokemon);
                }

                if ((!yourTurn || playerSwitchedPokemon) && !menuReturn.stunned && !menuReturn.changePokemon && !enemyPokemon.isFainted()){
                    //cpu turn
                    playerSwitchedPokemon = false;
                    for (int i = 0; i < enemyPokemon.getAttackLength(); i++){
                        if (enemyPokemon.getEnergy() >= enemyPokemon.getAttackList().get(i).getEnergyCost()){
                            youreStunned = enemyPokemon.attack(i, yourPokemon);
                            enemyAttacked = true;
                            break;
                        }
                    }
                    if (!enemyAttacked){
                        System.out.printf("%s passed%n",enemyPokemon.getName());
                    }
                    yourTurn = true;
                }
                else{
                    if (menuReturn.stunned){
                        System.out.printf("%s has been stunned\n%s can't move%n",enemyPokemon.getName(), enemyPokemon.getName());
                    }
                    menuReturn.stunned = false;
                    yourTurn = true;
                }

            }
            if(menuReturn.attacked){
                recoverEnergy(partyPokemon, enemyParty);
            }
        }
        if (yourPokemon.isFainted()){
            postBattle(partyPokemon, yourPokemon);
        }
        return menuReturn.switchPokemon;
    }

    private void postBattle(ArrayList<Pokemon> partyPokemon, Pokemon p){
        System.out.println(p.getName()+" has fainted");
        healHP(partyPokemon);
        p.setDisabled();
    }

    private void healHP(ArrayList<Pokemon> partyPokemon){
        for (Pokemon p:partyPokemon){
            if (!p.isFainted()){
                p.recoverHP();
            }
        }
    }

    private void recoverEnergy(ArrayList<Pokemon> partyPokemon, ArrayList<Pokemon> enemyParty){
        for (Pokemon p: partyPokemon){
            p.recoverEnergy(); // ??
        }
        for (Pokemon p: enemyParty){
            p.recoverEnergy();
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

    private int findNextAvailablePokemon(ArrayList<Pokemon> pokemon){
        for (int i = 0; i < pokemon.size(); i++){
            if (!pokemon.get(i).isFainted()){
                return i;
            }
        }
        return 0;
    }

    private boolean changePokemon(){
        System.out.println("\nWould you like to switch Pokemon? [Y/N]");
        String change = "";

        while(!change.toUpperCase().equals("N") && !change.toUpperCase().equals("Y")){
            try{
                Scanner kb = new Scanner(System.in);
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

    public void run(){
        ArrayList<Pokemon> partyPokemon = new ArrayList<Pokemon>();
        ArrayList<Pokemon> enemyParty = new ArrayList<Pokemon>();
        ArrayList<Pokemon> pokemonProfiles = readFile();

        System.out.println("WELCOME TO POKEMON ARENA!\nHit Enter To Play");
        Scanner kb = new Scanner(System.in);
        kb.nextLine();

        printMenu(pokemonProfiles, "POKEMON ARENA MENU");
        selectPokemon(pokemonProfiles, partyPokemon); //Ask user to select partySize number of Pokemon

        enemyParty = new ArrayList<Pokemon>(pokemonProfiles);
        for (int i = 0; i < partySize; i++){
            enemyParty.remove(partyPokemon.get(i));
        }

        Collections.shuffle(enemyParty);

        boolean gameOver = false;
        printMenu(partyPokemon, "PARTY POKEMON");
        Pokemon enemyInBattle = enemyParty.get(findNextAvailablePokemon(enemyParty));
        Pokemon inBattle = choosePokemonToFight(partyPokemon);
        boolean switchPokemon = false;
        boolean playerSwitchedPokemon = false;
        boolean yourTurn = randomizeTurn();

        while (!gameOver) {

            //System.out.println(enemyPokemon.size());

            //System.out.println(playerSwitchedPokemon);
            switchPokemon = battle(inBattle, enemyInBattle, partyPokemon, enemyParty, playerSwitchedPokemon, yourTurn);   ///change enemy pokemon when fainted
            gameOver = isGameOver(partyPokemon, enemyParty);
            //System.out.println(enemyInBattle.getName() + " has " + enemyInBattle.getHP() + " hp");
            if (enemyInBattle.isFainted() || inBattle.isFainted()){
                yourTurn = randomizeTurn();
            }

            if(!gameOver) {
                playerSwitchedPokemon = false;

                if (switchPokemon) {
                    playerSwitchedPokemon = true;
                }

                if (!switchPokemon && !inBattle.isFainted()) {
                    switchPokemon = changePokemon();
                }
                if (switchPokemon || inBattle.isFainted()) {
                    printMenu(partyPokemon, "PARTY POKEMON");
                    inBattle = choosePokemonToFight(partyPokemon);
                    switchPokemon = false;
                }
                enemyInBattle = enemyParty.get(findNextAvailablePokemon(enemyParty));
            }
        }
    }

    private boolean randomizeTurn(){
        Random rand = new Random();
        int chance = rand.nextInt(2);
        if (chance == 0){
            return true;
        }
        return false;
    }



    public static void main(String[] args) {

        PokemonArena game = new PokemonArena();
        game.run();
    }
}