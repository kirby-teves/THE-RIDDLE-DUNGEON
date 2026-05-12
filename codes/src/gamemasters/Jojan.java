package gamemasters;
import model.IRiddle;

public class Jojan extends GameMaster {
    public Jojan(IRiddle riddle) { super("Jojan", riddle); }
    @Override
    public String greet() { return "Jojan smiles mysteriously. 'A simple question for you.'"; }
}
