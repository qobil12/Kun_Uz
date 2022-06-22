package com.company.util;
// PROJECT NAME Kun_Uz
// TIME 17:17
// MONTH 06
// DAY 15

import com.company.dto.JwtDTO;
import com.company.enums.ProfileRole;
import com.company.exps.NoPermissionException;

import javax.servlet.http.HttpServletRequest;

public class HttpHeaderUtil {
    public static  Integer getId(HttpServletRequest request, ProfileRole requiredRole) {
        JwtDTO jwtDTO = (JwtDTO) request.getAttribute("jwtDTO");
        Integer id = jwtDTO.getId();

        if (requiredRole != null) {
            if (!requiredRole.equals(jwtDTO.getRole())) {
                throw new NoPermissionException("Not Access");
            }
        }
        return id;
    }

    public static Integer getId(HttpServletRequest request){
        return getId(request,null);
    }

}
