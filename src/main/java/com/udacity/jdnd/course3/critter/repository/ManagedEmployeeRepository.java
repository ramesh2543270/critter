package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class ManagedEmployeeRepository {
    @PersistenceContext
    EntityManager entityManager;

    public List<Long> findEmployeeIdsWithAllSkillsOnDay(Set<EmployeeSkill> skillsSet, DayOfWeek dayOfWeek) {
        String query = "select emp.id "+
                "from employee as emp, employee_skill as esk, day_of_week as dw "+
                "where emp.id=esk.id "+
                "and emp.id=dw.id "+
                "and esk.skill in ("+
                    skillsSet.stream()
                .map((skill) -> { return String.valueOf(skill.ordinal()); })
                .collect(Collectors.joining(",")) +
                ") and dw.day =" + dayOfWeek.ordinal() + " "+
                " group by emp.id HAVING count(esk.skill) = " + skillsSet.size() ;

        Query myQuery = entityManager.createNativeQuery(query);
        List<BigInteger> result = myQuery.getResultList();

        return result.stream()
                .map(BigInteger::longValue)
                .collect(Collectors.toList());

    }
}
