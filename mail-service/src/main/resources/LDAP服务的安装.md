## LDAP 服务

```
本质是一个统一用户管理的服务,一个用户账户可以在支持LDAP的系统中使用相同账号密码登录,即一个账号密码登录多个子系统(例如:gitea  Jenkins 等等)

目录树概念
    1. 目录树：在一个目录服务系统中，整个目录信息集可以表示为一个目录信息树，树中的每个节点是一个条目。
    2. 条目：每个条目就是一条记录，每个条目有自己的唯一可区别的名称（DN）。
    3. 对象类：与某个实体类型对应的一组属性，对象类是可以继承的，这样父类的必须属性也会被继承下来。
    4. 属性：描述条目的某个方面的信息，一个属性由一个属性类型和一个或多个属性值组成，属性有必须属性和非必须属性。

名词
    dn - CN, OU, DC 都是 LDAP 连接服务器的端字符串中的区别名称（DN, distinguished   name） 
    dc - domainComponent（域名）
    sn – suer name（真实名称）
    cn - common name（常用名称）

实际登录 ldap_server  使用的用户名是 cn=admin,dc=example,dc=org   

简单来说  cn ->  用户的loginName
```

### LDAP服务的docker安装使用

> 源代码:    https://github.com/osixia/docker-openldap

```bash
# 启动容器
docker run \
-d \
-p 389:389 \
-p 636:636 \
-v /usr/local/ldap:/usr/local/ldap \
--name ldap \
osixia/openldap:1.3.0

# 默认的配置 
dn dc=example,dc=org
admin admin,dc=example,dc=org
password admin
```

##### 验证安装的LDAP服务是否可用

> 主要是admin的用户名和密码

```bash
docker exec -it ldap ldapsearch -x -H ldap://localhost:389 -b dc=example,dc=org -D "cn=admin,dc=example,dc=org" -w admin

# ------->   返回

# extended LDIF
#
# LDAPv3
# base <dc=example,dc=org> with scope subtree
# filter: (objectclass=*)
# requesting: ALL
#

# example.org
dn: dc=example,dc=org
objectClass: top
objectClass: dcObject
objectClass: organization
o: Example Inc.
dc: example

# admin, example.org
dn: cn=admin,dc=example,dc=org
objectClass: simpleSecurityObject
objectClass: organizationalRole
cn: admin
description: LDAP administrator
userPassword:: e1NTSEF9SzNrZCtzdjZIY3NLRWdQZVd0VGZITUlXVFVlUUhjaFE=

# search result
search: 2
result: 0 Success

# numResponses: 3
# numEntries: 2
```

##### 搭建 phpLDAPadmin  ui管理

```bash
docker run -dit \
-p 8081:80 \
--link ldap \
--name suiyue_pla \
--env PHPLDAPADMIN_HTTPS=false \
--env PHPLDAPADMIN_LDAP_HOSTS=ldap \
--restart always \
--detach osixia/phpldapadmin
```

##### 浏览器访问 8081端口 

> 用户名:	cn=admin,dc=example,dc=org
>
> 密码:	admin

##### 接下来需要在界面中配置organization / user

- create a child entry
- posix group 是用户组     user  account 是用户    够用了
- 用户登录使用用户名  密码即可登录


#### 镜像文件保存 导入

```
docker save xxx/xxx:5.0 > xxx.tar
docker load xxx/xxx:5.0 < xxx.tar
```

## Gitea配置

 ```
   1. 配置认证源
   2. 认证类型 LDAP (via BindDN)
   3. 安全协议  Unencrypted
   4. 主机端口  :       xxx:389
   5. 绑定一个可以获取所有用户组织的的角色账号
   6. **用户搜索基准  cn=nancal,dc=example,dc=org**
   7. **用户过滤规则    (&(objectClass=posixAccount)(uid=%s))**
   8. 电子邮箱属性    Email
 ```

### Jenkins配置  不能和数据库用户共存(使用之后之前的用户不能登录)
 ```
   0. 在全局安全配置中 
   1. Server :       ldap://saas-dev.nancal.com:60000
   2. user  search base :     dc=example,dc=org
   3. user  search  filter :   uid={0}
   4. group search base :  dc=example,dc=org
   5. Manager DN / Password :  管理员的用户名密码
   6. Test LDAP setting
 ```

#### grafana

