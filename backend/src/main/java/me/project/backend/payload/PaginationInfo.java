package me.project.backend.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginationInfo {
    private int page;
    private int size;
    private int totalPages;
    private long totalItems;
}
