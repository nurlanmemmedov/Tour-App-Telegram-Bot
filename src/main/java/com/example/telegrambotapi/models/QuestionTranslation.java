package com.example.telegrambotapi.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name="question_id", nullable=false)
    private Question question;
    private String code;
    private String text;
}
