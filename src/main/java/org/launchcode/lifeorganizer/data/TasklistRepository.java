package org.launchcode.lifeorganizer.data;

import org.launchcode.lifeorganizer.models.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TasklistRepository  extends CrudRepository<List<Task>, Integer> {
}
