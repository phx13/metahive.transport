package com.metahive.transport.server.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author phx
 * @since 2021-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Coach对象", description = "")
public class Coach implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "车厢编号")
    @TableId(value = "id", type = IdType.NONE)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "司机")
    private String username;

    @ApiModelProperty(value = "车厢型号")
    private String type;

    @ApiModelProperty(value = "图片路径")
    private String imagePath;

    @ApiModelProperty(value = "视频路径")
    private String videoPath;

    @ApiModelProperty(value = "来自")
    private String fromName;

    @ApiModelProperty(value = "去往")
    private String toName;

    @ApiModelProperty(value = "起始坐标")
    private String fromCoord;

    @ApiModelProperty(value = "结束坐标")
    private String toCoord;

    @ApiModelProperty(value = "当前坐标")
    private String currentCoord;

    @ApiModelProperty(value = "油量")
    private Double oil;

    @ApiModelProperty(value = "压力")
    private Double pressure;

    @ApiModelProperty(value = "温度")
    private Double temperature;

    @ApiModelProperty(value = "湿度")
    private Double humidity;

    @ApiModelProperty(value = "软删除")
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "版本号")
    @Version
    private Integer version;


}
