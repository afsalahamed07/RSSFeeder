package org.araa.application.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EntriesDTO {
    List<EntryDto> entries;
    int page;
    int size;
    int totalPages;
    int totalElements;
    int nextPage;
    int previousPage;
}
