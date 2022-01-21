package com.user.entity;

/**
 * @author guoyiguang
 * @description $
 * @date 2022/1/21$
 */

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
@Data
public class User {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "username", unique = true, nullable = false, length = 64)
    private String username;

    @Column(name = "password", nullable = false, length = 64)
    private String password;

    @Column(name = "email", length = 64)
    private String email;



    // fetch=FetchType.EAGER 不懒加载，防止 session 过期
    @OneToMany(targetEntity =Address.class,fetch=FetchType.EAGER )
    // name 是目标表的字段 ，referencedColumnName 是本表的字段 关联生成sql片段如下
    // from
    //        `user` user0_
    //    left  join
    //        `address` addresslis1_
    //            on user0_.`id`=addresslis1_.`user_id`
    // 用本表的 id去关联  address 表的  user_id
     @JoinColumn(referencedColumnName = "id" ,name = "user_id")
     private List<Address> addressList;



    // fetch=FetchType.EAGER 不懒加载，防止 session 过期
    @OneToOne(targetEntity =Phone.class,fetch=FetchType.EAGER )
    // name 是本表的字段 ，referencedColumnName 是目标表的字段（默认value是目标表的id） 关联生成sql片段如下
    // 生成sql如下：
    // left outer join
    //        `phone` phone2_
    //            on user0_.`phone_id`=phone2_.`id`

    // 用本表的 phone_id 去关联  Phone 表的  id 取出数据
    @JoinColumn(name = "phone_id",referencedColumnName = "id")
    private Phone phone;

}
