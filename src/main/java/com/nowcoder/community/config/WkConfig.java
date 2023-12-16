package com.nowcoder.community.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * @Desc:描述
 * @Author：yyf
 * @Filename：WkConfig
 * @Date：2023/12/14 20:45
 */
@Configuration
public class WkConfig {

    private static final Logger logger = LoggerFactory.getLogger(WkConfig.class);

    @Value("${wk.image.command}")
    private String wkImagesStorage;

    @PostConstruct
    public void init(){
        File file = new File(wkImagesStorage);
        if (!file.exists()){
            file.mkdir();
            logger.info("创建wk图片目录："+wkImagesStorage);
        }

    }
}
