package pet.project.PetLand.handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import org.springframework.stereotype.Service;
import pet.project.PetLand.entity.Command;
import pet.project.PetLand.service.CustomerService;
import pet.project.PetLand.service.TelegramSenderService;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@Service
public class CommandHandler {
    // Хранилище для команд (добавление новых команд через конструктор + enum Command)
    private final Map<Command, BiConsumer<User, Message>> commandExecute = new HashMap<>();
    private final CustomerService customerService;
    private final CallBackQueryHandler callBackQueryHandler;
    private final TelegramSenderService telegramSenderService;
    public CommandHandler(CustomerService customerService, CallBackQueryHandler callBackQueryHandler, TelegramSenderService telegramSenderService) {
        this.customerService = customerService;
        this.callBackQueryHandler = callBackQueryHandler;
        this.telegramSenderService = telegramSenderService;

        commandExecute.put(Command.START, this::handleStart); // Добавление команд в хранилище (новые делать по примеру)
        commandExecute.put(Command.CANCEL, this::handleCancel);
    }

    /**
     * Обрабатывает все команды который использует бот
     *
     * @param user
     * @param message
     */
    // обработчик команд
    public void handler(User user, Message message) {
        Command[] commands = Command.values();
        for (Command command : commands) {
            if (("/" + command.name().toLowerCase()).equals(message.text())) {
                commandExecute.get(command).accept(user, message);
                break;
            }
        }
    }

    private void handleStart(User user, Message message) {
        customerService.customerIsExist(message.chat().id());

        // Написать старт в этом блоке расширяясь в сервисы
    }

    private void handleCancel(User user, Message message) {
        if(callBackQueryHandler.flagReport() || customerService.flagCustomer())
        {
            callBackQueryHandler.updateFlagReport();
            customerService.updateFlagCustomer();
        }
        else {
            telegramSenderService.send(user.id(), "В данный момент вы ничего не вводите");

        }

    }
}


