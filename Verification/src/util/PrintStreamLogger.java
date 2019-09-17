package util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PrintStreamLogger extends PrintStream {

    public PrintStreamLogger() throws FileNotFoundException {
        this(new FileOutputStream(Constants.LOG_FILE, true));
    }

    private PrintStreamLogger(FileOutputStream outputStream) throws FileNotFoundException {
        super(outputStream);
        super.println(System.lineSeparator());
    }

    @Override
    public void println(String toPrint) {
        super.println(getTime() + ": " + toPrint);
    }

    private String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss zzz - dd/MM/yy");
        Date now = new Date();

        return dateFormat.format(now);
    }

}