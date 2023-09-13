package pet.project.PetLand.handler;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
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
    private final Map<Command, BiConsumer<User, Chat>> commandExecute = new HashMap<>();

    public CommandHandler() {
        commandExecute.put(Command.START, this::handleStart); // Добавление команд в хранилище (новые делать по примеру)
    }

    /**
     * Обрабатывает все команды который использует бот
     * @param user
     * @param chat
     * @param text
     */
    // обработчик команд
    public void handler(User user, Chat chat, String text) {
        Command[] commands = Command.values();
        for (Command command : commands) {
            if (("/" + command.name()).equals(text)) {
                commandExecute.get(text).accept(user, chat);
                break;
            }
        }
    }

    private void handleStart(User user, Chat chat) {
        // Написать старт в этом блоке расширяясь в сервисы
    }

}
