package com.udacity.jdnd.course3.critter.schedule;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> findSchedulesByPetId(Long id) {
        return scheduleRepository.findSchedulesByPets_Id(id);
    }

    public List<Schedule> findSchedulesByEmployeesId(Long id) {
        return scheduleRepository.findSchedulesByEmployees_Id(id);
    }

    public List<Schedule> findSchedulesByPets_Customer_Id(Long id) {
        return scheduleRepository.findSchedulesByPets_Customer_Id(id);
    }
}
