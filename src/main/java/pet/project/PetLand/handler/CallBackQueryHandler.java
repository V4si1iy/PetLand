package pet.project.PetLand.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pet.project.PetLand.entity.CallBackData;
import pet.project.PetLand.model.Shelter;
import pet.project.PetLand.service.InLineKeyboard;
import pet.project.PetLand.service.ShelterService;
import pet.project.PetLand.util.FlagInput;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

import static pet.project.PetLand.util.Constant.*;


@Service
public class CallBackQueryHandler {
    // Хранилище для команд (добавление новых команд через конструктор + enum CallBackData)
    private final Map<CallBackData, BiConsumer<User, CallbackQuery>> commandExecute = new HashMap<>();
    private final InLineKeyboard inLineKeyboard;
    private final TelegramBot telegramBot;
    private final ShelterService shelterService;
    private final FlagInput flagInput;

    private final static Logger LOGGER = LoggerFactory.getLogger(CallBackQueryHandler.class);
    private Shelter shelter;

    public CallBackQueryHandler(InLineKeyboard inLineKeyboard, TelegramBot telegramBot, ShelterService shelterService, FlagInput flagInput) {
        // пример добавления команды: commandExecute.put(CallBackData.<Button>, this::handle<Button>);
        this.inLineKeyboard = inLineKeyboard;
        this.telegramBot = telegramBot;
        this.shelterService = shelterService;
        this.flagInput = flagInput;


        commandExecute.put(CallBackData.RECOMMENDATIONS, this::handleRecommendations);
        commandExecute.put(CallBackData.SHELTER_INFORMATION, this::handleInformationShelter);
        commandExecute.put(CallBackData.RECOMMENDATIONS_SHELTER, this::handleRecommendationShelter);
        commandExecute.put(CallBackData.RECOMMENDATIONS_DOG, this::handleRecommendationDog);
        commandExecute.put(CallBackData.RECOMMENDATIONS_CAT, this::handleRecommendationCat);
        commandExecute.put(CallBackData.HOW_TAKE_PET, this::handleHowTakePet);
        commandExecute.put(CallBackData.VOLUNTEER, this::handleVolunteer);
        commandExecute.put(CallBackData.REPORT, this::handleReport);
        commandExecute.put(CallBackData.REPORT_TELEGRAM, this::handleReportTelegram);
        commandExecute.put(CallBackData.REPORT_YANDEX_FORM, this::handleReportYandexForm);

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
            Shelter shelter = shelterService.findByName(callbackQuery.data());
            if (!Objects.isNull(shelter)) {
                this.shelter = shelter;
                handleAllShelters(callbackQuery, shelter);
            }
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
        LOGGER.info("Was invoked method to show information about shelter");
        EditMessageText editMessage = new EditMessageText(callbackQuery.message().chat().id(), callbackQuery.message().messageId(),
                "Адресс: " + shelter.getAddress() + "\n" +
                        "Описание: " + shelter.getDescription() + "\n" +
                        "Правила: " + shelter.getRules() + "\n" +
                        "Как зайти в здание: " + shelter.getLocationMap());
        telegramBot.execute(editMessage);
        startMenu(user.id());
    }

    private void handleRecommendations(User user, CallbackQuery callbackQuery) {
        LOGGER.info("Was invoked method to show recommendation menu");
        EditMessageText messageText = new EditMessageText(callbackQuery.message().chat().id(), callbackQuery.message().messageId(), "Какая рекомендация вам нужна?").replyMarkup(inLineKeyboard.recommendationsInLineKeyboard());
        telegramBot.execute(messageText);
    }

    private void handleRecommendationShelter(User user, CallbackQuery callbackQuery) {
        LOGGER.info("Was invoked method to show recommendation about shelter");
        EditMessageText messageText = new EditMessageText(callbackQuery.message().chat().id(), callbackQuery.message().messageId(), REASONS_FOR_REFUSAL + "Если вы собираетесь в приют для собак:\n" + DOG_SHELTER_SAFETY + "Если вы собираетесь в приют для кошек:\n" + CAT_SHELTER_SAFETY);
        telegramBot.execute(messageText);
        startMenu(user.id());

    }

