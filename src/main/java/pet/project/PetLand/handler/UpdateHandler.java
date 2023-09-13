package pet.project.PetLand.handler;

import com.pengrad.telegrambot.model.Update;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;

import java.util.Objects;

@AllArgsConstructor
public class UpdateHandler {
    private final CallBackQueryHandler callBackQueryHandler;
    private final CommandHandler commandHandler;

    /**
     * Метод для обработки всех данных полученных от бота (<b> Главный метод иерархии обработчиков </b>)
     *
     * @param update данные изменения бота
     */
    @Async
    public void handler(Update update) {
        if (!Objects.isNull(update.callbackQuery())) // Определяет нажата ли всплывающая кнопка
        {
            callBackQueryHandler.handler(update.callbackQuery());
        } else if (!update.message().text().isEmpty() && update.message().text().startsWith("/")) // поиск команд через "/"
        {
            commandHandler.handler(update.message().from(), update.message().chat(), update.message().text());
        }
    }
}
