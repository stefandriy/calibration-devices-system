package com.softserve.edu.dto.calibrator;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SymbolsAndSizesDTO {

    private List<String> sizes;

    private List<String> symbols;

    public SymbolsAndSizesDTO() {
    }
}
