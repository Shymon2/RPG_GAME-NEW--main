package MAP;
import java.io.*;
import java.util.*;
import CHARACTER.Character;
import CHARACTER.Monster;
import CHARACTER.Player;
import CHARACTER.RegularMonster;
import ITEM.*;
import MAP.TILE.*;


public class Map 
{
    private final int maxTileCols = 20;
    private final int maxTileRows = 20;
    private final int maxPossibleTypesOfTile = 4;

    private Tile[] tile;
    private int[][] tileManager;

    //private Character player;
    private List<Item> items;
    private List<Monster> monsters;

//--------------------------------------------------

    //Constructor
    public Map(String mapFilePath)
    {
        //Intialize items and monsters
        this.items = new LinkedList<>();
        this.monsters = new LinkedList<>();

        //Loading Map
        loadMap(mapFilePath);
    }

//------------------------------------------ Number of Items and Monster and possile types of tile---------------------------------------------------
    public int numberOfMonsters()
    {return this.monsters.size();}

    public int numberOfItems()
    {return this.items.size();}

    public int numberOfPossibleTypeOfTile()
    {return this.maxPossibleTypesOfTile;}

//---------------------------------------------- Getter and Setter Method ------------------------------------------------------

    public Monster getMonsterAtIndex(int index)
    {
        Monster monsterToGet = null;
        if(index < 0)
        {
            System.out.println("ERROR: Invalid index!");
        }
        else
            monsterToGet = this.monsters.get(index);
        return monsterToGet;
    }
    public Item getItemAtIndex(int index)
    {
        Item itemToGet = null;
        if(index < 0)
        {
            System.out.println("ERROR: Invalid index!");
        }
        else
            itemToGet = this.items.get(index);
        return itemToGet;
    }
    public int getTileManager_RowCol(int x, int y)
    {
        int result = -1;
        if(!outOfBorder(y, x))
        {
            result = this.tileManager[x][y];
        }
        return result;
    }
    public Tile getTile(int index)
    {
        Tile tileToGet = null;
        if(0 <= index && index < maxPossibleTypesOfTile)
        {
            tileToGet = this.tile[index];
        }
        return tileToGet;
    }
//---------------------------------------------- Load Map from File ------------------------------------------------------------

    public void loadMap(String mapFilePath)
    {
    //Define all possible types of tile on the map
        this.tile = new Tile[maxPossibleTypesOfTile];
        this.tile[0] = new WallTile();
        this.tile[1] = new LandTile();
        this.tile[2] = new WaterTile();
        this.tile[3] = new FireTile();

    //Initialize tileManager
        this.tileManager = new int[maxTileRows][maxTileCols];

    //Read Map From File
        File myFile = new File(mapFilePath);

        //Check if file does not exist
        if(!myFile.exists())
        {
            System.out.println("Unable to open file " + mapFilePath);
        }

        try 
        {
            //Open input file for reading
            Scanner inputFile = new Scanner(myFile);

            //Read line by line
            String line;
            for(int i = 0; i < maxTileRows; i++)            // i ~ y-coor of obj in xy plane
            {
                //Read every line in text file
                line = inputFile.nextLine();

                //Get tokens from the line
                String[] numbers = line.split(" ");

                //Load all tokens into int[][] tileManager
                for(int j = 0; j < maxTileCols; j++)        //j ~ x-coor of obj in xy plane
                {
                    tileManager[i][j] = Integer.parseInt(numbers[j]);
                }
            }

            //Close the file
            inputFile.close();
        } 
        catch (Exception e) 
        {
            System.out.println("Fail to open file!");
        }
    }


//--------------------------------  Diplaying Map + Objects on map ------------------------------------------------------------

    //Draw Map (draw every tile + items + monsters + player)
    public void drawMap(Player player)
    {
        for(int i = 0; i < maxTileRows; i++)        // i ~ y-coordinate
        {
            for(int j = 0; j < maxTileCols; j++)    // j ~ x-coordinate
            {    
                if(player != null && i == player.getY() && j == player.getX() )             //1. Draw player first
                {
                    tile[tileManager[i][j]].drawTile(player.getMark());   
                }
                else if(containMonsterAt(j, i))                                             //2. Draw monsters
                {
                    tile[tileManager[i][j]].drawTile(correspondingMonsterAt(j, i).getMark());
                }
                else if(containItemAt(j, i))                                                //3. Draw items
                {
                    tile[tileManager[i][j]].drawTile(correspondingItemAt(j, i).getMark());      
                }
                else                                                                        //4. Draw tiles
                {
                    tile[tileManager[i][j]].drawTile("");
                }
            }
            System.out.println("");
        }
    }



//---------------------------------------------- Searching Objects ------------------------------------------------------------

