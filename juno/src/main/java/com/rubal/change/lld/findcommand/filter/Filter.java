package com.rubal.change.lld.findcommand.filter;

import com.rubal.change.lld.findcommand.File;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public abstract class Filter {
    public abstract boolean apply(File file);
}