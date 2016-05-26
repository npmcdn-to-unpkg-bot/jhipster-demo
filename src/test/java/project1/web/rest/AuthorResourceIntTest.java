package project1.web.rest;

import project1.Project1App;
import project1.domain.Author;
import project1.repository.AuthorRepository;
import project1.service.AuthorService;
import project1.web.rest.dto.AuthorDTO;
import project1.web.rest.mapper.AuthorMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the AuthorResource REST controller.
 *
 * @see AuthorResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Project1App.class)
@WebAppConfiguration
@IntegrationTest
public class AuthorResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";
    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;
    private static final String DEFAULT_BOOK = "AAAAA";
    private static final String UPDATED_BOOK = "BBBBB";
    private static final String DEFAULT_LOGIN = "AAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBB";
    private static final String DEFAULT_PASSWORD = "AAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBB";

    @Inject
    private AuthorRepository authorRepository;

    @Inject
    private AuthorMapper authorMapper;

    @Inject
    private AuthorService authorService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAuthorMockMvc;

    private Author author;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AuthorResource authorResource = new AuthorResource();
        ReflectionTestUtils.setField(authorResource, "authorService", authorService);
        ReflectionTestUtils.setField(authorResource, "authorMapper", authorMapper);
        this.restAuthorMockMvc = MockMvcBuilders.standaloneSetup(authorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        author = new Author();
        author.setFirstName(DEFAULT_FIRST_NAME);
        author.setLastName(DEFAULT_LAST_NAME);
        author.setAge(DEFAULT_AGE);
        author.setBook(DEFAULT_BOOK);
        author.setLogin(DEFAULT_LOGIN);
        author.setPassword(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    public void createAuthor() throws Exception {
        int databaseSizeBeforeCreate = authorRepository.findAll().size();

        // Create the Author
        AuthorDTO authorDTO = authorMapper.authorToAuthorDTO(author);

        restAuthorMockMvc.perform(post("/api/authors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(authorDTO)))
                .andExpect(status().isCreated());

        // Validate the Author in the database
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).hasSize(databaseSizeBeforeCreate + 1);
        Author testAuthor = authors.get(authors.size() - 1);
        assertThat(testAuthor.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testAuthor.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testAuthor.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testAuthor.getBook()).isEqualTo(DEFAULT_BOOK);
        assertThat(testAuthor.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testAuthor.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    public void checkLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorRepository.findAll().size();
        // set the field null
        author.setLogin(null);

        // Create the Author, which fails.
        AuthorDTO authorDTO = authorMapper.authorToAuthorDTO(author);

        restAuthorMockMvc.perform(post("/api/authors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(authorDTO)))
                .andExpect(status().isBadRequest());

        List<Author> authors = authorRepository.findAll();
        assertThat(authors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorRepository.findAll().size();
        // set the field null
        author.setPassword(null);

        // Create the Author, which fails.
        AuthorDTO authorDTO = authorMapper.authorToAuthorDTO(author);

        restAuthorMockMvc.perform(post("/api/authors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(authorDTO)))
                .andExpect(status().isBadRequest());

        List<Author> authors = authorRepository.findAll();
        assertThat(authors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAuthors() throws Exception {
        // Initialize the database
        authorRepository.saveAndFlush(author);

        // Get all the authors
        restAuthorMockMvc.perform(get("/api/authors?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(author.getId().intValue())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
                .andExpect(jsonPath("$.[*].book").value(hasItem(DEFAULT_BOOK.toString())))
                .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN.toString())))
                .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())));
    }

    @Test
    @Transactional
    public void getAuthor() throws Exception {
        // Initialize the database
        authorRepository.saveAndFlush(author);

        // Get the author
        restAuthorMockMvc.perform(get("/api/authors/{id}", author.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(author.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.book").value(DEFAULT_BOOK.toString()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAuthor() throws Exception {
        // Get the author
        restAuthorMockMvc.perform(get("/api/authors/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuthor() throws Exception {
        // Initialize the database
        authorRepository.saveAndFlush(author);
        int databaseSizeBeforeUpdate = authorRepository.findAll().size();

        // Update the author
        Author updatedAuthor = new Author();
        updatedAuthor.setId(author.getId());
        updatedAuthor.setFirstName(UPDATED_FIRST_NAME);
        updatedAuthor.setLastName(UPDATED_LAST_NAME);
        updatedAuthor.setAge(UPDATED_AGE);
        updatedAuthor.setBook(UPDATED_BOOK);
        updatedAuthor.setLogin(UPDATED_LOGIN);
        updatedAuthor.setPassword(UPDATED_PASSWORD);
        AuthorDTO authorDTO = authorMapper.authorToAuthorDTO(updatedAuthor);

        restAuthorMockMvc.perform(put("/api/authors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(authorDTO)))
                .andExpect(status().isOk());

        // Validate the Author in the database
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).hasSize(databaseSizeBeforeUpdate);
        Author testAuthor = authors.get(authors.size() - 1);
        assertThat(testAuthor.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAuthor.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testAuthor.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testAuthor.getBook()).isEqualTo(UPDATED_BOOK);
        assertThat(testAuthor.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testAuthor.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void deleteAuthor() throws Exception {
        // Initialize the database
        authorRepository.saveAndFlush(author);
        int databaseSizeBeforeDelete = authorRepository.findAll().size();

        // Get the author
        restAuthorMockMvc.perform(delete("/api/authors/{id}", author.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).hasSize(databaseSizeBeforeDelete - 1);
    }
}
