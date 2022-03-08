package org.launchcode.lifeorganizer.data;

import org.launchcode.lifeorganizer.models.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends CrudRepository<Task, Integer> {
}
