package com.mapbook.batch.libraryCatalogBatch.util.download;

import java.nio.file.Path;

public interface DownLoader {

    Path downLoad(String outPutDirectory, String targetDate,boolean option,int limit);
}