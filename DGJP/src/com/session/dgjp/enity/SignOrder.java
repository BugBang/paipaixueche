package com.session.dgjp.enity;

import java.util.List;

/**
 * Created by user on 2016-12-14.
 */
public class SignOrder {

    private String orderId;
    private List<CostListBean> costList;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<CostListBean> getCostList() {
        return costList;
    }

    public void setCostList(List<CostListBean> costList) {
        this.costList = costList;
    }

    public static class CostListBean {
        private int id;
        private Double subjectOne;
        private Double subjectTwo;
        private Double subjectThree;
        private Double subjectFour;//科目四
        private Double documentsFee;//证件工本费

        private Double managementFee;//场地管理费（东莞财政）
        private Double commissionFee;//学习资料及代办手续费（代办机构）

        private Double examinationFee;//体检费
        private Double residencePermit;//居住证
        private Double oneSize;//驾驶证1寸数码照30元
        private Double digitalPhotosFee;//居住证数码照

        private Double trainingFee;//培训费
        private Double transferFee;//接送费
        private Double resit;//补考费
        private String type;//驾考类别

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Double getSubjectOne() {
            return subjectOne;
        }

        public void setSubjectOne(Double subjectOne) {
            this.subjectOne = subjectOne;
        }

        public Double getSubjectTwo() {
            return subjectTwo;
        }

        public void setSubjectTwo(Double subjectTwo) {
            this.subjectTwo = subjectTwo;
        }

        public Double getSubjectThree() {
            return subjectThree;
        }

        public void setSubjectThree(Double subjectThree) {
            this.subjectThree = subjectThree;
        }

        public Double getSubjectFour() {
            return subjectFour;
        }

        public void setSubjectFour(Double subjectFour) {
            this.subjectFour = subjectFour;
        }

        public Double getDocumentsFee() {
            return documentsFee;
        }

        public void setDocumentsFee(Double documentsFee) {
            this.documentsFee = documentsFee;
        }

        public Double getManagementFee() {
            return managementFee;
        }

        public void setManagementFee(Double managementFee) {
            this.managementFee = managementFee;
        }

        public Double getCommissionFee() {
            return commissionFee;
        }

        public void setCommissionFee(Double commissionFee) {
            this.commissionFee = commissionFee;
        }

        public Double getExaminationFee() {
            return examinationFee;
        }

        public void setExaminationFee(Double examinationFee) {
            this.examinationFee = examinationFee;
        }

        public Double getResidencePermit() {
            return residencePermit;
        }

        public void setResidencePermit(Double residencePermit) {
            this.residencePermit = residencePermit;
        }

        public Double getOneSize() {
            return oneSize;
        }

        public void setOneSize(Double oneSize) {
            this.oneSize = oneSize;
        }

        public Double getDigitalPhotosFee() {
            return digitalPhotosFee;
        }

        public void setDigitalPhotosFee(Double digitalPhotosFee) {
            this.digitalPhotosFee = digitalPhotosFee;
        }

        public Double getTrainingFee() {
            return trainingFee;
        }

        public void setTrainingFee(Double trainingFee) {
            this.trainingFee = trainingFee;
        }

        public Double getTransferFee() {
            return transferFee;
        }

        public void setTransferFee(Double transferFee) {
            this.transferFee = transferFee;
        }

        public Double getResit() {
            return resit;
        }

        public void setResit(Double resit) {
            this.resit = resit;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

}
