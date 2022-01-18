/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.zemris.ims;

import com.jme3.math.Vector3f;

import mmaracic.gameaiframework.AgentAI;
import mmaracic.gameaiframework.PacmanAgent;
import mmaracic.gameaiframework.PacmanVisibleWorld;
import mmaracic.gameaiframework.WorldEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 *
 * @author Marijo
 */
public class PacmanAI extends AgentAI{
	
    
    protected static class Location implements Comparable<Location>
    {
        int x=0,y=0;
        
        Location(int x, int y)
        {this.x=x; this.y=y;}
        
        int getX() {return x;}
        int getY() {return y;}
        
        @Override
        public boolean equals(Object o)
        {
            if (o instanceof Location)
            {
                Location temp = (Location) o;
                if ((temp.x==this.x) && (temp.y==this.y))
                    return true;
                else
                    return false;
            }
            else
                return false;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 79 * hash + this.x;
            hash = 79 * hash + this.y;
            return hash;
        }
        
        public float distanceTo(Location other)
        {
            int distanceX = other.x - x;
            int distanceY = other.y - y;
            
            return (float) Math.abs(distanceX) + Math.abs(distanceY);
//            return (float) Math.sqrt(distanceX*distanceX + distanceY+distanceY);
        }
        
        @Override
        public int compareTo(Location o) {
            if (x==o.x)
            {
                return Integer.compare(y, o.y);
            }
            else
            {
                return Integer.compare(x, o.x);
            }
        }
    }
    
    private Location myLocation = new Location(0, 0);
    
    private Date now = new Date();
    private Random r = new Random(now.getTime());
    
    private Location targetLocation = myLocation;
    private float targetDistance = Float.MAX_VALUE;
    private int targetDuration = 0;
    int [] lastLocation = {100,100};
    
    
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
        HashSet<Location> points = new HashSet<>();
        Location pointLocation = new Location(0, 0);
        int pointIndex = 0;
        boolean runningFromGhost = false;
    	pointIndex = 0;
    	
    	
        int radiusX = mySurroundings.getDimensionX()/2;
        int radiusY = mySurroundings.getDimensionY()/2;
               
        boolean powerUP = myInfo.hasProperty(PacmanAgent.powerupPropertyName);
        Vector3f pos = myInfo.getPosition();
//        printStatus("Location x: "+pos.x+" y: "+pos.y);
//        for(int[] k : moves)
//        printStatus("movesX = " + k[0] + "movesY = " + k[1]);
        
        float ghostDistance = Float.MAX_VALUE;
        Location ghostLocation = null;
        for (int i = -radiusX; i<=radiusX; i++)
        {
            for (int j = -radiusY; j<=radiusY; j++)
            {
                if (i==0 && j==0) continue;
                Location tempLocation = new Location(myLocation.getX()+i, myLocation.getY()+j);
                ArrayList<WorldEntity.WorldEntityInfo> neighPosInfos = mySurroundings.getWorldInfoAt(i, j);
                if (neighPosInfos != null)
                {
                    for (WorldEntity.WorldEntityInfo info : neighPosInfos)
                    {
                        if (info.getIdentifier().compareToIgnoreCase("Pacman")==0)
                        {
                            //Ignore myself
                        }
                        else if (info.getIdentifier().compareToIgnoreCase("Wall")==0)
                        {
                            //Its a wall, who cares!
                        }
                        else if (info.getIdentifier().compareToIgnoreCase("Point")==0 ||
                                info.getIdentifier().compareToIgnoreCase("Powerup")==0)
                        {
                            //Remember where it is!
                        	
//                        	printStatus("i = " + i + " j = " + j);
//                        	pointIndex = findClosest(moves, new int[]{i,j});
                        	
                        	int counter = 0;
                        	for(int[] k : moves) {
                        		if (k[0] == i && k[1] == j) {
                        			printStatus("i = " + i + " j = " + j + "counter = " + counter);
                        			pointIndex = counter;
                        		}
                        		counter++;
                        	}
                    		pointLocation = tempLocation;
                            float currPointDistance = myLocation.distanceTo(tempLocation);
                            points.add(tempLocation);
                        }
                        else if (info.getIdentifier().compareToIgnoreCase("Ghost")==0 && !myInfo.hasProperty(PacmanAgent.powerupPropertyName))		//ako vidi duha i nema powerup
                        {
                            //Remember him!
                            runningFromGhost = true;
                            float currGhostDistance = myLocation.distanceTo(tempLocation);
                            if (currGhostDistance<ghostDistance)
                            {
                                ghostDistance = currGhostDistance;
                                ghostLocation = tempLocation;
                            }
                        }
                        else
                        {
                            printStatus("I dont know what "+info.getIdentifier()+" is!");
                        }
                    }
                }
            }            
        }
        
