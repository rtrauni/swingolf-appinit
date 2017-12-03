package at.swingolf.appinit;

import com.monitorjbl.xlsx.StreamingReader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
public class HelloController {
    NumberFormat format = NumberFormat.getInstance(Locale.GERMAN);
    NumberFormat format2 = NumberFormat.getInstance(Locale.US);

    @Value("${result.directory}")
    private String fileDirectory;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping("/import")
    public String importer() throws IOException {
        Collection<File> files = FileUtils.listFiles(new File(fileDirectory), new String[]{"xlsx"}, false);
        List<Tournament> tournaments = files.stream().map(file -> parseFile(file)).flatMap(tournament -> tournament.stream()).map(tournament -> {
            printTournament(tournament);
            return tournament;
        }).collect(Collectors.toList());

        return toCypher(tournaments);
    }

    private String toCypher(List<Tournament> tournaments) {
        return "" + tournaments.size();
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
            String yearString = org.apache.commons.lang3.StringUtils.substringAfterLast(FilenameUtils.getBaseName(file.getName()), " ");
            Integer year = Integer.parseInt(yearString);
            for (Sheet sheet : workbook) {
                Tournament tournament = new Tournament();
                tournament.setYear(year);
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
                            if (validRow) {
                                if (c.getColumnIndex() == 0) {
                                    tournament.getPlayers().add(player);
                                    player.setName(c.getStringCellValue());
                                }
                                if (c.getColumnIndex() == 1) {
                                    player.setLicense(c.getStringCellValue());
                                }
                                if (c.getColumnIndex() == 2) {
                                   if (c.getStringCellValue().length()>2) {
                                       try {
                                           player.setOldHandicap(format.parse(c.getStringCellValue()).doubleValue());
                                       } catch (ParseException e) {
                                           player.setOldHandicap(c.getNumericCellValue());
                                           // throw new RuntimeException("error at file "+file.getAbsolutePath()+" sheet"+sheet.getSheetName()+" row "+r.getRowNum()+" cell "+c.getColumnIndex(),e);
                                       }
                                   }
                                }
                                if (c.getColumnIndex() >= 3 && c.getColumnIndex() < 3 + 18) {
                                    if (org.apache.commons.lang3.StringUtils.isNumeric(c.getStringCellValue())) {
                                        player.getScores().put(c.getColumnIndex() - 2, Double.valueOf(c.getNumericCellValue()).intValue());
                                    }
                                }
                                if (c.getColumnIndex() == 21) {
                                    if (c.getStringCellValue().length()>2) {
                                        String stringCellValue = c.getStringCellValue().replaceAll("\"", "");
                                        player.setResult(Double.valueOf(stringCellValue).intValue());
                                    }
                                }
                                if (c.getColumnIndex() == 22) {
                                    if (c.getStringCellValue().length()>2) {
                                        // TODO
                                            String stringCellValue = c.getStringCellValue().replaceAll("\"", "");
                                        if (!"#VALUE!".equals(stringCellValue)) {
                                            player.setResultAfterCorrecture(Double.valueOf(stringCellValue).intValue());
                                        }
                                    }
                                }
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