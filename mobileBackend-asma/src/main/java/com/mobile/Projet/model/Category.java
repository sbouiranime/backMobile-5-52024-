package com.mobile.Projet.model;


import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="category")
public class Category {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="Category_id")
    private long categoryId;

    @NotNull
    @Column(name="niveau_education")
    private String niveauEdu;

    @NotNull
    @Column(name="annee")
    private String annee;

    @Column(name="category_option")
    private String categoryOption;

    @NotNull
    @Column(name="matiere")
    private String matiere;

    @NotNull
    @Column(name="type_doc")
    private String typeDocument;



}
