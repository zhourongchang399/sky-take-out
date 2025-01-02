package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ：Zc
 * @description：TODO
 * @date ：2025/1/2 20:28
 */
@RestController
@RequestMapping("/user/addressBook")
@Api(tags = "地址相关接口")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @PostMapping
    @ApiOperation("新增地址")
    public Result addAddressBook(@RequestBody AddressBook addressBook) {
        log.info("新增地址：{}", addressBook);
        addressBookService.addAddressBook(addressBook);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("查询当前登录用户的所有地址信息")
    public Result<List<AddressBook>> listAddressBook() {
        log.info("查询当前登录用户的所有地址信息");
        List<AddressBook> addressBookList = addressBookService.getAllAddressBook(BaseContext.getCurrentId());
        return Result.success(addressBookList);
    }

    @GetMapping("/default")
    @ApiOperation("查询默认地址")
    public Result<AddressBook> defaultAddressBook() {
        log.info("查询当前登录用户的所有地址信息");
        AddressBook addressBook = addressBookService.getDefaultAddressBook(BaseContext.getCurrentId());
        return Result.success(addressBook);
    }

    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result updateDefaultAddressBook(@RequestBody AddressBook addressBook) {
        log.info("设置默认地址:{}",addressBook);
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookService.updateDefaultAddressBook(addressBook);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址")
    public Result<AddressBook> getAddressBook(@PathVariable Integer id) {
        log.info("根据id查询地址:{}", id);
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    @PutMapping
    @ApiOperation("根据id修改地址")
    public Result updateAddressBook(@RequestBody AddressBook addressBook) {
        log.info("根据id修改地址:{}", addressBook);
        addressBookService.update(addressBook);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("根据id删除地址")
    public Result deleteAddressBook(long id) {
        log.info("根据id删除地址:{}", id);
        addressBookService.delete(id);
        return Result.success();
    }

}
