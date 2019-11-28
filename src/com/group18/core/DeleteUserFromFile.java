package com.group18.core;

import java.io.*;
import java.nio.Buffer;

public class DeleteUserFromFile {
    private String userName;

    public DeleteUserFromFile(String userName){
        this.userName = userName;
        deleteLine();
    }

    private void deleteLine() {
//        try {
//            File inputFile= new File("./src/resources/UserNames.txt");
//            File tempFile= new File("./src/resources/tempFile.txt");
//            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
//            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
//            String currentLine;
//            while((currentLine = reader.readLine()) != null) {
//                String trimmedLine = currentLine.trim();
//                String[] userNameSplitList = trimmedLine.split(",");
//                if(!(userNameSplitList[0].equals(userName))) {
//                    writer.write(currentLine + System.getProperty("line.separator"));
//                }
//            }
//            writer.close();
//            reader.close();
//            inputFile.delete();
//            tempFile.renameTo(inputFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

}
