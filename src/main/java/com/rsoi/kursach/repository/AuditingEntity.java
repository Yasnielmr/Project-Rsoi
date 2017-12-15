package com.rsoi.kursach.repository;

import java.util.Date;
import javax.persistence.*;

@MappedSuperclass
public class AuditingEntity implements BaseEntity{
    
    @Column(name = "create_by")
    private String createdBy;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createdDate;
    
    @Column(name = "update_by")
    private String updateBy;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date")
    private Date updateDate;
    
    @Version
    @Column(name = "row_version")
    private Long rowVersion;

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public Date getCreatedDate() {
        return createdDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }
    
    public Date getUpdateDate() {
        return updateDate;
    }

    public Long getRowVersion() {
        return rowVersion;
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setRowVersion(Long rowVersion) {
        this.rowVersion = rowVersion;
    }

    @Override
    public String getUpdatedBy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setUpdatedBy(String updatedBy) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Date getUpdatedDate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setUpdatedDate(Date updatedDate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
