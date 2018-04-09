package lib;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GameStateNode {
    private int[] playfield;
    private HashMap<Integer, GameStateNode> tree = new HashMap<>();

    public GameStateNode(int[] playfield) {
        this.playfield = playfield;
    }

    public void addNode(Integer key, GameStateNode value) {
        tree.put(key, value);
    }

    public int[] getPlayfield() {
        return playfield;
    }

    @Override
    public String toString() {
        String string = "" + playfield[0];

        for (int i = 1; i < playfield.length; i++) {
            string += ", " + playfield[i];
        }

        string += "\n\nmoves: \n";

        Iterator it = tree.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            string.concat(pair.getKey() + " = " + pair.getValue() + "\n");
            it.remove();
        }

        string.concat("-------------------");
        return string;
    }
}
