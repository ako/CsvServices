//import csvservices.impl.DataGenerator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test1 {
    @Test
    public void myFirstTest() {
        //
        System.out.println("HelloWorld!");
        Assertions.assertEquals(1, 1);
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

    @Test
    public void testParseDate1() throws Exception {
        String myDate1 = "+01:23:22.123";
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("['+']hh:mm:ss.SSS", Locale.ENGLISH);
        // LocalDate date1 = LocalDate.parse(myDate1,parser);

    }

    @Test
    public void testParseDate2() throws Exception {
        String myDate2 = "3:22.123";
        SimpleDateFormat parser = new SimpleDateFormat("[hh:]mm:ss.SSS");
        // Date date2 = parser.parse(myDate2);
    }

//    @Test
//    public void testDataGenerator() {
//        DataGenerator dg = new DataGenerator();
//        String data = dg.generate("String;String", 10, ";");
//        System.out.println(data);
//    }
}
