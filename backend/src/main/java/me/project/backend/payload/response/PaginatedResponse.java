package me.project.backend.payload.response;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginatedResponse<T> {

    private List<T> data;

    private int page;

    private int size;

    private int totalPages;

    private long totalItems;

    private boolean hasNext;

    public PaginatedResponse(Page<T> page) {
        this.data = page.getContent();
        this.page = page.getNumberOfElements();
        this.size = page.getSize();
        this.totalPages = page.getTotalPages();
        this.totalItems = page.getTotalElements();
        this.hasNext = page.hasNext();
    }
}
