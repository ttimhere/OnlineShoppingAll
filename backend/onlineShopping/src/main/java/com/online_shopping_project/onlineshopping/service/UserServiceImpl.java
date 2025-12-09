package com.online_shopping_project.onlineshopping.service;
import com.online_shopping_project.onlineshopping.entity.User;
import com.online_shopping_project.onlineshopping.mapper.UserMapper;
import com.online_shopping_project.onlineshopping.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
/**
 * 用户服务实现类
 * 负责处理注册与登录的核心业务逻辑。
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    //用户注册逻辑实现
    @Override
    public boolean register(User user) {
        if (userMapper.findByEmail(user.getEmail()) != null) {// 检查邮箱是否重复
            return false;
        }
        // 加密密码后存入数据库
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        user.setStatus(1);
        userMapper.insert(user);
        return true;
    }
    //用户登录逻辑实现
    @Override
    public User login(String email, String password) {
        User dbUser = userMapper.findByEmail(email);
        if (dbUser == null) return null;
        // 校验加密密码是否匹配
        return passwordEncoder.matches(password, dbUser.getPasswordHash()) ? dbUser : null;
    }
    // 根据ID查询用户
    @Override
    public User findById(Long id) {
        return userMapper.findById(id);
    }
}

