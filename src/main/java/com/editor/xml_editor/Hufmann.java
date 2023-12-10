import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import static java.util.Objects.requireNonNull;

public class Hufmann {
    private Node root;
    private String xmlCode;
    private Map<Character,Integer> charFreq;
    private Map<Character,String> hufmannCharsCodes;
    public Hufmann(String code)
    {
        this.xmlCode = code;
        hufmannCharsCodes = new HashMap<>();
        fillCharsFreqMap();
    }
    private void fillCharsFreqMap()
    {
        charFreq = new HashMap<>();
        for(char character : xmlCode.toCharArray())
        {
            Integer integer = charFreq.get(character);
            charFreq.put(character,integer != null ?integer+1:1);
        }
    }
    private void generateHufmannCodes(Node node,String code)
    {
        if (node instanceof Leaf) {
            hufmannCharsCodes.put(((Leaf) node).getChar(), code);
        } else if (node != null) {
            generateHufmannCodes(node.getLeftNode(), code.concat("0"));
            generateHufmannCodes(node.getRightNode(), code.concat("1"));
        }
    }
    private String getEncodedCode()
    {
        StringBuffer sb = new StringBuffer();
        for(char character : xmlCode.toCharArray())
        {
            sb.append(hufmannCharsCodes.get(character));
        }
        return sb.toString();
    }
    private static String repeat(String str, int count) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append(str);
        }
        return result.toString();
    }

    private static String binaryToString(String binaryStr) {
        StringBuilder text = new StringBuilder();

        // Split the binary string into 8-bit chunks
        for (int i = 0; i < binaryStr.length(); i += 8) {
            String byteStr = binaryStr.substring(i, Math.min(i + 8, binaryStr.length()));

            // Convert binary to decimal and then to character
            int charCode = Integer.parseInt(byteStr, 2);
            text.append((char) charCode);
        }

        return text.toString();
    }
    private String encode()
    {
        Node nodeLeft;
        Node nodeRight;
        Queue<Node> queue = new PriorityQueue<>();
        charFreq.forEach((character,freq)-> queue.add(new Leaf(character,freq)));
        while(queue.size() > 1)
        {
            nodeLeft = queue.poll();
            nodeRight = queue.poll();
            queue.add(new Node(nodeLeft,nodeRight));
        }

        root = queue.poll();
        if(root != null) {
            generateHufmannCodes(root, "");
        }
        return getEncodedCode();
    }



    public String get_xml_encode()
    {
        String s = encode();
        return binaryToString(s);
    }


    public Node getRoot() {
        return root;
    }

}


