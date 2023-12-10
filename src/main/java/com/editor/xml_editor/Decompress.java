public class Decompress {
    private Node root;
    public Decompress(Node root) {
        this.root = root;
    }

    public String decode(String compressed_code) {
        String code = stringToBinary(compressed_code);
        StringBuffer sb = new StringBuffer();
        Node current = root;

        for (char character : code.toCharArray()) {
            current = character == '0' ? current.getLeftNode() : current.getRightNode();
            if (current instanceof Leaf) {
                sb.append(((Leaf) current).getChar());
                current = root;
            }
        }

        return sb.toString();
    }
    private static String stringToBinary(String inputString) {
        StringBuilder binaryString = new StringBuilder();

        for (int i = 0; i < inputString.length(); i++) {
            char currentChar = inputString.charAt(i);
            for (int j = 7; j >= 0; j--) {
                binaryString.append((currentChar & (1 << j)) != 0 ? '1' : '0');
            }
        }

        return binaryString.toString();
    }

}
