package com.url.shortner.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Entity
@Table(name = "clickEvent")
@Data
public class ClickEvent {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long clickEventId;
        private LocalDateTime clickDate;
        @ManyToOne
        @JoinColumn(name="urlId")
        private UrlMapping urlMapping;
}
