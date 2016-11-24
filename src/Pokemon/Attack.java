package Pokemon;

enum attackType {STUN, WILD_CARD, WILD_STORM, DISABLE, RECHARGE, NONE};

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
                returnType = attackType.STUN;
                break;
            case "wild card,":
                returnType = attackType.WILD_CARD;
                break;
            case "wild storm":
                returnType = attackType.WILD_STORM;
                break;
            case "disable":
                returnType = attackType.DISABLE;
                break;
            case "recharge":
                returnType = attackType.RECHARGE;
                break;
            default:
                returnType = attackType.NONE;
        }

        return returnType;

    }
}
