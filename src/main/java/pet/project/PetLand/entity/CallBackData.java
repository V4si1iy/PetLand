package pet.project.PetLand.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CallBackData // перечисление кнопок в боте
{
    SHELTER_INFORMATION("Информация", "дает полную информацию по выбранному приюту"),
    HOW_TAKE_PET("Как взять питомца", "дает информацию как взять питомца"),
    REPORT("Прислать отчет", "можно отправить отчет с помощью этой кнопки"),
    VOLUNTEER("Позвать волонтера", "вызывает функцию общения с волонтером"),
    RECOMMENDATIONS("Рекомендации", "дает рекомендации по приюту и животным"),
    RECOMMENDATIONS_SHELTER("Техника безопасности","дает рекомендацию по техники безопасности в приюте"),
    RECOMMENDATIONS_DOG("Рекомендации по собаке","дает рекомендацию по ухаживанию собаки"),
    RECOMMENDATIONS_CAT("Рекомендации по кошке","дает рекомендацию по ухаживанию кошки"),
    SETTINGS("Настройки","вызывает функцию настройки(добавление новых данных по пользователю)"),
    REPORT_TELEGRAM("Отправить отчет в телеграме","вызывает функцию получения данных через бота"),
    REPORT_YANDEX_FORM("Отправить отчет через Yandex Form","вызывает функцию получения данных через YandexForms");


    private final String name;

    private final String description;

    /**
     * парсер для поиска команда в enum
     *
     * @param data данные кнопки нажатой в боте
     * @return callBackData - возвращает найденную кнопку, если ее нет - null
     */
    public static CallBackData parse(String data) {
        for (CallBackData callBackData : CallBackData.values()) {
            if (callBackData.toString().equals(data)) {
                return callBackData;
            }

        }
        return null;
    }
}
