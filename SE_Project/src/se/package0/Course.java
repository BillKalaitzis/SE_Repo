package se.package0;

import java.io.Serializable;

public class Course  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String Name;
	private Professor Teacher;
	private Note CourseNotes;
	private Exercise CourseExercises;
	private String AdditionalInfo;
	
	public Course(String Name, Professor Teacher, Note CourseNotes, Exercise CourseExercises, String AdditionalInfo){
		
		this.Name = Name;
		this.Teacher = Teacher;
		this.CourseNotes = CourseNotes;
		this.CourseExercises = CourseExercises;
		this.AdditionalInfo = AdditionalInfo;
		
	}
	
	public boolean equals(Object obj){
		if(obj == null)
			return false;
		if(obj == this)
			return true;
		Course c = (Course) obj;
		if(this.Name.equals(c.getName()))
			return true;
		return false;
	}
	
	public String getAdditionalInfo(){
		return AdditionalInfo;
	}
	
	public Professor getTeacher() {
		return Teacher;
	}

	public String getName() {
		return Name;
	}


	public Note getCourseNotes() {
		
		return CourseNotes;
		
	}
	
	public Exercise getCourseExercises() {
		
		return CourseExercises;
		
	}

}
