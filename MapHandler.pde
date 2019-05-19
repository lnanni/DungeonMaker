import java.time.Instant;
class MapHandler{

    ArrayList<Action> actions;

    MapHandler(){        
        actions = new ArrayList<Action>();  
        //randomMap();
    }

    void draw(){
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

    void clear(){
 
        actions = new ArrayList<Action>();
    }

    void serialize(){
        JSONObject json = new JSONObject();
        int i = 0;
        for (Action a : actions) {
        json.setJSONObject(i++ + "", a.serialize());
        } 
        saveJSONObject(json , "data/new.json");
    }

    int nextNumber() {
        int count =0;
        for (Action a : actions) {
            if (a.action == ActionType.Circulo && a.index > 0) {
            count++;
            }
        }
        return count;
    }

    void add(Action a){
        actions.add(a);
    }

     

    void undo(){
        if (actions.size() > 0)
            actions.remove(actions.size()-1);
    }

    void randomMap(){
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
                if(n >= 0.75)
                    add( new Action(ActionType.Circulo, toGrid(i,j), nextNumber()+1));
            }
        }
           
         
        //link circles with lines
        for (int i = 0; i < actions.size(); i++) {
            for (int j = 0; j < actions.size(); j++) {
                if(i == j) break ;
                if(actions.get(i).action == ActionType.Circulo 
                && actions.get(j).isSafe() ){
                    if( actions.get(i).pos.dist(actions.get(j).pos) <200 && random(1) > 0.5){
                            add( new Action(ActionType.Linha, actions.get(i).pos, actions.get(j).pos ));
                            
                    }   
                }
            }
        } 
                                         

            
        

        //Done? 

    }

}