package pet.project.PetLand.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pet.project.PetLand.model.Customer;
import pet.project.PetLand.repository.CustomerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    private List<Customer> getInitialTestCustomers() {
        Customer customer1 = getTestCustomer(1L, "Александр", "+79099000000");
        Customer customer2 = getTestCustomer(2L, "Сергей", "+79109100000");

        List<Customer> customers = new ArrayList<>();
        customers.add(customer1);
        customers.add(customer2);

        return customers;
    }

    private Customer getTestCustomer(long id, String name, String phoneNumber) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName(name);
        customer.setPhone(phoneNumber);
        return customer;
    }
    @Test
    void getCustomers() {
        List<Customer> customers = getInitialTestCustomers();
        when(customerRepository.findAll()).thenReturn(customers);
        List<Customer> expected = customers;
        List<Customer> actual = (List<Customer>) customerService.findAll();
        Assertions.assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }
    @Test
    void findCustomerById() {
        Customer customer = getTestCustomer(12L, "Петр", "+79107004602");
        Customer customerWrong = getTestCustomer(13L, "Марина", "+79023002312");

        when(customerRepository.findById(12L)).thenReturn(Optional.of(customer));
        when(customerRepository.findById(13L)).thenReturn(Optional.of(customerWrong));
        Customer expected = customer;
        Customer actual = customerService.findById(12L);

        Assertions.assertThat(actual).isEqualTo(expected);

        Customer actualWrong = customerService.findById(13L);
        Assertions.assertThat(actualWrong).isNotEqualTo(expected);
    }
    @Test
    void createCustomer() {
        Customer customer = getTestCustomer(12L, "Петр", "+79107004602");
        when(customerRepository.save(customer)).thenReturn(customer);
        Customer expected = customer;
        Customer actual = customerService.create(customer);
        Assertions.assertThat(actual).isEqualTo(expected);

        Customer customer1 = getTestCustomer(13L, null, null);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer1);
        expected = customer1;
        actual = customerService.create(customer1);
        Assertions.assertThat(actual).isEqualTo(expected);
    }
    @Test
    void findCustomerByChatId() {
        Customer customer = getTestCustomer(12L, "Петр", "+79107004602");
        when(customerRepository.findByChatId(12L)).thenReturn(customer);
        Customer expected = customer;
        Customer actual = customerService.findByChatId(12L);
        Assertions.assertThat(actual).isEqualTo(expected);
    }
    @Test
    void updateCustomer() {
        Customer expected = getTestCustomer(12L, "Петр", "+79990000000");

        when(customerRepository.findById(expected.getId())).thenReturn(Optional.ofNullable(expected));
        when(customerRepository.save(expected)).thenReturn(expected);
        Customer actual = customerService.update(expected);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void deleteCustomer() {
            when(customerRepository.findById(13L)).thenReturn(Optional.empty());
            Customer expected = null;
            Customer actual = customerService.delete(13L);
            Assertions.assertThat(actual).isEqualTo(expected);
            Customer customer = getTestCustomer(12L, "Петр", "+79990000000");
            when(customerRepository.findById(12L)).thenReturn(Optional.of(customer));

            expected = customer;
            actual = customerService.delete(12L);
            Assertions.assertThat(actual).isEqualTo(expected);
        }
    }

