package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.data.Schedule;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import com.udacity.jdnd.course3.critter.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    UserService userService;

    @Autowired
    PetService petService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {

        //Check if the schedule exists
        Schedule schedule;
        try {
            schedule = scheduleService.findSchedule(scheduleDTO.getId());
        } catch (Exception e){
            schedule = new Schedule();
        }

        schedule.setDate(scheduleDTO.getDate());
        schedule.setEmployees(userService.findEmployees(scheduleDTO.getEmployeeIds()));
        schedule.setActivities(scheduleDTO.getActivities());
        schedule.setPets(petService.findPets(scheduleDTO.getPetIds()));

        schedule = scheduleService.saveSchedule(schedule);

        return convertToScheduleDTO(schedule);

    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        return convertToScheduleDTOList(scheduleService.findAllSchedules());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        return convertToScheduleDTOList(scheduleService.findSchedulesForPet(petId));
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        return convertToScheduleDTOList(scheduleService.findSchedulesForEmployee(employeeId));
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        return convertToScheduleDTOList(scheduleService.findScheduleForCustomer(customerId));
    }

    private ScheduleDTO convertToScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO, "employees", "pets");

        List<Long> employeeIds = new ArrayList<>();
        for(Employee e : schedule.getEmployees()){
            employeeIds.add(e.getId());
        }

        List<Long> petIds = new ArrayList<>();
        for(Pet p : schedule.getPets()){
            petIds.add(p.getId());
        }

        scheduleDTO.setEmployeeIds(employeeIds);
        scheduleDTO.setPetIds(petIds);

        return scheduleDTO;
    }

    private List<ScheduleDTO> convertToScheduleDTOList(List<Schedule> schedules) {

        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
        for(Schedule schedule : schedules){
            scheduleDTOs.add(convertToScheduleDTO(schedule));
        }

        return scheduleDTOs;
    }
}
