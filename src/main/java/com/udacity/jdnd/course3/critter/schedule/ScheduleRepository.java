package com.udacity.jdnd.course3.critter.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findSchedulesByPets_Id(Long id);

    List<Schedule> findSchedulesByEmployees_Id(Long id);

    List<Schedule> findSchedulesByPets_Customer_Id(Long id);
}
