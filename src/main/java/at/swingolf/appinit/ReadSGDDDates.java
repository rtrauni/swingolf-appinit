package at.swingolf.appinit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ReadSGDDDates {

    private static final String before ="Termine 2018";
    private static final String after ="Termine 2017";

    private final String tournamentURL = "http://www.swingolf-dachverband.de/index.php/sport/terminkalender";

    private class Entry {

        private final String date;
        private final String what;
        private final String whatelse;
        private final String who;
        private final String where;

        public Entry(String s) {
            String[] entries = StringUtils.splitByWholeSeparator(s, "xxx");
            this.date = entries[0].trim();
            this.what=entries[1].trim();
            this.whatelse =entries[2].trim();
            this.who = entries[3].trim();
            this.where = entries[4].trim();
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "date='" + date + '\'' +
                    ", what='" + what + '\'' +
                    ", whatelse='" + whatelse + '\'' +
                    ", who='" + who + '\'' +
                    ", where='" + where + '\'' +
                    '}';
        }
    }
    public List<Entry> parse() {
        String content = readStringFromURL(tournamentURL);
        String[] split = StringUtils.splitByWholeSeparator(content, " xxxx ");
        List<Entry> result = Arrays.stream(split).filter(s -> !StringUtils.isEmpty(s)).map(s -> new Entry(s)).collect(Collectors.toList());
        return result;
    }

    private static String readStringFromURL(String requestURL)
    {
        InputStream in = null;
        try {
            HttpURLConnection httpcon = (HttpURLConnection) new URL(requestURL).openConnection();
            httpcon.addRequestProperty("User-Agent", "Mozilla/4.76");
            in = httpcon.getInputStream();
            String string = IOUtils.toString(in);
            String between = StringUtils.substringBetween(string, before, after);
            between = between.replaceAll("</td>","xxx");
            between = between.replaceAll("</tr>","xxxx");
            string = html2text(between);
            string = StringUtils.remove(string,"Wann?xxx Was?xxx Was noch?xxx Wer?xxx Wo?xxx ");
            string = StringUtils.removeStart(string,"xxxx");
            string = StringUtils.removeEnd(string,"xxxx");
            string = StringUtils.substring(string, 0,StringUtils.lastIndexOf(string,"Hinweis: "));
            return string;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(),e);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }


    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }
    public static void main(String[] args) {
        List<Entry> result = new ReadSGDDDates().parse();
        System.out.println(result);

    }
}
