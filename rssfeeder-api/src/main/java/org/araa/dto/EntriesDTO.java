package org.araa.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EntriesDTO {
    List<EntryDTO> entries;
    int page;
    int size;
    int totalPages;
    int totalElements;
    int nextPage;
    int previousPage;
}
