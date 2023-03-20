package org.knalpot.knalpot.world;

import java.io.IOException;
import java.net.Socket;
import java.io.*;
import java.util.*;

public class WorldSocket {
    public static void main(String[] args) {
        try {
            try (Socket s = new Socket("localhost", 8080)) {
                InputStream inStream = s.getInputStream();
                Scanner in = new Scanner(inStream);

                while (in.hasNext()) {
                    if (in.hasNextInt()) {
                        int intValue = in.nextInt();
                        System.out.println("Received an int value: " + intValue);
                    } else if (in.hasNextFloat()) {
                        float floatValue = in.nextFloat();
                        System.out.println("Received a float value: " + floatValue);
                    } else {
                        String line = in.nextLine();
                        System.out.println("Received a message:" + line);
                    }
                }
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
