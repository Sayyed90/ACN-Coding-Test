package com.accenture.codingtest.springbootcodingtest.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "task")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @NotNull
    private long id;

    @Column(name = "TITLE")
    @NotNull
    private String title;

    @Column(name = "DESCRIPTION")
    @NotNull
    private String description;

    @Column(name = "STATUS",columnDefinition = "varchar(50) default 'NOT_STARTED'")
    @NotNull
    private String status;

    @Column(name = "project_id",nullable = true)
    //@Nullable
    private long project;

    @Column(name = "user_id",nullable = true)
    //@Nullable
    private long user;
}