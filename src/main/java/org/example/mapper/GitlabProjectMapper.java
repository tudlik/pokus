package org.example.mapper;

import org.example.dto.GitlabProjectApiDTO;
import org.example.dto.GitlabProjectDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GitlabProjectMapper {

    GitlabProjectDTO toGitlabProjectDTO(GitlabProjectApiDTO gitlabProjectApiDTO);


}
