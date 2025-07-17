package com.rentzy.utils;

import com.rentzy.model.dto.FilterParams;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

public class PageableUtils {
    private static final Sort.Direction DEFAULT_DIRECTION = Sort.Direction.DESC;
    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 100;

    public static Pageable buildPageable(FilterParams params, int defaultSize) {

        int pageSize = Math.max(Math.min(defaultSize, MAX_PAGE_SIZE), MIN_PAGE_SIZE);
        if (params.getSize() != null) {
            pageSize = Math.max(Math.min(params.getSize(), MAX_PAGE_SIZE), MIN_PAGE_SIZE);
        }

        int pageNumber = (params.getPage() != null)
                ? Math.max(params.getPage(), 0)
                : 0;

        String sortByRaw = params.getSortBy();
        Sort sort;
        if (!StringUtils.hasText(sortByRaw) || "none".equalsIgnoreCase(sortByRaw)) {
            sort = Sort.unsorted();
        } else {
            Sort.Direction direction = DEFAULT_DIRECTION;
            if (StringUtils.hasText(params.getSortDirection())) {
                direction = params.getSortDirection().equalsIgnoreCase("asc")
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;
            }
            sort = Sort.by(direction, sortByRaw);
        }

        return PageRequest.of(pageNumber, pageSize, sort);
    }
}
