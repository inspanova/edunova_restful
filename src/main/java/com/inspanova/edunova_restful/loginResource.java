/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inspanova.edunova_restful;

import com.inspanova.edunova.db.Db_Interactor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.inspanova.edunova.utility.Mail;

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
    public Map<String, String> addSchool(
            @Context HttpServletRequest request,
            Map<String, String> schoolInfo) throws Exception {
        HttpSession session = request.getSession(true);
        Map<String, String> userData = new HashMap<String, String>(3);
        int accountId = 1;
//        int accountId = (Integer) session.getAttribute("accountId");
        userData = Db_Interactor.addSchoolInfo(accountId, schoolInfo);
        int createdUserId = Integer.valueOf(userData.get("userId"));
        if (createdUserId > 0) {
            List<String> recipients = new ArrayList<String>();
            String subject = "Login created";
            String messageBody = "User Id: Password";
            recipients.add(userData.get("email"));
            Mail.sendMail(recipients, subject, messageBody);
        }
        return userData;
    }
}
