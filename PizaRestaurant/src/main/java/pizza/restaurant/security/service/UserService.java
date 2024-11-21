package pizza.restaurant.security.service;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pizza.restaurant.domain.service.BaseService;
import pizza.restaurant.security.entity.Customer;
import pizza.restaurant.security.entity.Role;
import pizza.restaurant.security.entity.User;
import pizza.restaurant.security.repository.UserRepository;
import pizza.restaurant.security.request.CustomerRequest;

import java.util.List;

/**
 * User Service
 */
@Service
public class UserService extends BaseService<User, UserRepository, Long> {

    /**
     * PasswordEncoder instance
     */
    private final PasswordEncoder bcryptEncoder;


    /**
     * Dependency injection
     *
     * @param bcryptEncoder
     */
    public UserService(UserRepository userRepository, PasswordEncoder bcryptEncoder) {
        super(userRepository);
        this.bcryptEncoder = bcryptEncoder;
    }


    public List<User> getCustomers() {
        return this.baseRepository.findAllByRole(Role.Customer);
    }

    public List<User> getEmployees() {
        return this.baseRepository.findAllByRole(Role.Employee);
    }

    public User getUserByUserName(String userName) {
        return baseRepository.findByUsername(userName);
    }

    /**
     * This method saves new customers
     *
     * @param customerRequest
     * @return
     */
    public Customer register(CustomerRequest customerRequest) {
        Customer newCustomer = new Customer();
        newCustomer.setUsername(customerRequest.getUsername());
        newCustomer.setFullName(customerRequest.getFullName());
        newCustomer.setPassword(bcryptEncoder.encode(customerRequest.getPassword()));
        newCustomer.setRole(Role.Customer);
        return this.baseRepository.save(newCustomer);
    }

    /**
     * Get current authenticated customer
     *
     * @return Customer
     */
    public Customer getCurrentCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Customer) this.getUserByUserName(authentication.getName());
    }
}
