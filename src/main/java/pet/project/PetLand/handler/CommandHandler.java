package pet.project.PetLand.handler;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pet.project.PetLand.entity.CallBackData;
import pet.project.PetLand.entity.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@Service
public class CommandHandler {
    // Хранилище для команд (добавление новых команд через конструктор + enum Command)
    private final Map<Command, BiConsumer<User, Message>> commandExecute = new HashMap<>();

    public CommandHandler() {
        commandExecute.put(Command.START, this::handleStart);// Добавление команд в хранилище (новые делать по примеру)
        commandExecute.put(Command.START, this::handleStart);
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
        // Написать старт в этом блоке расширяясь в сервисы
    }

    private void handleCancel(User user, Message message) {
//        DeleteMessage messageText = new DeleteMessage(chat.id(),message.messageId())
//        telegramBot.execute(messageText);
//        SendMessage newMessage = new SendMessage(callbackQuery.message().chat().id(), "test1").replyMarkup(inLineKeyboard.shelterInLineKeyboard());
//        telegramBot.execute(newMessage);
    }
}


