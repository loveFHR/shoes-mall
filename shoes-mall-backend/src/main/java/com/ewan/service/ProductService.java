package com.ewan.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ewan.model.entity.Product;
import com.ewan.mapper.ProductMapper;
@Service
public class ProductService extends ServiceImpl<ProductMapper, Product> {

}
