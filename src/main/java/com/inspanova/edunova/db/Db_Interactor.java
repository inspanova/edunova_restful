/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inspanova.edunova.db;

import com.inspanova.edunova.utility.DateUtility;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author krishna
 */
public class Db_Interactor {

    public static Map<String, Serializable> authenticate(String userName, String password) throws Exception {
        Map<String, Serializable> userMap = new HashMap<String, Serializable>();

        if (userName.equals("edunova.admin@gmail.com") && password.equals("admin123")) {

            userMap.put("roleId", 1);
            System.out.println("Admin");

        } else {

            Connection connection = DB_Connector.getMysqlConnection();
            String query = "select sc.school_id,sc.school_name,concat(sc.address_line1,',',sc.address_line2) as place,"
                    + "pd.personal_id,concat(pd.first_name,' ',pd.last_name)as full_name,pd.role_id, us.user_id from tbl_edunova_school sc"
                    + " inner join tbl_edunova_personal_details pd on pd.school_id=sc.school_id"
                    + " inner join tbl_edunova_user us on us.personal_id=pd.personal_id"
                    + " where us.user_name=? and us.password=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, userName);
            ps.setString(2, password);
            ResultSet result = ps.executeQuery();
            if (result.next()) {
                userMap.put("schoolId", result.getInt("user_id"));
                userMap.put("userId", result.getInt("user_id"));
                userMap.put("personalId", result.getInt("personal_id"));
                userMap.put("roleId", result.getInt("role_id"));
                userMap.put("schoolName", result.getString("school_name"));
                userMap.put("schoolPlace", result.getString("place"));
                userMap.put("fullName", result.getString("full_name"));
            } else {
                userMap.put("roleId", 2);
                System.out.println("No User");
            }

        }

        return userMap;
    }

    public static Map<String, String> addSchoolInfo(
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


        String fname = schoolInfo.get("firstName");
        String lName = schoolInfo.get("lastName");
        String email = schoolInfo.get("email");
        String password = schoolInfo.get("password");
        String expiryDate = schoolInfo.get("expiryDate");
//        
        Connection connection = DB_Connector.getMysqlConnection();
        String query = "insert into tbl_edunova_school(school_name,address_line1,address_line2,established_date,"
                + "registration,pincode,country_code,state_code,district_code,website,login_license,phone_number1,"
                + "phone_number2,fax,is_enabled) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,1)";
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

        query = "insert into tbl_edunova_user (personal_id,user_name,password,created_by,created_date,expiry_date,role_id) values (?,?,?,?,?,?,2)";
        ps = connection
                .prepareStatement(query, com.mysql.jdbc.Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, personalId);
        ps.setString(2, email);
        ps.setString(3, password);
        ps.setInt(4, UserId);
        ps.setString(5, expiryDate);
        ps.setString(6, expiryDate);
        ps.executeUpdate();
        keys = ps.getGeneratedKeys();
        keys.next();
        int CreateduserId = keys.getInt(1);
        Map<String, String> returnMap = new HashMap<String, String>(3);
        returnMap.put("email", email);
        returnMap.put("userId", "" + CreateduserId);
        ps.close();
        connection.close();
        return returnMap;
    }

