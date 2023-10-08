package pet.project.PetLand.handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
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
    private CustomerService customerService;

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
        } else if (!update.message().text().isEmpty()) {
            if (callBackQueryHandler.flagReport()) {
                reportService.createReport(update.callbackQuery().message());
                callBackQueryHandler.updateFlagReport();
                callBackQueryHandler.sendStartMenu(update.callbackQuery());
            } else if (customerService.flagCustomer()) {
                customerService.createCustomerStart(update.message().chat(), update.message());
                customerService.updateFlagCustomer();
                callBackQueryHandler.sendStartMenu(update.callbackQuery());
            }
        }
    }
    @Async
    public void handleUserInputForForm(User user, Message message) {
        if (customerService.isFillingOutForm()) {
            customerService.startFillingOutForm();
        } else if (customerService.flagCustomer()) {
            customerService.stopFillingOutForm();
        }
    }

    public void FormHandlerService(CustomerService customerService) {
        this.customerService = customerService;
    }
}
