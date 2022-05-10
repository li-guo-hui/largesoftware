package com.shanjupay.merchant.service;

import com.shanjupay.common.domain.BusinessException;

import java.io.InputStream;

public interface FileService {

    /**
     *  上传文件
     * @param fileName 文件名
     * @return  文件访问路径（绝对的url）
     * @throws BusinessException
     */
    public String upload(String fileName, InputStream inputStream) throws BusinessException;
}
