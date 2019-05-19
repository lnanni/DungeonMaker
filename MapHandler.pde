class MapHandler{

    ArrayList<Action> actions;

    MapHandler(){
        
        actions = new ArrayList<Action>();  
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
}