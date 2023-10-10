package pet.project.PetLand.handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pet.project.PetLand.entity.Command;
import pet.project.PetLand.entity.Flag;
import pet.project.PetLand.service.CustomerService;
import pet.project.PetLand.service.TelegramSenderService;
import pet.project.PetLand.util.FlagInput;

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
    private final FlagInput flagInput;
    private final static Logger LOGGER = LoggerFactory.getLogger(CallBackQueryHandler.class);


    public CommandHandler(CustomerService customerService, CallBackQueryHandler callBackQueryHandler, TelegramSenderService telegramSenderService, ManualInputHandler manualInputHandler, FlagInput flagInput) {
        this.customerService = customerService;
        this.callBackQueryHandler = callBackQueryHandler;
        this.telegramSenderService = telegramSenderService;
        this.flagInput = flagInput;


        commandExecute.put(Command.START, this::handleStart); // Добавление команд в хранилище (новые делать по примеру)
        commandExecute.put(Command.CANCEL, this::handleCancel);
        commandExecute.put(Command.MENU,this::handelMenu);
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
        LOGGER.info("Was invoked method -> /start");
            customerService.registerCustomer(user.id());


        // Написать старт в этом блоке расширяясь в сервисы
    }

    private void handleCancel(User user, Message message) {
        LOGGER.info("Was invoked method -> /cancel");
        if (flagInput.flag() != Flag.None && flagInput.flag() != Flag.Customer) {
            flagInput.flagNone();
            callBackQueryHandler.startMenu(user.id());
        } else if(flagInput.flag() == Flag.Customer) {
            telegramSenderService.send(user.id(), "Ввод пользователя нельзя отменить");
        }
        else {
            telegramSenderService.send(user.id(), "В данный момент вы ничего не вводите");
        }

    }

    private void handelMenu(User user, Message message) {
        LOGGER.info("Was invoked method -> /menu");
       if( customerService.customerIsExist(user.id())) {
           callBackQueryHandler.startMenu(user.id());
       }
       else{
           telegramSenderService.send(user.id(), "Вы не зарегестрировались, для старта напишите: /start");
       }
    }
}


