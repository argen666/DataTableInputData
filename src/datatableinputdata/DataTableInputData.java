/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datatableinputdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import oracle.sql.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author Developer
 */
public class DataTableInputData {
public static Connection connection = null;
    /**
     * @param args the command line arguments
     */
    private static void DBConnect() throws SQLException {
       CallableStatement cstmt = null;
       String serverIP = "192.168.16.13";
            String portNumber = "1521";
            String instanceName = "pudp";
            String username = "argen666";
            String password = "argen666";
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            String url = "jdbc:oracle:thin:@" + serverIP + ":" + portNumber + ":" + instanceName;
            connection = DriverManager.getConnection(url, username, password);
            
    }
    
    private static void WriteToDB(Map<String,String> map /*String num, String name, String filename*/) throws SQLException {
        CallableStatement cstmt = null;
        try {
            if (connection==null) DBConnect();

            Statement stmt = connection.createStatement (); 
            ResultSet r = null;
            for (Map.Entry<String, String> entry : map.entrySet()) {
 
                
                r = stmt.executeQuery ("insert into DATA values('"+entry.getValue()+"','"+entry.getKey()+"')");
			/*System.out.println("Country [code= " + entry.getKey() + " , name="
				+ entry.getValue() + "]");*/
 
		}
               
             
           /* String day = "";
            cstmt = connection.prepareCall("{call argen666.TESTPRM(?,?)}");
            cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);
            cstmt.setString(1, "Monday");
            cstmt.executeUpdate();
            day = cstmt.getString(2);
            System.out.println(day);*/

            /*cstmt = connection.prepareCall("{call ? :=parus.get_license()}");
            cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
            cstmt.executeUpdate();
            day = cstmt.getString(1);
            System.out.println(day);*/

            //parus.P_INORDERS_SET_STATUS(59945, 109795, 0, TO_DATE('25.09.2014', 'DD.MM.YYYY HH24:MI:SS'), NWARNING, SMSG);
            //java.sql.SQLException: ORA-20103: Работа в Системе невозможна, т.к. приложение "Other" не установлено.
            /*cstmt = connection.prepareCall("{call parus.P_INORDERS_SET_STATUS(59945, 109795, 0, TO_DATE('25.09.2014', 'DD.MM.YYYY HH24:MI:SS'), ?, ?)}");
             cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
             cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);
             cstmt.executeUpdate();
             day=cstmt.getString(1);
             System.out.println(day);*/
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            if (cstmt != null) {
                cstmt.close();
            }
        }
    }

    public static void main(String[] args) throws SQLException, IOException {
      // TODO code application logic here 
       ParseCSV();
      //WriteToDB();
        //System.out.println("54654");                           
    }

    private static Map ParseCSV() throws IOException, SQLException {

        String csvFile = "C:\\ParseToParus\\Абразивный инструмент.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        Map<String, String> map = null;
        try {
            map = new HashMap<String, String>();
            br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(csvFile), "windows-1251"));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] buf = line.split(cvsSplitBy);
                if (buf[1].isEmpty() || buf[3].isEmpty()) continue;
               map.put(buf[1].replaceAll("[\\s]{2,}", " "), buf[3].replaceAll(" ", ""));
               /* System.out.println("[" + buf[1].replaceAll("[\\s]{2,}", " ")
                        + "," + buf[3].replaceAll(" ", "") + "]");*/

            }
            
            WriteToDB(map);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

      return map;

    }

    

}
