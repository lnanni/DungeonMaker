import controlP5.*;
import garciadelcastillo.dashedlines.*;

DashedLines dashed;  
ControlP5 cp5;


PVector initialPoint;
int toSave = 0;
int last;
MapHandler map;



void setup() {
  cp5 = new ControlP5(this); 
  map = new MapHandler();
  size(700, 600); 
  dashed = new DashedLines(this);
  menu();
 
}


void draw() {
  print();
  toGrid();

}

 
void print() {
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
 

void mouseReleased() {
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

void mousePressed() { 
  if (mouseY > height - 50) return;
    initialPoint = toGrid();
}

void mouseDragged() {
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

void keyPressed() {  
  if(key == 'r'){
    modifier = true;
    return;
  }   
}

 
void keyReleased() {
  if(key == 'r'){
    modifier = false;
    return;
  }
  
  last = int(key);
  
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

PVector mouseVector(){  
  return new PVector(mouseX, mouseY);
}

PVector toGrid() {
  return toGrid(mouseX,mouseY);
}
PVector toGrid(int x, int y) {
  strokeWeight(1);
  stroke(51, 64);
  for (int i = 0; i < width; i+=50) {
    line(i, 0, i, height);  
    line(0, i, width, i);
  }
  stroke(0); 
  return new PVector(ceil(x/50) * 50 + 25, ceil(y/50) * 50 + 25);
}

void limparCanvas() {
  map.clear();
}
