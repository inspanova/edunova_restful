/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inspanova.edunova_restful;

import com.inspanova.edunova.db.Db_Interactor;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author krishna
 */
@Path("/login")
public class loginResource {

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> authenticate(
            @Context HttpServletRequest request,
            @FormParam("username") String username,
            @FormParam("password") String password) throws Exception {
        Map<String, String> userMap = Db_Interactor.authenticate(username, password);


        if (userMap.get("role") != "0") {
            HttpSession session = request.getSession(true);
            session.setAttribute("username", username);


        } else {
//            throw new AuthenticationException();
        }
        return userMap;
    }

    @POST
    @Path("/saveschool")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Serializable> addSchool(
            @Context HttpServletRequest request,
            Map<String, String> schoolInfo) throws Exception {
        HttpSession session = request.getSession(true);
        Map <String,Serializable> userData=new HashMap<String, Serializable>(2);
        int accountId=1;
//        int accountId = (Integer) session.getAttribute("accountId");
        userData.put("key", ""+Db_Interactor.addSchoolInfo(accountId, schoolInfo));
        return userData;
    }
}
