package com.github.vvv1559.highloadcup2017;

import com.github.vvv1559.highloadcup2017.dao.MetaDao;
import com.github.vvv1559.highloadcup2017.dao.model.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Component
public class InitialDataLoader {

    @Value("${production-load:false}")
    boolean isProductionLoad;

    private final MetaDao metaDao;

    private final Gson gson = new Gson();

    @Autowired
    public InitialDataLoader(MetaDao metaDao) {
        this.metaDao = metaDao;
    }


    @PostConstruct
    public void loadInitialData() throws IOException {
        URL zipFilePath = isProductionLoad
            ? new File("/tmp/data/data.zip").toURI().toURL()
            : this.getClass().getClassLoader().getResource("static/data.zip");

        if (zipFilePath == null) {
            return;
        }

        try (ZipFile zipFile = new ZipFile(zipFilePath.getFile())) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                String zipEntryName = zipEntry.getName();
                if (!zipEntryName.endsWith(".json")) {
                    continue;
                }

                EntityType type = EntityType.valueOf(zipEntryName.substring(0, zipEntryName.indexOf('_')));

                Class<? extends InitialData> dataClass;
                switch (type) {
                    case locations:
                        dataClass = LocationsData.class;
                        break;
                    case users:
                        dataClass = UsersData.class;
                        break;
                    case visits:
                        dataClass = VisitsData.class;
                        break;
                    default:
                        continue;
                }

                Reader jsonReader = new BufferedReader(
                    new InputStreamReader(zipFile.getInputStream(zipEntry), StandardCharsets.UTF_8));
                InitialData<Entity> initialData = gson.<InitialData<Entity>>fromJson(jsonReader, dataClass);
                initialData.getData().forEach(e -> metaDao.newEntity(type, e));
            }
        }
    }

    interface InitialData<T extends Entity> {
        List<T> getData();
    }

    private static class LocationsData implements InitialData<Location> {
        private final List<Location> locations;

        private LocationsData(List<Location> locations) {
            this.locations = locations;
        }

        @Override
        public List<Location> getData() {
            return locations;
        }
    }

    static class UsersData implements InitialData<User> {
        private final List<User> users;

        UsersData(List<User> users) {
            this.users = users;
        }

        @Override
        public List<User> getData() {
            return users;
        }
    }

    static class VisitsData implements InitialData<Visit> {
        private final List<Visit> visits;


        VisitsData(List<Visit> visits) {
            this.visits = visits;
        }

        @Override
        public List<Visit> getData() {
            return visits;
        }
    }
}
