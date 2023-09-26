package pet.project.PetLand.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;

// Таблица: Пользователь (Customer) в БД
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // уникальный id
    private long chatId; // id Telegram чата
    private long petId; // id взятого питомца
    private String surname; // фамилия
    private String name; // имя
    private String secondName; // отчество
    private String phone; // тлф формата +70000000000
    private String address; // адрес

    public Customer() {
    }

    public long getId() {
        return id;
    }

    public long getChatId() {
        return chatId;
    }
    public long getPetId() {
        return petId;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public void setPetId(long petId) {
        this.petId = petId;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Customer(long chatId, String surname, String name) {
        this.chatId = chatId;
        this.surname = surname;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return  id == customer.id &&
                chatId == customer.chatId &&
                petId == customer.petId &&
                Objects.equals(surname, customer.surname) &&
                        Objects.equals(name, customer.name)
                        && Objects.equals(secondName, customer.secondName)
                        && Objects.equals(phone, customer.phone)
                        && Objects.equals(address, customer.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, petId, surname, name, secondName, phone, address);
    }


}
