package at.swingolf.appinit.importresults;

import at.swingolf.appinit.ImportRegistry;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class TournamentFromExcelReader {
    NumberFormat format = NumberFormat.getInstance(Locale.GERMAN);
    NumberFormat format2 = NumberFormat.getInstance(Locale.US);

    @Value("${result.directory}")
    private String fileDirectory;


    @Autowired
    private ImportRegistry importRegistry;

    public void importTournaments() {
        Collection<File> files = FileUtils.listFiles(new File(fileDirectory), new String[]{"xlsx"}, false);
        List<TournamentFromExcel> tournamentFromExcels = files.stream().map(file -> parseFile(file)).flatMap(tournament -> tournament.stream()).map(tournamentFromExcel -> {
            printTournament(tournamentFromExcel);
            return tournamentFromExcel;
        }).collect(Collectors.toList());
    }

    private void printTournament(TournamentFromExcel tournamentFromExcel) {
//        System.out.println(tournamentFromExcel.toString());
//        tournamentFromExcel.getPlayerFromExcels().stream().forEach(playerFromExcel -> System.out.println(playerFromExcel));
    }

    private Collection<TournamentFromExcel> parseFile(File file) {
        Collection<TournamentFromExcel> tournamentFromExcels = new LinkedList<>();
        try (

                InputStream is = new FileInputStream(file);
                Workbook workbook = StreamingReader.builder()
                        .rowCacheSize(100)
                        .bufferSize(4096)
                        .open(is)) {
            String yearString = org.apache.commons.lang3.StringUtils.substringAfterLast(FilenameUtils.getBaseName(file.getName()), " ");
            Integer year = Integer.parseInt(yearString);
            for (Sheet sheet : workbook) {
                TournamentFromExcel tournamentFromExcel = new TournamentFromExcel(importRegistry);
                tournamentFromExcel.setYear(year);
                tournamentFromExcels.add(tournamentFromExcel);
                String sheetName = sheet.getSheetName();
                tournamentFromExcel.setSheetName(sheetName);


                for (Row r : sheet) {
                    boolean validRow = true;
                    PlayerFromExcel playerFromExcel = new PlayerFromExcel();
                    for (Cell c : r) {
                        // place
                        if (matches(r, c,0,1)) {
                            tournamentFromExcel.setPlace(c.getStringCellValue());
                        }
                        // date
                        if (matches(r, c,1,1)) {
                            tournamentFromExcel.setDate(c.getStringCellValue());
                        }
                        // name
                        if (matches(r, c,2,1)) {
                            tournamentFromExcel.setName(c.getStringCellValue());
                        }
                        // referee
                        if (matches(r, c,3,1)) {
                            tournamentFromExcel.setReferee(c.getStringCellValue());
                        }
                        // best15
                        if (matches(r, c,4,1)) {
                            tournamentFromExcel.setBest15(c.getStringCellValue());
                        }
                        // best20
                        if (matches(r, c,5,1)) {
                            tournamentFromExcel.setBest20(c.getStringCellValue());
                        }
                        if (r.getRowNum()>=7) {
                            if (c.getColumnIndex()==0 && c.getStringCellValue().length()<=2) {
                                validRow = false;
                            }
                            if (c.getColumnIndex()==1 && c.getStringCellValue().length()<=2) {
                                validRow = false;
                            }
                            if (validRow) {
                                if (c.getColumnIndex() == 0) {
                                    playerFromExcel.setName(c.getStringCellValue());
                                }
                                if (c.getColumnIndex() == 1) {
                                    playerFromExcel.setLicense(c.getStringCellValue());
                                }
                                if (c.getColumnIndex() == 2) {
                                    if (c.getStringCellValue().length()>2) {
                                        try {
                                            playerFromExcel.setOldHandicap(format.parse(c.getStringCellValue()).doubleValue());
                                        } catch (ParseException e) {
                                            playerFromExcel.setOldHandicap(c.getNumericCellValue());
                                            // throw new RuntimeException("error at file "+file.getAbsolutePath()+" sheet"+sheet.getSheetName()+" row "+r.getRowNum()+" cell "+c.getColumnIndex(),e);
                                        }
                                    }
                                }
                                if (c.getColumnIndex() >= 3 && c.getColumnIndex() < 3 + 18) {
                                    if (org.apache.commons.lang3.StringUtils.isNumeric(c.getStringCellValue())) {
                                        playerFromExcel.getScores().put(c.getColumnIndex() - 2, Double.valueOf(c.getNumericCellValue()).intValue());
                                    }
                                }
                                if (c.getColumnIndex() == 21) {
                                    if (c.getStringCellValue().length()>2) {
                                        String stringCellValue = c.getStringCellValue().replaceAll("\"", "");
                                        playerFromExcel.setResult(Double.valueOf(stringCellValue).intValue());
                                    }
                                }
                                if (c.getColumnIndex() == 22) {
                                    if (c.getStringCellValue().length()>2) {
                                        // TODO
                                        String stringCellValue = c.getStringCellValue().replaceAll("\"", "");
                                        if (!"#VALUE!".equals(stringCellValue) && !"ERROR:  #VALUE!".equals(stringCellValue)) {
                                            playerFromExcel.setResultAfterCorrecture(Double.valueOf(stringCellValue).intValue());
                                        }
                                    }
                                }
                            }
                        }
//                        System.out.println(c.getStringCellValue());
                    }
                    if (r.getRowNum()>=7 && validRow && StringUtils.hasText(playerFromExcel.getLicense())) {
                        tournamentFromExcel.getPlayerFromExcels().add(playerFromExcel);
                    };
                }
                tournamentFromExcel.toNeo4J();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tournamentFromExcels;
    }

    private boolean matches(Row r, Cell c, int rowNum, int colNum) {
        return r.getRowNum() == rowNum && c.getColumnIndex() == colNum;
    }
}
