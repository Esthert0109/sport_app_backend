package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class PopularSearch {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String popularKeywords;
    private Date createDate;
}
