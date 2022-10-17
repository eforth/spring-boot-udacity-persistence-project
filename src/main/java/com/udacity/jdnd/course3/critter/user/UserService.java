package com.udacity.jdnd.course3.critter.user;

import com.google.common.collect.Sets;
import com.udacity.jdnd.course3.critter.pet.Pet;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
public class UserService {

    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public UserService(CustomerRepository customerRepository, EmployeeRepository employeeRepository, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<Customer> allCustomers() {
        List<Customer> customers = new ArrayList<>();

        for (Customer customer : customerRepository.findAll()) {
            List<Pet> pets = customer.getPets();
            customer.setPets(pets);
            customers.add(customer);
        }

        return customers;
    }

    public Customer getOwnerByPet(Long petId) {
        return customerRepository.findCustomerByPets_Id(petId);
    }

    public Customer findByCustomerId(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    public Employee findEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public List<Employee> findEmployeeAvailability(Set<EmployeeSkill> skills, LocalDate date) {
        Set<DayOfWeek> dayOfWeeks = Sets.newHashSet(date.getDayOfWeek());
        List<Employee> employees = employeeRepository.getDistinctByDaysAvailableInAndSkillsIn(dayOfWeeks, skills);
        return employees.stream().filter(e -> this.strictFilter(skills, date, e)).collect(Collectors.toList());
    }

    private Boolean strictFilter (Set<EmployeeSkill> skills, LocalDate date, Employee employee) {
        return employee.getDaysAvailable().contains(date.getDayOfWeek()) && employee.getSkills().containsAll(skills);
    }
}
