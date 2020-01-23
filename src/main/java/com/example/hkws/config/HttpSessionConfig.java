package com.example.hkws.config;

/**
* @Description: 主要是添加@EnableRedisHttpSession注解即可，该注解会创建一个名字叫springSessionRepositoryFilter的Spring Bean，
 * 其实就是一个Filter，这个Filter负责用Spring Session来替换原先的默认HttpSession实现，在这个例子中，Spring Session是用Redis来实现的。
* @Param:
* @return:
* @Author: zzx
* @Date: 2019-08-15
**/
//@Configuration
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400*30)
public class HttpSessionConfig {

}