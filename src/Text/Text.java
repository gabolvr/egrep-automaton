package Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;   

public class Text {
    private ArrayList<String> text;

    public Text (String filePath){
        try{  
            File file = new File(filePath);   
            Scanner sc = new Scanner(file); 
            while (sc.hasNextLine()){
                text.add(sc.nextLine());
            }
            
            sc.close();
        } catch(Exception e){
            e.printStackTrace();
            System.exit(2);
        }  
    }

    public ArrayList<String> getText() {
        return text;
    }
    public String getLine(int index) {
        return text.get(index);
    }
}