```
## 官方文档  https://grafana.com/docs/grafana/latest/auth/ldap/
## docker 修改配置文件    https://grafana.com/docs/grafana/latest/installation/docker/
版本使用5.0.0   参考:https://www.cnblogs.com/woshimrf/p/docker-grafana.html  

## 普通启动，挂载数据盘
docker run  -d --name grafana -p 3000:3000   -v /gdata/grafana:/var/lib/grafana  grafana/grafana:5.0.0
 
## 配置文件 grafana.ini    ldap.toml  修改

## 修改ini ldap.toml  文件的权限  
chmod 644  grafana.ini

## 修改配置  grafana.ini  注意修改注释!!!!
[auth.ldap]
enabled = true
config_file = /etc/grafana/ldap.toml
allow_sign_up = true

## 挂载目录添加   ldap.toml文件 (LDAP 配置文件)
[[servers]]
# Ldap server host (specify multiple hosts space separated)
host = "saas-dev.nancal.com"
# Default port is 389 or 636 if use_ssl = true
port = 60000
# Set to true if ldap server supports TLS
use_ssl = false
# Set to true if connect ldap server with STARTTLS pattern (create connection in insecure, then upgrade to secure connection with TLS)
start_tls = false
# set to true if you want to skip ssl cert validation
ssl_skip_verify = false
  
# Search user bind dn
bind_dn = "cn=admin,dc=example,dc=org"
# Search user bind password
# If the password contains # or ; you have to wrap it with triple quotes. Ex """#password;"""
bind_password = '123456987'
 
# User search filter, for example "(cn=%s)" or "(sAMAccountName=%s)" or "(uid=%s)"
# Allow login from email or username, example "(|(sAMAccountName=%s)(userPrincipalName=%s))"
search_filter = "(uid=%s)"
 
# An array of base dns to search through
search_base_dns = ["dc=example,dc=org"]
 
# Specify names of the ldap attributes your ldap uses
[servers.attributes]
name = "givenName"
surname = "sn"
username = "cn"
member_of = "memberOf"
email =  "mail"

## 启动
docker run -d --name grafana -p 3000:3000  -v /data/grafana/etc:/etc/grafana/   grafana/grafana:5.0.0

```

#### Yapi

```
修改配置文件  config.json    k8s  修改对应deploy.yml

  "ldapLogin": {
      "enable": true,
      "server": "ldap://saas-dev.nancal.com:60000",
      "baseDn": "cn=admin,dc=example,dc=org",
      "bindPassword": "123456987",
      "searchDn": "dc=example,dc=org",
      "searchStandard": "uid", 
      "emailPostfix": "@nancal.com",
      "emailKey": "mail",
      "usernameKey": "name"
   }
```

### Harbor  配置

> 要开启Harbor的ldap认证功能就必须要在安装完，并启动服务后变更一下认证源。如果没有变更，就添加了用户就没有办法去修改配置变更了

![image-20210202175415823](C:\Users\nancal\AppData\Roaming\Typora\typora-user-images\image-20210202175415823.png)

##### kuboardV2

```
# 文档 https://www.kuboard.cn/learning/k8s-advanced/sec/authenticate/ldap.html#%E9%85%8D%E7%BD%AE-kubernetes-kuboard-%E4%BD%BF%E7%94%A8-openldap-%E7%99%BB%E5%BD%95
# 需要域名  需要https证书
```



###### Kuboard v3版本 支持

```
  # 关于如下参数的解释，请参考文档 https://kuboard.cn/install/v3/install-ldap.html
  # [ldap login]
  # KUBOARD_LOGIN_TYPE: "ldap"
  # KUBOARD_ROOT_USER: "your-user-name-in-ldap"
  # LDAP_HOST: "ldap-ip-address:389"
  # LDAP_BIND_DN: "cn=admin,dc=example,dc=org"
  # LDAP_BIND_PASSWORD: "admin"
  # LDAP_BASE_DN: "dc=example,dc=org"
  # LDAP_FILTER: "(objectClass=posixAccount)"
  # LDAP_ID_ATTRIBUTE: "uid"
  # LDAP_USER_NAME_ATTRIBUTE: "uid"
  # LDAP_EMAIL_ATTRIBUTE: "mail"
  # LDAP_DISPLAY_NAME_ATTRIBUTE: "cn"
  # LDAP_GROUP_SEARCH_BASE_DN: "dc=example,dc=org"
  # LDAP_GROUP_SEARCH_FILTER: "(objectClass=posixGroup)"
  # LDAP_USER_MACHER_USER_ATTRIBUTE: "gidNumber"
  # LDAP_USER_MACHER_GROUP_ATTRIBUTE: "gidNumber"
  # LDAP_GROUP_NAME_ATTRIBUTE: "cn"
```

