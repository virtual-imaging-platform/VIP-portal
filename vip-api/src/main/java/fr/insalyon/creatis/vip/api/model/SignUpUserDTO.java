package fr.insalyon.creatis.vip.api.model;

import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * @author KhalilKes
 *
 * DTO to transfer user data
 */
public class SignUpUserDTO {

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    private String institution;
    @NotNull
    private String password;
    @NotNull
    private CountryCode countryCode;
    private String comments;
    @NotNull
    private List<String> applications;

    public SignUpUserDTO(){

    }
    public SignUpUserDTO(String firstName, String lastName, String email, String institution, String password, CountryCode countryCode, String comments, List<String> applications) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.institution = institution;
        this.password = password;
        this.countryCode = countryCode;
        this.comments = comments;
        this.applications = applications;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(CountryCode countryCode) {
        this.countryCode = countryCode;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<String> getApplications() { return applications; }

}
