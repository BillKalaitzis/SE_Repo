package se.package0;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JScrollPane;

import org.apache.commons.io.FileUtils;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;


public class CoursesFrame extends JFrame{

	private JPanel contentPane;
	private JLabel AvailableActions;
	private JList CoursesJList;
	private JList AAList;
	private JButton RegisterButton;
	private JButton CAButton;
	private JList RCList;
	private Student s;
	private CoursesFrame cf = this;

	public CoursesFrame(Student st) {
		setVisible(true);
		setResizable(false);
		setTitle("Makedonia IS - Courses");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 416, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel RegisteredCourses = new JLabel("Registered Courses");
		RegisteredCourses.setBounds(29, 27, 123, 14);
		contentPane.add(RegisteredCourses);
		
		ArrayList<Course> courses = st.getRegisteredCourses();
		DefaultListModel model = new DefaultListModel();
		for(Course c:courses)
			model.addElement(c.getName());
		
		CoursesJList = new JList(model);
		JScrollPane sp = new JScrollPane(CoursesJList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sp.setBounds(27, 51, 136, 157);
		contentPane.add(sp);
		
		AvailableActions = new JLabel("Available Actions");
		AvailableActions.setBounds(265, 27, 116, 14);
		contentPane.add(AvailableActions);
		
		DefaultListModel model2 = new DefaultListModel();
		model2.addElement("Download Notes");
		model2.addElement("Download Exercises");
		model2.addElement("Course Info");
		model2.addElement("Contact Professor");
		model2.addElement("Unregister From Course");
		
		AAList = new JList(model2);
		JScrollPane sp2 = new JScrollPane(AAList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sp2.setBounds(245, 51, 146, 157);
		contentPane.add(sp2);
		
		RegisterButton = new JButton("Register for course");
		RegisterButton.setFont(new Font("Tahoma", Font.PLAIN, 10));
		RegisterButton.setBounds(15, 232, 143, 23);
		contentPane.add(RegisterButton);
		
		CAButton = new JButton("Complete Action");
		CAButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		CAButton.setBounds(250, 232, 126, 23);
		contentPane.add(CAButton);
		
		
		s = st;
		
		ButtonListener b = new ButtonListener();
		CAButton.addActionListener(b);
		RegisterButton.addActionListener(b);
	}
	
	class ButtonListener implements ActionListener {

		String courseName;
		String action;
		Course currCourse;
		
		public void actionPerformed(ActionEvent e) {
			
			File dest ;
			File source;
			
			if(e.getSource() == CAButton){
				courseName = (String) CoursesJList.getSelectedValue(); 
				action = (String) AAList.getSelectedValue();
				if(!(courseName == null) && !(action == null)){
					currCourse = identifyCourse(courseName);
					
					if (action.equals("Download Notes") || action.equals("Download Exercises")) {
						if(action.equals("Download Notes") && currCourse.getCourseNotes() == null)
							new MsgFrame("Not found","There are not available notes for the selected course.");
						else if(action.equals("Download Exercises") && currCourse.getCourseExercises() == null)
							new MsgFrame("Not Found","There are not available notes for the selected course.");
						else{
							
							JFileChooser fileChooser = new JFileChooser();
							fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
							fileChooser.showSaveDialog(null);
							File  f = fileChooser.getSelectedFile();
							if(action.equals("Download Notes")){
								if(currCourse.getCourseNotes() == null){
									new MsgFrame("Not found","There are not available notes for the selected course.");
								}
								else{
									
									if(f!=null){
										source = new File(currCourse.getCourseNotes().getAbsPath());
										dest = new File(f.getAbsolutePath()+"\\"+courseName.trim()+" Notes.txt");
										try {
											FileUtils.copyFile(source, dest);
										} catch (IOException e1) {
											e1.printStackTrace();
										}
										
									}
								}
							}
							else{
								if(currCourse.getCourseExercises() == null)
									new MsgFrame("Not Found","There are not available notes for the selected course.");
								else{
									if(f!=null){
										dest = new File(f.getAbsolutePath()+courseName+" Exercises.txt");
										source =  new File(currCourse.getCourseExercises().getAbsPath());
										try {
											FileUtils.copyFile(source, dest);
										} catch (IOException e1) {
											e1.printStackTrace();
										}
									}
								}
							}
						}
					}
					else if(action.equals("Contact Professor")){
						new ContactProfessorFrame(s,currCourse);
					}
					else if(action.equals("Unregister From Course")){
						unregisterFromCourse(s,currCourse);
					}
					else if(action.equals("Course Info")){
						new MsgFrame("Course Info",currCourse.getAdditionalInfo());
						
					}
				}
				else
					new MsgFrame("Warning","Please select both a course and an action.");
			}
			else 
				new RegisterForCourseFrame(s,cf);
			
		}
		
	}
	
	public Course identifyCourse(String name){
		
		ArrayList<Course> course = FileOperations.readCourses();
		for(Course c:course){
			if(c.getName().equals(name)){
				return c;
			}
		}
		return null;
	}
	
	public void unregisterFromCourse(Student st, Course c){
		
		ArrayList<Course> courses;
		ArrayList<Student> students = FileOperations.readStudents();
		for(Student stud:students){
			if(stud.getEmail().equals(st.getEmail())){
				courses = stud.getRegisteredCourses();
				courses.remove(c);
				stud.setRegisteredCourses(courses);
			}
		}
		FileOperations.writeStudents(students);
		dispose();
		ArrayList<Student> s1 = FileOperations.readStudents();
		for(Student s:s1){
			if(s.equals(st))
				new CoursesFrame(s);
		}
	}
}
