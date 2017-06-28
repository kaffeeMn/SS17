import java.io.Reader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileDescriptor;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Input{
    private BufferedReader reader;

    public Input(String kind, Object param){
        switch(kind){
            case "input":
                if(! (param instanceof InputStream)){
                    throw new IllegalArgumentException("submit an inputstream");
                }
                initBufferedReader(new InputStreamReader((InputStream) param));
            case "File":
                if(! (param instanceof String || param instanceof File || param instanceof FileDescriptor)){
                    throw new IllegalArgumentException("submit a file");
                }
                try{
                    initBufferedReader(new FileReader((String) param));
                }catch(FileNotFoundException fe){
                    fe.printStackTrace();
                }
        }
    }
    private void  initBufferedReader(Reader r){
        this.reader = new BufferedReader(r);
    }
    public String readLine(){
        try{
            return this.reader.readLine();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        return "";
    }
}
