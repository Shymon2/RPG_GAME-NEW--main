package CHARACTER;

import java.util.LinkedList;
import java.util.List;
import MAP.Map;
import java.util.Random;

import ITEM.*;
import MAP.Pair;

public class TargetMonster extends Monster
{
 



//-----------------------------------------------------------

    //Constructor
    public TargetMonster(String name, int maxHP, int atk, int def, int x, int y) 
    {
        super(name, maxHP, atk, def, 1, x, y); 
        this.setItemToDrop();   
        this.type = 2;
    }

//----------------------------- Override Methods -------------------------------------------------
    @Override
    public String getMark() 
    {
        return "TM";
    }


    @Override
    public void setItemToDrop() 
    {
        this.itemsToDrop.add(new Armor("Shield", 10, 10, this.getX(), this.getY()));
        this.itemsToDrop.add(new Armor("Shield", 10, 10, this.getX(), this.getY()));
        this.itemsToDrop.add(new Potion("Big Potion", 40, this.getX(), this.getY()));
        this.itemsToDrop.add(new Potion("Small Potion", 10, this.getX(), this.getY()));
        this.itemsToDrop.add(new Potion("Small Potion", 10, this.getX(), this.getY()));
        this.itemsToDrop.add(new Potion("Small Potion", 10, this.getX(), this.getY()));
        this.itemsToDrop.add(new Potion("Small Potion", 10, this.getX(), this.getY()));
    }


    @Override
    public Item lootItem() 
    {
        Random random = new Random();
        int ranNum = random.nextInt(100) + 1;       //1,2,3,....,100

        Item itemToLoot = null;
        //Loot root = 60%
        if(ranNum <= 60)
        {
            ranNum = random.nextInt(itemsToDrop.size()) + 1;            //1,2,3, .... size of itemToDrop
            itemToLoot = itemsToDrop.get(ranNum - 1);
            itemToLoot.setXY(this.getX(), this.getY());  
        }
        
        return itemToLoot;     
    }
//----------------------------- Method Move ---------------------------------------------------------

    public void moveForwardTo(Character player, Map map)
    {
        List<Pair> path = new LinkedList<>();
        int currentStep = 0;
        if(map.findPath_BFS_Between(this.getX(), this.getY(), player.getX(), player.getY(), path))
        {
            //Search the current position of monster, compared to the path
            for(int i = 0; i < path.size(); i++)
            {
                if(path.get(i).getX() == this.getX() && path.get(i).getY() == this.getY())
                {
                    currentStep = i;
                    break;
                }
            }


            //Navigate monster to follow the correct path
            if(currentStep < path.size() - 1)       //if the monster does not reach target
            {
                int dx = path.get(currentStep + 1).getX() - this.getX();
                int dy = path.get(currentStep + 1).getY() - this.getY();
                if(dx == 0 && dy == -1)
                {
                    this.moveUp(map);
                }
                else if(dx == 0 && dy == 1)
                {
                    this.moveDown(map);
                }
                else if(dx == -1 && dy == 0)
                {
                    this.moveLeft(map);
                }
                else if(dx == 1 && dy == 0)
                {
                    this.moveRight(map);
                }
            }
        }
        else        //random move if does't find path
        {
            Random random = new Random();
            int ranNum = random.nextInt(100) + 1;
    
            if(ranNum <= 25)
            {
                this.moveUp(map);
            }
            else if (25 < ranNum && ranNum <= 50)
            {
                this.moveDown(map);
            }
            else if (50 < ranNum && ranNum <= 75) 
            {
                this.moveLeft(map);
            }
            else
            {
                this.moveRight(map);
            }
        }


    }


 
    public static void main(String[] args) 
    {
        TargetMonster monster = new TargetMonster("monster", 0, 0, 0, 0, 0);
        
        Item item = monster.lootItem();

        if(item == null)
        {
            System.out.println("null");
        }
        else
        {
            System.out.println(item);
        }
        
    }
}
