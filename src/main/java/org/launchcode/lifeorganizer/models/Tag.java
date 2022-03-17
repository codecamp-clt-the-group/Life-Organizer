package org.launchcode.lifeorganizer.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
public class Tag {

    @Id
    @GeneratedValue
    private int id;

    @NotEmpty(message = "Tag field must not be empty.")
    @Size(min = 3, max = 200, message = "Tag field must be between 3 and 200 characters.")
    private String name;

    public Tag () {}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
