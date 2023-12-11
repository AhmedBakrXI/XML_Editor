public class Decompress {
    private Node root;

    public Decompress(Node root) {
        this.root = root;
    }

    public String decode(String compressed_code) {

        StringBuffer sb = new StringBuffer();
        Node current = root;

        for (char character : compressed_code.toCharArray()) {
            current = character == '0' ? current.getLeftNode() : current.getRightNode();
            if (current instanceof Leaf) {
                sb.append(((Leaf) current).getChar());
                current = root;
            }
        }
        return sb.toString();
    }
}
