package com.github.vvv1559.highloadcup2017;

import com.github.vvv1559.highloadcup2017.dao.EntityNotFoundException;
import com.github.vvv1559.highloadcup2017.dao.MetaDao;
import com.github.vvv1559.highloadcup2017.dao.ValidationException;
import com.github.vvv1559.highloadcup2017.dao.model.*;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ServerController {

    private final MetaDao metaDao;
    private static final Gson GSON = new Gson();
    private static final String EMPTY_RESPONSE = GSON.toJson(new Object());
    private final ThreadLocal<DecimalFormat> formatThreadLocal;


    @Autowired
    public ServerController(MetaDao metaDao) {
        this.metaDao = metaDao;
        this.formatThreadLocal = ThreadLocal.withInitial(() -> new DecimalFormat("0.0####"));

    }

    private Entity deserialize(EntityType type, String body) {
        JsonElement element = new JsonParser().parse(body);
        if (element.getAsJsonObject().entrySet().stream().map(Map.Entry::getValue).anyMatch(JsonElement::isJsonNull)) {
            throw new ValidationException();
        }

        Class<? extends Entity> entityClass;
        switch (type) {
            case users:
                entityClass = User.class;
                break;
            case visits:
                entityClass = Visit.class;
                break;
            case locations:
                entityClass = Location.class;
                break;
            default:
                throw new EntityNotFoundException();
        }

        return GSON.fromJson(element, entityClass);
    }

    @GetMapping("/{type}/{id}")
    public String getEntity(@PathVariable String type, @PathVariable int id) {
        EntityType entityType = EntityType.valueOf(type);
        return GSON.toJson(metaDao.getEntity(entityType, id));
    }

    @PostMapping("/{type}/{id}")
    public String updateEntity(@PathVariable String type, @PathVariable int id, @RequestBody String body) {
        EntityType entityType = EntityType.valueOf(type);
        Entity entity = deserialize(entityType, body);
        metaDao.updateEntity(entityType, id, entity);
        return EMPTY_RESPONSE;
    }

    @PostMapping("/{type}/new")
    public String newEntity(@PathVariable String type, @PathVariable int id, @RequestBody String body) {
        EntityType entityType = EntityType.valueOf(type);
        Entity entity = deserialize(entityType, body);
        metaDao.newEntity(entityType, entity);
        return EMPTY_RESPONSE;
    }

    @GetMapping(value = "/locations/{id}/avg")
    public String locationAvg(
        @PathVariable int id,
        @RequestParam(value = "fromDate", required = false) Integer fromDate,
        @RequestParam(value = "toDate", required = false) Integer toDate,
        @RequestParam(value = "fromAge", required = false) Integer fromAge,
        @RequestParam(value = "toAge", required = false) Integer toAge,
        @RequestParam(value = "gender", required = false) Character gender
    ) {
        double avg = metaDao.getAllLocationsAvg(id, fromDate, toDate, fromAge, toAge, gender);
        return "{\"avg\":" + formatThreadLocal.get().format(avg) + "}";
    }

    @GetMapping("/users/{id}/visits")
    public String getUserVisits(
        @PathVariable int id,
        @RequestParam(value = "fromDate", required = false) Integer fromDate,
        @RequestParam(value = "toDate", required = false) Integer toDate,
        @RequestParam(value = "country", required = false) String country,
        @RequestParam(value = "toDistance", required = false) Integer toDistance
    ) {
        return GSON.toJson(new VisitsResponse(metaDao.getUserVisits(id, fromDate, toDate, country, toDistance)));
    }

    private class VisitsResponse {
        private final List<Visit> visits;

        private VisitsResponse(List<Visit> visits) {
            this.visits = visits;
        }

        public List<Visit> getVisits() {
            return visits;
        }
    }
}
