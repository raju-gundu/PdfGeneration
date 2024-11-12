package com.pdf.pdfgeneration.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {
    private String name;
    private String quantity;
    private double rate;
    private double amount;
}
