package com.ceny.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ceny.app.Status;
import com.ceny.app.UserFile;
import com.ceny.app.UserInfoProvider;
import com.ceny.utils.AppInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

/**
 * Created by chensongkui on 2017/3/20.
 */
@RestController
public class FileController {

    private static final Logger LOGGER = LogManager.getLogger(FileController.class);

    @Autowired
    UserInfoProvider userInfoProvider;

    @RequestMapping(value = "/user/file",method = RequestMethod.GET)
    public List<UserFile> getUserFiles(){
        if(userInfoProvider.getRootFile() == null){
            return null;
        }
        return userInfoProvider.getRootFile().getChildren();
    }

    @RequestMapping(value = "/user/folder", method = RequestMethod.POST)
    public Status createFolder(@RequestBody JSONObject jsonObject, Authentication auth){
        String parentPath = jsonObject.getString("parentPath");
        String folderName = jsonObject.getString("folderName");
        StringBuilder pathBuilder = new StringBuilder();
        pathBuilder.append(AppInfo.getInstance().getDiskPath(auth.getName()))
                .append(parentPath)
                .append("/")
                .append(folderName);
        LOGGER.info(pathBuilder);
        File file = new File(pathBuilder.toString());
        if(file.mkdirs()){
            userInfoProvider.addFolder(parentPath,folderName);
            return Status.SUCCESS;
        }
        else{
            return Status.NULL;
        }
        //return Status.SUCCESS;
    }
}
