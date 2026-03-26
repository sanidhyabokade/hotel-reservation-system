import java.sql.*;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_reservations";
    private static final String username = "root";
    private static final String password = "Bokade@2002";


    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url,username,password);
            while (true){
                System.out.println();
                System.out.println("HOTEL RESERVATION SYSTEM");
                Scanner sc = new Scanner(System.in);
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservation");
                System.out.println("5. Delete Reservation");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");
                int choice = sc.nextInt();
                switch (choice){
                    case 1:
                        reserveRoom(connection,sc);
                        break;
                    case 2:
                        viewReservations(connection);
                        break;
                    case 3:
                        getRoomNumber(connection, sc);
                        break;
                    case 4:
                        updateReservation(connection, sc);
                        break;
                    case 5:
                        deleteReservation(connection,sc);
                        break;
                    case 0:
                        exit();
                        sc.close();
                        return;
                    default:
                        System.out.println("Enter a valid option!!!");

                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    private static void reserveRoom(Connection connection, Scanner sc){
        System.out.print("Enter guest name: ");
        String guestName = sc.next();
        sc.nextLine();
        System.out.print("Enter room number: ");
        int roomNum = sc.nextInt();
        System.out.print("Enter guest contact number: ");
        String contactNumber = sc.next();

        String query = "INSERT INTO reservations (guest_name, room_number, contact_number)"+"VALUES('"+guestName+"',"+roomNum+",'"+contactNumber+"')";
        try {
            Statement statement=connection.createStatement();
            int affectedRows = statement.executeUpdate(query);
            if(affectedRows>0){
                System.out.println("Reservation successfull!");
            }else{
                System.out.println("Reservation unsuccessfull!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void viewReservations(Connection connection){
        String query = "SELECT * FROM reservations";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            System.out.println("***********************************************************************************************");
            System.out.println("**                                     RESERVATIONS                                          **");
            System.out.println("***********************************************************************************************");
            System.out.println("** Reservation ID | Guest Name      | Room Number | Contact Number     | Reservation Date    **");
            System.out.println("***********************************************************************************************");

            while(resultSet.next()){
                int reservationId = resultSet.getInt("reservation_id");
                String guestName = resultSet.getString("guest_name");
                int roomNum = resultSet.getInt("room_number");
                String contactNum = resultSet.getString("contact_number");
                String reservationDate = resultSet.getString("reservation_date");

                System.out.printf("** %-14d | %-15s | %-11d | %-18s | %-19s **\n",
                        reservationId, guestName, roomNum, contactNum, reservationDate);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("***********************************************************************************************");
    }

    private static void getRoomNumber(Connection connection, Scanner sc){
        System.out.print("Enter Reservation ID: ");
        int reservationId = sc.nextInt();
        System.out.print("Enter Guest Name: ");
        String guestName = sc.next();

        String query = "SELECT room_number FROM reservations WHERE guest_name='"+guestName+"' and reservation_id="+reservationId;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if(resultSet.next()){
                int roomNumber = resultSet.getInt("room_number");
                System.out.println("Room Number for Reservation ID "+reservationId+" is "+roomNumber);
            }else{
                System.out.println("Reservation not found for the given reservation ID!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void updateReservation(Connection connection, Scanner sc){
        System.out.print("Enter Reservation ID :");
        int reservationId = sc.nextInt();
        sc.nextLine();
        try {
            if(!existReservation(connection,reservationId)){
                System.out.println("Reservation not found for the given ID!");
            }else{
                System.out.print("Enter New Guest Name: ");
                String guestName = sc.nextLine();
                System.out.print("Enter New Contact Number: ");
                String contactNumber = sc.next();

                String query = "UPDATE reservation SET guest_name='"+guestName+"',contact_number = '"+contactNumber+"'";
                Statement statement = connection.createStatement();
                int affectedRows = statement.executeUpdate(query);
                if(affectedRows>0){
                    System.out.println("Reservation updated successfully!");
                }else{
                    System.out.println("Reservation update failed!");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    private static boolean existReservation(Connection connection, int reservationId) throws RuntimeException, SQLException {
        String query = "SELECT reservation_id FROM reservations WHERE reservation_id = "+reservationId;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            return resultSet.next();
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static void deleteReservation(Connection connection, Scanner sc){
        System.out.print("Enter Reservation ID: ");
        int reservationId = sc.nextInt();

        try {
            if(!existReservation(connection,reservationId)){
                System.out.println("Reservation not found for the given ID!");
                return;
            }

            String  query = "DELETE FROM reservations WHERE reservation_id = "+ reservationId;

            Statement statement = connection.createStatement();
            int affectedRows = statement.executeUpdate(query);
            if (affectedRows > 0) {
                System.out.println("Reservation deleted successfully!");
            } else {
                System.out.println("Reservation deletion failed.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void exit() throws InterruptedException {
        System.out.print("Exiting System");
        int i = 5;
        while(i!=0){
            System.out.print(".");
            Thread.sleep(1000);
            i--;
        }
        System.out.println();
        System.out.println("ThankYou For Using Hotel Reservation System!!!");
    }
}

