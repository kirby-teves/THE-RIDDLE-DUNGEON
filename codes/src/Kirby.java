public class Kirby extends GameMaster {
    public Kirby(IRiddle riddle) { super("Kirby", riddle); }
    @Override
    public String greet() { return "Kirby blocks the path! 'Let's play a game!'"; }
}