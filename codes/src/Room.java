// Room.java
public class Room {
    private final int roomNumber;
    private final String difficultyLabel; // "Easy", "Medium", "Hard"
    private final GameMaster gameMaster;
    private boolean isLocked;

    public Room(int roomNumber, String difficultyLabel, GameMaster gameMaster) {
        this.roomNumber = roomNumber;
        this.difficultyLabel = difficultyLabel;
        this.gameMaster = gameMaster;
        this.isLocked = true;
    }

    public boolean attemptAnswer(String playerAnswer) {
        if (playerAnswer == null) return false;
        String comp = playerAnswer.toLowerCase().trim();
        return comp.equals(gameMaster.getRiddle().getAnswer());
    }

    public void unlock() {
        this.isLocked = false;
    }

    public boolean isLocked() { return isLocked; }
    public int getRoomNumber() { return roomNumber; }
    public String getDifficultyLabel() { return difficultyLabel; }

    public String getMasterName() { return gameMaster.getName(); }
    public String getGreeting() { return gameMaster.greet(); }
    public String getRiddleQuestion() { return gameMaster.getRiddle().getQuestion(); }
    public String getRiddleHint() { return gameMaster.getRiddle().getHint(); }
}