package study.datajpa.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public class JpaBaseEntity {

    @Column(updatable = false)
    private LocalDateTime createDate;
    private LocalDateTime updateDate;


    @PrePersist //  persist 하기전에 발생
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createDate = now;
        updateDate = now; // 미리 넣어두면 편하다 null 보다
    }

    @PreUpdate
    public void preUpdate() {
        updateDate = LocalDateTime.now();
    }
}
