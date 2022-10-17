package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PetService petService;

    public UserController(UserService userService, PetService petService) {
        this.userService = userService;
        this.petService = petService;
    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        return toCustomerDTO(userService.saveCustomer(toCustomerEntity(customerDTO)));
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers() {
        return userService.allCustomers().stream().map(UserController::toCustomerDTO).collect(Collectors.toList());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId) {
        return toCustomerDTO(userService.getOwnerByPet(petId));
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return toEmployeeDTO(userService.saveEmployee(toEmployeeEntity(employeeDTO)));
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        return toEmployeeDTO(userService.findEmployeeById(employeeId));
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        Employee employee = userService.findEmployeeById(employeeId);
        employee.setDaysAvailable(daysAvailable);
        userService.saveEmployee(employee);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        return userService.findEmployeeAvailability(employeeDTO.getSkills(), employeeDTO.getDate())
                .stream().map(UserController::toEmployeeDTO).collect(Collectors.toList());
    }

    private static CustomerDTO toCustomerDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        List<Long> petIds = customer.getPets().stream().map(Pet::getId).collect(Collectors.toList());
        BeanUtils.copyProperties(customer, dto);
        dto.setPetIds(petIds);
        return dto;
    }

    private static EmployeeDTO toEmployeeDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        BeanUtils.copyProperties(employee, dto);
        return dto;
    }

    public Customer toCustomerEntity(CustomerDTO dto) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(dto, customer);

        if (dto.getPetIds() != null) {
            for (Long id: dto.getPetIds()) {
                customer.getPets().add(petService.findById(id));
            }
        }

        return customer;
    }

    private static Employee toEmployeeEntity(EmployeeDTO dto) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(dto, employee);
        return employee;
    }
}
