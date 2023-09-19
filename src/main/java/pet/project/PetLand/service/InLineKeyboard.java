package pet.project.PetLand.service;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import pet.project.PetLand.entity.CallBackData;

public class InLineKeyboard {
    InlineKeyboardMarkup inlineKeyboardMarkup;

    public EditMessageReplyMarkup shelterInLineKeyboard(User user, String inLineMessageId) {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton[] buttons = {
                new InlineKeyboardButton("Информация").callbackData(CallBackData.SHELTER_INFORMATION.toString()),
                new InlineKeyboardButton("Как взять питомца").callbackData(CallBackData.HOW_TAKE_PET.toString()),
                new InlineKeyboardButton("Прислать отчет").callbackData(CallBackData.REPORT.toString()),
                new InlineKeyboardButton("Позвать волонтера").callbackData(CallBackData.VOLUNTEER.toString()),
        };
        inlineKeyboardMarkup.addRow(buttons[0], buttons[1]);
        inlineKeyboardMarkup.addRow(buttons[2], buttons[3]);
        return new EditMessageReplyMarkup(inLineMessageId).replyMarkup(inlineKeyboardMarkup);
    }
}
