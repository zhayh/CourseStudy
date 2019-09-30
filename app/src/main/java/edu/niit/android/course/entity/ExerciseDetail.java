package edu.niit.android.course.entity;

import java.io.Serializable;

/**
 * 每章的每道习题的信息
 */
public class ExerciseDetail implements Serializable {
    private int exercise_id;// 练习Id， 暂时没使用
    private int subjectId;  // 习题id
    private String subject; // 题干
    private String a;       // 选项A
    private String b;       // 选项B
    private String c;       // 选项C
    private String d;       // 选项D
    private int answer;     // 正确答案
    /**
     * 0：选择正确，
     * 1：选中的A选项是错的
     * 2：选中的B选项是错的
     * 3：选中的C选项是错的
     * 4：选中的D选项是错的
     */
    private int select;
    public int getExercise_id() {
        return exercise_id;
    }

    public void setExercise_id(int exercise_id) {
        this.exercise_id = exercise_id;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }
}
