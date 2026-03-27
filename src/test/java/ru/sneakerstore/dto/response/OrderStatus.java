package ru.sneakerstore.dto.response;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("Ожидает оплаты"),
    PAID("Оплачен"),
    CANCELLED("Отменён"),
    DELIVERED("Доставлен");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

}
