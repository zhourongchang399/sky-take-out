package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

/**
 * @author ：Zc
 * @description：TODO
 * @date ：2025/1/2 20:31
 */
public interface AddressBookService {

    void addAddressBook(AddressBook addressBook);

    List<AddressBook> getAllAddressBook(Long currentId);

    AddressBook getDefaultAddressBook(Long currentId);

    void updateDefaultAddressBook(AddressBook addressBook);

    AddressBook getById(long id);

    void update(AddressBook addressBook);

    void delete(long id);
}
