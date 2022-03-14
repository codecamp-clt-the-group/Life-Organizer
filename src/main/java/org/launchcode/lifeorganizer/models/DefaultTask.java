package org.launchcode.lifeorganizer.models;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.persistence.Entity;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class DefaultTask extends Task{

    private static final String DATA_FILE = "default_task_data.csv";
    private static boolean isDataLoaded = false;

    private static ArrayList<DefaultTask> allDefaultTasks;

    private static ArrayList<Integer> allIds = new ArrayList<>();
    private static ArrayList<String> allNames = new ArrayList<>();
    private static ArrayList<Integer> allTimeRequired = new ArrayList<>();
    private static ArrayList<Boolean> allIsComplete = new ArrayList<>();

    public DefaultTask () {}

    public DefaultTask(int id, String name, int timeRequired, boolean isComplete) {
    }

    public static ArrayList<DefaultTask> findAll() {
        loadData();
        return new ArrayList<>(allDefaultTasks);
    }

    private static Object findExistingObject(ArrayList list, String value){
        for (Object item : list){
            if (item.toString().toLowerCase().equals(value.toLowerCase())){
                return item;
            }
        }
        return null;
    }

    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Resource resource = new ClassPathResource(DATA_FILE);
            InputStream is = resource.getInputStream();
            Reader reader = new InputStreamReader(is);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(reader);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allDefaultTasks = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {

                int anId = Integer.parseInt(record.get(0));
                String aName = record.get(1);
                int aTimeRequired = Integer.parseInt(record.get(2));
                boolean anIsComplete = Boolean.parseBoolean(record.get(3));

                Integer newId = (Integer) findExistingObject(allIds, String.valueOf(anId));
                String newName = (String) findExistingObject(allNames, aName);
                Integer newTimeRequired = (Integer) findExistingObject(allTimeRequired, String.valueOf(aTimeRequired));
                Boolean newIsComplete = (boolean) findExistingObject(allIsComplete, String.valueOf(anIsComplete));

                if (newId == null){
                    newId = anId;
                    allIds.add(newId);
                }

                if (newName == null){
                    newName = aName;
                    allNames.add(newName);
                }

                if (newTimeRequired == null){
                    newTimeRequired = aTimeRequired;
                    allTimeRequired.add(newTimeRequired);
                }

                if (newIsComplete == null){
                    newIsComplete = anIsComplete;
                    allIsComplete.add(newIsComplete);
                }

                DefaultTask newDefaultTask = new DefaultTask(anId, aName, aTimeRequired, anIsComplete);

                allDefaultTasks.add(newDefaultTask);
            }
            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }



}
