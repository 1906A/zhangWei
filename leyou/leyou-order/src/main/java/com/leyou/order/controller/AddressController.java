package com.leyou.order.controller;

import com.leyou.order.service.AddressService;
import com.leyou.pojo.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AddressController {
    @Autowired
    AddressService addressService;

    @GetMapping("selectAll")
    public List<Address> selectAll() {
        return addressService.selectAll();
    }

    @PostMapping("addAddress")
    public void addAddress(@RequestBody Address address) {
        /*{
	"name":"张三",
	"phone":"15139233408",
	"all":"江苏-无锡-锡山-二泉路",
	"zipcode":"453000",
	"defaulte":"false"
}*/
        addressService.addAddress(address);
    }

    @PostMapping("updateAddress")
    public void updateAddress(@RequestBody Address address) {
        addressService.updateAddress(address);
    }

    @GetMapping("selectById")
    public Address selectById(@RequestParam("id")Integer id){
        return addressService.selectById(id);
    }

    @GetMapping("delete")
    public void delete(@RequestParam("id")Integer id){
        addressService.deleteAddress(id);
    }
}
