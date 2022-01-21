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
@Table(name = "address")
@Data
public class Address {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "detail_info", unique = false, nullable = false, length = 64)
    private String detailInfo;


    @Column(name = "user_id", length = 64)
    private String userId;

}
