package com.leyou.order.service;

import com.leyou.order.mapper.AddressMapper;
import com.leyou.pojo.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    @Autowired
    private AddressMapper addressMapper;

    /**
     * 查询
     *
     * @return
     */
    public List<Address> selectAll() {
        List<Address> addressList = addressMapper.selectAll();
        addressList.forEach(address -> {
            address.setAll(String.join("-",
                    address.getState(), address.getCity(), address.getDistrict(), address.getAddress()));

        });
        return addressList;
    }

    /**
     * 添加地址
     * 分割详细地址,赋值给指定属性
     *
     * @param address
     */
    public void addAddress(Address address) {

        String[] arr = address.getAll().split("-");
        address.setState(arr[0]);
        address.setCity(arr[1]);
        address.setDistrict(arr[2]);
        address.setAddress(arr[3]);

        address.setDefaulte(false);

        addressMapper.insertSelective(address);

    }
    /**
     * 根据指定id返回对象
     *
     * @param id
     * @return
     */
    public Address selectById(Integer id) {

        Address address = addressMapper.selectByPrimaryKey(id);

        address.setAll(String.join("-",
                address.getState(), address.getCity(), address.getDistrict(), address.getAddress()));

        address.setDefaulte(false);

        return address;
    }

    /**
     * 修改地址
     * 分割详细地址,赋值给指定属性
     *
     * @param address
     */
    public void updateAddress(Address address) {

        String[] arr = address.getAll().split("-");
        address.setState(arr[0]);
        address.setCity(arr[1]);
        address.setDistrict(arr[2]);
        address.setAddress(arr[3]);

        address.setDefaulte(false);

        addressMapper.updateByPrimaryKeySelective(address);

    }

    /**
     * 删除地址
     *
     * @param id
     */
    public void deleteAddress(Integer id){
         addressMapper.deleteByPrimaryKey(id);
    }

}
