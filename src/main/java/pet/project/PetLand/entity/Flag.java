package pet.project.PetLand.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Flag {
    Report("флаг готовности принять пользовательский ввод отчета"),
    Customer("флаг готовности принять пользовательский ввод анкеты"),
    None("флаг отсутсвия пользователького ввода");
    private final String description;
}
