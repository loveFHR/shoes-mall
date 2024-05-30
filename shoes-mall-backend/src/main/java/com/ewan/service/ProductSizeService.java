package com.ewan.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ewan.mapper.ProductSizeMapper;
import com.ewan.model.entity.ProductSize;
@Service
public class ProductSizeService extends ServiceImpl<ProductSizeMapper, ProductSize> {

}
