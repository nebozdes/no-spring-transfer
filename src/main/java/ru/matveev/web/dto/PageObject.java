package ru.matveev.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PageObject<T> {

    private final long found;
    private final List<T> results;
}
