package com.github.vvv1559.highloadcup2017.preloader;

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
import java.util.function.Consumer;
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
                if (!zipEntry.getName().endsWith(".json")) {
                    continue;
                }

                Class<? extends InitialData> dataClass;
                Consumer<Entity> saver;
                switch (zipEntry.getName().charAt(0)) {
                    case 'l':
                        dataClass = LocationsData.class;
                        saver = entity -> metaDao.newLocation((Location) entity);
                        break;
                    case 'u':
                        dataClass = UsersData.class;
                        saver = entity -> metaDao.newUser((User) entity);
                        break;
                    case 'v':
                        dataClass = VisitsData.class;
                        saver = entity -> metaDao.newVisit((Visit) entity);
                        break;
                    default:
                        continue;
                }

                Reader jsonReader = new BufferedReader(
                    new InputStreamReader(zipFile.getInputStream(zipEntry), StandardCharsets.UTF_8));
                InitialData<Entity> initialData = gson.<InitialData<Entity>>fromJson(jsonReader, dataClass);
                initialData.getData().forEach(saver);
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
