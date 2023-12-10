public class Leaf extends Node{
    private final char character;

    public Leaf(char character,int freq)
    {
        super(freq);
        this.character = character;
    }

    public char getChar() {
        return character;
    }
}
