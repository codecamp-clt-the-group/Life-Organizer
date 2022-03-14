package org.launchcode.lifeorganizer.data;

import org.launchcode.lifeorganizer.models.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Integer> {
    List<Task> findAllByUserId(int id);

}
