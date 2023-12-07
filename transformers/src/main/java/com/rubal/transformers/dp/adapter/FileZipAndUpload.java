package com.rubal.transformers.dp.adapter;

import java.io.File;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class FileZipAndUpload implements CloudFileUpload, FileZipper{
    @Override
    public void uploadFile() {

    }

    @Override
    public File compressFile(File file) {
        return null;
    }
}
