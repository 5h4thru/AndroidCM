package edu.utdallas.dxp141030.contactmanager.util;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class to help saving the contacts in a file.
 * @author Durga Sai Preetham Palagummi
 * NetID dxp141030
 * Date 17th March 2015
*/


public class FileUtil {

    private FileOutputStream fileOutputStream;
    private BufferedReader bufferedReader;

    /*Write to file*/
    public boolean writeToFile(String stringToWrite, boolean isAppendMode) {
        boolean success = false;

        if(isWritableToStorage()) {
            File fileToWrite = new File(Environment.getExternalStorageDirectory(), "contacts.txt"); 
            try {
                if(!fileToWrite.exists()) {
                    fileToWrite.createNewFile();
                }
                fileOutputStream = new FileOutputStream(fileToWrite, isAppendMode);
                fileOutputStream.write(stringToWrite.getBytes());
                fileOutputStream.close();
                success = true;
            } catch (Exception e) {
                //Will not use logger file as of now. Wil implement logger file if time permits
                e.printStackTrace();
            }
        }

        return success;
    }

    /*Read from File*/
    public ArrayList<String> readFile() {
        String str;

        ArrayList<String> resultArray = new ArrayList<String>();
        if(isWritableToStorage()) {
            try {
                File file = new File(Environment.getExternalStorageDirectory(), "contacts.txt");
                if(!file.exists()){
                    file.createNewFile();
                }
                bufferedReader = new BufferedReader(new FileReader(file));
                while((str = bufferedReader.readLine()) != null){
                    resultArray.add(str);
                }
            } catch (IOException e) {
                //Will not use logger file as of now. Wil implement logger file if time permits
                e.printStackTrace();
            }
        }

        return resultArray;
    }

    /*Check Writable to storage*/
    private boolean isWritableToStorage() {
        String storageState = Environment.getExternalStorageState();

        if(storageState.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }

        return false;
    }
}
