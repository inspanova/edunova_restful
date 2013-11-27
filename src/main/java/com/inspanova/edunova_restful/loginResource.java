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
import javax.ws.rs.PUT;

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
    public Map<String, Serializable> authenticate(
            @Context HttpServletRequest request,
            @FormParam("username") String username,
            @FormParam("password") String password) throws Exception {
        Map<String, Serializable> userMap = Db_Interactor.authenticate(username, password);


        if (userMap.get("roleId") != "0") {
            HttpSession session = request.getSession(true);
            session.setAttribute("username", username);
            System.out.println(userMap.get("roleId"));


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

    @GET
    @Path("/getschool")
    @Produces(MediaType.APPLICATION_JSON)
    public static List<Map<String, Serializable>> getSchools(
            @Context HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession(true);
        return Db_Interactor.getSchoolDetails();
    }

    @POST
    @Path("/editschool")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> editSchool(
            @Context HttpServletRequest request,
            Map<String, String> schoolInfo) throws Exception {
        HttpSession session = request.getSession(true);
        Map<String, String> userData = new HashMap<String, String>(3);
        int accountId = 1;
//        int accountId = (Integer) session.getAttributae("accountId");
        userData = Db_Interactor.editSchoolInfo(schoolInfo);
       
        return userData;
    }
    @PUT
	@Path("/activate")
	@Produces(MediaType.APPLICATION_JSON)
	public void enableOrDisable(
			@Context HttpServletRequest request,
			@FormParam("value") String value,
                        @FormParam("schoolId") String schoolId) throws Exception {
        System.out.println(schoolId);
        Db_Interactor.activateOrDeactivateSchool(Integer.valueOf(value),Integer.valueOf(schoolId));
    }
}
