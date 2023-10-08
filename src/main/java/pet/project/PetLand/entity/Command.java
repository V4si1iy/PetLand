package pet.project.PetLand.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Command // Перечисление команд в боте
{
    START("start", "запускает бота"),
    CANCEL("cancel" , "отменяет действие и переносит на начальное меню");

    private final String name;

    private final String description;
}
