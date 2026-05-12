package gamemasters;
import model.IRiddle;

public abstract class GameMaster {
    private final String name;
    private final IRiddle riddle;

    public GameMaster(String name, IRiddle riddle) {
        this.name = name;
        this.riddle = riddle;
    }

    public String getName() { return name; }
    public IRiddle getRiddle() { return riddle; }

    // Polymorphic method
    public abstract String greet();
}