package Pokemon;

//Attack.java

enum attackType {Stun, Wild_Card, Wild_Storm, Disable, Recharge, None};

public class Attack {
    private String name;
    private int energyCost, damage;
    private attackType special;

    private void setAttack(String name, int energyCost, int damage, attackType special){
        this.name = name;
        this.energyCost = energyCost;
        this.damage = damage;
        this.special = special;
    }
    public Attack(String name, String energyCost, String damage, String special){
        setAttack(name, Integer.parseInt(energyCost), Integer.parseInt(damage), stringToAttackType(special));
    }

    private attackType stringToAttackType(String type){
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
        System.out.printf("%4s %-1s %-2d %-13s ENERGY COST: %-5d DAMAGE: %-5d SP. ATK: %s%n", (char)149, "ATK", attackNumber,  name,
                energyCost, damage, (special==attackType.None)?"-":special.toString().replace("_", " "));
        //new String(Character.toChars(0x0A66))

    }

    public int getDamage(){
        return damage;
    }

    public int getEnergyCost(){
        return energyCost;
    }

}
