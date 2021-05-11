package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.data.Schedule;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    UserService userService;

    @Autowired
    PetService petService;

    @Transactional
    public Schedule saveSchedule(Schedule schedule){

        Schedule newSchedule = scheduleRepository.save(schedule);

        for(Employee e : newSchedule.getEmployees()){
            e.getSchedules().add(newSchedule);
            employeeRepository.save(e);
        }

        for(Pet p : newSchedule.getPets()){
            p.getSchedules().add(newSchedule);
            petRepository.save(p);
        }

        return newSchedule;
    }

    public Schedule findSchedule(Long id){
        Optional<Schedule> optionalSchedule =  scheduleRepository.findById(id);
        Schedule schedule = optionalSchedule.orElseThrow(ScheduleNotFoundException::new);
        return schedule;
    }

    public List<Schedule> findAllSchedules(){
        return scheduleRepository.findAll();
    }

    public List<Schedule> findSchedulesForPet(long petId){
        Pet p = petRepository.findById(petId).orElseThrow(PetNotFoundException::new);
        return p.getSchedules();
    }

    public List<Schedule> findSchedulesForEmployee(long empId){
        Employee e = employeeRepository.findById(empId).orElseThrow(UserNotFoundException::new);
        return e.getSchedules();
    }

    public List<Schedule> findScheduleForCustomer(long customerId){
        Customer c = customerRepository.findById(customerId).orElseThrow(UserNotFoundException::new);

        List<Schedule> thisCustomerSchedules = c.getPets()
                .stream()
                .map(Pet::getSchedules)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return thisCustomerSchedules;
    }



}
