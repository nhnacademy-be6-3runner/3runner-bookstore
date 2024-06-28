package com.nhnacademy.bookstore.entity.purchaseBook;

import com.nhnacademy.bookstore.entity.book.Book;
import com.nhnacademy.bookstore.entity.purchase.Purchase;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PurchaseBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @Setter
    private Book book;


    @NotNull
    @Min(0)
    @Setter
    private int quantity;

    @NotNull
    @Min(0)
    @Setter
    private int price;

    @ManyToOne
    @Setter
    private Purchase purchase;

    public PurchaseBook(Book book, int quantity, int price, Purchase purchase) {
        this.book = book;
        this.quantity = quantity;
        this.price = price;
        this.purchase = purchase;
    }

}
