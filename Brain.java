/******************************************************
 * Authors: Jonathan Goode & Madeleine Hill
 * Title: Amazebot 2016
 * Script: Brain.java
 * Description: Brain decides where to go in the maze
 *****************************************************/

package bots.SampleBot;

import amazebot2016.*;
import amazebot2016.utils.Compass;
import java.awt.Point;
import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import static java.lang.Math.abs;
import static java.lang.Math.min;

//VERY IMPORTANT: 0 = north, 1 = east, 2 = south, 3 = west

public class Brain extends BotBrain {
	@Override
	public String getName() {
		return "AM16_032";
	}
        int newDirection=3;//Maddie likes west
        boolean decided;
        final int UNKNOWN = 0;
        final int OPEN = 1;
        final int WALL = 2;
        int[][] map = new int[29][29];

        ArrayList<Point> history = new ArrayList<Point>();
        public String p[] = new String[4];
        Bot MazeBot;
	@Override
	public void run() {
            p[0]="North";
            p[1]="East";
            p[2]="South";
            p[3]="West";
            Point p = getStartPosition();
            // The program would sometimes add 10 to the x value, but not always 
            // which compromised the distance calculations. This bit of code is
            // to fix that issue.
            int a=p.x;
            int b= p.y;
            if (a < p.x) a += p.x-a;
            if (a > p.x) a -= a-p.x;
            if (b < p.y) b += p.y-b;
            if (b > p.y) b -= b-p.y;
            int c=a;
            int d=b;
            p = getGoalCorner();
            a=p.x;
            b= p.y;
            if (a < p.x) a += p.x-a;
            if (a > p.x) a -= a-p.x;
            if (b < p.y) b += p.y-b;
            if (b > p.y) b -= b-p.y;
            MazeBot= new Bot(c,d,a,b);
            history.add(new Point(c,d));
            runMaze();
              
        }

// A forever loop that will run until the maze is solved or the bot exhausts
private void runMaze(){
    Point Po;
    boolean done = false;
    int bestDurr,secondDurr;
    for(;;){
        decided=false;
        // To fill our map array, we look in each direction (if we haven't before)
        // so that we make good choices later with minimal energy spent
        for (int i=0;i<=3;i++) {
            Po = MazeBot.CheckPaws(i);
            if (goodToGo(Po) && map[Po.x][Po.y]==UNKNOWN) {
                if(look(p[i])) {
                    map[Po.x][Po.y] = OPEN;
                } else {
                    map[Po.x][Po.y] = WALL;
                }
            }
        }
        bestDurr = MazeBot.bestDirection(newDirection);
        secondDurr = MazeBot.secondBestDirection(newDirection, bestDurr);
        
        // Find the best direction to go in during this iteration
        findDirection(newDirection,bestDurr,secondDurr);
        dontBeSilly();
        
        // Moves in the determined direction and puts it in the history array
        move(p[newDirection]);
        MazeBot.MovePaws(newDirection);
        history.add(new Point(MazeBot.xPos,MazeBot.yPos));
        
        // Check for flutter or square to stop the bot from being whacko
        if(history.size()>4){
            if(isFlutter()){
                runBackHistory();  
            }
            if(isSquare()) {
                runBackHistory();
            }
        }
        
        // After each iteration (and any other time we move) we add fake walls 
        // for areas that we have been to and want to avoid later.
        blockItOff();
        if (done) break;
    }
}

// This function checks to see if there is an open square not in our history
// at our current position so that the bot goes there no matter what instead of
// somewhere it's already been.
public void dontBeSilly(){ 
    if(history.contains(MazeBot.CheckPaws(newDirection))){
        Point Po;
        for(int i=0;i<=3;i++)
            if(i != newDirection){
                Po=MazeBot.CheckPaws(i);
                if(goodToGo(Po) && map[Po.x][Po.y]==OPEN && !history.contains(Po)){
                    newDirection=i;
                    break;
                }
            }
    }
}

// This function checks if the bot fluttered (went back and forth or is on a WALL
// spot after an area was blocked off.
public boolean isFlutter(){
    if(history.get(history.size()-1).equals(history.get(history.size()-3))
            && history.get(history.size()-2).equals(history.get(history.size()-4))){
        return true;
    } else if(map[MazeBot.xPos][MazeBot.yPos] == WALL){
        return true;
    } else
        return false;  
}

// Make sure the bot stays in the 28x28 grid
public boolean goodToGo(Point Po){
    if(Po.x<=28 && Po.x>=0 && Po.y>=0 && Po.y<=28)
        return true;
    else
        return false;
}

// Changes newDirection based on our list of priorities
// PRIORITIES: 1) best direction (unless been to)
//             2) current direction
//             3) second best direction
//             4) backwards
// TO DO: check to see if changing 2 & 3 is more efficient
public void findDirection(int currDurr,int bestDurr,int secondDurr) {
    decided=false;
    Point Po=MazeBot.CheckPaws(bestDurr);
    if(MazeBot.checkWDirection(bestDurr) || map[Po.x][Po.y] == WALL){
            if (bestDurr != currDurr){
                Po=MazeBot.CheckPaws(currDurr);
                if(goodToGo(Po) && !MazeBot.checkWDirection(currDurr) && map[Po.x][Po.y] != WALL){
                    decided=true;
                    newDirection= currDurr;
                }
                if (!decided) {
                    Po=MazeBot.CheckPaws(secondDurr);
                    if(goodToGo(Po) && secondDurr != currDurr && !MazeBot.checkWDirection(secondDurr) && map[Po.x][Po.y] != WALL){
                        decided=true;
                        newDirection= secondDurr;
                    }
                }
                if (!decided) {
                    Po =  MazeBot.CheckPaws((bestDurr+2)%4);   
                    if(goodToGo(Po) && !MazeBot.checkWDirection((bestDurr+2)%4) && map[Po.x][Po.y] != WALL){
                        decided=true;
                        newDirection= (bestDurr+2)%4;
                    }
                }
                if (!decided) {  
                    decided = true;
                    newDirection = (currDurr+2)%4;
                }
                    
            } else {
                Po = MazeBot.CheckPaws(secondDurr); 
                if(goodToGo(Po) && !MazeBot.checkWDirection(secondDurr) && map[Po.x][Po.y] != WALL){
                    decided = true;
                    newDirection = secondDurr;
                }
                if (!decided) {
                    Po = MazeBot.CheckPaws((secondDurr+2)%4);
                    if(goodToGo(Po) && !MazeBot.checkWDirection((secondDurr+2)%4) && map[Po.x][Po.y] != WALL){
                        decided = true;
                        newDirection = (secondDurr+2)%4;
                    }
                }
                if (!decided) {
                    decided = true;
                    newDirection = (currDurr+2)%4;
                }
            }
        } else {
            decided = true;
            newDirection = bestDurr;
        }
}

// Distance between points bitches
public double dbp(Point a,Point b){
    return sqrt(pow(a.x-b.x,2)+pow(a.y-b.y,2));
}

// The function checks whether the bot moved in a 2x2 square
public boolean isSquare() {
    if (history.get(history.size()-1).equals(history.get(history.size()-5))) {
        Point Po1 = history.get(history.size()-1),Po2 = history.get(history.size()-2),
                Po3=history.get(history.size()-3),Po4=history.get(history.size()-4);
        if(dbp(Po1,Po3)<1.5 && dbp(Po1,Po3)>1.0 && dbp(Po2,Po4)<1.5 && dbp(Po2,Po4)>1.0){
            return true;
        }
        else return false;
    } else return false;
    
}

// If flutter or square is determined, this function goes through the history and
// finds the first spot in the history that has an open square beside it that the
// bot has not been to, and travels back through the history to that point (efficiently
// by skipping chunks seamlessly) in hopes that the new open point is good.
// TO DO: pick the open point that is closest to the goal if there are multiple
public void runBackHistory() {
    Point Po = new Point(0,0),Po2;
    Point tempPo=new Point(0,0),tempGoal= new Point(0,0);
    int diffX=1,diffY=0;
    boolean dobreak=false;
    int ind = history.size()-1;
    int ind2=ind;
    Po2=history.get(ind);
    while(ind>0 && !dobreak){
        ind--;
        Po = history.get(ind);
        if(Po.equals(Po2)){
            ind2=ind;
        }
        for(int i=-1;i<=1;i++){
            for(int j=-1;j<=1;j++){
                if(i != j && i != -j){
                    tempPo= new Point(Po.x+i,Po.y+j);
                    if(goodToGo(tempPo) && map[tempPo.x][tempPo.y]==OPEN && !history.contains(tempPo)){
                        dobreak=true;
                        tempGoal=tempPo;
                    }            
                }
                if(dobreak)break;
            }
            if (dobreak)break;
        }
    }
    Po=history.get(ind2);
    while(ind2 > ind){
        ind2--;
        tempPo=history.get(ind2);
        diffX=tempPo.x-Po.x;
        diffY=tempPo.y-Po.y;
        if(diffX!=0){
            diffX=(diffX+4)%4;
            move(p[diffX]);
            MazeBot.MovePaws(diffX);
        } else {
            diffY=(diffY+1)%4;
            move(p[diffY]);
            MazeBot.MovePaws(diffY);
        }
        history.add(MazeBot.getPaws());
        Po=tempPo;
        blockItOff();
    }
    diffX=tempGoal.x-Po.x;
    diffY=tempGoal.y-Po.y;
    if(diffX!=0){
        diffX=(diffX+4)%4;
        move(p[diffX]);
        MazeBot.MovePaws(diffX);
    } else {
        diffY=(diffY+1)%4;
        move(p[diffY]);
        MazeBot.MovePaws(diffY);
    }
    history.add(MazeBot.getPaws());
    blockItOff();
}

// This function adds walls to areas that are deadends and removes it from history
// so that the bot avoids these areas in the next iteration.
public void blockItOff(){
    int size=history.size()-1;
    Point Po = history.get(size),tempPo;
    Boolean BlockIt = true;
    int ind1=size;
    int minX=100,minY=100,maxX=-100,maxY=-100;
    for(int i=size-1;i>=0;i--){
        tempPo=history.get(i);
        if(tempPo.equals(Po)){ // possible deadends occur if bot makes a loop
            ind1=i;
            break;
        }
    }
    if(ind1 != size){
        // find the size of the possible deadend area 
        for(int k= size-1;k>ind1;k--){
            Po=history.get(k);
            if(Po.x<minX)minX=Po.x;
            if(Po.x>maxX)maxX=Po.x;
            if(Po.y<minY)minY=Po.y;
            if(Po.y>maxY)maxY=Po.y;
            
        }
        System.out.println("xrange: " + minX + "," + maxX + "yrange: " + minY + "," + maxY);
        Po=history.get(size);
        for(int i = minX-1;i<=maxX+1;i++){
            for(int j = minY-1;j<=maxY+1;j++){
                tempPo=new Point(i,j);
                // determine if area is infact a deadend, i.e. surround by walls or boundaries
                if((i == minX-1 || i == maxX+1 || j == minY-1 || j == maxY+1) 
                        && !tempPo.equals(new Point(minX-1,minY-1))
                        && !tempPo.equals(new Point(minX-1,maxY+1))
                        && !tempPo.equals(new Point(maxX+1,minY-1))
                        && !tempPo.equals(new Point(maxX+1,maxY+1))){
                    if(goodToGo(tempPo) && map[i][j]!=WALL && !tempPo.equals(Po)){
                       BlockIt=false;
                    } else if(minX==0 && maxX==28 && minY==0 && maxY==28){
                        BlockIt=false;
                    }
                }
                if(!BlockIt)break;
            }
            if(!BlockIt)break;
        }
        if(BlockIt){ // if area is deadend then add walls to the area and remove from history
            System.out.println(history.get(size));
            System.out.println(minX + "," + maxX + "   " + minY + "," + maxY);
            for(int i=size-1;i>ind1;i--){
                tempPo=history.get(i);
                
                history.remove(i);
                if(map[tempPo.x][tempPo.y]!=WALL){
                    map[tempPo.x][tempPo.y]=WALL;
                    System.out.println(tempPo.toString() + " new Wall");
                }
            }
            history.remove(ind1);
        }
    }
    //System.out.println("The history at point" + history.get(history.size()-1));
    //System.out.println(history.toString());
}
}

