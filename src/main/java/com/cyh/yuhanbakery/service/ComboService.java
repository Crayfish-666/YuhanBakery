package com.cyh.yuhanbakery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cyh.yuhanbakery.dto.ComboSaveDTO;
import com.cyh.yuhanbakery.entity.Combo;

public interface ComboService extends IService<Combo> {
    void saveComboWithGroups(ComboSaveDTO dto);
    void deleteCombo(Long id);
}
