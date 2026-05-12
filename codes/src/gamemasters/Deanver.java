package gamemasters;
import model.IRiddle;

public class Deanver extends GameMaster {
    public Deanver(IRiddle riddle) { super("Deanver", riddle); }
    @Override
    public String greet() { return "Deanver crosses his arms. 'Prove your intellect.'"; }
}
