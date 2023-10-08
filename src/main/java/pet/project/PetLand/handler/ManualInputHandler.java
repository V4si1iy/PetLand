package pet.project.PetLand.handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pet.project.PetLand.entity.Flag;
import pet.project.PetLand.service.CustomerService;
import pet.project.PetLand.service.ReportService;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@Service
public class ManualInputHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(ManualInputHandler.class);
    private final Map<Flag, BiConsumer<User, Message>> commandExecute = new HashMap<>();
    private final ReportService reportService;
    private final CustomerService customerService;
    private final CallBackQueryHandler callBackQueryHandler;

    public ManualInputHandler(ReportService reportService, CustomerService customerService, CallBackQueryHandler callBackQueryHandler) {
        this.reportService = reportService;
        this.customerService = customerService;
        this.callBackQueryHandler = callBackQueryHandler;
        commandExecute.put(Flag.Report, this::reportHandler);
        commandExecute.put(Flag.Customer, this::reportCustomerStart);
    }

    /**
     * Метод обрабатывает весь пользовательский ввод из телеграм бота
     * @param user
     * @param message
     */
    protected void handler(User user, Message message) {
            Flag[] flags = Flag.values();
            for (Flag data : flags) {
                if (flag() == data) {
                    commandExecute.get(data).accept(user, message);
                    break;
                }
            }
    }

    private void reportHandler(User user, Message message) {
        LOGGER.info("Was invoked method to input Report by user");
        if (reportService.createReport(message)) {
            flagNone();
            callBackQueryHandler.startMenu(user.id());
        }
    }

    private void reportCustomerStart(User user, Message message) {
        LOGGER.info("Was invoked method to input Customer name and surname by user");
        customerService.createCustomerStart(message.chat(), message);
        flagNone();
        callBackQueryHandler.startMenu(user.id());
    }


    private static Flag flag = Flag.None;

    public Flag flag() {
        LOGGER.info("Was invoked method to get flag");
        LOGGER.debug(String.valueOf(flag));
        return flag;
    }

    public void flagNone() {
        LOGGER.info("Was invoked method to change flag to None");
        flag = Flag.None;
    }

    public void flagCustomer() {
        LOGGER.info("Was invoked method to change flag to Customer");
        flag = Flag.Customer;
    }

    public void flagReport() {
        LOGGER.info("Was invoked method to change flag report to Report");
        flag = Flag.Report;
    }


}
