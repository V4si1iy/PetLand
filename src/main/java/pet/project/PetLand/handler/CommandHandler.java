package pet.project.PetLand.handler;

import com.pengrad.telegrambot.model.Chat;
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
    private final TelegramSenderService telegramSenderService;
    private final CustomerService customerService;
    public CommandHandler(TelegramSenderService telegramSenderService, CustomerService customerService) {
        this.telegramSenderService = telegramSenderService;
        this.customerService = customerService;

        commandExecute.put(Command.START, this::handleStart); // Добавление команд в хранилище (новые делать по примеру)
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
            if (("/" + command.name()).equals(message.text())) {
                commandExecute.get(message.text()).accept(user, message);
                break;
            }
        }
    }

    private void handleStart(User user, Message message) {
        customerService.customerIsExist(message.chat().id());

        // Написать старт в этом блоке расширяясь в сервисы
    }

    private void handleCancel(User user, Message message) {
//        DeleteMessage messageText = new DeleteMessage(chat.id(),message.messageId())
//        telegramBot.execute(messageText);
//        SendMessage newMessage = new SendMessage(callbackQuery.message().chat().id(), "test1").replyMarkup(inLineKeyboard.shelterInLineKeyboard());
//        telegramBot.execute(newMessage);
    }
}


