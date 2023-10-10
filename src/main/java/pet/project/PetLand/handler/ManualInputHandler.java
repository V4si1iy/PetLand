package pet.project.PetLand.handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pet.project.PetLand.entity.Flag;
import pet.project.PetLand.service.CustomerService;
import pet.project.PetLand.service.ReportService;
import pet.project.PetLand.util.FlagInput;

import java.io.IOException;
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
    private final FlagInput flagInput;

    public ManualInputHandler(ReportService reportService, CustomerService customerService, CallBackQueryHandler callBackQueryHandler, FlagInput flagInput) {
        this.reportService = reportService;
        this.customerService = customerService;
        this.callBackQueryHandler = callBackQueryHandler;
        this.flagInput = flagInput;
        commandExecute.put(Flag.Report, this::reportHandler);
        commandExecute.put(Flag.Customer, this::reportCustomerStart);
    }

    /**
     * Метод обрабатывает весь пользовательский ввод из телеграм бота
     *
     * @param user
     * @param message
     */
    protected void handler(User user, Message message) {
        Flag[] flags = Flag.values();
        for (Flag data : flags) {
            if (flagInput.flag() == data) {
                commandExecute.get(data).accept(user, message);
                break;
            }
        }
    }

    private void reportHandler(User user, Message message) {
        LOGGER.info("Was invoked method to input Report by user");
        if (reportService.createReport(message)) {
            flagInput.flagNone();
            callBackQueryHandler.startMenu(user.id());
        }
    }

    private void reportCustomerStart(User user, Message message) {
        LOGGER.info("Was invoked method to input Customer name and surname by user");
        if (customerService.createCustomerStart(message.chat(), message)) {
            flagInput.flagNone();
            callBackQueryHandler.startMenu(user.id());
        }
    }


}
