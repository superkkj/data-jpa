package study.datajpa.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item implements Persistable<Long> {

    @Id  //persist 할때 생성
    private Long id;

    @CreatedDate
    private LocalDateTime createDate;


    public Item(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return this.id;
    }


    @Override
    public boolean isNew() {
        return createDate == null;
    }
}
