package sicxe;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;


public class Main {

  public static void main(String[] args) throws IOException {
    FileReader fr = new FileReader("SRCFILE");
    FileWriter fwLISFILE = new FileWriter("LISFILE");  
    FileWriter fwOBJFILE = new FileWriter("OBJFILE");

    PassOne pass1 = new PassOne(fr);
    pass1.findStart();
    pass1.findLoc();
    fr.close();
    
    PassTwo pass2 = new PassTwo(pass1, fwLISFILE, fwOBJFILE);
    pass2.findObcode();
    fwLISFILE.flush();
    fwLISFILE.close();
    fwOBJFILE.flush();
    fwOBJFILE.close();

  }

}
