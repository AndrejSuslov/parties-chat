package com.ddc.chat.repository.parser;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Data
@Component
public class ListParser {

    public List<Long> parseString(String str) {
        return Arrays.stream(str.split(","))
                .map(Long::parseLong)
                .toList();
    }

}
