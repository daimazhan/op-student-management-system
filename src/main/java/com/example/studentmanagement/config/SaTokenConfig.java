package com.example.studentmanagement.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.example.studentmanagement.entity.Menu;
import com.example.studentmanagement.mapper.MenuMapper;
import com.example.studentmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SaTokenConfig implements WebMvcConfigurer {

    private final UserService userService;
    private final MenuMapper menuMapper;

    @Override
    public void addInterceptors(@SuppressWarnings("null") InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> {
            SaRouter.match("/**")
                    .notMatch("/api/auth/login", "/api/auth/register")
                    .check(r -> StpUtil.checkLogin());
        })).addPathPatterns("/**");
    }

    /**
     * 权限验证接口实现
     */
    @Bean
    public StpInterface stpInterface() {
        return new StpInterface() {
            @Override
            public List<String> getPermissionList(Object loginId, String loginType) {
                Long userId = Long.parseLong(loginId.toString());
                Long roleId = userService.getRoleIdByUserId(userId);
                
                // 获取该角色下的所有菜单权限
                List<Menu> menus = menuMapper.listByRole(roleId);
                List<String> permissions = new ArrayList<>();
                for (Menu menu : menus) {
                    if (menu.getPermission() != null && !menu.getPermission().isEmpty()) {
                        permissions.add(menu.getPermission());
                    }
                }
                return permissions;
            }

            @Override
            public List<String> getRoleList(Object loginId, String loginType) {
                Long userId = Long.parseLong(loginId.toString());
                String role = userService.getRoleByUserId(userId);
                List<String> roles = new ArrayList<>();
                roles.add(role);
                return roles;
            }
        };
    }
}
