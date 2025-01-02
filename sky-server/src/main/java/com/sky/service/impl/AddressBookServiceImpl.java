package com.sky.service.impl;

import com.sky.annotation.AutoFill;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * @author ：Zc
 * @description：TODO
 * @date ：2025/1/2 20:31
 */
@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    AddressBookMapper addressBookMapper;

    @Override
    public void addAddressBook(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(StatusConstant.DISABLE);
        addressBookMapper.insert(addressBook);
    }

    @Override
    public List<AddressBook> getAllAddressBook(Long currentId) {
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(currentId);
        List<AddressBook> addressBookList = addressBookMapper.list(addressBook);
        return addressBookList;
    }

    @Override
    public AddressBook getDefaultAddressBook(Long currentId) {
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(currentId);
        addressBook.setIsDefault(StatusConstant.ENABLE);
        List<AddressBook> addressBookList = addressBookMapper.list(addressBook);
        return addressBookList.get(0);
    }

    @Override
    @Transactional
    public void updateDefaultAddressBook(AddressBook addressBook) {
        // 获取当前用户默认地址信息
        addressBook.setIsDefault(StatusConstant.ENABLE);
        List<AddressBook> addressBookList = addressBookMapper
                .list(AddressBook.builder()
                        .isDefault(StatusConstant.ENABLE)
                        .userId(addressBook.getUserId())
                        .build());
        // 更新默认地址信息
        for (AddressBook book : addressBookList) {
            if (book.getId() != addressBook.getId()) {
                book.setIsDefault(StatusConstant.DISABLE);
                addressBookMapper.update(book);
            }
        }
        addressBookMapper.update(addressBook);
    }

    @Override
    public AddressBook getById(long id) {
        AddressBook addressBook = new AddressBook();
        addressBook.setId(id);
        return addressBookMapper.list(addressBook).get(0);
    }

    @Override
    public void update(AddressBook addressBook) {
        addressBookMapper.update(addressBook);
    }

    @Override
    public void delete(long id) {
        AddressBook addressBook = new AddressBook();
        addressBook.setId(id);
        addressBookMapper.delete(addressBook);
    }

}
