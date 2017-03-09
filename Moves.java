package bots.SampleBot;

import java.awt.Point;
/**
 *
 * @author jonathan.goode & madeleine.hill
 */
public class Moves {
    
    private boolean[] from;
    private boolean[] to; 
    
    public Moves(){
        
        from = new boolean[4];
        to = new boolean[4];
        
        for(int i=0;i<4;i++){
            from[i]=false;
            to[i]=false;
        }
    }
   public void addCameFrom(int x){
       from[x]=true;
   }
   public void addWentTo(int x){
       to[x]=true;
   }
   public boolean checkCameFrom(int x){
       return from[x];
   }
   public boolean checkWentTo(int x){
       return to[x];
   }
}