###### Zentao 需要专业版支持 , 开源版插件支持到7.3版本

```
开源版 git插件  测试没有成功
https://github.com/iscarson/zentao-ldap
```

###### Sonarqube 8.3版本

```
# 官方文档 : https://docs.sonarqube.org/8.3/setup/environment-variables/

# 启用LDAP功能
SONAR_SECURITY_REALM=LDAP

#使用不区分大小写的设置连接到LDAP服务器时设置为true。
SONAR_AUTHENTICATOR_DOWNCASE=true

# LDAP服务器的URL。请注意，如果使用的是ldaps，则应将服务器证书安装到Java信任库中。
LDAP_URL=ldap://saas-dev.nancal.com:60000

# 绑定DN是要连接（或绑定）的LDAP用户的用户名。保留此空白可匿名访问LDAP目录（可选）
LDAP_BINDDN=cn=admin,dc=example,dc=org

#绑定密码是要连接的用户的密码。保留此空白可匿名访问LDAP目录（可选）
LDAP_BINDPASSWORD=123456987

# 可能的值：简单| CRAM-MD5 | DIGEST-MD5 | GSSAPI请参见
LDAP_AUTHENTICATION=simple
 
LDAP_REALM=example.org

# 上下文工厂类（可选）
LDAP_CONTEXTFACTORYCLASS=com.sun.jndi.ldap.LdapCtxFactory

# 启用StartTLS的使用tartTLS  默认为false
LDAP_STARTTLS=false

# 关注或不推荐。
LDAP_FOLLOWREFERRALS=false 

# LDAP中要从中搜索用户的根节点的专有名称（DN）（强制性）
LDAP_USER_BASEDN=dc=example,dc=org

# LDAP用户请求。（默认值：（＆（objectClass = inetOrgPerson）（uid = {login}）））
LDAP_USER_REQUEST=(&(objectClass=posixAccount)(uid={login}))

#  LDAP中的属性，用于定义用户的真实姓名。（默认值：cn）
LDAP_USER_REALNAMEATTRIBUTE=sn

# LDAP中的属性，用于定义用户的电子邮件。（默认：邮件）
LDAP_USER_EMAILATTRIBUTE=email

组映射
# LDAP中用于搜索组的根节点的专有名称（DN）。（可选，默认：空）
LDAP_GROUP_BASEDN=cn=groups,dc=example,dc=org

# LDAP组请求（默认值：（＆（objectClass = groupOfUniqueNames）（uniqueMember = {dn}）））
LDAP_GROUP_REQUEST=(&(objectClass=group)(member={dn}))

LDAP_GROUP_IDATTRIBUTE=sAMAccountName
```



------



## Java的LDAP连接与关键接口

##### pom中添加

```
<!--LDAP-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-ldap</artifactId>
</dependency>
```

##### application.yml 文件配置

```yaml
spring:
  ldap:
    urls: ldap://xx.xxx.xx.xx:389
    base: dc=example,dc=org     // 基础域
    username: cn=admin,dc=example,dc=org   
    password: admin
```

##### 实体

```kotlin
@Cn("LDAP用户")
@Document
@DbEntityGroup("corp")
data class LDAPUser(

    //posixAccount 必要的参数
    @Cn("用户姓名")
    var name: String = "",

    @Cn("真实姓名")
    var realname: String = "",

    @Cn("密码")
    var userpassword:String = "",

    @Cn("身份证号")
    var uidnumber:String = "",

    @Cn("gidnumber")
    var gidnumber:String = "",

    @Cn("uid")
    var uid:String = "",

    @Cn("objectclass")
    var objectclass:String = "",

    //可选参数

    @Cn("手机号")
    var mobile: String = "",

    @Cn("邮件")
    var email: String = "",

) : BaseEntity(), IMongoDocument
```

##### CRUD 等简单的接口

