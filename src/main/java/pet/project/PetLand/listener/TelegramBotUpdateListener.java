package pet.project.PetLand.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pet.project.PetLand.handler.UpdateHandler;
import pet.project.PetLand.service.InLineKeyboard;
import pet.project.PetLand.service.TelegramSenderService;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TelegramBotUpdateListener implements UpdatesListener {
    private static final Logger logger = LoggerFactory.getLogger(TelegramBotUpdateListener.class);


    private final TelegramBot telegramBot;
    private final TelegramSenderService telegramSenderService;
    private final UpdateHandler updateHandler;
    InLineKeyboard inLineKeyboard;


    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {

        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            updateHandler.handler(update);

        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}







