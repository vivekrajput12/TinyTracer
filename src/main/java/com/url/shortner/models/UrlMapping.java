package com.url.shortner.models;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "urlMapping")
@Data
public class UrlMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long urlId;
    private String originalUrl;
    private String shortUrl;
    private LocalDateTime createdDate;
    private int clickCount = 0;

    @ManyToOne
    @JoinColumn(name = "uid",referencedColumnName = "uid")
    private User user;

    @OneToMany(mappedBy = "urlMapping")
    @ToString.Exclude
    private List<ClickEvent> clickEvents;
}
