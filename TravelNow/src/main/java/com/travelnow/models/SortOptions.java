package com.travelnow.models;

public enum SortOptions {

    PRICE_ASC("price_asc"),
    PRICE_DESC("price_desc"),
    GRADE_ASC("grade_asc"),
    GRADE_DESC("grade_dec"),
    POPULAR_ASC("popular_asc"),
    POPULAR_DESC("popular_desc");

    public final String option;

    SortOptions(String option) {
        this.option = option;
    }
      
}
