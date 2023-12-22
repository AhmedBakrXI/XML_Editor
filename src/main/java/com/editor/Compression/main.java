package com.editor.Compression;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class main {
    public static void main(String []args)
    {
        File file_in = new File("F:\\Fall 23\\Data Structure\\proj\\project test\\Compression\\input.txt");
        File file_out =  new File("F:\\Fall 23\\Data Structure\\proj\\project test\\Compression\\output.txt");
        File file_after_decompress =  new File("F:\\Fall 23\\Data Structure\\proj\\project test\\Compression\\after_decompress.txt");
        try {
            file_in.createNewFile();
            FileWriter writer = new FileWriter("F:\\Fall 23\\Data Structure\\proj\\project test\\Compression\\input.txt", false);

            writer.write("{\"users\": {\"user\": [{\"id\": 1,\"name\": \"Ahmed Ali\",\"posts\": {\"post\": [{\"body\": \"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore\\n                    et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut\\n                    aliquip ex ea commodo consequat.\",\"topics\": {\"topic\": [\"economy\",\"finance\"]}},{\"body\": \"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore\\n                    et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut\\n                    aliquip ex ea commodo consequat.\",\"topics\": {\"topic\": \"solar_energy\"}}]},\"followers\": {\"follower\": [{\"id\": 2},{\"id\": 3}]}},{\"id\": 2,\"name\": \"Yasser Ahmed\",\"posts\": {\"post\": {\"body\": \"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore\\net dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut\\naliquip ex ea commodo consequat.\",\"topics\": {\"topic\": \"education\"}}},\"followers\": {\"follower\": {\"id\": 1}}},{\"id\": 3,\"name\": \"Mohamed Sherif\",\"posts\": {\"post\": {\"body\": \"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore\\net dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut\\naliquip ex ea commodo consequat.\",\"topics\": {\"topic\": \"sports\"}}},\"followers\": {\"follower\": {\"id\": 1}}}]}}");

            writer.close();
            HuffmanCompression.compress(file_in,file_out);
            HuffmanCompression.decompress(file_out,file_after_decompress);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
