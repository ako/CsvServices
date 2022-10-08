import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test1 {
    @Test
    public void myFirstTest() {
        //
        System.out.println("HelloWorld!");
        assertEquals(1, 1);
    }

    @Test
    public void testZipFileRead() throws IOException {
        /*
        String csvUrl = "jar:http://ergast.com/downloads/f1db_csv.zip";
        String fileName = csvUrl.substring(csvUrl.indexOf(".zip#") + 5);
        InputStream is = null;
        try (FileSystem fileSystem = FileSystems.newFileSystem(URI.create(csvUrl), null)) {
            Path fileToExtract = fileSystem.getPath(fileName);
            is = Files.newInputStream(fileToExtract);
            int i;
            while ((i = is.read()) != -1) {
                System.out.print((char) i);
            }
        }
        */
    }

    @Test
    public void testZip2() throws Exception {
        System.out.println("> testZip2");
        /*
        try {
            String csvUrl = "http://ergast.com/downloads/f1db_csv.zip#drivers.csv";
            String fileName = csvUrl.substring(csvUrl.indexOf(".zip#") + 5);
            URL csvUrl2 = new URL(csvUrl);
            ZipInputStream zis = new ZipInputStream(csvUrl2.openStream());
            ZipEntry ze = zis.getNextEntry();
            InputStream is = null;
            while(ze != null){
                String name = ze.getName();
                System.out.println(String.format("entry: %s",name));
                if(name.equals(fileName)){
                    System.out.println("found entry");
                    is = zis;
                    break;
                }
                ze = zis.getNextEntry();
            }
            if(is == null){
                throw new FileNotFoundException(String.format("Zipfile not found: %s in %s",fileName,csvUrl));
            }
            int i;
            while ((i = is.read()) != -1) {
                System.out.print((char) i);
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("< testZip2");
         */
    }
}
