package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.data.User;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    PetService petService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = userService.saveCustomer(convertFromCustomerDTO(customerDTO));
        return convertToCustomerDTO(customer);
    }

    @GetMapping("/customers")
    public List<CustomerDTO> getAllCustomers(){

        return convertToCustomerDTOList(userService.getAllCustomers());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        return convertToCustomerDTO(petService.findPetById(petId).getOwner());
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = userService.saveEmployee(convertFromEmployeeDTO(employeeDTO));
        return convertToEmployeeDTO(employee);
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee = userService.getEmployeeById(employeeId);
        return convertToEmployeeDTO(employee);
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
       userService.setAvailability(daysAvailable,employeeId);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> availableEmployees = userService.findEmployeesForService(employeeDTO.getSkills(),employeeDTO.getDate());

        return convertToEmployeeDTOList(availableEmployees);
    }

    private Customer convertFromCustomerDTO(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

    private CustomerDTO convertToCustomerDTO(Customer user) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(user, customerDTO);
        List<Long> petIds = new ArrayList<Long>();
        for(Pet p : user.getPets()){
            petIds.add(p.getId());
        }
        customerDTO.setPetIds(petIds);
        return customerDTO;
    }

    private Employee convertFromEmployeeDTO(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return employee;
    }

    private EmployeeDTO convertToEmployeeDTO(Employee user) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(user, employeeDTO);
        return employeeDTO;
    }

    private List<EmployeeDTO> convertToEmployeeDTOList(List<Employee> users) {
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();
        for (Employee user: users) {
            EmployeeDTO cd = new EmployeeDTO();
            BeanUtils.copyProperties(user, cd);
            employeeDTOList.add(cd);
        }
        return employeeDTOList;
    }

    private List<CustomerDTO> convertToCustomerDTOList(List<Customer> users) {
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        for (Customer user: users) {
            CustomerDTO cd = new CustomerDTO();
            BeanUtils.copyProperties(user, cd);
            List<Long> petIds = new ArrayList<Long>();
            for(Pet p : user.getPets()){
                petIds.add(p.getId());
            }
            cd.setPetIds(petIds);
            customerDTOList.add(cd);
        }
        return customerDTOList;
    }

}
