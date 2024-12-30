package me.project.backend.payload.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.project.backend.payload.PaginationInfo;
import me.project.backend.payload.dto.PostDTO;

@Data
@AllArgsConstructor
public class FindPostsResponse {
    List<PostDTO> data;

    PaginationInfo pagination;
}
