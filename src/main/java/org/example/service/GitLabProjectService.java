package org.example.service;

import java.util.List;
import org.example.dto.GitlabProjectDTO;
import org.example.enums.Ordering;
import org.example.enums.Restrict;
import org.example.enums.Sorting;

public interface GitLabProjectService {

    List<GitlabProjectDTO> getDataFromGitlab(Restrict restrict, int limit, Sorting sorting, Ordering ordering);
}
