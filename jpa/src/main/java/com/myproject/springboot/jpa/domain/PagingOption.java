package com.myproject.springboot.jpa.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagingOption {

    private int pageSize;
    private int pageNumber;
}
