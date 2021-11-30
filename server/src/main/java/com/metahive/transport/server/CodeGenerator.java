package com.metahive.transport.server;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;

// 演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
public class CodeGenerator {

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir") + "\\server"; // 获取当前项目路径
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("phx");
        gc.setOpen(false); // 生成后是否打开文件资源管理器
        gc.setFileOverride(false); // 重新生成文件的时候是否会覆盖
        gc.setIdType(IdType.NONE); // 主键策略
        gc.setServiceName("%sService"); // 所有自动生成的Service接口去掉首字母I
        gc.setDateType(DateType.ONLY_DATE); // 设置日期类型
        gc.setSwagger2(true); // 实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/metahive.transport?serverTimezone=GMT%2B8&characterEncoding=utf-8&rewriteBatchedStatements=true");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("server");
        pc.setParent("com.metahive.transport");
        pc.setEntity("entity");
        pc.setController("controller");
        pc.setMapper("mapper");
        pc.setService("service");
        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude("coach"); // 映射的表名
        strategy.setNaming(NamingStrategy.underline_to_camel); // 数据映射到实体类的策略
        strategy.setEntityLombokModel(true); // lombox注解

        /* 逻辑删除 */
        strategy.setLogicDeleteFieldName("is_deleted"); // 逻辑删除字段名
        strategy.setEntityBooleanColumnRemoveIsPrefix(true); // 去除bool值的is前缀

        /* 自动填充 */
        TableFill createTime = new TableFill("create_time", FieldFill.INSERT);
        TableFill updateTime = new TableFill("update_time", FieldFill.INSERT_UPDATE);
        ArrayList<TableFill> tableFills = new ArrayList<>();
        tableFills.add(createTime);
        tableFills.add(updateTime);
        strategy.setTableFillList(tableFills);

        strategy.setVersionFieldName("version"); // 乐观锁列名
        strategy.setRestControllerStyle(true); // restful风格api
        strategy.setControllerMappingHyphenStyle(true); // camel命名转化为_

        mpg.setStrategy(strategy);
        mpg.execute();
    }

}
