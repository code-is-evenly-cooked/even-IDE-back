package com.evenly.evenide.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MyEditorResponse {
    private List<ProjectResponse> myProjects;
}