    //Check if position (x, y) contains any monster
    public boolean containMonsterAt(int x, int y)
    {
        boolean contain = false;
        for(int i = 0; i < this.monsters.size(); i++)
        {
            if(this.monsters.get(i).getX() == x && this.monsters.get(i).getY() == y)
            {
                contain = true;
                break;
            }
        }
        return contain;
    }
   
    
    //Find the monster in the list whose position is (x, y)
    public Monster correspondingMonsterAt(int x, int y)
    {
        Monster monsterToFind = null;
        for(int i = 0; i < monsters.size(); i++)
        {
            if(monsters.get(i).getX() == x && monsters.get(i).getY() == y)
            {
                monsterToFind = monsters.get(i);
                break;
            }
        }
        return monsterToFind;
    }


    //Check if position (x, y) contains any item
    public boolean containItemAt(int x, int y)
    {
        boolean contain = false;
        for(int i = 0; i < this.items.size(); i++)
        {
            if(this.items.get(i).getX() == x && this.items.get(i).getY() == y)
            {
                contain = true;
                break;
            }
        }
        return contain;
    }


    //Find the item in the list whose position is (x, y)
    public Item correspondingItemAt(int x, int y)
    {
        Item itemToFind = null;
        for(int i = 0; i < this.items.size(); i++)
        {
            if(this.items.get(i).getX() == x && this.items.get(i).getY() == y)
            {
                itemToFind = this.items.get(i);
                break;
            }
        }
        return itemToFind;
    }


    //Check if the area at position (x, y) is solid
    public boolean isSolidAt(int x, int y)
    {
        boolean solid = false;
        if(this.tile[tileManager[y][x]].getSolid() == true)
        {
            solid = true;
        }
        return solid;
    }

    //Check if the object out of the border
    public boolean outOfBorder(int x, int y)
    {
        boolean out = true;
        if(0 <= x && x < maxTileCols && 0 <= y && y < maxTileRows)
        {
            out = false;
        }
        return out;
    }
//---------------------------------------------- Setting Objects ------------------------------------------------------------

    //Adding the player in the map: position of player reset to (0, 0) by default
    /*
    public void addPlayer(Character player)
    {
        if(this.player != null)
        {
            System.out.println("ERROR: Cannot Add More Hero (Player) in This Map");
        }
        else
        {
            this.player = player;
            this.player.setX(0);
            this.player.setY(0);
        }
    }
    */


    //Adding a monster in the map
    public void addMonster(Monster monster)
    {
        if(!isSolidAt(monster.getX(), monster.getY()))
        {
            this.monsters.add(monster);
        }
        else
        {
            System.out.println("ERROR: You Cannot Set a Monster into a Solid Area!");
        }
    }


    //Adding an item in the map
    public void addItem(Item item)
    {
        if(item != null && !isSolidAt(item.getX(), item.getY()) && 0 <= item.getX() && item.getX() < maxTileCols && 0 <= item.getY() && item.getX() < maxTileRows)
        {
            this.items.add(item);
        }
        else
        {
            System.out.println("ERROR: You Cannot Set an Item into a Solid Area or Out Of Border!");
        }
    }


    //Remove a monster from map
    public boolean removeMonsterHavingPosition(int x, int y)
    {
        //Search monster having postion (x,y) in the list
        boolean found = false;
        for(Character monster: this.monsters)
        {
            if(monster != null && monster.getX() == x && monster.getY() == y)
            {
                found = true;
                this.monsters.remove(monster);
                break;
            }
        }

        if(!found)
        {
            System.out.println("ERROR: Not Found Corresponding Monster to Delete!");
        }     
        return found; 
    }


    //Remove an Item from map
    public boolean removeItemHavingPosition(int x, int y)
    {
       //Search item having postion (x,y) in the list
       boolean found = false;
       for(Item item: this.items)
       {
           if(item != null && item.getX() == x && item.getY() == y)
           {
               found = true;
               this.items.remove(item);
               break;
           }
       }    
       return found; 
    }

   

//---------------------------------------------- Check Validation of Moving ------------------------------------------------------------

