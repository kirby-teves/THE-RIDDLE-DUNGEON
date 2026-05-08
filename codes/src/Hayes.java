public class Hayes extends GameMaster {
    public Hayes(IRiddle riddle) { super("Hayes", riddle); }
    @Override
    public String greet() { return "Hayes points to the door. 'Speak the truth to pass.'"; }
}