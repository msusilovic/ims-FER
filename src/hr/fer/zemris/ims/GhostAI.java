/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.zemris.ims;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import mmaracic.gameaiframework.AgentAI;
import mmaracic.gameaiframework.PacmanAgent;
import mmaracic.gameaiframework.PacmanVisibleWorld;
import mmaracic.gameaiframework.WorldEntity;

/**
 *
 * @author Marijo
 */
public class GhostAI extends AgentAI{
    
    private Date now = new Date();
    private Random r = new Random(now.getTime());
    
    int [] lastLocation395 = {100,100};
    int [] lastLocation396 = {100,100};
    int [] lastLocation397 = {100,100};
    int [] lastLocation398 = {100,100};

    
    private int findClosest(ArrayList<int []> moves, int [] pos)
    {
        int index=0;
        int[] move = moves.get(0);
        float dist = Math.abs(pos[0]-move[0]) + Math.abs(pos[1]-move[1]);
        for(int i=1; i< moves.size(); i++)
        {
            move = moves.get(i);
            float currDist = Math.abs(pos[0]-move[0]) + Math.abs(pos[1]-move[1]);
            if (currDist<dist)
            {
                dist=currDist;
                index=i;
            }
        }
        return index;
    }
    
    @Override
    public int decideMove(ArrayList<int []> moves, PacmanVisibleWorld mySurroundings, WorldEntity.WorldEntityInfo myInfo)
    {
        int[] move=null;
                        
        
        for(int i=-mySurroundings.getDimensionX()/2; i<=mySurroundings.getDimensionX()/2;i++)
        {
            for(int j=-mySurroundings.getDimensionY()/2; j<=mySurroundings.getDimensionY()/2;j++)
            {
                if (i==0 && j==0) continue;
                //find pacman
                ArrayList<WorldEntity.WorldEntityInfo> elements = mySurroundings.getWorldInfoAt(i, j);
                HashMap<Integer,Object> metaHash = mySurroundings.getWorldMetadataAt(i, j);
                if (elements !=null && metaHash!=null)
                {
                    for(WorldEntity.WorldEntityInfo el : elements)
                    {
                        if (el.getIdentifier().compareToIgnoreCase("Pacman")==0)
                        {
                        	//metoda za trazenje najkrace udaljenosti do pacmana
                            int index = findClosest(moves, new int[]{i,j});
                        	//ako nema powerup idi prema njemu, ako ima onda ga ignoriraj
                            if (el.hasProperty(PacmanAgent.powerupPropertyName) == true) {
                                printStatus("pacman has powerup at " + index);
                                
                                
                                
                            }
                            
                            //pacman nema powerup i javljamo ostalima
                            else {
                                metaHash.clear();
                                metaHash.put(myInfo.getID(), moves.get(index));
                                printStatus("pacman at " + index);
                            }
                            return index;
                        }
                    }
                    //Check if someone else found him
                    if (!metaHash.isEmpty())
                    {
                        for (Integer id : metaHash.keySet())
                        {
                            if (id!=myInfo.getID())
                            {
                                move = (int[]) metaHash.remove(id);
                                //printStatus(myInfo.getID()+": Found pacman trail left by ghost: "+id+"!");
                            }
                        }
                    }
                }
            }
        }
        
        //Go where metadata pointed
        if (move!=null)
        {
            for(int i=0; i<moves.size(); i++)
            {
                int[] m=moves.get(i);
                if (m[0]==move[0] && m[1]==move[1])
                {
                    return i;
                }
            }
        }
        
        //Go random, but not back
    	int choice = r.nextInt(moves.size());

        if (myInfo.getID() == 395) {
            for(int i = 0; i < 10000; i++) {
            	if (moves.get(choice)[0] == -lastLocation395[0] && moves.get(choice)[1] == lastLocation395[1] && moves.get(choice)[0] != 0 ||
            		moves.get(choice)[0] == lastLocation395[0] && moves.get(choice)[1] == -lastLocation395[1] && moves.get(choice)[1] != 0)		//ako se odluci da idemo natrag trazi novu opciju, ako nema nove opcije onda idi natrag
            		choice = r.nextInt(moves.size());
            }
        	lastLocation395 = moves.get(choice);
    	return choice;
        }
        
        else if (myInfo.getID() == 396) {
            for(int i = 0; i < 10000; i++) {
            	if (moves.get(choice)[0] == -lastLocation396[0] && moves.get(choice)[1] == lastLocation396[1] && moves.get(choice)[0] != 0 ||
            		moves.get(choice)[0] == lastLocation396[0] && moves.get(choice)[1] == -lastLocation396[1] && moves.get(choice)[1] != 0)		//ako se odluci da idemo natrag trazi novu opciju, ako nema nove opcije onda idi natrag
            		choice = r.nextInt(moves.size());
            }
        	lastLocation396 = moves.get(choice);
    	return choice;
        }
        
        else if (myInfo.getID() == 397) {
            for(int i = 0; i < 10000; i++) {
            	if (moves.get(choice)[0] == -lastLocation397[0] && moves.get(choice)[1] == lastLocation397[1] && moves.get(choice)[0] != 0 ||
            		moves.get(choice)[0] == lastLocation397[0] && moves.get(choice)[1] == -lastLocation397[1] && moves.get(choice)[1] != 0)		//ako se odluci da idemo natrag trazi novu opciju, ako nema nove opcije onda idi natrag
            		choice = r.nextInt(moves.size());
            }
        	lastLocation397 = moves.get(choice);
    	return choice;
        }
        
        else if (myInfo.getID() == 398) {
           for(int i = 0; i < 10000; i++) {
           	if (moves.get(choice)[0] == -lastLocation398[0] && moves.get(choice)[1] == lastLocation398[1] && moves.get(choice)[0] != 0 ||
           		moves.get(choice)[0] == lastLocation398[0] && moves.get(choice)[1] == -lastLocation398[1] && moves.get(choice)[1] != 0)		//ako se odluci da idemo natrag trazi novu opciju, ako nema nove opcije onda idi natrag
           		choice = r.nextInt(moves.size());
           }
        //printStatus(lastLocation398[0] + " " + lastLocation398[1]);
       	lastLocation398 = moves.get(choice);
        //printStatus(lastLocation398[0] + " " + lastLocation398[1]);
       	return choice;
       }
       else return 0;
       };
    }

//0 je prvi izbor, 1 je drugi, itd.
//398 je prvi uz vrata, brojevi idu od 395 do 398
//ako pacman ima powerup, treba bjezati u suprotnom smjeru