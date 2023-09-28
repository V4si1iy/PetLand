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
                new InlineKeyboardButton("Информация").callbackData(CallBackData.SHELTER_INFORMATION.toString()),
                new InlineKeyboardButton("Как взять питомца").callbackData(CallBackData.HOW_TAKE_PET.toString()),
                new InlineKeyboardButton("Прислать отчет").callbackData(CallBackData.REPORT.toString()),
                new InlineKeyboardButton("Рекомендации").callbackData(CallBackData.RECOMMENDATIONS.toString()),
                new InlineKeyboardButton("Позвать волонтера").callbackData(CallBackData.VOLUNTEER.toString())

        };
        inlineKeyboardMarkup.addRow(buttons[0], buttons[1]);
        inlineKeyboardMarkup.addRow(buttons[2], buttons[3]);
        inlineKeyboardMarkup.addRow(buttons[4]);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup recommendationsInLineKeyboard() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton[] buttons = {
                new InlineKeyboardButton("Техника безопасности").callbackData(CallBackData.RECOMMENDATIONS_SHELTER.toString()),
                new InlineKeyboardButton("Рекомендации по собаке").callbackData(CallBackData.RECOMMENDATIONS_DOG.toString()),
                new InlineKeyboardButton("Рекомендации по кошке").callbackData(CallBackData.RECOMMENDATIONS_CAT.toString()),

        };
        inlineKeyboardMarkup.addRow(buttons[0]);
        inlineKeyboardMarkup.addRow(buttons[1]);
        inlineKeyboardMarkup.addRow(buttons[2]);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup allSheltersInLineKeyboard() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();
        shelterService.findAll().forEach(shelter ->
                {
                   inlineKeyboardMarkup = inlineKeyboardBuilder(inlineKeyboardMarkup,shelter);
                }
        );
        return inlineKeyboardMarkup;
    }

    private InlineKeyboardMarkup inlineKeyboardBuilder(InlineKeyboardMarkup inlineKeyboardMarkup, Shelter shelter) {
        return inlineKeyboardMarkup.addRow(new InlineKeyboardButton(shelter.getName()).callbackData(shelter.getName()));
    }
}
