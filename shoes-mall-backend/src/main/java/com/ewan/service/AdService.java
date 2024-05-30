package com.ewan.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ewan.model.entity.Ad;
import com.ewan.mapper.AdMapper;
@Service
public class AdService extends ServiceImpl<AdMapper, Ad> {

}
