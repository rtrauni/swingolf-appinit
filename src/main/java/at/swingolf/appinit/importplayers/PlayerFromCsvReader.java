package at.swingolf.appinit.importplayers;

import at.swingolf.appinit.ImportRegistry;
import one.util.streamex.StreamEx;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerFromCsvReader {
    @Value("${player.directory}")
    private String fileDirectory;

    @Autowired
    private ImportRegistry importRegistry;
    public PlayerFromCsvReader() {
    }

    public void importTournaments() {
        Collection<File> files = FileUtils.listFiles(new File(fileDirectory), new String[]{"csv"}, false);
        files.stream().forEach(file -> parseFile(file));
    }

        public void parseFile(File file) {
            System.out.println(file);
            String yearString = org.apache.commons.lang3.StringUtils.substringAfterLast(FilenameUtils.getBaseName(file.getName()), " ");
            Integer year = Integer.parseInt(yearString);

            List<String> lines = null;
            try {
                lines = FileUtils.readLines(file, "UTF-8");
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(),e);
            }
            List<String> entries = StreamEx.of(lines).skip(7).collect(Collectors.toList());
            List<String> joined = new LinkedList<>();
            for(int i = 0; i < entries.size(); i += 2) {
                joined.add(entries.get(i)+","+entries.get(i+1));
            }
            new PlayerFromCsv(joined,year,importRegistry).toNeo4J();
        }
    }
