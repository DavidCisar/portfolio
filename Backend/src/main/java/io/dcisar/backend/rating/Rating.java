package io.dcisar.backend.rating;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Rating {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private String name;
    private String message;
    private String link;
    private boolean isAccepted;

}
