package at.swingolf.appinit;

import com.monitorjbl.xlsx.StreamingReader;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.util.Collection;

@RestController
public class HelloController {
    @Value("${result.directory}")
    private String fileDirectory;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping("/import")
    public String importer() throws IOException {
        Collection<File> files = FileUtils.listFiles(new File(fileDirectory), new String[]{"xlsx"}, false);
        files.stream().forEach(file -> parseFile(file));
        return "Import succeeded!";
    }

    private void parseFile(File file) {

        try (

                InputStream is = new FileInputStream(file);
                Workbook workbook = StreamingReader.builder()
                        .rowCacheSize(100)
                        .bufferSize(4096)
                        .open(is)) {
            for (Sheet sheet : workbook) {
                System.out.println(sheet.getSheetName());
                for (Row r : sheet) {
                    for (Cell c : r) {
                        System.out.println(c.getStringCellValue());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}