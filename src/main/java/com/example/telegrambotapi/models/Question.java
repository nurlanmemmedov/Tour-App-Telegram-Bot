package com.example.telegrambotapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "question_text")
    private String questionText;
    @OneToMany(mappedBy = "question",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Action> actions;
}
