package sebastian.GHData.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import sebastian.GHData.service.GetAndMapData;

@Controller
@RequestMapping("/GitHub")
public class DataController {

    @Autowired
    private GetAndMapData getAndMapData;

    @GetMapping(value = "/getData/{username}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity processUsername(@PathVariable String username) {
        return getAndMapData.processStringToJson(username);
    }

}