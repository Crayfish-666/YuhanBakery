package com.cyh.yuhanbakery.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("combo_group")
public class ComboGroup {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long comboId;
    private String groupName;
    private Integer chooseCount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDeleted;
}
