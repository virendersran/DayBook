package xpresswebsolutionz.com.daybook;

import java.io.Serializable;

public class BeanDepartment implements Serializable {

    int id;
    String deptName;

    public BeanDepartment(int id, String deptName) {
        this.id = id;
        this.deptName = deptName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
