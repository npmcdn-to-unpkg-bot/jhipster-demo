package project1.web.rest.mapper;

import project1.domain.*;
import project1.web.rest.dto.AuthorDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Author and its DTO AuthorDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface AuthorMapper {

    @Mapping(source = "authorToUser.id", target = "authorToUserId")
    AuthorDTO authorToAuthorDTO(Author author);

    List<AuthorDTO> authorsToAuthorDTOs(List<Author> authors);

    @Mapping(source = "authorToUserId", target = "authorToUser")
    Author authorDTOToAuthor(AuthorDTO authorDTO);

    List<Author> authorDTOsToAuthors(List<AuthorDTO> authorDTOs);
}
