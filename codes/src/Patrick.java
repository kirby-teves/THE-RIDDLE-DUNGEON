public class Patrick extends GameMaster {
    public Patrick(IRiddle riddle) { super("Patrick", riddle); }
    @Override
    public String greet() { return "Patrick stands firm. 'Only one answer unlocks this chain.'"; }
}