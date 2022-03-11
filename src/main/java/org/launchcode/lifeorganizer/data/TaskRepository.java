package org.launchcode.lifeorganizer.data;

import org.launchcode.lifeorganizer.models.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Integer> {
    @Query(value = "SELECT * FROM TASK WHERE tasklist_id is null", nativeQuery = true)
    List<Task> findTasksForTasklist();

    List<Task> findAllByUserId(int id);
}
