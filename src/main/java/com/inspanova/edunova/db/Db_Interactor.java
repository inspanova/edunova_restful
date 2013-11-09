/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inspanova.edunova.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author krishna
 */
public class Db_Interactor {

    public static Map<String, String> authenticate(String userName, String Password) throws Exception {
        Map<String, String> userMap = new HashMap<String, String>();

        if (userName.equals("edunova.admin@gmail.com") && Password.equals("admin123")) {

            userMap.put("role", "1");

        } else {
            userMap.put("role", "0");
        }

        return userMap;
    }

    public static Map<String,String> addSchoolInfo(
            int UserId, Map<String, String> schoolInfo)
            throws Exception {
        String schoolName = schoolInfo.get("schoolName");
        String address1 = schoolInfo.get("address1");
        String address2 = schoolInfo.get("address2");
        String pincode = schoolInfo.get("pincode");

        int country = Integer.valueOf(schoolInfo.get("country"));
        int state = Integer.valueOf(schoolInfo.get("state"));
        int district = Integer.valueOf(schoolInfo.get("district"));
        String registration = schoolInfo.get("registration");
        String website = schoolInfo.get("website");
        String establishedDate = schoolInfo.get("establishedDate");
        int license = Integer.valueOf(schoolInfo.get("license"));
        String phone1 = schoolInfo.get("phone1");
        String phone2 = schoolInfo.get("phone2");
        String fax = schoolInfo.get("fax");
        System.out.print("_____________________________________________________________");


        String fname=schoolInfo.get("firstName");
        String lName=schoolInfo.get("lastName");
        String email=schoolInfo.get("email");
        String password=schoolInfo.get("password");
        String expiryDate=schoolInfo.get("expiryDate");
//        
        Connection connection = DB_Connector.getMysqlConnection();
        String query = "insert into tbl_edunova_school(school_name,address_line1,address_line2,established_date,"
                + "registration,pincode,country_code,state_code,district_code,website,login_license,phone_number1,"
                + "phone_number2,fax) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, schoolName);
        ps.setString(2, address1);
        ps.setString(3, address2);
        ps.setString(4, establishedDate);
        ps.setString(5, registration);
        ps.setString(6, pincode);
        ps.setInt(7, country);
        ps.setInt(8, state);
        ps.setInt(9, district);
        ps.setString(10, website);
        ps.setInt(11, license);
        ps.setString(12, phone1);
        ps.setString(13, phone2);
        ps.setString(14, fax);
        ps.executeUpdate();
        ResultSet keys = ps.getGeneratedKeys();
        keys.next();
        int schoolId = keys.getInt(1);

        query = "insert into tbl_edunova_personal_details (first_name,last_name,role_id,school_id) values (?,?,2,?)";
        ps = connection
                .prepareStatement(query, com.mysql.jdbc.Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, fname);
        ps.setString(2, lName);
        ps.setInt(3, schoolId);
         ps.executeUpdate();
        keys = ps.getGeneratedKeys();
        keys.next();
        int personalId = keys.getInt(1);
        
         query = "insert into tbl_edunova_user (personal_id,user_name,password,created_by,created_date,expiry_date) values (?,?,?,?,?,?)";
        ps = connection
                .prepareStatement(query, com.mysql.jdbc.Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, personalId);
        ps.setString(2, email);
        ps.setString(3, password);
        ps.setInt(4, UserId);
         ps.setString(5,expiryDate);
          ps.setString(6,expiryDate);
         ps.executeUpdate();
        keys = ps.getGeneratedKeys();
        keys.next();
        int CreateduserId = keys.getInt(1);
        Map<String,String> returnMap=new HashMap<String, String>(3);
        returnMap.put("email", email);
        returnMap.put("userId", ""+CreateduserId);
        ps.close();
        connection.close();
        return returnMap;
    }
}
