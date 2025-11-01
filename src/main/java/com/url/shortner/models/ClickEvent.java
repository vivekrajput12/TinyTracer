package com.url.shortner.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
@Entity
@Table(name = "clickEvent")
@Data
public class ClickEvent {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long clickEventId;
        private LocalDateTime clickDate;

        @Column(name = "urlId" , insertable = false , updatable = false)
        private Long urlId;

        @PostPersist
        private void fillUrlId(){
            if(urlMapping != null){
                this.urlId = urlMapping.getUrlId();
            }
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name="urlId" , insertable = true , updatable = true)
        @ToString.Exclude
        private UrlMapping urlMapping;
}
