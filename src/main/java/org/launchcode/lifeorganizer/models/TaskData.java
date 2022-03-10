package org.launchcode.lifeorganizer.models;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class TaskData {

    private static final String DATA_FILE = "task_data.csv";
    private static boolean isDataLoaded = false;

    private static ArrayList<Task> allTasks;

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

    allTasks = new ArrayList<>();

    // Put the records into a more friendly format
            for (CSVRecord record : records) {

        String name = record.get(1);
        int timeRequired = Integer.parseInt(record.get(2));
        boolean isComplete = Boolean.parseBoolean(record.get(3));


//        Task newTask = (Task) findExistingObject(allTasks, aTask);
//
//        if (newEmployer == null){
//            newEmployer = new Employer(anEmployer);
//            allEmployers.add(newEmployer);
//        }
//
//        if (newLocation == null){
//            newLocation = new Location(aLocation);
//            allLocations.add(newLocation);
//        }
//
//        if (newSkill == null){
//            newSkill = new CoreCompetency(aSkill);
//            allCoreCompetency.add(newSkill);
//        }
//
//        if (newPosition == null){
//            newPosition = new PositionType(aPosition);
//            allPositionTypes.add(newPosition);
//        }

        Task newTask = new Task(name, timeRequired, isComplete);

        allTasks.add(newTask);
    }
    // flag the data as loaded, so we don't do it twice
    isDataLoaded = true;

} catch (IOException e) {
        System.out.println("Failed to load job data");
        e.printStackTrace();
        }
        }
}
