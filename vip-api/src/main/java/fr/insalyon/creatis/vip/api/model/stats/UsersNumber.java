package fr.insalyon.creatis.vip.api.model.stats;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.insalyon.creatis.vip.core.server.CarminProperties;

import java.time.LocalDateTime;

public class UsersNumber {

    private LocalDateTime start;
    private LocalDateTime end;
    private Long total;

    public UsersNumber() {}

    public UsersNumber(LocalDateTime start, LocalDateTime end, Long total) {
        this.start = start;
        this.end = end;
        this.total = total;
    }

    @JsonFormat(pattern= CarminProperties.STATS_DATE_FORMAT)
    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    @JsonFormat(pattern= CarminProperties.STATS_DATE_FORMAT)
    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