    public static List<Map<String, Serializable>> getSchoolDetails()
            throws Exception {
        Connection connection = DB_Connector.getMysqlConnection();
        String query = "select sc.school_id,sc.school_name,sc.address_line1,sc.address_line2,sc.established_date,"
                + "sc.registration,sc.pincode,sc.country_code,sc.state_code,sc.district_code,sc.website,sc.login_license,sc.phone_number1,"
                + "sc.phone_number2,sc.fax,sc.is_enabled,pd.first_name,pd.last_name,us.user_name,us.password,us.expiry_date from tbl_edunova_school sc "
                + "inner join tbl_edunova_personal_details pd on pd.school_id=sc.school_id "
                + "inner join tbl_edunova_user us on us.personal_id=pd.personal_id";
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet result = ps.executeQuery();
        List<Map<String, Serializable>> schoolList = new ArrayList<Map<String, Serializable>>();
        while (result.next()) {
            Map<String, Serializable> schoolInfo = new HashMap<String, Serializable>();
            schoolInfo.put("schoolId", result.getString("school_id"));
            schoolInfo.put("schoolName", result.getString("school_name"));
            schoolInfo.put("addressLine1", result.getString("address_line1"));
            schoolInfo.put("addressLine2", result.getString("address_line2"));
            schoolInfo.put("establishedDate", DateUtility.convertMYSQLDateTimeToMMDDYYYY(
                    result.getString("established_date")));
            schoolInfo.put("registration", result.getString("registration"));
            schoolInfo.put("pincode", result.getString("pincode"));
            schoolInfo.put("countryCode", result.getString("country_code"));
            schoolInfo.put("stateCode", result.getString("state_code"));
            schoolInfo.put("districtCode", result.getString("district_code"));
            schoolInfo.put("website", result.getString("website"));
            schoolInfo.put("loginLicense", result.getString("login_license"));
            schoolInfo.put("phone1", result.getString("phone_number1"));
            schoolInfo.put("phone2", result.getString("phone_number2"));
            schoolInfo.put("fax", result.getString("fax"));
            schoolInfo.put("enabled", result.getString("is_enabled"));
            schoolInfo.put("firstName", result.getString("first_name"));
            schoolInfo.put("lastName", result.getString("last_name"));
            schoolInfo.put("userName", result.getString("user_name"));
            schoolInfo.put("password", result.getString("password"));
            schoolInfo.put("expiryDate", DateUtility.convertMYSQLDateTimeToMMDDYYYY(
                    result.getString("expiry_date")));

            schoolList.add(schoolInfo);
        }
        ps.close();
        connection.close();
        return schoolList;
    }

    public static Map<String, String> editSchoolInfo(
            Map<String, String> schoolInfo)
            throws Exception {
        int schooId = Integer.valueOf(schoolInfo.get("schoolId"));
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
        String fname = schoolInfo.get("firstName");
        String lName = schoolInfo.get("lastName");
        String email = schoolInfo.get("email");
        String password = schoolInfo.get("password");
        String expiryDate = schoolInfo.get("expiryDate");
//        
        Connection connection = DB_Connector.getMysqlConnection();
        String query = "update tbl_edunova_school set school_name=?,address_line1=?,address_line2=?,established_date=?,"
                + "registration=?,pincode=?,country_code=?,state_code=?,district_code=?,website=?,login_license=?,phone_number1=?,"
                + "phone_number2=?,fax=? where school_id=" + schooId;
        PreparedStatement ps = connection.prepareStatement(query);
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


        query = "update tbl_edunova_personal_details set first_name=?,last_name=? where school_id=" + schooId;
        ps = connection
                .prepareStatement(query);
        ps.setString(1, fname);
        ps.setString(2, lName);



        Map<String, String> returnMap = new HashMap<String, String>(3);

        returnMap.put("userId", "9");
        ps.close();
        connection.close();
        return returnMap;
    }

    public static void activateOrDeactivateSchool(int value, int schoolId) throws Exception {
        Connection connection = DB_Connector.getMysqlConnection();
        String query = "update tbl_edunova_school set is_enabled=? where school_id=" + schoolId;
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, value);
        ps.executeUpdate();
        ps.close();
        connection.close();

    }

    public static List<Map<String, String>> getStandards() throws Exception {
        Connection connection = DB_Connector.getMysqlConnection();
        String query = "select * from tbl_edunova_standard";
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet result = ps.executeQuery();
        List<Map<String, String>> stdList = new ArrayList<Map<String, String>>();
        while (result.next()) {
            Map<String, String> stdInfo = new HashMap<String, String>();
            stdInfo.put("stdId", "" + result.getInt("standard_id"));
            stdInfo.put("stdName", result.getString("standard_name"));
            stdList.add(stdInfo);

        }
        ps.close();
        connection.close();
        return stdList;

    }
     public static List<Map<String, String>> getDivisions() throws Exception {
        Connection connection = DB_Connector.getMysqlConnection();
        String query = "select * from tbl_edunova_divisions";
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet result = ps.executeQuery();
        List<Map<String, String>> divList = new ArrayList<Map<String, String>>();
        while (result.next()) {
            Map<String, String> divInfo = new HashMap<String, String>();
            divInfo.put("divId", "" + result.getInt("division_id"));
            divInfo.put("divName", result.getString("division_name"));
            divList.add(divInfo);

        }
        ps.close();
        connection.close();
        return divList;

    }
}
