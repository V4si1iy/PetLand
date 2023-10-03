package pet.project.PetLand.handler;

import com.pengrad.telegrambot.model.Update;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pet.project.PetLand.service.CustomerService;
import pet.project.PetLand.service.ReportService;

import java.util.Objects;

@AllArgsConstructor
@Service
public class UpdateHandler {
    private final CallBackQueryHandler callBackQueryHandler;
    private final CommandHandler commandHandler;
    private final ReportService reportService;
    private final CustomerService customerService;

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
            commandHandler.handler(update.message().from(), update.message());
        } else if (!update.message().text().isEmpty())// проверка на ввод пользователя
        {
            if (callBackQueryHandler.flagReport()) // если стоит флаг то получаем ручной ввод Отчета
            {
                if (reportService.createReport(update.message())) {
                    callBackQueryHandler.updateFlagReport();
                    callBackQueryHandler.startMenu(update.message().chat().id());
                }

            } else if (customerService.flagCustomer()) // если стоит флаг то получаем ручной ввод Анкеты
            {
                customerService.createCustomerStart(update.message().chat(), update.message());
                customerService.updateFlagCustomer();
                callBackQueryHandler.startMenu(update.message().chat().id());
            }
        }
    }
}
