package com.rubal.lld.findcommand.filter;

import com.rubal.lld.findcommand.File;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class TypeFilter extends Filter {

    int type;

    public TypeFilter(int type) {
        this.type = type;
    }

    @Override
    public boolean apply(File file) {
        return file.type == type;
    }
}
