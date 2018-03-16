package at.swingolf.appinit.importresults;

import at.swingolf.appinit.ImportRegistry;
import at.swingolf.appinit.ReadSGDDDates;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TournamentFromHttpReader {
    @Autowired
    private ImportRegistry importRegistry;

    @Autowired
    ReadSGDDDates readSGDDDates;

    public void importTournaments() {
        List<ReadSGDDDates.Entry> entries = readSGDDDates.parse();
        entries.stream().forEach(entry -> new TournamentFromHttp(importRegistry,entry).toNeo4J());
    }
}
