/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inspanova.edunova_restful;

import com.inspanova.edunova.db.Db_Interactor;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 */
@Path("/schoolsettings")
public class Schoolsettings {
    
    @GET
    @Path("/getstandards")
    @Produces(MediaType.APPLICATION_JSON)
    public static List<Map<String, String>> getStandards(
            @Context HttpServletRequest request) throws Exception {
        System.out.println("Called");
        return Db_Interactor.getStandards();
    }
    @GET
    @Path("/getdivisions")
    @Produces(MediaType.APPLICATION_JSON)
    public static List<Map<String, String>> getDivisions(
            @Context HttpServletRequest request) throws Exception {
       System.out.println("Called div");
        return Db_Interactor.getDivisions();
    }
}