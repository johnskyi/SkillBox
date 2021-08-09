import java.io.FileWriter;
import java.io.IOException;

public class Writer {

    private static final String siteMap = "src/main/resources/map.txt";

    public void getWriting(String s) {
        try {
            FileWriter fw = new FileWriter(Writer.siteMap, true);
            fw.write(s);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}