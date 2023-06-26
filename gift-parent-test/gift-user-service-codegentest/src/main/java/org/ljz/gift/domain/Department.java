package org.ljz.gift.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ljz
 * @since 2023-06-20
 */
@TableName("t_department")
public class Department extends Model<Department> {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Department{" +
        ", id=" + id +
        ", name=" + name +
        "}";
    }
}
