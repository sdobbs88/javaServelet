/**
 * Class: CSCI5520U Rapid Java Programming
 * Instructor: Y. Daniel Liang
 * Description: Web servelet with database operations
 * Due: 02/01/2017
 *
 * @author Shaun C. Dobbs
 * @version 1.0
 *
 * I pledge by honor that I have completed the programming assignment
 * independently. I have not copied the code from a student or any source. I
 * have not given my code to any student. * Sign here: Shaun C. Dobbs
 */
package Chapter37;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class Exercise37_13 extends HttpServlet {
// Use a prepared statement to store a student into the database

    private PreparedStatement pstmt;

    /**
     * Initialize variables
     *
     * @throws javax.servlet.ServletException
     */
    @Override
    public void init() throws ServletException {
        initializeJdbc();
    }

    /**
     * Process the HTTP Post request
     *
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String ID;
        String lastName;
        String firstName;
        String mi;
        String address;
        String city;
        String state;
        String telephone;

        if (request.getParameter("insert") != null) {
            // Invoke FirstServlet's job here.
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            // Obtain parameters from the client
            ID = request.getParameter("ID");
            lastName = request.getParameter("lastName");
            firstName = request.getParameter("firstName");
            mi = request.getParameter("mi");
            address = request.getParameter("address");
            city = request.getParameter("city");
            state = request.getParameter("state");
            telephone = request.getParameter("telephone");
            try {
                storeStudent(ID, lastName, firstName, mi, address, city, state, telephone);
                out.println(firstName + " " + lastName + " is now registered in the database");

            } catch (SQLException ex) {
                out.println("Error: " + ex.getMessage());
            } finally {
                out.close(); // Close stream
            }
            ///////////////////////////////////////////////
        } else if (request.getParameter("view") != null) {
            //Initialize the database
            ID = request.getParameter("ID");
            PrintWriter out = response.getWriter();
            try {
                String query = "select * from staffinfo where id=" + ID + ";";
                ResultSet rs = pstmt.executeQuery(query);

                init();

                while (rs.next()) {

                    int IDcol = rs.getInt("ID");
                    String idStr = Integer.toString(IDcol);
                    String lastNameStr = rs.getString("lastName");
                    String firstNameStr = rs.getString("firstName");
                    String mIStr = rs.getString("mI");
                    String addressStr = rs.getString("address");
                    String cityStr = rs.getString("city");
                    String stateStr = rs.getString("state");
                    String telStr = rs.getString("telephone");
                       
                        out.print("<html>\n"
                                + "    <head>\n"
                                + "        <title>Exercise 37_13</title>\n"
                                + "    </head>\n"
                                + "    <body>\n"
                                + "        <table>\n"
                                + "            <tr>\n"
                                + "                <td>\n"
                                + "                    <fieldset>\n"
                                + "                        <legend>Staff Information</legend>\n"
                                + "                        <form method = \"get\" action = \"Exercise37_13\">\n"
                                + "                            <p>ID <input type = \"text\" name = \"ID\" value =\"" + idStr + "\" size = \"10\" required >&nbsp;\n"
                                + "                            </p>\n"
                                + "\n"
                                + "                            <p>Last Name: <input type = \"text\" name = \"lastName\" value =\"" + lastNameStr + "\"size = \"20\" >&nbsp;\n"
                                + "                               First Name: <input type = \"text\" name = \"firstName\" value =\"" + firstNameStr + "\" size = \"20\" >&nbsp;\n"
                                + "                               MI: <input type = \"text\" name = \"mi\" value =\"" + mIStr + "\" size = \"2\">&nbsp;\n"
                                + "                            </p>          \n"
                                + "\n"
                                + "                            <p>Address: <input type = \"text\" name = \"address\" value =\"" + addressStr + "\" size = \"20\" >\n"
                                + "                            </p>\n"
                                + "\n"
                                + "                            <p>City: <input type = \"text\" name = \"city\" value =\"" + cityStr + "\" size = \"20\" >&nbsp;               \n"
                                + "                               State: <input type = \"text\" name = \"state\"value =\"" + stateStr + "\" size = \"2\" >\n"
                                + "                            </p>\n"
                                + "\n"
                                + "                            <p>Telephone <input type = \"text\" name = \"telephone\" value =\"" + telStr + "\" size = \"15\" >&nbsp;\n"
                                + "                            </p>\n"
                                + "                            <p ALIGN=\"CENTER\">\n"
                                + "                                <input type = \"submit\" name = \"insert\" value = \"Insert\">\n"
                                + "                                <input type = \"submit\" name = \"view\" value = \"View\">\n"
                                + "                                <input type = \"submit\" name = \"update\" value = \"Update\">\n"
                                + "                                <input type = \"submit\" name = \"reset\" value = \"Clear\">\n"
                                + "                            </p>\n"
                                + "                        </form>\n"
                                + "                    </fieldset>\n"
                                + "                </td>\n"
                                + "            </tr>\n"
                                + "        </table>\n"
                                + "    </body>\n"
                                + "</html>");
                }

            } catch (SQLException ex) {
                out.println("Error: " + ex.getMessage());
            } finally {
                out.close();
            }
            //////////////////////////////////////////////
        } else if (request.getParameter("update") != null) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            // Obtain parameters from the client
            ID = request.getParameter("ID");
            lastName = request.getParameter("lastName");
            firstName = request.getParameter("firstName");
            mi = request.getParameter("mi");
            address = request.getParameter("address");
            city = request.getParameter("city");
            state = request.getParameter("state");
            telephone = request.getParameter("telephone");

            try {
                //Loading the driver
                Class.forName("com.mysql.jdbc.Driver");
                System.out.println("Driver loaded");
                //Start connection
                //"jdbc:mysql://localhost:3307/sdobbs01?autoReconnect=true", "sdobbs01", "Scmpd11809"
                //"jdbc:mysql://localhost:3306/javabook", "scott", "tiger"
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/javabook", "scott", "tiger");
                System.out.println("Database Connected");
                //Statement to delete the tuple before it is updated
                pstmt = conn.prepareStatement("delete from staffinfo where id = " + ID + ";");
                pstmt.executeUpdate();
            } catch (ClassNotFoundException | SQLException ex) {
            }
            //Write the updated information back into the tuple that was deleted
            try {
                init();
                storeStudent(ID, lastName, firstName, mi, address, city, state, telephone);
                out.println(firstName + " " + lastName + " has been updated in the database");

            } catch (SQLException ex) {
                out.println("Error: " + ex.getMessage());
            } finally {
                out.close(); // Close stream
            }
        } else if (request.getParameter("reset") != null) {
            response.sendRedirect("index.html");
        }
    }

    /**
     * Initialize database connection
     */
    private void initializeJdbc() {
        try {
// Load the JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded");
// Establish a connection

            //"jdbc:mysql://localhost:3306/javabook", "scott", "tiger"
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/javabook", "scott", "tiger");
            System.out.println("Database connected");
// Create a Statement
            pstmt = conn.prepareStatement("insert into staffinfo "
                    + "(ID, lastName, firstName, mi, address, city, state, telephone) "
                    + "values (?, ?, ?, ?, ?, ?, ?, ?)");
        } catch (ClassNotFoundException | SQLException ex) {
        }
    }

    /**
     * Store a student record to the database
     */
    private void storeStudent(String ID, String lastName,
            String firstName, String mi, String address, String city,
            String state, String telephone) throws SQLException {
        pstmt.setString(1, ID);
        pstmt.setString(2, lastName);
        pstmt.setString(3, firstName);
        pstmt.setString(4, mi);
        pstmt.setString(5, address);
        pstmt.setString(6, city);
        pstmt.setString(7, state);
        pstmt.setString(8, telephone);
        pstmt.executeUpdate();
    }
}
