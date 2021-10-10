package Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;   

public class Text {
    private ArrayList<String> text;

    public Text (String filePath){
        text = new ArrayList<>();
        try{  
            File file = new File(filePath);
            Scanner sc = new Scanner(file); 
            while (sc.hasNextLine()){
                text.add(sc.nextLine());
            }
            sc.close();
        } catch(Exception e){
            System.out.println(e);
            System.exit(2);
        }
    }

    public ArrayList<String> getText() {
        return text;
    }

    public int getSize() {
        return text.size();
    }
}
