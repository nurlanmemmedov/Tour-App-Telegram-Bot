package com.example.telegrambotapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Dictionary;
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
    @OneToMany(mappedBy = "question",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<QuestionTranslation> questionTranslations;

    public String getQuestionText(String code) {
        QuestionTranslation translation =  questionTranslations.stream()
                .filter(t -> t.getCode().equals(code))
                .findFirst().orElse(null);
        if (translation == null) return this.questionText;
        return translation.getText();
    }
}

