package com.udacity.jdnd.course3.critter.user;

import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Nationalized
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