    private void handleRecommendationDog(User user, CallbackQuery callbackQuery) {
        LOGGER.info("Was invoked method to show recommendation about dog");
        executeRecommendationAnimal(user,callbackQuery,DOG_TRANSPORTATION,HOME_FOR_PUPPY,HOME_FOR_ADULT_DOG,HOME_FOR_RESTRICTED_DOG);
        startMenu(user.id());

    }

    private void handleRecommendationCat(User user, CallbackQuery callbackQuery) {
        LOGGER.info("Was invoked method to show recommendation about cat");
        executeRecommendationAnimal(user,callbackQuery,CAT_TRANSPORTATION,HOME_FOR_KITTY, HOME_FOR_ADULT_CAT,HOME_FOR_RESTRICTED_CAT);
        startMenu(user.id());

    }
    private void executeRecommendationAnimal(User user,CallbackQuery callbackQuery, String text1 , String text2, String tex3,String text4)
    {
        EditMessageText messageText = new EditMessageText(callbackQuery.message().chat().id(), callbackQuery.message().messageId(), text1);
        SendMessage message1 = new SendMessage(user.id(), text2);
        SendMessage message2 = new SendMessage(user.id(), tex3);
        SendMessage message3 = new SendMessage(user.id(), text4);

        telegramBot.execute(messageText);
        telegramBot.execute(message1);
        telegramBot.execute(message2);
        telegramBot.execute(message3);

    }

    private void handleHowTakePet(User user, CallbackQuery callbackQuery) {
        LOGGER.info("Was invoked method to show information about how take pet");
        EditMessageText messageText = new EditMessageText(callbackQuery.message().chat().id(), callbackQuery.message().messageId(), "Здесь должна быть информация как взять питомца");
        telegramBot.execute(messageText);
        startMenu(user.id());

    }

    private void handleVolunteer(User user, CallbackQuery callbackQuery) {
        LOGGER.info("Was invoked method to ask volunteer");
        EditMessageText messageText = new EditMessageText(callbackQuery.message().chat().id(), callbackQuery.message().messageId(), "Спасибо что оставили заявку на звонок,ближайшее время с вами свяжется наш волонтер");
        telegramBot.execute(messageText);
        startMenu(user.id());

    }

    private void handleReport(User user, CallbackQuery callbackQuery) {
        LOGGER.info("Was invoked method to show report menu");
        EditMessageText messageText = new EditMessageText(callbackQuery.message().chat().id(), callbackQuery.message().messageId(), "Как вы хотите представить отчет?").replyMarkup(inLineKeyboard.choseKindReport());
        telegramBot.execute(messageText);
    }

    private void handleReportTelegram(User user, CallbackQuery callbackQuery) {
        LOGGER.info("Was invoked method to get report by telegram bot");
        flagInput.flagReport();
        EditMessageText messageText = new EditMessageText(callbackQuery.message().chat().id(), callbackQuery.message().messageId(), "Напишите пожалуйста отчет по образцу: \n" + "Имя: <Имя животного> \n" + "Отчет: <отчет о животном(Рацион животного, общее самочувствие и привыкание к новому месту, изменение в поведении: отказ от старых привычек, приобретение новых> \n" + "Для отмены напишите /cancel");
        telegramBot.execute(messageText);
    }

    private void handleReportYandexForm(User user, CallbackQuery callbackQuery) {
        LOGGER.info("Was invoked method to get report by Yandex Form");
        DeleteMessage delete = new DeleteMessage(user.id(), callbackQuery.message().messageId());
        telegramBot.execute(delete);
        startMenu(user.id());
    }

    public void handleAllShelters(CallbackQuery callbackQuery, Shelter shelter) {
        LOGGER.info("Was invoked method to show menu of shelter");
        EditMessageText messageText = new EditMessageText(callbackQuery.message().chat().id(), callbackQuery.message().messageId(), shelter.getName()).replyMarkup(inLineKeyboard.shelterInLineKeyboard());
        telegramBot.execute(messageText);
    }

    public void startMenu(Long chatId) {
        LOGGER.info("Was invoked method to show all shelters");
        if (shelterService.findAll().isEmpty()) {
            telegramBot.execute(new SendMessage(chatId, "В данный момент у нас нет работающих приютов"));
        } else {
            SendMessage newMessage = new SendMessage(chatId, "Выберите приют").replyMarkup(inLineKeyboard.allSheltersInLineKeyboard());
            telegramBot.execute(newMessage);
        }
    }

}
