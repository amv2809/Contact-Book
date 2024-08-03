import java.awt.*;
import javax.swing.*;
import java.sql.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


class App extends JFrame
{
JTextField tf1 = new JTextField(15);
JTextField tf2 = new JTextField(15);
JTextField tf3 = new JTextField(15);
JTextField tf4 = new JTextField(15);
JLabel label = new JLabel(new ImageIcon("pic1.png"));
JLabel lb1 = new JLabel("Enter Name : ");
JLabel lb2 = new JLabel("Enter Mobile_No : ");
JLabel lb3 = new JLabel("Enter Address : ");
JLabel lb4 = new JLabel("Enter Email_ID : ");
JButton ins = new JButton("Add Record");
JButton del = new JButton("Delete Record");
JButton res = new JButton("Reset");
JButton search = new JButton("Search Record");
JTextArea ta = new JTextArea(); //use to display records
Connection conn = null;
PreparedStatement ps = null;
Statement stmt= null;
App()
{
setSize(750,700);
setTitle("My Contacts");
getContentPane().setBackground(Color.black);
//design GUI for database
JTabbedPane tp = new JTabbedPane();
add(tp);
JPanel p1 = new JPanel();
p1.setLayout(new BorderLayout());
JPanel center = new JPanel();
center.setBackground(Color.ORANGE);
center.setLayout(null);
lb1.setFont(new Font("Arial",Font.PLAIN,15));
lb2.setFont(new Font("Arial",Font.PLAIN,15));
lb3.setFont(new Font("Arial",Font.PLAIN,15));
lb4.setFont(new Font("Arial",Font.PLAIN,15));
label.setBounds(200,10,255,255);
lb1.setBounds(130,300,350,40);
tf1.setBounds(300,300,200,40);
lb2.setBounds(130,360,350,40);
tf2.setBounds(300,360,200,40);
lb3.setBounds(130,420,350,40);
tf3.setBounds(300,420,200,40);
lb4.setBounds(130,480,350,40);
tf4.setBounds(300,480,200,40);
center.add(label);
center.add(lb1);
center.add(tf1);
center.add(lb2);
center.add(tf2);
center.add(lb3);
center.add(tf3);
center.add(lb4);
center.add(tf4);
p1.add(center,"Center");
JPanel south = new JPanel();
south.add(ins);
south.add(del);
south.add(search);
south.add(res);
p1.add(south,"South");
tp.addTab("Contacts",p1);
JPanel p3 = new JPanel();
p3.setLayout(new BorderLayout(100,100));
JScrollPane sp = new JScrollPane(ta);
p3.add(sp);
ta.setEditable(false);
ta.setFont(new Font("Segoe UI",Font.PLAIN,20));
tp.addTab("Show Contacts",p3);
//establish connection with database
try{
Driver d= Class.forName("com.mysql.cj.jdbc.Driver").newInstance();//new com.mysql.cj.jdbc.Driver();
DriverManager.registerDriver(d);
System.out.println("Driver is registered");
conn =DriverManager.getConnection("jdbc:mysql://localhost:3306/contacts","abc","89abc!63");
}
catch(Exception e)
{
System.out.println(e);
}
//display initial records in TextArea
display_records();
//Add record into table whenever button is clicked
ins.addActionListener(new ActionListener()
{
public void actionPerformed(ActionEvent e)
{
try
{
if(tf1.getText().trim().isEmpty() ||tf2.getText().trim().isEmpty()||tf3.getText().trim().isEmpty()||tf4.getText().trim().isEmpty())
{
JOptionPane.showMessageDialog(null,"Feilds are empty !");
}
String pno = tf2.getText().trim();
int len = pno.length();
System.out.println(len);
if(len<10 || len>10)
{
JOptionPane.showMessageDialog(null,"Number must be 10 digits ! ");
}
String vemail = tf4.getText();
if(!vemail.contains("@gmail.com"))
{
JOptionPane.showMessageDialog(null,"Enter valid Email ! ");
}
ps = conn.prepareStatement("Insert into contacts_deatils values(?,?,?,?)");
ps.setString(1,tf1.getText());
ps.setInt(2,Integer.parseInt(tf2.getText()));
ps.setString(3,tf3.getText());
ps.setString(4,tf4.getText());
int x = ps.executeUpdate();
if(x>=1)
JOptionPane.showMessageDialog(null,"Contact is Added Successfully ! ");
display_records();
tf1.setText("");
tf2.setText("");
tf3.setText("");
tf4.setText("");
}
catch(Exception ex)
{
System.out.println(ex);
}
}
});
// to reset data
res.addActionListener(new ActionListener()
{
public void actionPerformed(ActionEvent e)
{
tf1.setText("");
tf2.setText("");
tf3.setText("");
tf4.setText("");
}
});
//delete record from the table whenever button is clicked
del.addActionListener(new ActionListener()
{
public void actionPerformed(ActionEvent e)
{
String str = JOptionPane.showInputDialog("Enter the name wish to delete : ");
try
{
ps = conn.prepareStatement("delete from contacts_deatils where Name=?");
ps.setString(1,str);
int x = ps.executeUpdate();
if(x>=1)
JOptionPane.showMessageDialog(null,"Contact is Deleted Successfully !");
display_records();
}
catch(Exception ex)
{
System.out.println(ex);
}
}
});
//To search record into table whenever button is clicked
search.addActionListener(new ActionListener()
{
public void actionPerformed(ActionEvent e)
{
String s = JOptionPane.showInputDialog("Enter the name wish to search : ");
try
{
ps = conn.prepareStatement("Select * from contacts_deatils where Name=?");
ps.setString(1,s);
ResultSet rs = ps.executeQuery();
if(rs.next())
{
JOptionPane.showMessageDialog(null,"Name : "+rs.getString(1)+'\n'+ "Mobile_No : "+rs.getInt(2)+'\n'+"Address : "+rs.getString(3)+'\n'+ "Email_ID : "+rs.getString(4));
}
else{
JOptionPane.showMessageDialog(null,"Contact is not found ! ");
}
display_records();
tf1.setText("");
tf2.setText("");
tf3.setText("");
tf4.setText("");
}
catch(Exception ex)
{
System.out.println(ex);
}
}
});
} // end of constructor
// following function display records in TextArea
void display_records()
{
ta.setText("");
try{
stmt = conn.createStatement();
ResultSet rs=stmt.executeQuery("select * from contacts_deatils");
ta.append("NAME\tMobile_No.\tAddress\tEmail_ID\n\r");
ta.append("---------------------------------------------------------------------------------------------------------------\n\r");
while(rs.next())
ta.append(rs.getString(1)+"\t"+rs.getInt(2)+"\t"+rs.getString(3)+"\t"+rs.getString(4)+"\n\r");
ta.append("--------------------------------------------------------------------------------------------------------------\n\r");
}
catch(Exception e)
{
System.out.println(e);
}
}
public static void main(String [] args)
{
App f = new App();
f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
f.setVisible(true);
}
}