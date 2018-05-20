package at.swingolf.appinit.handicapservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HandicapRestController {
    @Autowired
    HandicapUpdater handicapUpdater;

    @RequestMapping("/player/{id}")
    public Player greeting(@PathVariable String id) {
        return handicapUpdater.players.values().stream().filter(player -> player.getLicense().equalsIgnoreCase(id)).findFirst().orElseGet(null);
    }

    @RequestMapping("/player/{id}/handicap")
    public String greeting2(@PathVariable String id) {
        Player player2 = handicapUpdater.players.values().stream().filter(player -> player.getLicense().equalsIgnoreCase(id)).findFirst().orElseGet(null);
        if (player2 == null) {
            return "";
        }
        return Double.valueOf(player2.getHandicap()).toString();
    }

}
