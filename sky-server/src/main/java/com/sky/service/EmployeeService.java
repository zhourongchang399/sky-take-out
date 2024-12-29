package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.result.Result;

public interface EmployeeService {

    /**
     * 新增员工
     * @param employeeDTO
     * @return
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return PageResult
     */
    PageResult list(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 根据ID更新员工启用或停用状态
     * @param status, id
     * @return
     */
    void stopOrOpen(Integer status, long id);

    Employee getById(long id);

    void update(EmployeeDTO employeeDTO);
}
