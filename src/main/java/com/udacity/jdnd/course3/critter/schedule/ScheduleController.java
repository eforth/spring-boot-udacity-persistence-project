package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final UserService userService;
    private final PetService petService;

    public ScheduleController(ScheduleService scheduleService, UserService userService, PetService petService) {
        this.scheduleService = scheduleService;
        this.userService = userService;
        this.petService = petService;
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = toEntity(scheduleDTO);
        List<Employee> employees = scheduleDTO.getEmployeeIds().stream()
                .map(userService::findEmployeeById).collect(Collectors.toList());
        List<Pet> pets = scheduleDTO.getPetIds().stream().map(petService::findById)
                .collect(Collectors.toList());
        schedule.setEmployees(employees);
        schedule.setPets(pets);
        scheduleService.save(schedule);
        return toDTO(schedule);
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        return scheduleService.findAll().stream().map(ScheduleController::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        return scheduleService.findSchedulesByPetId(petId).stream()
                .map(ScheduleController::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        return scheduleService.findSchedulesByEmployeesId(employeeId).stream()
                .map(ScheduleController::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        return scheduleService.findSchedulesByPets_Customer_Id(customerId).stream()
                .map(ScheduleController::toDTO).collect(Collectors.toList());
    }

    private static ScheduleDTO toDTO (Schedule schedule) {
        ScheduleDTO dto = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, dto);
        List<Long> employeeIds = schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList());
        List<Long> petIds = schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList());
        dto.setEmployeeIds(employeeIds);
        dto.setPetIds(petIds);
        return dto;
    }

    private static Schedule toEntity (ScheduleDTO dto) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(dto, schedule);
        return schedule;
    }
}
