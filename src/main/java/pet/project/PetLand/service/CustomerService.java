package pet.project.PetLand.service;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import org.springframework.stereotype.Service;
import pet.project.PetLand.handler.CallBackQueryHandler;
import pet.project.PetLand.handler.ManualInputHandler;
import pet.project.PetLand.model.Customer;
import pet.project.PetLand.repository.CustomerRepository;


import java.util.Collection;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private TelegramSenderService telegramSenderService;
    private final CallBackQueryHandler callBackQueryHandler;
    private final ManualInputHandler manualInputHandler;
    private final Pattern patternCustomer = Pattern.compile("(Имя:)(\\s)([\\W+]+)(\\n)(Фамилия:)(\\s)([\\W+]+)");

    public CustomerService(CustomerRepository customerRepository, TelegramSenderService telegramSenderService, CallBackQueryHandler callBackQueryHandler, ManualInputHandler manualInputHandler) {

        this.customerRepository = customerRepository;
        this.telegramSenderService = telegramSenderService;
        this.callBackQueryHandler = callBackQueryHandler;
        this.manualInputHandler = manualInputHandler;
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

    public Customer delete(Long id) {
        Customer customer = findById(id);
        if (customer == null) {
            return null;
        } else {
            customerRepository.deleteById(id);
            return customer;
        }
    }

    public Customer findByChatId(Long id) {

        return customerRepository.findByChatId(id);
    }

    public boolean customerIsExist(Long chatId) {
        Customer customer = findByChatId(chatId);
        if (!Objects.isNull(customer)) {
            return true;
        } else return false;
    }

    public void registerCustomer(Long chatId) {
        if (customerIsExist(chatId)) {
            telegramSenderService.send(chatId, "Здравствуйте! " + findByChatId(chatId).getName() + " Давно Вас не видели)");
            callBackQueryHandler.startMenu(chatId);
        } else {
            telegramSenderService.send(chatId, "Здраствуйте! Вы новый пользователь. Введите свои данные по шаблону: \n" + "Имя: <Имя> \n" + "Фамилия: <Фамилия> \n" + "Отменить действие /cancel");
            manualInputHandler.flagCustomer();
        }
    }

    public void createCustomerStart(Chat chat, Message message) {

        Matcher matcher = patternCustomer.matcher(message.text());
        if (matcher.matches()) {
            String name = matcher.group(3);
            String surname = matcher.group(7);
            create(new Customer(chat.id(), surname, name));
        }

    }
}
