package io.dcisar.backend.rating;

import io.dcisar.backend.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

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
    private int rating;
    private String message;

    @OneToOne
    private User user;

}
