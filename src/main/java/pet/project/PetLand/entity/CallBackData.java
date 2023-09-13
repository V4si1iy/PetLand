package pet.project.PetLand.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CallBackData // перечисление кнопок в боте
{
    ; // при появлении первой кнопки убрать комментарий и точку с запятой (сейчас это заглушка)

    private final String name;

    private final String description;

    /**
     * парсер для поиска команда в enum
     *
     * @param data данные кнопки нажатой в боте
     * @return callBackData - возвращает найденную кнопку, если ее нет - null
     *
     */
    public static CallBackData parse(String data) {
        for (CallBackData callBackData : CallBackData.values()) {
            if (callBackData.toString() == data) {
                return callBackData;
            }
            break;
        }
        return null;
    }
}
