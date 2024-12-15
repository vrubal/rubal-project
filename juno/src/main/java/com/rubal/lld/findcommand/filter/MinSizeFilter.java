package com.rubal.lld.findcommand.filter;

import com.rubal.lld.findcommand.File;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class MinSizeFilter extends Filter {

    int minSize;

    public MinSizeFilter(int minSize) {
        this.minSize = minSize;
    }

    @Override
    public boolean apply(File file) {
        return file.size > minSize;
    }
}


