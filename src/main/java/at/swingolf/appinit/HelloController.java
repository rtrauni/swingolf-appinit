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
import java.util.LinkedList;

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
        files.stream().map(file -> parseFile(file)).flatMap(tournament -> tournament.stream()).forEach(tournament -> {
            printTournament(tournament);});
        return "Import succeeded!";
    }

    private void printTournament(Tournament tournament) {
        System.out.println(tournament.toString());
        tournament.getPlayers().stream().forEach(player -> System.out.println(player));
    }

    private Collection<Tournament> parseFile(File file) {
        Collection<Tournament> tournaments = new LinkedList<>();
        try (

                InputStream is = new FileInputStream(file);
                Workbook workbook = StreamingReader.builder()
                        .rowCacheSize(100)
                        .bufferSize(4096)
                        .open(is)) {
            for (Sheet sheet : workbook) {
                Tournament tournament = new Tournament();
                tournaments.add(tournament);
                String sheetName = sheet.getSheetName();
                tournament.setSheetName(sheetName);


                for (Row r : sheet) {
                    boolean validRow = false;
                    Player player = new Player();
                    for (Cell c : r) {
                        // place
                        if (matches(r, c,0,1)) {
                            tournament.setPlace(c.getStringCellValue());
                        }
                        // date
                        if (matches(r, c,1,1)) {
                            tournament.setDate(c.getStringCellValue());
                        }
                        // name
                        if (matches(r, c,2,1)) {
                            tournament.setName(c.getStringCellValue());
                        }
                        // referee
                        if (matches(r, c,3,1)) {
                            tournament.setReferee(c.getStringCellValue());
                        }
                        // best15
                        if (matches(r, c,4,1)) {
                            tournament.setBest15(c.getStringCellValue());
                        }
                        // best20
                        if (matches(r, c,5,1)) {
                            tournament.setBest20(c.getStringCellValue());
                        }
                        if (r.getRowNum()>=7) {
                            if (c.getColumnIndex()==0 && c.getStringCellValue().length()>2) {
                                    validRow = true;
                            }
                            if (validRow && c.getColumnIndex() == 0) {
                                tournament.getPlayers().add(player);
                                player.setName(c.getStringCellValue());
                            }
                            if (validRow && c.getColumnIndex()>=3 && c.getColumnIndex()<=3+18) {

                            }
                        }

//                        System.out.println(c.getStringCellValue());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tournaments;
    }

    private boolean matches(Row r, Cell c, int rowNum, int colNum) {
        return r.getRowNum() == rowNum && c.getColumnIndex() == colNum;
    }


}