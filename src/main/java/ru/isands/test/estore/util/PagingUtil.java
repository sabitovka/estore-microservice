package ru.isands.test.estore.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.isands.test.estore.exception.IllegalParamException;

import java.lang.reflect.Field;
import java.util.List;

@UtilityClass
public class PagingUtil {
    private static boolean isSortableField(Class<?> clazz, String sortField) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equalsIgnoreCase(sortField)) {
                return true;
            }
        }
        return false;
    }

    public static PageRequest createPageRequest(int page, int size, String sortField, String sortType, Class<?> clazz) {
        Sort sort = Sort.unsorted();
        if (sortField != null && !sortField.isEmpty()) {
            if (!isSortableField(clazz, sortField)) {
                throw new IllegalParamException("Не удалось выполнить фильтр", List.of(String.format("Поле %s не найдено", sortField)));
            }
            if (List.of("asc", "desc").contains(sortType)) {
                sort = Sort.by(Sort.Direction.fromString(sortType), sortField);
            }
        }
        return PageRequest.of(page, size, sort);
    }
}
