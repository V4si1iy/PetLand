package pet.project.PetLand.handler;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import org.springframework.stereotype.Service;
import pet.project.PetLand.entity.CallBackData;
import pet.project.PetLand.entity.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
@Service
public class CallBackQueryHandler {
    // Хранилище для команд (добавление новых команд через конструктор + enum CallBackData)
    private final Map<CallBackData, BiConsumer<User, String>> commandExecute = new HashMap<>();

    public CallBackQueryHandler() {
        // пример добавления команды: commandExecute.put(CallBackData.<Button>, this::handle<Button>);
    }

    /**
     * Обрабатывает все нажатые кнопки в боте
     * @param callbackQuery
     */
    // открытый обработчик кнопок (вызывать его если надо)
    public  void handler(CallbackQuery callbackQuery)
    {
        String inLineMessageId = callbackQuery.inlineMessageId();
        User user = callbackQuery.from();
        CallBackData callBackData = CallBackData.parse(callbackQuery.data());
        if(Objects.isNull(callBackData))
        {
            return;
        }
        handler(user,callBackData,inLineMessageId);
    }

    private void handler(User user, CallBackData callBackData, String inLineMessageId) {
        CallBackData[] callBackDates = CallBackData.values();
        for (CallBackData data : callBackDates) {
            if (callBackData ==data) {
                commandExecute.get(data).accept(user,inLineMessageId);
                break;
            }
        }
    }
}
