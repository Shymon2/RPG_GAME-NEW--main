package CHARACTER;

import ITEM.*;

public class Player extends Character
{
 
    private Item weapon  = null;
    private Item armor = null;

//-------------------------------------------

    //Constructor
    public Player(String name, int maxhp, int atk, int def, int range, int x, int y)
    {
        super(name, maxhp, atk, def, range, x, y);
    }


    @Override
    public String getMark()
    {return "X";}

    //Show state of player
    public void showState()
    {
        System.out.println("HP: " + this.getHP() + " / " + this.getMaxHp());
        System.out.println("Attack: " + this.getAttack());
        System.out.println("Defense: " + this.getDefense());
        System.out.println("Range: " + this.getRange());
        System.out.println("Current Weapon: " + (weapon != null ? weapon.getName() : "None"));
        System.out.println("Current Armor: " + (armor != null ? armor.getName() : "None"));
    }

    //Get Weapon and Armor
    public String getCurrentWeapon(){
        if(weapon == null){
            return "No weapon";
        }
        else return this.weapon.toString();
    }
    public String getCurrentArmor(){
        if(armor == null){
            return "No weapon";
        }
        else return this.armor.toString();
    }
    
    //How to unequip attack weapon from player
    public void unequipWeapon()
    {
        if(this.weapon != null)
        {
            //Remove current attack weapon (if exist) with its effect and state of use
            this.weapon.setInUse(false);
            this.weapon.unapplyEffectTo(this);
            this.weapon = null;
        }
    }

    //How to unequip defensive weapon from player
    public void unequipArmor()
    {
        if(this.armor != null)
        {
            //Remove current defensive weapon (if exist) with its effect and state of use
            this.armor.setInUse(false);
            this.armor.unapplyEffectTo(this);       
            this.armor = null;
        }
    }


    //How to equip potion to heal
    public void equipPotion(Item itemToEquip)
    {      
        // Pro max
        
        if (itemToEquip instanceof Potion){
            itemToEquip.setInUse(true);
            itemToEquip.applyEffectTo(this);
        }
        
    }
    
    public void equipWeapon(Item itemToEquip)
    {
        //Identify which type of item
        boolean isWeapon = false;
        if(itemToEquip.getType() == 1)
        {
            isWeapon = true;
        }

        //Equip Item
        if(!isWeapon)
        {
            System.out.println("ERROR: Invalid Item To Equip");
        }
        else
        {
             //1. Remove current attack weapon with corresponding effect
             this.unequipWeapon();

             //2. Equip new attack weapon with corresponding effect and change state of use
             itemToEquip.setInUse(true);
             this.weapon = itemToEquip;
             this.weapon.applyEffectTo(this);           
        }
    } 


    public void equipArmor(Item itemToEquip)
    {
        //Identify which type of item
        boolean isArmor = false;
        if(itemToEquip.getType() == 2)
        {
            isArmor = true;
        }

        //Equip Item
        if(!isArmor)
        {
            System.out.println("ERROR: Invalid Item To Equip");
        }
        else
        {
             //1. Remove current attack weapon with corresponding effect
             this.unequipArmor();

             //2. Equip new attack weapon with corresponding effect and change state of use
             itemToEquip.setInUse(true);
             this.armor = itemToEquip;
             this.armor.applyEffectTo(this);           
        }
    } 


    public static void main(String[] args) 
    {
        Item potion = new Weapon(null, 0, 0, 0, 0);
        System.out.println(potion instanceof Item);
    }


    
}

