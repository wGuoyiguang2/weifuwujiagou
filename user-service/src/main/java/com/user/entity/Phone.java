package com.user.entity;

/**
 * @author guoyiguang
 * @description $
 * @date 2022/1/21$
 */

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "phone")
@Data
public class Phone {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "num", unique = false, nullable = false, length = 64)
    private String num;


    @Column(name = "user_id", length = 64)
    private String userId;

}
