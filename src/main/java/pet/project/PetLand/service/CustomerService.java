package pet.project.PetLand.service;

import org.springframework.stereotype.Service;
import pet.project.PetLand.model.Customer;
import pet.project.PetLand.repository.CustomerRepository;


import java.util.Collection;
import java.util.Objects;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private TelegramSenderService telegramSenderService;

    public CustomerService(CustomerRepository customerRepository) {

        this.customerRepository = customerRepository;
    }

    public Collection<Customer> findAll() {

        return customerRepository.findAll();
    }

    public Customer findById(Long id) {

        return customerRepository.findById(id).orElse(null);
    }

    public Customer create(Customer customer) {

        return customerRepository.save(customer);
    }

    public Customer update(Customer customer) {
        return (findById(customer.getId()) != null) ? customerRepository.save(customer) : null;
    }

    public void delete(Long id) {
        customerRepository.deleteById(id);
    }

    public Customer findByChatId(Long id) {

        return customerRepository.findByChatId(id);
    }
    public Customer customerIsExist(Long chatId) {
        Customer customer = findByChatId(chatId);

        if (!Objects.isNull(customer)) {
            return customer;
        } else {
            telegramSenderService.send(chatId, "Введите свои данные");
        }

    }

}
