package pet.project.PetLand.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pet.project.PetLand.entity.CallBackData;
import pet.project.PetLand.entity.Command;
import pet.project.PetLand.service.InLineKeyboard;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

@Service
public class CallBackQueryHandler {
    // Хранилище для команд (добавление новых команд через конструктор + enum CallBackData)
    private final Map<CallBackData, BiConsumer<User, CallbackQuery>> commandExecute = new HashMap<>();
    InLineKeyboard inLineKeyboard;
    TelegramBot telegramBot;
    private boolean flag = false;

    public CallBackQueryHandler(InLineKeyboard inLineKeyboard, TelegramBot telegramBot) {
        // пример добавления команды: commandExecute.put(CallBackData.<Button>, this::handle<Button>);
        this.inLineKeyboard = inLineKeyboard;
        this.telegramBot = telegramBot;
        commandExecute.put(CallBackData.RECOMMENDATIONS, this::handleRecommendations);
        commandExecute.put(CallBackData.SHELTER_INFORMATION, this::handleInformationShelter);
        commandExecute.put(CallBackData.RECOMMENDATIONS_SHELTER, this::handleRecommendationShelter);
        commandExecute.put(CallBackData.RECOMMENDATIONS_DOG, this::handleRecommendationDog);
        commandExecute.put(CallBackData.RECOMMENDATIONS_CAT, this::handleRecommendationCat);
        commandExecute.put(CallBackData.HOW_TAKE_PET, this::handleHowTakePet);
        commandExecute.put(CallBackData.VOLUNTEER, this::handleVolunteer);
        commandExecute.put(CallBackData.REPORT, this::handleReport);
    }


    /**
     * Обрабатывает все нажатые кнопки в боте
     *
     * @param callbackQuery
     */
    // открытый обработчик кнопок (вызывать его если надо)
    public void handler(CallbackQuery callbackQuery) {
        User user = callbackQuery.from();
        CallBackData callBackData = CallBackData.parse(callbackQuery.data());
        if (Objects.isNull(callBackData)) {
            return;
        }
        handler(user, callBackData, callbackQuery);
    }

    private void handler(User user, CallBackData callBackData, CallbackQuery callbackQuery) {
        CallBackData[] callBackDates = CallBackData.values();
        for (CallBackData data : callBackDates) {
            if (callBackData == data) {
                commandExecute.get(data).accept(user, callbackQuery);
                break;
            }
        }
    }

    private void handleInformationShelter(User user, CallbackQuery callbackQuery) {
        EditMessageText editMessage = new EditMessageText(callbackQuery.message().chat().id(), callbackQuery.message().messageId(),
                "Здесь должна быть информация");
        telegramBot.execute(editMessage);
        sendStartMenu(callbackQuery.message().chat().id());
    }

    private void handleRecommendations(User user, CallbackQuery callbackQuery) {
        EditMessageText messageText = new EditMessageText(callbackQuery.message().chat().id(), callbackQuery.message().messageId(),
                "Какая рекомендация вам нужна?")
                .replyMarkup(inLineKeyboard.recommendationsInLineKeyboard());
        telegramBot.execute(messageText);
    }

    private void handleRecommendationShelter(User user, CallbackQuery callbackQuery) {
        EditMessageText messageText = new EditMessageText(callbackQuery.message().chat().id(), callbackQuery.message().messageId(),
                "Здесь должна быть техника безопасни в приюте и рекомендации по приюту");
        telegramBot.execute(messageText);
        sendStartMenu(callbackQuery.message().chat().id());

    }

    private void handleRecommendationDog(User user, CallbackQuery callbackQuery) {
        EditMessageText messageText = new EditMessageText(callbackQuery.message().chat().id(), callbackQuery.message().messageId(),
                "Здесь рекомендации по ухаживанию собаки и щенка");
        telegramBot.execute(messageText);
        sendStartMenu(callbackQuery.message().chat().id());

    }

    private void handleRecommendationCat(User user, CallbackQuery callbackQuery) {
        EditMessageText messageText = new EditMessageText(callbackQuery.message().chat().id(), callbackQuery.message().messageId(),
                "Здесь рекомендации по ухаживанию кошки и котенка");
        telegramBot.execute(messageText);
        sendStartMenu(callbackQuery.message().chat().id());

    }

    private void handleHowTakePet(User user, CallbackQuery callbackQuery) {
        EditMessageText messageText = new EditMessageText(callbackQuery.message().chat().id(), callbackQuery.message().messageId(),
                "Здесь должна быть информация как взять питомца");
        telegramBot.execute(messageText);
        sendStartMenu(callbackQuery.message().chat().id());

    }

    private void handleVolunteer(User user, CallbackQuery callbackQuery) {
        EditMessageText messageText = new EditMessageText(callbackQuery.message().chat().id(), callbackQuery.message().messageId(),
                "Спасибо что оставили заявку на звонок,ближайшее время с вами свяжется наш волонтер");
        telegramBot.execute(messageText);
        sendStartMenu(callbackQuery.message().chat().id());

    }

    private void handleReport(User user, CallbackQuery callbackQuery) {
        this.flag = true;
        EditMessageText messageText = new EditMessageText(callbackQuery.message().chat().id(), callbackQuery.message().messageId(),
                "Напишите пожалуйста отчет по образцу: \n" +
                "Имя: <Имя животного> \n"
                + "Отчет: <отчет о животном(Рацион животного, общее самочувствие и привыкание к новому месту, изменение в поведении: отказ от старых привычек, приобретение новых> \n"
                + "Для отмены напишите /cancel");
        telegramBot.execute(messageText);

    }

    public void sendStartMenu(Long chatId) {
        SendMessage newMessage = new SendMessage(chatId, "Здесь должен будет написан выбранный приют").replyMarkup(inLineKeyboard.shelterInLineKeyboard());
        telegramBot.execute(newMessage);
    }

    public boolean flagReport() {
        return flag;
    }

    public void updateFlagReport() {
        flag = false;
    }

}
