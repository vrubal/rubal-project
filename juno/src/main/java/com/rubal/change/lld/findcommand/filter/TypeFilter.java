package com.rubal.change.lld.findcommand.filter;

import com.rubal.change.lld.findcommand.File;

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
