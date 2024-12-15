package com.rubal.lld.httpserver;

import com.rubal.lld.httpserver.model.HttpRequest;
import com.rubal.lld.httpserver.model.HttpResponse;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public interface RequestRunner {
    HttpResponse run(HttpRequest request);
}
