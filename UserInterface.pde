void menu() {
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

void Circulo(int v) {
  last = '1';
}

void Linha(int v) {
  last = '2';
}

void Traco(int v) {
  last = '3';
}

void Pontilhado(int v) {
  last = '4';
}
void Curva(int v) {
  last = '5';
}
void Cubo(int v) {
  last = '6';
}
void Arc(int v) {
  last = '7';
}

void Volta(int v) {
  volta();    
  println("Volta");
}

void Salvar(int v) {
  saveImg();
  println("Pontilhado");
} 

void saveImg() {
  cp5.hide();
  toSave = 1;
  print();
  saveFrame("mapa-######.png");
  toSave = 0;
  cp5.show();
}

void volta() {
  map.undo();
}

void Limpa() {
  limparCanvas();
}
