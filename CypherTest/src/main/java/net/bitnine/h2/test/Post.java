package net.bitnine.h2.test;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Post {
    @Id
    @GeneratedValue
    int id;
    String subject;
    
    @Column(length = 100000000)
    String content;
    
    Date regDate;
    //... Getter, Setter (lombok은 다음번에 나오니 걱정말자 ㅋ)
}
