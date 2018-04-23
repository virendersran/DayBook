package xpresswebsolutionz.com.daybook.Bean;

import java.io.Serializable;

public class BeanDayReport implements Serializable {

    String date,name,dept,purpose,payType,payCaT,frequency,status;
    double amount;

    public BeanDayReport() {
    }

    public BeanDayReport(String date, String name, String dept, String purpose, String payType, String payCaT, String frequency, String status, double amount) {
        this.date = date;
        this.name = name;
        this.dept = dept;
        this.purpose = purpose;
        this.payType = payType;
        this.payCaT = payCaT;
        this.frequency = frequency;
        this.status = status;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayCaT() {
        return payCaT;
    }

    public void setPayCaT(String payCaT) {
        this.payCaT = payCaT;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "BeanDayReport{" +
                "date='" + date + '\'' +
                ", name='" + name + '\'' +
                ", dept='" + dept + '\'' +
                ", purpose='" + purpose + '\'' +
                ", payType='" + payType + '\'' +
                ", payCaT='" + payCaT + '\'' +
                ", frequency='" + frequency + '\'' +
                ", status='" + status + '\'' +
                ", amount=" + amount +
                '}';
    }
}
