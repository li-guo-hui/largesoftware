package com.shanjupay.merchant.service;


import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;

import com.shanjupay.common.util.TencentUtils;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;
import java.sql.BatchUpdateException;

@org.springframework.stereotype.Service  //实例为一个bean
@Slf4j
public class FileServiceImpl implements FileService{

    private String url = "https://software-structure-1304198901.cos.ap-shanghai.myqcloud.com/";
    @Override
    public String upload(String fileName, InputStream inputStream) throws BusinessException {

        try {
            TencentUtils.Upload(fileName,inputStream);
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
            throw new BusinessException(CommonErrorCode.E_100106);
        }
        return url+fileName;
    }
}
