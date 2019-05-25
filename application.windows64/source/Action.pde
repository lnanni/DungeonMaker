public class Action {

    private ActionType action;
    private PVector pos,end;
    private int index;  

    public JSONObject serialize(){
        JSONObject json = new JSONObject();
        json.setString("pos",pos.toString());
        if(end != null)
            json.setString("end",end.toString());
        json.setString("ActionType",action.toString());
        json.setInt("index",index);
        return json;

    }    
  
  

    public Action (ActionType action, PVector pos) {
        this.action = action;
        this.pos = pos;
    }

    public Action (ActionType action, PVector pos, int index) {
        this.action = action;
        this.pos = pos;
        this.index = index; 
    }

    public Action (ActionType action, PVector init, PVector end) {
        this.action = action;
        this.pos = init;
        this.end = end; 
        this.index = 0;  
    }
    public Action (ActionType action, PVector init, PVector end, int index) {
        this.action = action;
        this.pos = init;
        this.end = end; 
        this.index = index;  
    }

    /*public void drawMidPoints(){
     for(PVector p : midPoints){
       println(p);
       circle(p.x, p.y, 10);
     }
    }*/

    public void draw(){
        switch (this.action) {            
            case Linha :
                fill(51); 
                line(pos.x, pos.y, end.x, end.y );
                fill(255);
                
            break;

            case Traco : 
               dashed.pattern(10, 5); 
               dashed.line(pos.x, pos.y, end.x, end.y );
            break;

            case Ponto :
                dashed.pattern(5, 10);
                dashed.line(pos.x, pos.y, end.x, end.y );
            break;

            case Bezier :
                pushMatrix();
                dashed.pattern(5, 10);
                if(index == 0)
                  dashed.bezier(pos.x, pos.y, end.x, pos.y, pos.x, end.y, end.x, end.y);
                if(index == 1)
                  dashed.bezier(pos.x, pos.y, pos.x, end.y, end.x, pos.y,  end.x, end.y);
                popMatrix();
            break;

            case  Circulo: 
                pushMatrix(); 
                circle(pos.x, pos.y, 50);
                fill(51);
                textSize(25);
                if(index > 0){
                  int offset = 7;
                  if ( index > 9) 
                      offset = 14;
                  text(this.index, (pos.x)-offset, pos.y+7);
                }
                fill(255); 
                popMatrix();
            break;

            case Cubo:
                rectMode(CENTER);
                square(pos.x, pos.y, 50);
            break;

            case Arc:
                arc(pos.x-25, pos.y+25,150,150, 0, PI);
            break;

            case Triangulo:
                int[] a = {-22, 22, 0, -20, 22, 22};
                triangle(pos.x+a[0], pos.y+a[1], pos.x+a[2], pos.y+a[3], pos.x+a[4], pos.y+a[5]);
                //TODO: add text (N, S, L, O)
            break;
        }

    }
    public boolean isOver(int x, int y){
      return PVector.dist(pos,new PVector(x,y)) < 50;
    }

    public boolean isSafe (){
        return(this.action == ActionType.Circulo||this.action == ActionType.Cubo||this.action == ActionType.Triangulo);

    } 
    
   
    public void splitLine(PVector start, PVector end, int level, ArrayList<PVector> lst){
      if(level == 4) {
       lst.add( end.sub(start)); 
        return;
      }
       
       splitLine(start.copy(),end.copy().div(2), level+1,lst);
       splitLine(end.copy().div(2),end.copy(), level+1,lst);
       return;
    }
     
}

public enum ActionType{
    Circulo, Linha, Traco, Ponto, Bezier, Cubo, Arc, Triangulo;

}
