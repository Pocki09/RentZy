package com.rentzy.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class FilterParams {
    /**
     * Số trang (bắt đầu từ 0). Nếu null hoặc âm, PageableUtils sẽ tự động chuyển thành 0.
     */
    @Min(value = 0, message = "Page không được nhỏ hơn 0")
    private Integer page;

    /**
     * Số phần tử trên mỗi trang. Nếu null, PageableUtils sẽ dùng defaultSize;
     * phải nằm trong khoảng [1, 100].
     */
    @Min(value = 1, message = "Size phải lớn hơn hoặc bằng 1")
    @Max(value = 100, message = "Size không được lớn hơn 100")
    private Integer size;

    /**
     * Tên field để sort. Nếu null hoặc blank, PageableUtils sẽ dùng DEFAULT_SORT_BY.
     */
    private String sortBy;

    /**
     * Chiều sort: "asc" hoặc "desc". Nếu null hoặc blank, PageableUtils sẽ dùng DEFAULT_SORT_DIRECTION.
     */
    private String sortDirection;
}
