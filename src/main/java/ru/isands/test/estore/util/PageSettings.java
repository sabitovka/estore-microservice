package ru.isands.test.estore.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Slf4j
@Data
public class PageSettings {
    private int page = 0;
    private int elementPerPage = 10;
    private String direction = "dsc";
    private String key;

    public Sort buildSort() {
        if (key == null) {
            return Sort.unsorted();
        }
        switch (direction) {
            case "dsc":
                return Sort.by(key).descending();
            case "asc":
                return Sort.by(key).ascending();
            default:
                log.warn("Неверное направление сортировки передано в PageSettings, будет использовано направление по-умолчанию");
                return Sort.by(key).descending();
        }
    }

    public PageRequest createPageRequest() {
        Sort sort = buildSort();
        return PageRequest.of(getPage(), getElementPerPage(), sort);
    }
}