```kotlin
package xx.xxx.xxx

@Api(description = "Corporation", tags = arrayOf("Corporation"))
@RestController
@JsonpMapping("/open/ldap")
@OpenAction
class Open_LDAP_Controller {

    @Value("\${spring.ldap.base}")
    private val base: String? = null

    @Value("\${spring.ldap.urls}")
    private val url: String? = null

    @Value("\${spring.ldap.username}")
    private val dn: String? = null

    @Value("\${spring.ldap.password}")
    private val pwd: String? = null

    @Autowired
    var ldapTemplate: LdapTemplate? = null


    //add  posixGroup
    @JsonpMapping("/dept/add")
    fun addDept(
            name: String
    ): JsonResult {

        //获取最新的gidNumber
        val gidNumber = findDept().data.last().gidnumber + 1

        try {
            val groupDn = LdapNameBuilder.newInstance().add("cn", name).build();

            val context = DirContextAdapter(groupDn)
            context.setAttributeValues("objectclass", arrayOf("top", "posixGroup"))
            //应该定义在数据库中
            context.setAttributeValue("gidNumber", gidNumber)

            ldapTemplate!!.bind(context)
        } catch (e: Exception) {
            throw e
        }
        return JsonResult()
    }

    class LdapDept(
            var cn: String = "",
            var gidnumber: String = "",
            var objectclass: String = ""
    )

    //usersGroup 即 dept    返回所有的 用户组 信息
    @JsonpMapping("/dept/all")
    fun findDept(): ListResult<LdapDept> {
        val list = mutableListOf<LdapDept>()

        try {
            ldapTemplate!!.search(
                    query().where("objectclass").`is`("posixGroup"),
                    AttributesMapper { attrs: Attributes ->

                        val cn = attrs["cn"].get() as String
                        val gidNumber = attrs["gidNumber"].get() as String
                        val objectclass = attrs["objectclass"].get() as String

                        list.add(LdapDept(cn, gidNumber, objectclass))

                    } as AttributesMapper<*>)

            return ListResult(data = list, total = list.size)

        } catch (e: Exception) {
            throw e
        }
    }

    @JsonpMapping("/dept/one")
    fun getDeptByGitnumber(gitnumber:String):LdapDept{
        val info = findDept().data.filter { it.gidnumber == gitnumber }.firstOrNull()?:return LdapDept()

        return info
    }


    //在ldap中，有两个"查询"概念，search和lookup。search是ldaptemplate对每一个entry进行查询，lookup是通过DN直接找到某个条目。
    /**
     * 根据DN查询指定人员信息   精确查询
     * @param dn    e.g:   如果用户在 cn=devops,dc=example,dc=org下  具体到某一个人   需要传入的是  username=cy,dept=devops
     *
     * @return
     */
    @JsonpMapping("/userByNameAndDept")
    fun findUserWithDn(username: String,dept: String): ApiResult<LDAPUser> {

        val dn = "cn=$username,cn=$dept"

        try {
            val res = ApiResult.of(ldapTemplate!!.lookup(dn, LdapUserMapper()))

            return res
        } catch (e: Exception) {
            throw RuntimeException("无此用户或查询的dn错误")
        }
    }

    //根据用户名   模糊   查询是否有用户   只需要username
    @JsonpMapping("/userByUsername")
    fun findUserByCn(
            username: String
    ): ListResult<LDAPUser> {

        val filter = AndFilter()
                .and(EqualsFilter("objectclass", "posixAccount"))
                .and(EqualsFilter("cn", username))

        val list = ldapTemplate!!.search("", filter.encode(), LdapUserMapper())

        return ListResult(data = list, total = list.size)

    }

    @JsonpMapping("/checkPassword")
    fun checkPassword(username: String, password: String): JsonResult {

        val filter = AndFilter().and(EqualsFilter("objectclass", "posixAccount")).and(EqualsFilter("cn", username))
        val flag = ldapTemplate!!.authenticate(DistinguishedName.EMPTY_PATH, filter.toString(), password)

        if (flag) {
            println("用户:$username 验证密码通过")
            return JsonResult()
        }
        return JsonResult("LDAP校验密码错误,请重新输入")
    }


    @JsonpMapping("/user/add")
    fun addUser(
            @JsonModel user: LDAPUser,
            dept: String,    //devops   xxx 等用户组
    ): JsonResult {

        val ocAttr = BasicAttribute("objectclass")

        ocAttr.add("top");
        ocAttr.add("posixAccount");
        ocAttr.add("inetOrgPerson");

        val attrs: Attributes = BasicAttributes()

        attrs.put(ocAttr);

        //根据dept 查询gidNumber
        val deptInfo = findDept().data.filter { it.cn == dept }.firstOrNull()?: return JsonResult("没有找到对应的用户组")
        user.gidnumber = deptInfo.gidnumber

        attrs.put("gidNumber", user.gidnumber)
        //uidNumber  这里是身份证号
        attrs.put("uidNumber", user.uidnumber)

        attrs.put("homeDirectory", "/home/users/${user.name}")
        attrs.put("uid", user.name)

        attrs.put("cn", user.name);
        attrs.put("sn", user.realname);
        attrs.put("displayName", user.name);
        attrs.put("mail", user.email);
        attrs.put("mobile", user.mobile);
        attrs.put("userPassword", user.userpassword)

        try{
            ldapTemplate!!.bind("cn=${user.name},cn=$dept", null, attrs)
        }catch (e:Exception){
            throw RuntimeException("添加用户${user.name}到LDAP出错:${e.printStackTrace()}")
        }

        return JsonResult()
    }

    //修改用户信息
    @JsonpMapping("/user/update")
    fun update(
            @JsonModel user: LDAPUser,
            dept: String,  //devops    xxx   等用户组
    ): JsonResult {

        try {
            ldapTemplate!!.modifyAttributes(
                    "cn=${user.name},cn=$dept", arrayOf(
                    ModificationItem(DirContext.REPLACE_ATTRIBUTE, BasicAttribute("sn", user.realname)),
                    ModificationItem(DirContext.REPLACE_ATTRIBUTE, BasicAttribute("mail", user.email)),
                    ModificationItem(DirContext.REPLACE_ATTRIBUTE, BasicAttribute("mobile", user.mobile)),
            )
            )
        } catch (e: Exception) {
            throw RuntimeException("修改失败,请重试: ${e.printStackTrace()}")
        }

        return JsonResult()
    }

    @JsonpMapping("/user/resetPassword")
    fun resetPassword(
            username: String,
            password: String,
            dept: String,  //分组
    ): JsonResult {

        try {
            ldapTemplate!!.modifyAttributes(
                    "cn=${username},cn=${dept.toLowerCase()}", arrayOf(
                    ModificationItem(DirContext.REPLACE_ATTRIBUTE, BasicAttribute("userPassword", password))
            )
            )
        } catch (e: Exception) {
            throw RuntimeException("修改失败,请重试: ${e.printStackTrace()}")
        }

        return JsonResult()
    }

    //登录名是手机号
    @JsonpMapping("/user/changeMobile")
    fun changeMobile(
            username: String,
            mobile: String
    ): JsonResult {

        //先查询当前ldap 用户信息  获取密码
        val userInfo = findUserByCn(username).data.firstOrNull()?:return JsonResult("找不到用户:$username")

        //获取dept的名称    devops
        val deptinfo = getDeptByGitnumber(userInfo.gidnumber)

        //先删除 后添加  直接更新cn 会报错
        delete(username,deptinfo.cn)

        //添加新 ldapUser
        val ldapUser = LDAPUser()
        ldapUser.name = mobile
        ldapUser.realname = mobile
        ldapUser.mobile = mobile
        ldapUser.uidnumber = mobile
        ldapUser.userpassword = userInfo.userpassword

        //存入ldap
        val  res = addUser(ldapUser,deptinfo.cn);

        if (res.msg.HasValue){
            return JsonResult(res.msg)
        }

        return JsonResult()
    }


    @JsonpMapping("/user/delete")
    fun delete(
            username: String,
            dept: String
    ): JsonResult {

        try {
            ldapTemplate!!.unbind("cn=${username},cn=$dept")
        } catch (e: Exception) {
            throw RuntimeException("删除用户失败,请确认后重试")
        }

        return JsonResult()
    }


    //init LdapUser by CorpUser
    @JsonpMapping("/user/initByCorp")
    fun initLdapUserByCorpUser(){

        val list = mor.corp.corpUser.query()
                .where { it.id match_exists true }
                .toList()

        list.forEach { item ->

            if (item.mobile == "" || item.mobile.length < 10){
                println(" 手机号为 ${item.mobile} 的用户不符合添加标准")
                return@forEach
            }

            //查密码
            val subUser = mor.system.subSystemUserInfo.query()
                    .where { it.user.name match item.mobile  }
                    .toEntity() ?: return@forEach

            val auth = subUser.authList.firstOrNull()?:return@forEach

            val ldapUser = LDAPUser()

            ldapUser.email = item.email
            ldapUser.name = item.mobile
            ldapUser.uidnumber = item.mobile
            ldapUser.realname  = item.mobile
            ldapUser.mobile = item.mobile

            ldapUser.gidnumber = "500"

            val password = auth.password.replace(subUser.secKey,"")

            ldapUser.userpassword = password

            addUser(ldapUser,"devops");

            println("添加 = ${item.mobile}")
        }
    }

    class LdapUserMapper : AttributesMapper<LDAPUser> {

        @Throws(NamingException::class)
        override fun mapFromAttributes(attrs: Attributes): LDAPUser {
            val ldapUser = LDAPUser()
            try {
                ldapUser.name = attrs["cn"].get() as String
                ldapUser.realname = attrs["sn"].get() as String

                ldapUser.mobile = attrs["mobile"].get() as String
                ldapUser.gidnumber = attrs["gidnumber"].get() as String
                ldapUser.objectclass = attrs["objectclass"].get() as String

                val bytes = attrs["userPassword"].get() as ByteArray
                ldapUser.userpassword = String(bytes)
                //base64( md5( "123456" ) )
                //ldapUser.userpassword = String(bytes).replace("{MD5}", "")

            } catch (e: Exception) {
                throw RuntimeException("用户查询错误,请检查对应的dn是否正确")
            }
            return ldapUser
        }
    }

    /*    @JsonpMapping("/allUserName")
    fun getAllUserName(): Any {
        val res = ldapTemplate!!.search(
                query().where("objectclass").`is`("person"),
                AttributesMapper { attrs: Attributes ->
                    attrs["cn"].get() as String
                } as AttributesMapper<*>)

        return res
    }*/

/*    @JsonpMapping("/allUser")
    fun getUser(): ApiResult<List<LDAPUser>> {
        val data = ldapTemplate!!.search(
                query()
                        .where("objectclass").`is`("person"), LdapUserMapper()
        )

        return ApiResult.of(data)
    }*/
}
```

