package io.dcisar.backend.reference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.util.Date;

import static javax.persistence.GenerationType.AUTO;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Reference {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private String name;
    private String message;
    private String link;
    private Date createdAt;
    private boolean isAccepted;

}
