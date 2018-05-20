package at.swingolf.appinit.handicapservice;

import at.swingolf.appinit.importresults.TournamentFromExcel;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

@Service
public class HandicapUpdater implements InitializingBean {
    private final String tournamentURL = "http://swingolf-dachverband.de/images/hc/Handicap_Lizenzspieler.xls";
    private byte[] uebersicht;
    public Map<String, Player> players = new HashMap<>();

    @Scheduled(cron = "0 0 * * * *")
    public void update() {
        uebersicht = read();
        parseFile(uebersicht);
        System.out.println(new Date() + " read from " + tournamentURL + " " + uebersicht.length + " bytes");

    }

    private void parseFile(byte[] file) {
        Map<String, Player> players = new HashMap<>();
        Collection<TournamentFromExcel> tournamentFromExcels = new LinkedList<>();
        try {
            InputStream is = new ByteArrayInputStream(file);
            HSSFWorkbook workbook = new HSSFWorkbook(is);
            HSSFSheet sheet = workbook.getSheetAt(0);
            for (Row myrow : sheet) {
                if (myrow.getRowNum() > 6 && (myrow.getRowNum() + 1) % 2 == 0) {
                    String lastname = myrow.getCell(1).getStringCellValue();
                    HSSFRow nextRow = sheet.getRow(myrow.getRowNum() + 1);
                    String firstname = nextRow.getCell(1).getStringCellValue();
                    String license = myrow.getCell(2).getStringCellValue();
                    String category = myrow.getCell(4).getStringCellValue();
                    double handicap = myrow.getCell(39).getNumericCellValue();
                    players.put(license, new Player(license, firstname, lastname, handicap, category));
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        this.players = players;
    }

    public byte[] read() {
        InputStream is = null;
        URL url = null;
        try {
            url = new URL(tournamentURL);
            is = url.openStream();
            byte[] imageBytes = IOUtils.toByteArray(is);
            return imageBytes;
        } catch (IOException e) {
            System.err.printf("Failed while reading bytes from %s: %s", url.toExternalForm(), e.getMessage());
            throw new RuntimeException("");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new RuntimeException("");
                }
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        update();
    }
}
