import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 
import garciadelcastillo.dashedlines.*; 
import java.time.Instant; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class nodes extends PApplet {




DashedLines dashed;  
ControlP5 cp5;


PVector initialPoint;
int toSave = 0;
int last;
MapHandler map;



public void setup() {
  cp5 = new ControlP5(this); 
  map = new MapHandler();
   
  dashed = new DashedLines(this);
  menu();
 
}


public void draw() {
  print();
  toGrid();

}

 
public void print() {
  strokeWeight(1);
  background(255);
  if (toSave == 0) {
    line(0, mouseY, width, mouseY);
    line(mouseX, 0, mouseX, height);
  } 
  
  map.draw();

  if (toSave == 0) {
    fill(51);
    text(last, 50, 50);
    fill(255);
  }
}
 

public void mouseReleased() {
  if (mouseY > height - 50) 
      return;
  if (last == '1') {
    map.add( new Action(ActionType.Circulo, toGrid(), modifier ? -1 : map.nextNumber()+1));
  }
  if (last == '6') {
    map.add( new Action(ActionType.Cubo, toGrid()));
  }
  if (last == '2' ) {
    map.add( new Action(ActionType.Linha, initialPoint, toGrid()) );
  }
  if (last == '3' ) {
    map.add( new Action(ActionType.Traco, initialPoint, toGrid()) );
  }
  if (last == '4' ) {
    map.add( new Action(ActionType.Ponto, initialPoint, toGrid()) );
  }
  if (last == '5' ) {
    map.add( new Action(ActionType.Bezier, initialPoint, toGrid(), modifier ? 1: 0) );
  }
  if (last == '7' ) {
    map.add( new Action(ActionType.Arc,toGrid()) );
  }
}

public void mousePressed() { 
  if (mouseY > height - 50) return;
    initialPoint = toGrid();
}

public void mouseDragged() {
  if (initialPoint == null) return;
  if (last == '1') {
    circle(mouseX, mouseY, 50);
  }
  if (last == '2') { 
    line(initialPoint.x, initialPoint.y, mouseX, mouseY);
  }
  if (last == '3') {   
    dashed.pattern(20, 10);
    dashed.line(initialPoint.x, initialPoint.y, mouseX, mouseY);
  } 
  if (last == '4') {   
    dashed.pattern(10, 10);
    dashed.line(initialPoint.x, initialPoint.y, mouseX, mouseY );
  }
  
  if (last == '7') {   
    float d = dist(mouseX, mouseY, initialPoint.x, initialPoint.y);
    arc(mouseX, mouseY,  150,150, -d,d);
  }
  if (last == '5'&& !modifier) {   
    dashed.pattern(5, 10);
    dashed.bezier(initialPoint.x, initialPoint.y, mouseX, initialPoint.y, initialPoint.x, mouseY, mouseX, mouseY);
  }
  if (last == '5' && modifier) {   
    dashed.pattern(5, 10);
    dashed.bezier(initialPoint.x, initialPoint.y, initialPoint.x, mouseY, mouseX, initialPoint.y,  mouseX, mouseY);
  }
} 

boolean modifier = false;

public void keyPressed() {  
  if(key == 'r'){
    modifier = true;
    return;
  }   
}

 
public void keyReleased() {
  if(key == 'r'){
    modifier = false;
    return;
  }
  
  last = PApplet.parseInt(key);
  
  if (key == 19) {
    saveImg();
  }
  if (key == 1) { 
    map.serialize();    
  }
  if (key == 14) {
    limparCanvas();
  }
  if (key == 26) {
    volta();
  }
  if (key == 14) {
    limparCanvas();
  }
  
}

public PVector mouseVector(){  
  return new PVector(mouseX, mouseY);
}

public PVector toGrid() {
  return toGrid(mouseX,mouseY);
}
public PVector toGrid(int x, int y) {
  strokeWeight(1);
  stroke(51, 64);
  for (int i = 0; i < width; i+=50) {
    line(i, 0, i, height);  
    line(0, i, width, i);
  }
  stroke(0); 
  return new PVector(ceil(x/50) * 50 + 25, ceil(y/50) * 50 + 25);
}

public void limparCanvas() {
  map.clear();
}
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
               dashed.pattern(20, 10); 
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

class MapHandler{

    ArrayList<Action> actions;

    MapHandler(){        
        actions = new ArrayList<Action>();  
        randomMap();
    }

    public void draw(){
        strokeWeight(5);
        for (Action a : actions) {
            if (a.action == ActionType.Bezier)
            a.draw();
        }
        for (Action a : actions) {
            if (a.action != ActionType.Bezier)
            a.draw();
        }
        for (Action a : actions) {
            if (a.isSafe())
                a.draw();
        }

    }

    public void clear(){
 
        actions = new ArrayList<Action>();
    }

    public void serialize(){
        JSONObject json = new JSONObject();
        int i = 0;
        for (Action a : actions) {
        json.setJSONObject(i++ + "", a.serialize());
        } 
        saveJSONObject(json , "data/new.json");
    }

    public int nextNumber() {
        int count =0;
        for (Action a : actions) {
            if (a.action == ActionType.Circulo && a.index > 0) {
            count++;
            }
        }
        return count;
    }

    public void add(Action a){
        actions.add(a);
    }

     

    public void undo(){
        if (actions.size() > 0)
            actions.remove(actions.size()-1);
    }

    public void randomMap(){
        //place 4 tri
         add(new Action(ActionType.Triangulo, toGrid(50, height/2-50)));        
         add(new Action(ActionType.Triangulo, toGrid(width/2, 50)));        
         add(new Action(ActionType.Triangulo, toGrid(width/2, height-150)));
         add(new Action(ActionType.Triangulo, toGrid(width-50,height/2-50)));

        //place random circles;
        noiseSeed(Instant.now().toEpochMilli());
        for(int i = 100; i < width -50; i+=50){
            for (int j = 100; j < height-150; j+=50) {
                float n = noise(i,j);
                if(n >= 0.74f)
                    add( new Action(ActionType.Circulo, toGrid(i,j), nextNumber()+1));
            }
        }
           
         
        //link circles with lines
        for (int i = 0; i < actions.size(); i++) {
            for (int j = 0; j < actions.size(); j++) {
                if(i == j)  break;
                if(actions.get(i).action == ActionType.Circulo && actions.get(j).action == ActionType.Circulo ){
                    if( actions.get(i).pos.dist(actions.get(j).pos) <200){
                        //add( new Action(ActionType.Linha, actions.get(i).pos, actions.get(j).pos ));
                        add( new Action(ActionType.Linha, actions.get(i).pos, near(actions.get(j).pos)));
                        
                    }   
                }
            }
        } 
        // Link each triangle to ONE cricle
        for (int i = 0; i < actions.size(); i++) {
            Action o = actions.get(i);
            if(o.action == ActionType.Triangulo){
                add(new Action(ActionType.Linha, o.pos, near(o.pos)));

            }
        } 
        //Done? 

    }


    private PVector near(PVector v){
        PVector near =v;
        float d = 1000;

        for (int i = 0; i < actions.size(); ++i) {
            if((actions.get(i).pos.dist(v)) < d && actions.get(i).pos != v)
            {
                d = actions.get(i).pos.dist(v);
                near = actions.get(i).pos;
            }
        }
        return near;

    }
}
public void menu() {
    int offset = 0;
  cp5.addButton("Circulo").setPosition(offset++*50, height-50).setSize(45, 45);
  cp5.addButton("Cubo").setPosition(offset++*50, height-50).setSize(45, 45);
  cp5.addButton("Arc").setPosition(offset++*50, height-50).setSize(45, 45);
  cp5.addButton("Linha").setPosition(offset++*50, height-50).setSize(45, 45);
  cp5.addButton("Traco").setPosition(offset++*50, height-50).setSize(45, 45);
  cp5.addButton("Pontos").setPosition(offset++*50, height-50).setSize(45, 45);   
  cp5.addButton("Curva").setPosition(offset++*50, height-50).setSize(45, 45); 
  cp5.addButton("Volta").setPosition(offset++*50, height-50).setSize(45, 45); 
  cp5.addButton("Salvar").setPosition(offset++*50, height-50).setSize(45, 45);
  cp5.addButton("Limpa").setPosition(offset++*50, height-50).setSize(45, 45);  
}

public void Circulo(int v) {
  last = '1';
}

public void Linha(int v) {
  last = '2';
}

public void Traco(int v) {
  last = '3';
}

public void Pontilhado(int v) {
  last = '4';
}
public void Curva(int v) {
  last = '5';
}
public void Cubo(int v) {
  last = '6';
}
public void Arc(int v) {
  last = '7';
}

public void Volta(int v) {
  volta();    
  println("Volta");
}

public void Salvar(int v) {
  saveImg();
  println("Pontilhado");
} 

public void saveImg() {
  cp5.hide();
  toSave = 1;
  print();
  saveFrame("mapa-######.png");
  toSave = 0;
  cp5.show();
  map.serialize();
}

public void volta() {
  map.undo();
}

public void Limpa() {
  map.clear();
  map.randomMap();
  map.draw();
  //limparCanvas();
}
  public void settings() {  size(700, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "nodes" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
