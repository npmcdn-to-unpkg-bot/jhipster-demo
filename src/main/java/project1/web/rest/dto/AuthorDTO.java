package project1.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Author entity.
 */
public class AuthorDTO implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    private Integer age;

    private String book;

    @NotNull
    @Size(min = 8, max = 25)
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String login;

    @NotNull
    @Size(min = 8, max = 25)
    private String password;

    private Long userId;


    private Long authorToUserId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAuthorToUserId() {
        return authorToUserId;
    }

    public void setAuthorToUserId(Long userId) {
        this.authorToUserId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AuthorDTO authorDTO = (AuthorDTO) o;

        if ( ! Objects.equals(id, authorDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AuthorDTO{" +
            "id=" + id +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            ", age='" + age + "'" +
            ", book='" + book + "'" +
            ", login='" + login + "'" +
            ", password='" + password + "'" +
            ", userId='" + userId + "'" +
            '}';
    }
}
