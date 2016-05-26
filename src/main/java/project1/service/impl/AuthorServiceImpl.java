package project1.service.impl;

import project1.service.AuthorService;
import project1.domain.Author;
import project1.repository.AuthorRepository;
import project1.web.rest.dto.AuthorDTO;
import project1.web.rest.mapper.AuthorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Author.
 */
@Service
@Transactional
public class AuthorServiceImpl implements AuthorService{

    private final Logger log = LoggerFactory.getLogger(AuthorServiceImpl.class);
    
    @Inject
    private AuthorRepository authorRepository;
    
    @Inject
    private AuthorMapper authorMapper;
    
    /**
     * Save a author.
     * 
     * @param authorDTO the entity to save
     * @return the persisted entity
     */
    public AuthorDTO save(AuthorDTO authorDTO) {
        log.debug("Request to save Author : {}", authorDTO);
        Author author = authorMapper.authorDTOToAuthor(authorDTO);
        author = authorRepository.save(author);
        AuthorDTO result = authorMapper.authorToAuthorDTO(author);
        return result;
    }

    /**
     *  Get all the authors.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<AuthorDTO> findAll() {
        log.debug("Request to get all Authors");
        List<AuthorDTO> result = authorRepository.findAll().stream()
            .map(authorMapper::authorToAuthorDTO)
            .collect(Collectors.toCollection(LinkedList::new));
        return result;
    }

    /**
     *  Get one author by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public AuthorDTO findOne(Long id) {
        log.debug("Request to get Author : {}", id);
        Author author = authorRepository.findOne(id);
        AuthorDTO authorDTO = authorMapper.authorToAuthorDTO(author);
        return authorDTO;
    }

    /**
     *  Delete the  author by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Author : {}", id);
        authorRepository.delete(id);
    }
}