###### 附录:  (错误码)

```
LDAP_SUCCESS = 0 //成功

LDAP_OPERATIONS_ERROR = 1 //操作错误

LDAP_PROTOCOL_ERROR = 2 //协议错误

LDAP_TIME_LIMIT_EXCEEDED = 3 //超过最大时间限制

LDAP_SIZE_LIMIT_EXCEEDED = 4 //超过最大返回条目数

LDAP_COMPARE_FALSE = 5 //比较不匹配

LDAP_COMPARE_TRUE = 6 //比较匹配

LDAP_AUTH_METHOD_NOT_SUPPORTED = 7 //认证方法未被支持

LDAP_STRONG_AUTH_REQUIRED = 8 //需要强认证

LDAP_PARTIAL_RESULTS = 9 //null

LDAP_REFERRAL = 10 //Referral

LDAP_ADMIN_LIMIT_EXCEEDED = 11 //超出管理员权限

LDAP_UNAVAILABLE_CRITICAL_EXTENSION = 12 //Critical扩展无效

LDAP_CONFIDENTIALITY_REQUIRED = 13 //需要Confidentiality

LDAP_SASL_BIND_IN_PROGRESS = 14 //需要SASL绑定

LDAP_NO_SUCH_ATTRIBUTE = 16 //未找到该属性

LDAP_UNDEFINED_ATTRIBUTE_TYPE = 17 //未定义的属性类型

LDAP_INAPPROPRIATE_MATCHING = 18 //不适当的匹配

LDAP_CONSTRAINT_VIOLATION = 19 //约束冲突

LDAP_ATTRIBUTE_OR_value_EXISTS = 20 //属性或值已存在

LDAP_INVALID_ATTRIBUTE_SYNTAX = 21 //无效的属性语法

LDAP_NO_SUCH_OBJECT = 32 //未找到该对象

LDAP_ALIAS_PROBLEM = 33 //别名有问题

LDAP_INVALID_DN_SYNTAX = 34 //无效的DN语法

LDAP_IS_LEAF = 35 //null

LDAP_ALIAS_DEREFERENCING_PROBLEM = 36 //Dereference别名有问题

LDAP_INAPPROPRIATE_AUTHENTICATION = 48 //不适当的认证

LDAP_INVALID_CREDENTIALS = 49 //无效的Credential

LDAP_INSUFFICIENT_ACCESS_RIGHTS = 50 //访问权限不够

LDAP_BUSY = 51 //遇忙

LDAP_UNAVAILABLE = 52 //无效

LDAP_UNWILLING_TO_PERform = 53 //意外问题

LDAP_LOOP_DETECT = 54 //发现死循环

LDAP_NAMING_VIOLATION = 64 //命名冲突

LDAP_OBJECT_CLASS_VIOLATION = 65 //对象类冲突

LDAP_NOT_ALLOWED_ON_NON_LEAF = 66 //不允许在非叶结点执行此操作

LDAP_NOT_ALLOWED_ON_RDN = 67 //不允许对RDN执行此操作

LDAP_ENTRY_ALREADY_EXISTS = 68 //Entry已存在

LDAP_OBJECT_CLASS_MODS_PROHIBITED = 69 //禁止更改对象类

LDAP_AFFECTS_MULTIPLE_DSAS = 71 //null

LDAP_OTHER = 80 //其它

再来一份十六进制的，大家对照吧。

下面是winldap.h文件中的定义的十六进制错误码，我给其中的绝大部分加上了从活动目录的书上看的汉语说明，。

typedef enum

Unknown macro: {

LDAP_SUCCESS = 0x00,//操作成功

LDAP_OPERATIONS_ERROR = 0x01,//一个未指定的错误发生在处理LDAP请求的服务器上

LDAP_PROTOCOL_ERROR = 0x02,//服务器接受到一个没有正确格式化或顺序出错的包

LDAP_TIMELIMIT_EXCEEDED = 0x03,//操作上指定的时间限制被超出。这不同于服务器没有及时响应时的客户方检测到的超时错误

LDAP_SIZELIMIT_EXCEEDED = 0x04,//搜索返回的项数超过了管理限制或请求限制

LDAP_COMPARE_FALSE = 0x05,//LDAP比较函数(例如ldap_compare())返回FALSE

LDAP_COMPARE_TRUE = 0x06,//LDAP比较函数(例如ldap_compare())返回TRUE

LDAP_AUTH_METHOD_NOT_SUPPORTED = 0x07,//绑定(bind)操作中(例如ldap_bind())请求的认证方法不被服务器支持。如果你使用一个非微软LDAP客户与活动目录通信，这种情况可能发生

LDAP_STRONG_AUTH_REQUIRED = 0x08,//服务器要求一个字符串认证方法而不是一个简单口令

LDAP_REFERRAL_V2 = 0x09,//搜索结果包含LDAPv2引用或者一个部分结果集

LDAP_PARTIAL_RESULTS = 0x09,

LDAP_REFERRAL = 0x0a,//请求操作必须由另一个拥有适当的命名上下文备份的服务器处理

LDAP_ADMIN_LIMIT_EXCEEDED = 0x0b,//管理限制被超出。例如，搜索操作花费的时间超出了服务器所允许的最大时间

LDAP_UNAVAILABLE_CRIT_EXTENSION = 0x0c,//客户请求一个LDAP扩展并且指示该扩展是关键的，但是服务器并不支持扩展

LDAP_CONFIDENTIALITY_REQUIRED = 0x0d,//操作要求某种级别的加密

LDAP_SASL_BIND_IN_PROGRESS = 0x0e,//当一个SASL绑定(bind)已经在客户处理过程中时，请求一个绑定(bind)操作

LDAP_NO_SUCH_ATTRIBUTE = 0x10,//客户尝试修改或者删除一个并不存在的项的一个属性

LDAP_UNDEFINED_TYPE = 0x11,//未定义的类型

LDAP_INAPPROPRIATE_MATCHING = 0x12,//提供的匹配规则对搜索不合适或者对于属性不合适

LDAP_CONSTRAINT_VIOLATION = 0x13,//客户请求一个将违背目录中语义约束的操作。一个经常的原因是不合适的改变了模式--例如当添加一个新类时提供了一个重复的OID(对象识别符)

LDAP_ATTRIBUTE_OR_value_EXISTS = 0x14,//客户尝试添加一个已经存在的属性或值

LDAP_INVALID_SYNTAX = 0x15,//搜索过滤器的语法无效

LDAP_NO_SUCH_OBJECT = 0x20,//客户尝试或者删除一个在目录中并不存在的项

LDAP_ALIAS_PROBLEM = 0x21,//服务器在处理别名时遇到了一个错误

LDAP_INVALID_DN_SYNTAX = 0x22,//请求中指定的可区别名字的格式无效

LDAP_IS_LEAF = 0x23,//函数中指定的项是目录树中的一个叶子项

LDAP_ALIAS_DEREF_PROBLEM = 0x24,//在解除对一个别名的引用时服务器遇到了一个错误。例如，目的项并不存在

LDAP_INAPPROPRIATE_AUTH = 0x30,//认证级别对于操作不足

LDAP_INVALID_CREDENTIALS = 0x31,//绑定(bind)请求中提供的证书是无效的--例如一个无效的口令

LDAP_INSUFFICIENT_RIGHTS = 0x32,//没有执行该操作所需的足够的访问权限

LDAP_BUSY = 0x33,//服务器太忙碌而无法服务该请求。稍后重新尝试

LDAP_UNAVAILABLE = 0x34,//目录服务暂不可用。稍后重新尝试

LDAP_UNWILLING_TO_PERform = 0x35,//由于管理策略约束方面的原因，服务器将不支持该操作--例如，如果在模式修改没有被允许或者没有连接到模式管理器的情况下，试图修改该模式

LDAP_LOOP_DETECT = 0x36,//在追踪引用的过程中，客户引用到它以前已经引用的服务器

LDAP_SORT_CONTROL_MISSING = 0x3C,

LDAP_OFFSET_RANGE_ERROR = 0x3D,

LDAP_NAMING_VIOLATION = 0x40,//客户指定了一个不正确的对象的可区别名字

LDAP_OBJECT_CLASS_VIOLATION = 0x41,//操作违背了类定义中定义的语义规则

LDAP_NOT_ALLOWED_ON_NONLEAF = 0x42,//所请求的操作只可能在一个叶子对象(非容器)上执行

LDAP_NOT_ALLOWED_ON_RDN = 0x43,//在相对可区别名字上不允许该操作

LDAP_ALREADY_EXISTS = 0x44,//客户试图添加一个已经存在的对象

LDAP_NO_OBJECT_CLASS_MODS = 0x45,//客户试图通过改变一个对象的objectClass属性来修改对象的类

LDAP_RESULTS_TOO_LARGE = 0x46,//搜索操作的结果集太大，服务器无法处理

LDAP_AFFECTS_MULTIPLE_DSAS = 0x47,//所请求的操作将影响多个DSA--例如，在一个子树包含一个下级引用，该引用指向另一个命名上下文的情况下，删除该子树将影响多个DSA(目录服务器代理)

LDAP_VIRTUAL_LIST_VIEW_ERROR = 0x4c,

LDAP_OTHER = 0x50,//发生了一些其他的LDAP错误

LDAP_SERVER_DOWN = 0x51,//LDAP服务器已关闭

LDAP_LOCAL_ERROR = 0x52,//客户发生了其他一些未指定的错误

LDAP_ENCODING_ERROR = 0x53,//在将一个LDAP请求编码为ASN.1的过程中发生了一个错误

LDAP_DECODING_ERROR = 0x54,//从服务器接受到的ASN.1编码的数据是无效的

LDAP_TIMEOUT = 0x55,//在指定的时间内服务器不能响应客户

LDAP_AUTH_UNKNOWN = 0x56,//在绑定(bind)请求中指定了一种未知的认证机制

LDAP_FILTER_ERROR = 0x57,//搜索过滤器出现了某种错误

LDAP_USER_CANCELLED = 0x58,//用户取消了操作

LDAP_PARAM_ERROR = 0x59,//函数中指定的某个参数出现了错误。例如，向一个LDAP API函数传递一个NULL指针，但该函数并不希望这样，在这种情况下就产生该错误

LDAP_NO_MEMORY = 0x5a,//客户试图分配内存并且失败了

LDAP_CONNECT_ERROR = 0x5b,//客户试图向服务器建立一个TCP连接并且失败了

LDAP_NOT_SUPPORTED = 0x5c,//所请求的操作不被这种版本的LDAP协议所支持

LDAP_NO_RESULTS_RETURNED = 0x5e,//从服务器接受到一个响应，但是它没有包含结果

LDAP_CONTROL_NOT_FOUND = 0x5d,//从服务器接受到的数据表明有一个LDAP控制出现但是在数据中没有找到一个LDAP控制

LDAP_MORE_RESULTS_TO_RETURN = 0x5f,//因为有太多的结果，所以客户无法检索

LDAP_CLIENT_LOOP = 0x60,//在处理引用时客户检测到一个循环

引用数目超过了限制

LDAP_REFERRAL_LIMIT_EXCEEDED = 0x61//客户追踪的
```

