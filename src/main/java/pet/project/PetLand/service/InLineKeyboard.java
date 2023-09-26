package pet.project.PetLand.service;


import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import org.springframework.stereotype.Service;
import pet.project.PetLand.entity.CallBackData;

@Service
public class InLineKeyboard {
    InlineKeyboardMarkup inlineKeyboardMarkup;

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
    public InlineKeyboardMarkup recommendationsInLineKeyboard()
    {
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
}
