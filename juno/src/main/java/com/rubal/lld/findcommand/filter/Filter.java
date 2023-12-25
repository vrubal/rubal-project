package com.rubal.lld.findcommand.filter;

import com.rubal.lld.findcommand.File;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public abstract class Filter {
    public abstract boolean apply(File file);
}