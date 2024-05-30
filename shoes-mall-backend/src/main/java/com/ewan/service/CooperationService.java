package com.ewan.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ewan.mapper.CooperationMapper;
import com.ewan.model.entity.Cooperation;
@Service
public class CooperationService extends ServiceImpl<CooperationMapper, Cooperation> {

}
