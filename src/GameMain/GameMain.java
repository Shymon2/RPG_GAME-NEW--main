package GameMain;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

import CHARACTER.*;
import CHARACTER.Character;
import ITEM.*;
import MAP.*;

public class GameMain {
    
    
    public Player Hero(String name){
        Player player = new Player(name, 1000, 10, 0, 5, 0, 0);
        return player;
    }
    public String path = "src\\InputFile\\map1.txt";
    private Scanner input = new Scanner(System.in);

//--------------------------------------------------------------
    public void moveControl(Map m, Player p, Inventory i) {
        int choice;
        boolean status = true;
        do {
            System.out.println("\n------------------------------------------------------\n");
            System.out.println("1. Move Up");
            System.out.println("2. Move Down");
            System.out.println("3. Move Left");
            System.out.println("4. Move Right");
            System.out.println("5. No Move");
            System.out.println("6. Back to menu");
            System.out.print("Enter your choice: ");
            choice = input.nextInt();
            switch (choice) {
                case 1:
                    p.moveUp(m);
                    updateGame(m, p, i);                       
                    m.drawMap(p);
                    p.showState();
                    messageToShow(m, p);
                    break;

                case 2:
                    p.moveDown(m);
                    updateGame(m, p, i);
                    m.drawMap(p);
                    p.showState();
                    messageToShow(m, p);
                    break;

                case 3:
                    p.moveLeft(m);
                    updateGame(m, p, i);
                    m.drawMap(p);
                    p.showState();
                    messageToShow(m, p);
                    break;

                case 4:
                    p.moveRight(m);
                    updateGame(m, p, i);
                    m.drawMap(p);
                    p.showState();
                    messageToShow(m, p);
                    break;

                case 5:
                    updateGame(m, p, i);
                    m.drawMap(p);
                    p.showState();
                    messageToShow(m, p);
                    break;

                case 6:
                    status = false;
                    break;

                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        } while (status == true);
    }
    public void inventoryControl(Map m, Player p, Inventory i){
        System.out.println("\n------------------------------------------------------\n");
        if(i.isEmpty())         
            System.out.println("Empty inventory");
        else{
            i.displayInventory();
            System.out.println("Attack weapon: " + p.getCurrentWeapon());
            System.out.println("Defense weapon: " + p.getCurrentArmor());
            int choice, choice1;
            boolean status = true;
            do{              
                System.out.print("Enter a number to show item (Exit: 0 | Range: 1 - " + i.size() + "): ");
                choice = input.nextInt();
                if(choice == 0) 
                    status = false;
                else if(0 < choice && choice <= i.size()){
                    boolean status1 = true;
                    System.out.println("\n------------------------------------------------------\n");  
                    if(i.getItem(choice - 1) instanceof Weapon)
                        System.out.println((i.getItem(choice - 1)).toString());
                    else if(i.getItem(choice - 1) instanceof Armor)
                        System.out.println((i.getItem(choice - 1)).toString());
                    else if(i.getItem(choice - 1) instanceof Potion)
                        System.out.println(i.getItem(choice - 1).toString());
                    do{               
                        System.out.println("1. Use item");
                        System.out.println("2. Remove item");
                        System.out.println("3. Back");
                        System.out.print("Enter your choice: ");
                        choice1 = input.nextInt();
                        if(choice1 == 1){
                            if(i.getItem(choice - 1) instanceof Weapon)
                                p.equipWeapon(i.getItem(choice - 1));
                            else if (i.getItem(choice - 1) instanceof Armor)
                                p.equipArmor(i.getItem(choice - 1));
                            else if (i.getItem(choice -1) instanceof Potion){
                                p.equipPotion(i.getItem(choice -1));
                                i.removeItem(choice - 1);
                            }
                            System.out.println("Equip sucessfully");
                            System.out.println("\n------------------------------------------------------\n");
                            i.displayInventory();
                            System.out.println("Current weapon: " + p.getCurrentWeapon());
                            System.out.println("Current armor: " + p.getCurrentArmor());
                            status1 = false;
                        }       
                        else if(choice1 == 2){
                            if(i.getItem(choice - 1) instanceof Weapon && i.getItem(choice - 1).getInUse() == true)
                                p.unequipWeapon();
                            else if(i.getItem(choice - 1) instanceof Armor && i.getItem(choice - 1).getInUse() == true)
                                p.unequipArmor();
                            i.removeItem(choice - 1);
                            System.out.println("Remove sucessfully");
                            System.out.println("\n------------------------------------------------------\n");
                            i.displayInventory();
                            System.out.println("Current weapon: " + p.getCurrentWeapon());
                            System.out.println("Current armor: " + p.getCurrentArmor());
                            status1 = false;
                        }
                        else if(choice1 == 3){            
                            System.out.println("\n------------------------------------------------------\n");
                            i.displayInventory();
                            System.out.println("Current weapon: " + p.getCurrentWeapon());
                            System.out.println("Current armor: " + p.getCurrentArmor());
                            status1 = false;
                        }
                        else System.out.println("Invalid choice");
                    } while (status1 == true);          
                }
                else System.out.println("Invalid choice");
            }while(status == true);
            System.out.println("\n------------------------------------------------------\n");
            m.drawMap(p);
            p.showState();
        }
    }

    public boolean isInRange(Character obj1, Character obj2){
        boolean status = false;
        int max_X = obj1.getX() + obj1.getRange();
        int min_X = obj1.getX() - obj1.getRange();
        int max_Y = obj1.getY() + obj1.getRange();
        int min_Y = obj1.getY() - obj1.getRange();
        if(min_X <= obj2.getX() && obj2.getX() <= max_X && min_Y <= obj2.getY() && obj2.getY() <= max_Y)
            status = true;
        return status;
    }
    public void attackMenu(Map m, Player obj, Inventory j){
        ArrayList<Monster> targets = new ArrayList<Monster>();
        for(int i = 0; i < m.numberOfMonsters(); i++){
            if(isInRange(obj, m.getMonsterAtIndex(i)))
                targets.add(m.getMonsterAtIndex(i));
        }
        System.out.printf("|%10s | %20s | %10s |\n", "No.",
                                                       "Name",
                                                       "HP");
        for(int i = 0; i < targets.size(); i++){
            System.out.printf("|%10s | %20s | %10s |\n", i + 1, 
                    targets.get(i).getName() + "(" + targets.get(i).getMark() + ")",
                    targets.get(i).getHP() + "/" + targets.get(i).getMaxHp());
        }
        int choice;
        System.out.print("Choose a number (0: Exit || 1 - " + targets.size() + ") to attack monster: ");
        choice = input.nextInt();
        if(choice > 0){
            targets.get(choice - 1).takeDamage(obj.getAttack());
            updateGame(m, obj, j);
            m.drawMap(obj);
            obj.showState();
            targets.clear();
        }
        else if(choice < 0)
            System.out.println("Invalid choice");
        else{           
            System.out.println("\n------------------------------------------------------\n");
            m.drawMap(obj);
        }
    }


    public void MainMenu(Map m, Player obj, Inventory i){
        int choice;

        do
        {
            System.out.println("\n------------------------------------------------------\n");
            System.out.println("1. Move");
            System.out.println("2. Show Inventory");
            System.out.println("3. Attack");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            choice = input.nextInt();
            switch (choice) {
                case 1:
                    moveControl(m, obj, i);
                    break;
                case 2:
                    inventoryControl(m, obj, i);
                    break;
                case 3:
                    attackMenu(m, obj, i);         
                    break;
                case 4:
                    System.out.println("Thanks for playing");
                    break;               
                default:
                    System.out.println("Invalid choice");
                    break;
            }           
        } while(choice != 4);
    }
    public GameMain(){}
    public void StartGame(){
        String name, key;
        System.out.print("Enter name of Player: ");
        name = input.nextLine();
        Player player = Hero(name);
        player.showState();
        System.out.print("Press an key to start..........");
        key = input.nextLine();
        Inventory inventory = new Inventory(5);
        Map1 map1 = new Map1(this.path, player);
        map1.drawMap(player);
        MainMenu(map1, player, inventory);
    }
    public void updateGame(Map m, Player p, Inventory i){
        updatePlayer(m, p);
        updateItems(m, p, i);
        updateMonsters(m, p);
    }
    public void updatePlayer(Map m, Player p)
    {
        m.getTile(m.getTileManager_RowCol(p.getY(), p.getX())).applyEffectTo(p);
    }
    public void updateItems(Map m, Player p, Inventory i){
        if(m.containItemAt(p.getX(), p.getY())){ 
            if(!i.isFull()){
            i.addItem(m.correspondingItemAt(p.getX(), p.getY()));
            m.removeItemHavingPosition(p.getX(), p.getY());
            }
            else JOptionPane.showMessageDialog(null, "Inventory is full!!!");
        } 
    }
    public void updateMonsters(Map m, Player obj){
        for(int i = 0; i < m.numberOfMonsters(); i++){
            if(m.getMonsterAtIndex(i).getHP() == 0){
                m.addItem(m.getMonsterAtIndex(i).lootItem());
                m.removeMonsterHavingPosition(m.getMonsterAtIndex(i).getX(), m.getMonsterAtIndex(i).getY());
            }else{
            if(isInRange(m.getMonsterAtIndex(i), obj)){
                JOptionPane.showMessageDialog(null, "WARNING: " 
                                                + m.getMonsterAtIndex(i).getName() 
                                                + " attacked you. You lost " 
                                                + obj.takeDamage(m.getMonsterAtIndex(i).getAttack()) 
                                                + " HP!!!");
            }
            else{
                if(m.getMonsterAtIndex(i) instanceof RegularMonster)
                ((RegularMonster)m.getMonsterAtIndex(i)).randomMove(m);
                else if(m.getMonsterAtIndex(i) instanceof TargetMonster)
                ((TargetMonster)m.getMonsterAtIndex(i)).moveForwardTo(obj, m);;
            }
        }
        }
    }
    public void messageToShow(Map m, Player p){
        if(playerCollideAnyMonster(m, p))
            JOptionPane.showMessageDialog(null, "WARNING: Having monster in range!!!!");
    }
    public boolean playerCollideAnyMonster(Map m, Player p){
        boolean status = false;
        for(int i = 0; i < m.numberOfMonsters(); i++){
            if(isInRange(p, m.getMonsterAtIndex(i))){
                status = true;
                break;
            }
        }
        return status;
    }

    public static void main(String[] args) {
        GameMain gm = new GameMain();
        gm.StartGame();
    }
}
