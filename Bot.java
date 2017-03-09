/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bots.SampleBot;

import amazebot2016.utils.Compass;
import java.awt.Point;
import static java.lang.Math.*;
/**
 *
 * @author jonathan.goode & maddie.hill
 */
public class Bot {
    
    public int xPos;
    public int yPos;
    public Point goal;
    private double[] bestD = new double[4];
    private Moves[][] moveSet = new Moves[29][29];
    public Bot(int x, int y, int l, int m){
        xPos=x;
        yPos=y;
        goal = new Point(l+1,m+1);
        for(int i=0;i<29;i++)
            for(int j=0;j<29;j++)
                moveSet[i][j]= new Moves();
    }
public int bestDirection(int oldDurr)
{
    Point east = new Point(xPos+1,yPos);
    Point west = new Point(xPos-1,yPos);
    Point north = new Point(xPos,yPos-1);
    Point south = new Point(xPos,yPos+1);
    int bestDurr=oldDurr;            
    bestD[1]= goal.distance(east);
    bestD[3]= goal.distance(west);
    bestD[0] = goal.distance(north);
    bestD[2] = goal.distance(south);
    double miniD=10000000;
    for(int i = 0;i<=3;i++){
        
        if(oldDurr != (i+2)%4){
            miniD=min(bestD[i],miniD);
            if(abs(miniD-bestD[i])<pow(10.0,-10))
                bestDurr = i;
            
        }
        
    }
    return bestDurr;
}
public int secondBestDirection(int oldDurr,int bestDurr){
    int newDurr=3;
    Point east = new Point(xPos+1,yPos);
    Point west = new Point(xPos-1,yPos);
    Point north = new Point(xPos,yPos-1);
    Point south = new Point(xPos,yPos+1);          
    bestD[1]= goal.distance(east);
    bestD[3]= goal.distance(west);
    bestD[0] = goal.distance(north);
    bestD[2] = goal.distance(south);
    double miniD=10000000;
    for(int i = 0;i<=3;i++){
        
        if(oldDurr != (i+2)%4 && i != bestDurr)
        {
            miniD=min(bestD[i],miniD);
            if(abs(miniD-bestD[i])<pow(10.0,-10))
                newDurr = i;
        }
    }
    return newDurr;
}
 public void MovePaws(int durr)
 {
     moveSet[xPos][yPos].addWentTo(durr);
     if(durr==0) yPos--;
     if(durr==1) xPos++;
     if(durr==2) yPos++;
     if(durr==3) xPos--;
     moveSet[xPos][yPos].addCameFrom((durr+2)%4);
 }
 public Point CheckPaws(int durr){
     Point p=new Point(xPos,yPos);
     if(durr==0) p =new Point(xPos,yPos-1);
     if(durr==1) p =new Point(xPos+1,yPos);
     if(durr==2) p =new Point(xPos,yPos+1);
     if(durr==3) p =new Point(xPos-1,yPos);
     return p;
 }
 public boolean checkWDirection(int d){
     
     return moveSet[xPos][yPos].checkWentTo(d);
 }
 public boolean checkCDirection(int d){
     return moveSet[xPos][yPos].checkCameFrom(d);
 }
 public Point getPaws(){
     return new Point(xPos,yPos);
 }
         
}
