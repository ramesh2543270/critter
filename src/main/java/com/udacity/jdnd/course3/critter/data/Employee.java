package com.udacity.jdnd.course3.critter.data;

import com.udacity.jdnd.course3.critter.user.EmployeeSkill;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="employee")
public class Employee extends User{
    @ElementCollection
    @CollectionTable(name="employee_skill",joinColumns = @JoinColumn(name="id"),uniqueConstraints = @UniqueConstraint(columnNames = {"ID","SKILL"}))
    @Column(name="skill")
    private Set<EmployeeSkill> skills;


    @ElementCollection
    @CollectionTable(name="day_of_week",joinColumns =@JoinColumn(name="id"), uniqueConstraints = @UniqueConstraint(columnNames={"ID","DAY"}))
    @Column(name="day")
    private Set<DayOfWeek> daysAvailable;

    @ManyToMany(mappedBy = "employees")
    private List<Schedule> schedules = new ArrayList<>();

    public Set<EmployeeSkill> getSkills() {
        return skills;
    }

    public void setSkills(Set<EmployeeSkill> skills) {
        this.skills = skills;
    }

    public Set<DayOfWeek> getDaysAvailable() {
        return daysAvailable;
    }

    public void setDaysAvailable(Set<DayOfWeek> daysAvailable) {
        this.daysAvailable = daysAvailable;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }
}
