package fr.insalyon.creatis.vip.api.model.stats;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.insalyon.creatis.vip.api.CarminProperties;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonInclude(Include.NON_NULL)
public class StatUser {
    private String city;
    private String country;
    private LocalDateTime lastSeen;
    private LocalDateTime memberSince;
    private String position;
    private String researchInstitute;
    private String researchInstituteUrl;

    public StatUser() {}

    public StatUser(String city, String country, LocalDateTime lastSeen,
                    LocalDateTime memberSince, String position,
                    String researchInstitute, String researchInstituteUrl) {
        this.city = city;
        this.country = country;
        this.lastSeen = lastSeen;
        this.memberSince = memberSince;
        this.position = position;
        this.researchInstitute = researchInstitute;
        this.researchInstituteUrl = researchInstituteUrl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("last_seen")
    @JsonFormat(pattern= CarminProperties.STATS_DATE_FORMAT)
    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }

    @JsonProperty("member_since")
    @JsonFormat(pattern= CarminProperties.STATS_DATE_FORMAT)
    public LocalDateTime getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(LocalDateTime memberSince) {
        this.memberSince = memberSince;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @JsonProperty("research_institute")
    public String getResearchInstitute() {
        return researchInstitute;
    }

    public void setResearchInstitute(String researchInstitute) {
        this.researchInstitute = researchInstitute;
    }

    @JsonProperty("research_institute_url")
    public String getResearchInstituteUrl() {
        return researchInstituteUrl;
    }

    public void setResearchInstituteUrl(String researchInstituteUrl) {
        this.researchInstituteUrl = researchInstituteUrl;
    }
}
