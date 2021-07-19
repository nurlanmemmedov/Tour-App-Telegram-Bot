package com.example.telegrambotapi.models.entities;

import com.example.telegrambotapi.enums.ActionType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "actions")
public class Action implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name="question_id", nullable=false)
    private Question question;
    private ActionType type;
    private String answer;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "next_id", referencedColumnName = "id")
    private Question nextQuestion;

    @JsonBackReference
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "action",
            cascade = CascadeType.ALL)
    private List<Translation> translations;
}
