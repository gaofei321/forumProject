package com.kaishengit.service;


import com.kaishengit.dao.AdminDao;
import com.kaishengit.entity.Admin;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.util.Config;
import com.kaishengit.util.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminService {

    private AdminDao adminDao=new AdminDao();

    public Admin findAdminByNameAndPassword(String adminName, String password) {

        Admin admin=adminDao.findAdminByName(adminName);
        if(admin!=null&&DigestUtils.md5Hex(password+Config.get("user.password.salt")).equals(admin.getPassword())){

            Logger logger= LoggerFactory.getLogger(AdminService.class);
            logger.info("{}登录了管理员系统",admin.getAdminname());
            return admin;
        }else {
            throw new ServiceException("账号或密码错误，请重新登录");
        }

    }




}
