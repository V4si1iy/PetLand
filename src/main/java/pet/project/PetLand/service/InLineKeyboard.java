package pet.project.PetLand.service;


import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.stereotype.Service;
import pet.project.PetLand.entity.CallBackData;
import pet.project.PetLand.model.Shelter;

@Service
public class InLineKeyboard {
    private InlineKeyboardMarkup inlineKeyboardMarkup;
    private final ShelterService shelterService;

    public InLineKeyboard(ShelterService shelterService) {
        this.shelterService = shelterService;

    }

    public InlineKeyboardMarkup shelterInLineKeyboard() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton[] buttons = {
                new InlineKeyboardButton(CallBackData.SHELTER_INFORMATION.name()).callbackData(CallBackData.SHELTER_INFORMATION.toString()),
                new InlineKeyboardButton(CallBackData.HOW_TAKE_PET.name()).callbackData(CallBackData.HOW_TAKE_PET.toString()),
                new InlineKeyboardButton(CallBackData.REPORT.name()).callbackData(CallBackData.REPORT.toString()),
                new InlineKeyboardButton(CallBackData.RECOMMENDATIONS.name()).callbackData(CallBackData.RECOMMENDATIONS.toString()),
                new InlineKeyboardButton(CallBackData.VOLUNTEER.name()).callbackData(CallBackData.VOLUNTEER.toString()),
                new InlineKeyboardButton(CallBackData.SETTINGS.name()).callbackData(CallBackData.SETTINGS.toString())

        };
        inlineKeyboardMarkup.addRow(buttons[0], buttons[1]);
        inlineKeyboardMarkup.addRow(buttons[2], buttons[3]);
        inlineKeyboardMarkup.addRow(buttons[4]);
        inlineKeyboardMarkup.addRow(buttons[5]);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup recommendationsInLineKeyboard() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton[] buttons = {
                new InlineKeyboardButton(CallBackData.RECOMMENDATIONS_SHELTER.name()).callbackData(CallBackData.RECOMMENDATIONS_SHELTER.toString()),
                new InlineKeyboardButton(CallBackData.RECOMMENDATIONS_DOG.name()).callbackData(CallBackData.RECOMMENDATIONS_DOG.toString()),
                new InlineKeyboardButton(CallBackData.RECOMMENDATIONS_CAT.name()).callbackData(CallBackData.RECOMMENDATIONS_CAT.toString()),

        };
        inlineKeyboardMarkup.addRow(buttons[0]);
        inlineKeyboardMarkup.addRow(buttons[1]);
        inlineKeyboardMarkup.addRow(buttons[2]);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup allSheltersInLineKeyboard() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();
        shelterService.findAll().forEach(shelter ->
                inlineKeyboardMarkup = inlineKeyboardBuilder(inlineKeyboardMarkup,shelter)
        );
        return inlineKeyboardMarkup;
    }

    private InlineKeyboardMarkup inlineKeyboardBuilder(InlineKeyboardMarkup inlineKeyboardMarkup, Shelter shelter) {
        return inlineKeyboardMarkup.addRow(new InlineKeyboardButton(shelter.getName()).callbackData(shelter.getName()));
    }

    public InlineKeyboardMarkup choseKindReport()
    {
        inlineKeyboardMarkup=new InlineKeyboardMarkup();
        InlineKeyboardButton[] buttons = {
                new InlineKeyboardButton(CallBackData.REPORT_TELEGRAM.name()).callbackData(CallBackData.REPORT_TELEGRAM.toString()),
                new InlineKeyboardButton(CallBackData.REPORT_YANDEX_FORM.name()).callbackData(CallBackData.REPORT_YANDEX_FORM.toString()).url("https://forms.yandex.ru/u/650ab773068ff033bb7a0a2f/"),


        };
        inlineKeyboardMarkup.addRow(buttons[0]);
        inlineKeyboardMarkup.addRow(buttons[1]);
        return inlineKeyboardMarkup;

    }
}
