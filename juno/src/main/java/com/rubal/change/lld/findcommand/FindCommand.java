package com.rubal.change.lld.findcommand;

import com.rubal.change.lld.findcommand.exception.NotADirectoryException;
import com.rubal.change.lld.findcommand.filter.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class FindCommand implements Command{
    private List<File> output;
    private File directory;
    private List<Filter> filters;

    public FindCommand(List<File> output, File directory, List<Filter> filters) {
        this.output = output;
        this.directory = directory;
        this.filters = filters;
    }
    private void execute(File directory, List<Filter> filters, List<File> output) {
        if (directory.children == null) {
            return;
        }
        for (File file : directory.children) {
            if (file.isDirectory) {
                execute(file, filters, output);
            } else {
                boolean selectFile = true;
                for (Filter filter : filters) {
                    if (!filter.apply(file)) {
                        selectFile = false;
                    }
                }
                if (selectFile) {
                    output.add(file);
                }
            }
        }
    }
    @Override
    public void execute() {
        execute(this.directory, this.filters, this.output);
    }
}
