package pet.project.PetLand.handler;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import org.springframework.stereotype.Service;
import pet.project.PetLand.entity.Command;
import pet.project.PetLand.service.TelegramSenderService;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@Service
public class CommandHandler {
    // Хранилище для команд (добавление новых команд через конструктор + enum Command)
    private final Map<Command, BiConsumer<User, Chat>> commandExecute = new HashMap<>();
    private final TelegramSenderService telegramSenderService;

    public CommandHandler(TelegramSenderService telegramSenderService) {
        this.telegramSenderService = telegramSenderService;

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
        telegramSenderService.send(chat.id(), "Привет! Вы к нам за верным другом? Тогда вперед!");// Написать старт в этом блоке расширяясь в сервисы
    }



}
