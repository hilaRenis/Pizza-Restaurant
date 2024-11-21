package pizza.restaurant.domain.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@Slf4j
public abstract class Model<T> {

    /**
     * Entity Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private T id;

    /**
     * Model create at.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "createdAt", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @PrePersist
    @PreUpdate
    private void beforeSave() {
        Date date = new Date();
        this.setCreatedAt(date);
    }
}
