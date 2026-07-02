package com.cyh.yuhanbakery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyh.yuhanbakery.dto.ComboGroupDTO;
import com.cyh.yuhanbakery.dto.ComboGroupItemDTO;
import com.cyh.yuhanbakery.dto.ComboSaveDTO;
import com.cyh.yuhanbakery.entity.Combo;
import com.cyh.yuhanbakery.entity.ComboGroup;
import com.cyh.yuhanbakery.entity.ComboGroupItem;
import com.cyh.yuhanbakery.mapper.ComboGroupItemMapper;
import com.cyh.yuhanbakery.mapper.ComboGroupMapper;
import com.cyh.yuhanbakery.mapper.ComboMapper;
import com.cyh.yuhanbakery.service.ComboService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComboServiceImpl extends ServiceImpl<ComboMapper, Combo> implements ComboService {

    private final ComboMapper comboMapper;
    private final ComboGroupMapper comboGroupMapper;
    private final ComboGroupItemMapper comboGroupItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveComboWithGroups(ComboSaveDTO dto) {
        Combo combo = new Combo();
        combo.setId(dto.getId());
        combo.setName(dto.getName());
        combo.setDescription(dto.getDescription());
        combo.setImage(dto.getImage());
        combo.setPrice(dto.getPrice());
        combo.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);

        // 1. 保存或更新套餐主表
        if (combo.getId() == null) {
            comboMapper.insert(combo);
        } else {
            comboMapper.updateById(combo);
            // 2. 清理原有子表配置数据
            List<ComboGroup> oldGroups = comboGroupMapper.selectList(new LambdaQueryWrapper<ComboGroup>()
                    .eq(ComboGroup::getComboId, combo.getId()));
            for (ComboGroup oldGroup : oldGroups) {
                comboGroupItemMapper.delete(new LambdaQueryWrapper<ComboGroupItem>()
                        .eq(ComboGroupItem::getGroupId, oldGroup.getId()));
            }
            comboGroupMapper.delete(new LambdaQueryWrapper<ComboGroup>()
                    .eq(ComboGroup::getComboId, combo.getId()));
        }

        Long comboId = combo.getId();

        // 3. 重新插入最新的级联数据
        for (ComboGroupDTO groupDto : dto.getGroups()) {
            ComboGroup group = new ComboGroup();
            group.setComboId(comboId);
            group.setGroupName(groupDto.getGroupName());
            group.setChooseCount(groupDto.getChooseCount());
            comboGroupMapper.insert(group);

            Long groupId = group.getId();
            for (ComboGroupItemDTO itemDto : groupDto.getItems()) {
                ComboGroupItem item = new ComboGroupItem();
                item.setGroupId(groupId);
                item.setSkuId(itemDto.getSkuId());
                item.setExtraPrice(itemDto.getExtraPrice());
                comboGroupItemMapper.insert(item);
            }
        }
        log.info("级联保存套餐及配置分组成功，套餐ID={}", comboId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCombo(Long id) {
        // 1. 逻辑删除套餐主表
        comboMapper.deleteById(id);

        // 2. 级联逻辑删除分组表及关联项表
        List<ComboGroup> groups = comboGroupMapper.selectList(new LambdaQueryWrapper<ComboGroup>()
                .eq(ComboGroup::getComboId, id));

        for (ComboGroup group : groups) {
            comboGroupItemMapper.delete(new LambdaQueryWrapper<ComboGroupItem>()
                    .eq(ComboGroupItem::getGroupId, group.getId()));
            comboGroupMapper.deleteById(group.getId());
        }
        log.info("级联逻辑删除套餐及分组成功，套餐ID={}", id);
    }
}
