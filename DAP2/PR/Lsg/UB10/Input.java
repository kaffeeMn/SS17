import java.io.Reader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Reader{
    private final BufferedReader reader;

    public Input(String kind, Object param){
        switch(kind){
            case "input":
                if(! param instanceof InputStream){
                    throw new IllegalArgumentException("submit an inputstream");
                }
                initBufferedReader(new InputReader((InputStream) param));
            case "File":
                if(! (param instanceof String || param instanceof File || param instanceof FileDescriptor)){
                    throw new IllegalArgumentException("submit a file");
                }
                initBufferedReader(new FileReader(param));
        }
    }
    private initBufferedReader(Reader r){
        this.reader = new BufferedReader(r);
    }
    private String readLine(){
        return this.reader.readLine();
    }
}
