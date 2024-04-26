package org.araa.application.dto;

import lombok.Data;

@Data
public class RSSDto {
    private String title;
    private String description;
    private String feedType;
    private String url;
}