      if(  myInfo.hasProperty(PacmanAgent.powerupPropertyName) ==  true) printStatus("Imam powerup!!!");

        //move toward the point
        //pick next if arrived
//        if (targetLocation==myLocation)
//        {
//            targetLocation = points.iterator().next();
//            targetDistance = myLocation.distanceTo(targetLocation);
//            targetDuration=0;
//        }
//
//         targetDuration++;
// 
//        //sticking with target too long -> got stuck
//        //dont get stuck
//       if (targetDuration>10)
//        {
//            ArrayList<Location> pointList = new ArrayList<>(points);
//            int choice = r.nextInt(pointList.size());
//            
//            targetLocation = pointList.get(choice);
//            targetDistance = myLocation.distanceTo(targetLocation);
//            targetDuration = 0;
//        }
       
            
        //select move
//        float currMinPDistance = Float.MAX_VALUE;
        Location nextLocation = myLocation;
        int moveIndex = 0;
                
        for (int i=moves.size()-1; i>=0; i--)
        {
            int[] move = moves.get(i);
            Location moveLocation = new Location(myLocation.getX()+move[0], myLocation.getY()+move[1]);
//            float newPDistance = moveLocation.distanceTo(targetLocation);
            float newGDistance=(ghostDistance<Float.MAX_VALUE)?moveLocation.distanceTo(ghostLocation):Float.MAX_VALUE;
            if (newGDistance>1 && !myInfo.hasProperty(PacmanAgent.powerupPropertyName))		//gledati info na plocama na koje se mozemo kretati (moves koordinate) i prioritizirati 
            															//one s pointsima ili rand ako ih nema, glavno da se ne ide na onu s ghostom
            {
                //that way
//                currMinPDistance = newPDistance;
                nextLocation = moveLocation;
                
                moveIndex = i;
//                printStatus("Index3 = " + moveIndex);
            }
            
        }
            if (runningFromGhost == false && !points.isEmpty()) {		//go after the points
//            	printStatus("Index = " + moveIndex);
//            	moveIndex = pointIndex;
//            	printStatus("Index1 = " + moveIndex);
            	
            	
                int choice = pointIndex;
                printStatus("lastLocation = " + lastLocation[0] + ", " + lastLocation[1]);
                printStatus("moves = " + moves.get(choice)[0] + ", " + moves.get(choice)[1]);
                 for(int j = 0; j < 10000; j++) {
                    	if (moves.get(choice)[0] == -lastLocation[0] && moves.get(choice)[1] == lastLocation[1] && moves.get(choice)[0] != 0 ||
                    		moves.get(choice)[0] == lastLocation[0] && moves.get(choice)[1] == -lastLocation[1] && moves.get(choice)[1] != 0)		//ako se odluci da idemo natrag trazi novu opciju, ako nema nove opcije onda idi natrag
                    		choice = r.nextInt(moves.size());
                    }
                 moveIndex = choice;
            	
            }
            else if (runningFromGhost == false && points.isEmpty()) {		//rand but not back if there isn't a point nearby
               int choice = r.nextInt(moves.size());
               printStatus("lastLocation = " + lastLocation[0] + ", " + lastLocation[1]);
               printStatus("moves = " + moves.get(choice)[0] + ", " + moves.get(choice)[1]);
               printStatus("choice prije = " + choice);
                for(int j = 0; j < 10000; j++) {
                   	if (moves.get(choice)[0] == -lastLocation[0] && moves.get(choice)[1] == lastLocation[1] && moves.get(choice)[0] != 0 ||
                   		moves.get(choice)[0] == lastLocation[0] && moves.get(choice)[1] == -lastLocation[1] && moves.get(choice)[1] != 0)		//ako se odluci da idemo natrag trazi novu opciju, ako nema nove opcije onda idi natrag
                   		choice = r.nextInt(moves.size());
                   }
                moveIndex = choice;
            }

        points.remove(myLocation);
        myLocation = nextLocation;
        points.remove(myLocation);
        
        lastLocation = moves.get(moveIndex);
        return moveIndex;
    }       
}

//ako se naiđe na duha treba se bježati u suprotnom smjeru