    public boolean validMove(Character obj, int dx, int dy)
   {
        int x_toCome = obj.getX() + dx;
        int y_toCome = obj.getY() + dy;

        if(x_toCome >= maxTileCols || x_toCome < 0 || y_toCome >= maxTileRows || y_toCome < 0)   //if obj move out of border
        {
            return false;
        }
        else if(this.tile[tileManager[y_toCome][x_toCome]].getSolid() == true)     //if obj collides solid tile after moving
        {
            return false;
        }
        else if(containMonsterAt(x_toCome, y_toCome))     //if obj collides a monster after moving
        {
            return false;
        }
        else
        {
            return true;
        }
   }
   

//---------------------------------------------- Find Shortest Path ----------------------------------------------------------

    //O(V + E)
    public boolean findPath_BFS_Between(int x_start, int y_start, int x_end, int y_end, List<Pair> path)
    {
        boolean[][] visited = new boolean[maxTileRows][maxTileCols];
        for(int i = 0; i < maxTileRows; i++)
        {
            for(int j = 0; j < maxTileCols; j++)
            {
                visited[i][j] = false;
            }
        }
        
    
        Pair parent[][] = new Pair[maxTileRows][maxTileCols];
        for(int i = 0; i < maxTileRows; i++)
        {
            for(int j = 0; j < maxTileCols; j++)
            {
                parent[i][j] = new Pair(-1, -1);
            }
        }

        //1. Initialize an empty queue
        LinkedList<Pair> queue = new LinkedList<>();

        //2. Put source (starting point) into the queue
        queue.add(new Pair(x_start, y_start));
        parent[y_start][x_start] = new Pair(x_start, y_start);  //parent of starting point is itself
        visited[y_start][x_start] = true;                       

        //3. While the queue is not empty, then do:
        Pair front, adjacent;   
        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};
        int x_toCome, y_toCome;
        while(!queue.isEmpty())
        {
            //3.1.  Pop the front element from the queue
            front = new Pair(queue.get(0).getX(), queue.get(0).getY());   
            queue.removeFirst();

            //3.2.  Push all adjacent (unvisited) of that front into the queue
            for(int i = 0; i < 4; i++)              //4 possible adjacent (above, below, left, right vertex)
            {                                       //of that front
                x_toCome = front.getX() + dx[i];
                y_toCome = front.getY() + dy[i];
                
                //Check if an adjacent (among 4 possible cases) is actually an adjacent of the front
                if(0 <= x_toCome && x_toCome < maxTileCols && 0 <= y_toCome && y_toCome < maxTileRows && 
                    tile[tileManager[y_toCome][x_toCome]].getSolid() == false && visited[y_toCome][x_toCome] == false)
                {
                    adjacent = new Pair(x_toCome, y_toCome);
                    queue.add(adjacent);
                    parent[y_toCome][x_toCome] = new Pair(front.getX(), front.getY());      //parent of adjecent 
                    visited[y_toCome][x_toCome] = true;
                }
            }
        }

        //4. Tracing the path (if found)
        if(parent[y_end][x_end].getX() == -1 && parent[y_end][x_end].getY() == -1)  //not found path
        {
            System.out.println("DOES NOT EXIST PATH");
            return false;
        }
        else
        {
            /* 
            for(int i = 0; i < maxTileRows; i++)
            {
                for(int j = 0; j < maxTileCols; j++)
                {
                     System.out.println("Parent of " + j + " " + i + ":  " + parent[i][j].getX() + "<---->" + parent[i][j].getY());
                }
            }
            */
            
            //4.1 Tracing path using parent
            int tempX, tempY;
            while(x_end != x_start || y_end != y_start)     //while destination is not source
            {
                path.add(0, new Pair(x_end, y_end));        //Add at First
                //System.out.println("x_end = " + x_end + " y_end = " + y_end);
                //Swap replace destination cell by its parent

                //Update new destination = parent of old destination
                tempX = parent[y_end][x_end].getX();
                tempY = parent[y_end][x_end].getY();
                x_end = tempX;
                y_end = tempY;
            }
            path.add(0, new Pair(x_start, y_start));        //Add at First  
            return true;
        }
    }

    
   //Embedded Main
   public static void main(String[] args) 
   {
        
        Player hero = new Player("Hero", 0, 0, 0, 0, 0, 0);

        String path = "src/InputFile/map1.txt";
        Map map = new Map(path);
        map.drawMap(hero);
        //LinkedList<LinkedList<Integer>> vertice = new LinkedList<>();
        //map.to_EdgeList_Graph(vertice);
        //System.out.println(map.path(0, 0, 1, 0));
        
         
        List<Pair> Tpath = new LinkedList<>();
        map.findPath_BFS_Between(0, 0, 19, 19, Tpath);

          
        for(Pair p : Tpath)
        {
            System.out.println(p.getX() + " ---- " + p.getY());
        }   
   }
   
}








  
